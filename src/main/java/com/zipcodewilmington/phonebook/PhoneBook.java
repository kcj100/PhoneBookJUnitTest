package com.zipcodewilmington.phonebook;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by leon on 1/23/18.
 * Made WAY better by kristofer 6/16/20
 */
public class PhoneBook {
    private Map<String, List<String>> phoneBook;

    public PhoneBook() {
        this.phoneBook = new LinkedHashMap<>();
    }

    public PhoneBook(Map<String, List<String>> map) {
        this.phoneBook = new LinkedHashMap<>(map);
    }

    public void add(String name, String phoneNumber) {
        if(phoneNumber.isBlank()) {
            phoneBook.computeIfAbsent(name, k -> new ArrayList<String>()).add(phoneNumber);
        }else if (phoneNumberIsValid(phoneNumber)) {
            phoneBook.computeIfAbsent(name, k -> new ArrayList<String>()).add(formatPhoneNumber(phoneNumber));
        } else {
            System.out.println("Cannot add " + phoneNumber + " to " + name + ". Phone number " +
                    "is invalid.");
        }
    }

    public void addAll(String name, String... phoneNumbers) {
        if (phoneNumbers.length == 0) {
            throw new IllegalArgumentException("Phone number array is not greater than 0");
        }
        int invalidNumbers = 0;
        for (String phoneNumber : phoneNumbers) {
            if (!phoneNumberIsValid(phoneNumber)) {
                System.out.println("Invalid phone number: " + phoneNumber);
                invalidNumbers++;
                continue;
            }
            String existingName = "";
            if (phoneNumberExists(phoneNumber)) {
                for (String nameTemp : phoneBook.keySet()) {
                    for (String phoneNumberTemp : phoneBook.get(nameTemp)) {
                        if (formatPhoneNumber(phoneNumber).equals(phoneNumberTemp)) {
                            existingName = nameTemp;
                            System.out.println(formatPhoneNumber(phoneNumber) + " is already assigned to " + existingName);
                            break;
                        }
                    }
                }
            }
            if (phoneNumberIsValid(phoneNumber) && !phoneNumberExists(phoneNumber)) {
                phoneBook.computeIfAbsent(name, k -> new ArrayList<String>()).add(formatPhoneNumber(phoneNumber));
            }

        }
        if (invalidNumbers == phoneNumbers.length) {
            throw new IllegalArgumentException("All phone numbers passed to addAll are invalid");
        }
        if (invalidNumbers > 0) {
            System.out.println("Number of invalid phone numbers from array: " + invalidNumbers);
        }

    }

    public void remove(String name) {
        if (hasEntry(name)) {
            this.phoneBook.remove(name);
        } else {
            System.out.println(name + " not found in phonebook");
        }

    }

    public boolean hasEntry(String name) {
        return this.phoneBook.containsKey(name);
    }

    public List<String> lookup(String name) {
        if (hasEntry(name)) {
            return phoneBook.get(name);
        }
        throw new IllegalArgumentException(name + " is not registered in the phonebook.");
    }

    public String reverseLookup(String phoneNumber) {
        String formattedPhoneNumber = formatPhoneNumber(phoneNumber);
        if (!phoneNumberIsValid(phoneNumber)) {
            throw new IllegalArgumentException("Phone number passed to reverse lookup was not valid.");
        }
        for (Map.Entry<String, List<String>> entry : phoneBook.entrySet()) {
            if (entry.getValue().contains(formattedPhoneNumber)) {
                return entry.getKey();
            }
        }
        throw new IllegalArgumentException("Phone number passed to reverse lookup does not exist");
    }

    public List<String> getAllContactNames() {
        if (phoneBook.isEmpty()) {
            throw new IllegalArgumentException("Cannot get all contact names, phonebook is empty.");
        }
        List<String> list = new ArrayList<>();
        list.addAll(phoneBook.keySet());
        return list;
    }

    // format phone number to (xxx) xxx-xxxx
    private String formatPhoneNumber(String phoneNumber) {
        String formattedNumber = phoneNumber.replaceAll("[-.\\s]?", "");
        formattedNumber = String.format("%s-%s-%s",
                formattedNumber.substring(0, 3),
                formattedNumber.substring(3, 6),
                formattedNumber.substring(6, 10)
        );
        return formattedNumber;
    }

    // valid phone number formats
    private boolean phoneNumberIsValid(String phoneNumber) {
        if (phoneNumber == null) {
            return false;
        }
        String regex1 = "^(\\d{3}[-.\\s]?\\d{3}[-.\\s]?\\d{4})$";
        String regex2 = "^(\\d{10})$";
        String regex3 = "^(\\d{6}[-.\\s]?\\d{4})$";
        String regex4 = "^(\\d{3}[-.\\s]?\\d{7})$";
        String regex5 = "(\\(\\d{3}\\)[-.\\s]?\\d{3}[-.\\s]?\\d{4})$";
        return phoneNumber.matches(regex1)
                || phoneNumber.matches(regex2)
                || phoneNumber.matches(regex3)
                || phoneNumber.matches(regex4)
                || phoneNumber.matches(regex5);
    }

    private boolean phoneNumberExists(String phoneNumber) {
        for (List<String> phoneNumbers : phoneBook.values()) {
            if (phoneNumbers.contains(formatPhoneNumber(phoneNumber))) {
                return true;
            }
        }
        ;
        return false;
    }

    // for testing
    public Map<String, List<String>> getMap() {
        return phoneBook;
    }
}
