package com.birzeit.recursivedescentparser.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class FileOperations {

    public static void main(String[] args) {
        String filePath = "C:\\Users\\twitter\\IdeaProjects\\RecursiveDescentParser\\src\\main\\java\\com\\birzeit\\recursivedescentparser\\file\\reserved-word.txt";
        String type = "terminal"; // Specify the type: "non-terminal", "terminal", "reserved-word"
        HashMap<String, String> resultMap = read(filePath, type);

        for (String key : resultMap.keySet()) {
            System.out.println(key);
        }
    }

    public static HashMap<String, String> read(String filePath, String type) {
        HashMap<String, String> dataMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("//")) {
                    continue; // Skip empty lines or comments
                }

                if (type.trim().equalsIgnoreCase("non-terminal")) {
                    dataMap.put(line, type);
                } else if (type.trim().equalsIgnoreCase("terminal")) {
                    dataMap.put(line, type);
                } else if (type.equals("reserved-word")) {
                    dataMap.put(line, type);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataMap;
    }

}
