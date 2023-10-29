#!/usr/bin/env python3
'''
    conversions.py
    Jeff Ondich, 6 May 2022

    Shows how to compute a SHA-256 hash and manipulate the
    relevant Python types.

    Note that when you want to do a new hash, you need to
    call hashlib.sha256() again to get a fresh sha256 object.
'''
import hashlib
import binascii
import statistics as stats
import time as t
#time it takes to treat all the data and compute one individual hash
treatingData = []
#time it takes for the hash command to run. i.e. time it takes to hash the treated data with sha 256
hashData = []
#number of hashes computed
hashNumber = 0
dictionary = {}
file = open("words.txt", "r")
#We will hash evrery single password in the list and add them to the dictionary
hashing = t.time()
for line in file:
    #We need to treat data first
    treatingTime = t.time()
    password = line.lower()[:-1]
    encoded_password = password.encode('utf-8') # type=bytes
    #After it is treated we can hash it using sha256
    hashTime = t.time()
    hasher = hashlib.sha256(encoded_password)
    digest = hasher.digest() # type=bytes
    hashOk = t.time()
    hashData.append(hashOk - hashTime)
    #converting data to the format we can save in the dictionary
    digest_as_hex = binascii.hexlify(digest) # weirdly, still type=bytes
    digest_as_hex_string = digest_as_hex.decode('utf-8') # type=string
    treatedTime = t.time()
    #adding to the dictionary
    treatingData.append(treatedTime - treatingTime)
    dictionary[digest_as_hex_string] = password
    hashNumber += 1
hashed = t.time()
print("Hashed in (ms): ", hashed - hashing)
print("Time it took to do all operations to compute a single hash, including data processing")
print("mean: {}\nmedian = {}".format(stats.mean(treatingData), stats.median(treatingData)))
print("How long it takes to effectively compute a single hash")
print("mean: {}\nmedian: {}".format(stats.mean(hashData), stats.median(hashData)))
print("Number of hashes computed: ", hashNumber)

file2 = open("passwords1.txt", "r")
file3 = open("answers.txt", "w")
#time it takes to find a single hash in the dictionary
passwordDict = []
#Time it takes to break a single password, including treating data
passFind = []
#We do some data treating, to get just the hash out of the password file
passwords = 0
fullDetect = t.time()
for line in file2:
    breakTime = t.time()
    line = line.split(":")
    #looking for the password in the dictionary
    discoveringTime = t.time()
    line[1] = dictionary[line[1]]
    discoveredTime = t.time()
    passwordDict.append(discoveredTime - discoveringTime)
    file3.write(line[0]+":"+line[1]+"\n")
    breakDone = t.time()
    passFind.append(breakDone - breakTime)
    passwords += 1
detect = t.time()
print("Time it took to break all passwords: ", detect - fullDetect)
print("Time it took to compute each individual password, not including processing data")
print("mean: "+str(stats.mean(passwordDict)) + "\nmedian: " + str(stats.median(passwordDict)))
print("Time it took to compute each individual password, including data processing")
print("mean: {}\nmedian: {}".format(stats.mean(passFind), stats.median(passFind)))
print("Passwords cracked: {}".format(passwords))
