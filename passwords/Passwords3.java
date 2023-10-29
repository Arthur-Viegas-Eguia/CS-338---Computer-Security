import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Scanner;
import java.io.FileWriter;
public class Passwords3{
    /* Special thanks to Geeks for Geeks
    Method copied from https://www.geeksforgeeks.org/sha-256-hash-in-java/#*/ 
    public static String toHexString(byte[] hash)
    {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);
 
        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));
 
        // Pad with leading zeros
        while (hexString.length() < 64)
        {
            hexString.insert(0, '0');
        }
 
        return hexString.toString();
    }

    public static void main(String[] args) {
        File usersFile, passwordsFile;
        usersFile = new File("passwords3.txt");
        try{
            Scanner users, passwords;
            users = new Scanner(usersFile);
            String[] userSeparated, passwordSeparated;
            String username, salt, password, passwordFromList, passwordTohash, passwordBytes;
            long processTime, timeToHash, timeHashed, hashesTried = 0, passwordCracking, passwordDiscovered, hashedTime;
            byte[] digest;
            double hashTime = 0.0, totalProcessingTime = 0.0, passwordFindingTime = 0.0;
            int passwordsCracked = 0;
            MessageDigest hash = MessageDigest.getInstance("SHA-256");
            while(users.hasNextLine()){
                userSeparated = users.nextLine().split(":");
                username = userSeparated[0];
                salt = String.copyValueOf(userSeparated[1].toCharArray(), 3, 8);
                password = String.copyValueOf(userSeparated[1].toCharArray(),12, 64);
                passwordsFile = new File("words.txt");
                passwords = new Scanner(passwordsFile);
                passwordCracking = System.nanoTime();
                while (passwords.hasNextLine()) {
                    processTime = System.nanoTime();
                    passwordFromList = passwords.nextLine().toLowerCase();
                    passwordTohash = salt + passwordFromList;
                    timeToHash = System.nanoTime();
                    digest = hash.digest(passwordTohash.getBytes(StandardCharsets.UTF_8));
                    timeHashed = System.nanoTime();
                    hashTime = hashTime + ((double)(timeHashed - timeToHash));
                    passwordBytes = toHexString(digest);
                    hashesTried++;
                    hashedTime = System.nanoTime();
                    totalProcessingTime = totalProcessingTime + ((double)(hashedTime - processTime));
                    if(passwordBytes.equals(password)){
                        System.out.print(username+":"+passwordFromList+"\n");
                        passwordDiscovered = System.nanoTime();
                        passwordFindingTime = passwordFindingTime + ((double) (passwordDiscovered - passwordCracking));
                        passwordsCracked++;
                        break;
                    }
                }
                passwords.close();
            }
            users.close();
            System.out.println("Total number of passwords cracked "+passwordsCracked);
            System.out.println("Mean time it took to compute each individual hash, including data processing "+(totalProcessingTime/hashesTried));
            System.out.println("Mean time it took to compute each hash, not including data processing "+(hashTime/hashesTried));
            System.out.println("Mean time to compute each password "+(passwordFindingTime/passwordsCracked));
            System.out.println("Total number of hashes computed "+hashesTried);

        } catch(Exception e){
            System.out.println(e);
        }
        
        
    }
}