package com.birzeit.recursivedescentparser.scanner;

import com.birzeit.recursivedescentparser.parser.FileOperations;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {

    public TokenizerResult tokenize(String input) {

        HashMap<String, String> nonTerminals = FileOperations.read("C:\\Users\\twitter\\IdeaProjects\\RecursiveDescentParser\\src\\main\\java\\com\\birzeit\\recursivedescentparser\\file\\non-terminal.txt", "non-terminal");
        HashMap<String, String> terminals = FileOperations.read("C:\\Users\\twitter\\IdeaProjects\\RecursiveDescentParser\\src\\main\\java\\com\\birzeit\\recursivedescentparser\\file\\terminal.txt", "terminal");
        HashMap<String, String> reservedWords = FileOperations.read("C:\\Users\\twitter\\IdeaProjects\\RecursiveDescentParser\\src\\main\\java\\com\\birzeit\\recursivedescentparser\\file\\reserved-word.txt", "reserved-word");

        List<String> tokens = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\w+|<|<=|>|>=|<>|\\*|/|%|\\+|-|=|:=|;|,|:|\\(|\\)|\\.");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String token = matcher.group();
            tokens.add(token);
        }

        return new TokenizerResult(tokens, nonTerminals, terminals, reservedWords);
    }

}
