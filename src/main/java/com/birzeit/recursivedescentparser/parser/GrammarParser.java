package com.birzeit.recursivedescentparser.parser;

import com.birzeit.recursivedescentparser.scanner.Token;
import com.birzeit.recursivedescentparser.scanner.Tokenizer;
import com.birzeit.recursivedescentparser.scanner.TokenizerResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GrammarParser {

    public static HashMap<String, String> nonTerminals;
    public static HashMap<String, String> terminals;
    public static HashMap<String, String> reservedWords;

    public static void main(String[] args) throws Exception {

        Tokenizer tokenizer = new Tokenizer();
        TokenizerResult result = tokenizer.tokenize("project MyProject;\n" +
                "const P3I4 = 3;\n" +
                "const MAX_VALUE= 100;");
        tokenList = result.getTokens();

        reservedWords = result.getReservedWords();
        terminals = result.getTerminals();
        nonTerminals = result.getNonTerminals();

        //print tokens
        for (String token : tokenList) {
            System.out.println(token);
        }

        System.out.println("------------------");

        GrammarParser grammarParser = new GrammarParser();
        grammarParser.parse();

    }

    public static List<String> tokenList ;
    public static int current = 0;

    public GrammarParser() {

        Tokenizer tokenizer = new Tokenizer();
        TokenizerResult result = tokenizer.tokenize("project MyProject;\n" +
                "const PI = 3;\n" +
                "const MAX_VALUE= 100;");
        tokenList = result.getTokens();

    }

    public boolean parse() {
        try {
            projectDeclaration();
            return true;
        } catch (Exception e) {
            System.out.println("Error parsing at token " + current + ": " + e.getMessage());
            return false;
        }
    }

    private void projectDeclaration() throws Exception {
        projectDef();
        //match(".");

        if (current != tokenList.size()) {
            throw new Exception("End of input expected, but more tokens found.");
        }
    }

    private void projectDef() throws Exception{
        //project-heading
        projectHeading();
        //declarations
        declarations();
        //compound-statement
    }

    private void declarations() throws Exception{



    }

    private void projectHeading() throws Exception {
        try {

            //Check if project definition starts with project keyword
            if (getToken().equals("project")) {
                //Check if project name is not a reserved word
                if (!reservedWords.containsKey(getToken())){
                    if (getToken().equals(";")){
                        System.out.println("Project heading is correct");
                    }else{
                        throw new Exception("Error parsing at " + current + "expected ; but found " + tokenList.get(current));
                    }
                }else{
                    throw new Exception("Error parsing at " + current + "project name cant be a reserved word -> " + tokenList.get(current));
                }
            } else {
                throw new Exception("Error parsing at " + current + "expected project keyword but found " + tokenList.get(current));
            }

        }catch (Exception e){
            throw new Exception("Error in projectHeading");
        }
    }

    private static String getToken() throws Exception{

        if (current >= tokenList.size()) {
            throw new Exception("End of input expected, but more tokens found.");
        }
        String token = tokenList.get(current);
        current++;
        return token;

    }
}
