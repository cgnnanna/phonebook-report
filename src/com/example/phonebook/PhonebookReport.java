package com.example.phonebook;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class PhonebookReport {

    public static void main(String[] args) {
        // Read the full filename as an input and make sure to strip the
        // quotations in cases where the user mistakenly adds them.
        // This will make the solution work seamlessly without the user having to stress
        // about pasting a file path surrounded with quotation of not.
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input the full filename (i.e the full file path with the filename and extension) e.g c:/file/phonebook.txt");
        String filePath = stripQuotation(scanner.nextLine());
        readFile(filePath);
    }

    public static void readFile(String fullFileName){
        int[] countArr = new int[5];
        StringBuilder brokenString = new StringBuilder();
        // use a file input stream to read the file in chunks,
        // this way the computer's memory is not overwhelmed even if the file is very large
        try (FileInputStream fis = new FileInputStream(fullFileName)) {
            byte[] chunk = new byte[4096];
            int len;
            while ((len = fis.read(chunk)) > 0){
                // properly decode the byte array to string without adding the unused portions of the byte array
                // by specifying the length of the data to decode
                String value = new String(chunk, 0, len, StandardCharsets.UTF_8);
                String[] arr = value.split("\\s+");

                // use an array as an in_out parameter to count the phone numbers per network.
                // In the array, the value at index;
                // 0 = mtn
                // 1 = airtel
                // 2 = glo
                // 3 = 9mobile
                // 4 = mtel
                processReport(arr, countArr, brokenString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // print the result of the operation
        System.out.printf("mtn: %s%n", countArr[0]);
        System.out.printf("airtel: %s%n", countArr[1]);
        System.out.printf("glo: %s%n", countArr[2]);
        System.out.printf("9mobile: %s%n", countArr[3]);
        System.out.printf("mtel: %s%n", countArr[4]);
    }

    public static void processReport(String[] phoneNumbers, int[] countArr, StringBuilder brokenString){
        // use sets to hold the unique identifiers for the several networks. This will make for better comparisons
        Set<String> mtn = new HashSet<>(Arrays.asList("0703", "0706", "0806", "0803", "0810", "0813", "0814", "0816", "0903", "0906", "0913", "0916", "07025", "07026", "0704"));
        Set<String> airtel = new HashSet<>(Arrays.asList("0701", "0708", "0802", "0808", "0812", "0901", "0902", "0904", "0907", "0912"));
        Set<String> glo = new HashSet<>(Arrays.asList("0705", "0805", "0807", "0811", "0815", "0905", "0915"));
        Set<String> nMobile = new HashSet<>(Arrays.asList("0809", "0817", "0818", "0909", "0908"));
        Set<String> mtel = new HashSet<>(Arrays.asList("0804"));

        for(String phoneNumber : phoneNumbers){

            // since we are using chunks, it's possible the chunk starts or stops halfway through a phone number
            // this would have rendered the phone number invalid. But with the checks below, the phone number will
            // be patched up and still processed correctly.
            if ((phoneNumber.length()<11) && (brokenString.length()>0)) {
                brokenString.append(phoneNumber);
                phoneNumber = brokenString.toString();
                brokenString.delete(0, brokenString.length());
            }
            else if ((phoneNumber.length()<11) && (brokenString.length()==0)) {
                brokenString.append(phoneNumber);
                continue;
            }

            String prefix4 = phoneNumber.substring(0, 4);
            String prefix5 = phoneNumber.substring(0, 5);

            // get the first 5 digits to compare since mtn has some identifiers with length of 5
            // this comparison of the first 5 digits is equally done for other networks as well
            // even though it will always result to false at the moment.
            if(mtn.contains(prefix4) || mtn.contains(prefix5)){
                countArr[0]++;
            }
            else if(airtel.contains(prefix4) || airtel.contains(prefix5)){
                countArr[1]++;
            }
            else if(glo.contains(prefix4) || glo.contains(prefix5)){
                countArr[2]++;
            }
            else if(nMobile.contains(prefix4) || nMobile.contains(prefix5)){
                countArr[3]++;
            }
            else if(mtel.contains(prefix4) || mtel.contains(prefix5)){
                countArr[4]++;
            }
        }
    }

    private static String stripQuotation(String strVal){
        if (((strVal.charAt(0)=='\"') && (strVal.charAt(strVal.length()-1)=='\"'))){
            return strVal.substring(1, strVal.length()-1);
        }
        return strVal;
    }
}