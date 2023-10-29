import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Scanner;
import java.util.HashMap;
import java.security.NoSuchAlgorithmException;
import java.io.FileWriter;

public class Passwords2{
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
    public static void main(String[] args){
        File usersToDict = new File("passwords2.txt");
        try{
            Scanner readData = new Scanner(usersToDict);
            int i = 0;
            String data, passwordTohash, passwordBytes, thisLine;
            String[] splitString;
            long initializing, initialized, initializeOne, initializedOne, dictionaryOne;
            double initializeSum = 0.0, initializeDictionary = 0.0, usersInitialized = 0.0;
            HashMap<String, String> userBase = new HashMap<String, String>();
            initializing = System.nanoTime();
            while (readData.hasNextLine()) {
                initializeOne = System.nanoTime();
                data = readData.nextLine();
                splitString = data.split(":");
                dictionaryOne = System.nanoTime();
                userBase.put(splitString[1], splitString[0]);
                initializedOne = System.nanoTime();
                initializeSum = initializeSum + ((double)(initializedOne - initializeOne));
                initializeDictionary = initializeDictionary + ((double)(initializedOne - dictionaryOne));
                usersInitialized++;
            }

            initialized = System.nanoTime();
            System.out.println("Time to initialize users: "+(initialized - initializing));
            readData.close();
            System.out.println("Mean time to initialize one user, inclusing data processing "+(initializeSum/usersInitialized));
            System.out.println("Mean time to add one user to the HashMap "+(initializeDictionary/usersInitialized));
            MessageDigest hash = MessageDigest.getInstance("SHA-256");
            File passwordsFile = new File("words.txt");
            Scanner passwords = new Scanner(passwordsFile);
            File passwords2File;
            Scanner passwords2;
            File answerFile = new File("answers2.txt");
            FileWriter answers = new FileWriter(answerFile);
            double hashTime = 0.0, processTimeHash = 0.0, hashTried = 0.0, passwordTime = 0.0, passwordsHacked = 0.0;
            long processTime, timeToHash, timeHashed, processedTime, passWord = 0, passWordDone, timeHacking, timeHacked;
            byte[] digest;
            boolean flag = true;
            long hashComputed = 0;
            timeHacking = System.nanoTime();
            while(passwords.hasNextLine()){
                passwords2File = new File("words.txt");
                passwords2 = new Scanner(passwords2File);
                thisLine = passwords.nextLine();
                while(passwords2.hasNextLine()){
                    if(flag){
                        passWord = System.nanoTime();
                        flag = !flag;
                    }
                    processTime = System.nanoTime();
                    passwordTohash = (thisLine + passwords2.nextLine()).toLowerCase();
                    timeToHash = System.nanoTime();
                    digest = hash.digest(passwordTohash.getBytes(StandardCharsets.UTF_8));
                    timeHashed = System.nanoTime();
                    hashTime = hashTime + ((double)(timeHashed - timeToHash));
                    passwordBytes = toHexString(digest);
                    hashComputed++;
                    if(userBase.containsKey(passwordBytes)){
                        answers.write(userBase.get(passwordBytes)+":"+passwordTohash+"\n");
                        flag = true;
                        passWordDone = System.nanoTime();
                        passwordTime = passwordTime + ((double) (passWordDone - passWord));
                        passwordsHacked++;
                    }
                    processedTime = System.nanoTime();
                    processTimeHash = processTimeHash + ((double)(processedTime - processTime));
                    hashTried++;
                }
                passwords2.close();
                if(passwordsHacked >= 100){
                    break;
                }
            }
            passwords.close();
            timeHacked = System.nanoTime();
            answers.close();
            System.out.println("This code cracked "+passwordsHacked+"passwords in "+(timeHacked-timeHacking));
            System.out.println("Mean time it took to compute each individual hash, including data processing "+(processTimeHash/hashTried));
            System.out.println("Mean time it took to compute each hash, not including data processing "+(hashTime/hashTried));
            System.out.println("Mean time it took to compute each password "+(passwordTime/passwordsHacked));
            System.out.println("Number of hashes computed "+hashComputed);
        } catch(Exception e){
            System.out.println("Error found in the code");
        }
    }
}