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
    public static String currentToken;

    public static void main(String[] args) throws Exception {

        GrammarParser grammarParser = new GrammarParser();

        //print tokens
        for (String token : tokenList) {
            System.out.println(token);
        }

        System.out.println("------------------");


        grammarParser.parse();

    }

    public static List<String> tokenList;
    public static int current = 0;

    public GrammarParser() {

        Tokenizer tokenizer = new Tokenizer();
        TokenizerResult result = tokenizer.tokenize("project MyProject;\n" +
                "const PI = 3;\n" +
                "const MAX_VALUE = 100; const obada = 200;\n" +
                "var mo\n" +
                "end.");
        reservedWords = result.getReservedWords();
        tokenList = result.getTokens();

    }

    public void parse() throws Exception {

        projectDeclaration();

    }

    private void projectDeclaration() throws Exception {
        System.out.println("projectDeclaration");
        projectDef();
        //match(".");


    }

    private void projectDef() throws Exception {
        System.out.println("projectDef");
        //project-heading
        projectHeading();
        //declarations
        declarations();
        //compound-statement
    }

    private void declarations() throws Exception {

        System.out.println("declarations");
        //const-declaration
        constDeclaration();
        if (getToken().equals("const")) {
            System.out.println("Hiiiiiiiiiiiiii");
            constDeclaration();
        }

        //var-decl
        varDeclaration();
        //subroutine-decl

    }

    private void varDeclaration() throws Exception {

        System.out.println("varDeclaration");
        System.out.println("Token Number " + current + " is " + tokenList.get(current));
        //var-decl  var (var-item ";" )^+  |  lambda
        if (getToken().equals("var")) {
            System.out.println("token equals var");
            varItem();
            if (!getToken().equals(";")) {
                throw new Exception("Error parsing at " + current + "expected ';' but found " + tokenList.get(current));
            }
        }

    }

    private void varItem() throws Exception {
        System.out.println("varItem");
        //var-item    name-list  ":"  int
        nameList();
        if (getToken().equals(":")) {
            if (!getToken().equals("int")) {
                throw new Exception("Error parsing at " + current + "expected 'int' but found " + tokenList.get(current));
            }
        } else {
            throw new Exception("Error parsing at " + current + "expected ':' but found " + tokenList.get(current));
        }
    }


    private void nameList() throws Exception {
        //name-list   "name" (","  "name")^*
        if (!reservedWords.containsKey(getToken())) {
            String token = getToken();
            if (token.equals(",")) {
                nameList();
            }
        } else {
            throw new Exception("Error parsing at " + current + "var name cant be a reserved word -> " + tokenList.get(current));
        }
    }


    private void constDeclaration() throws Exception {

        System.out.println("constDeclaration");
        System.out.println("Currrrrrrrrrentt token : " + currentToken);

        //const-decl  const ( const-item  ";" )^+  | lambda
        if (getToken().equals("const")) {
            System.out.println("token equals const");
            constItem();
            String token = getToken();
            if (!token.equals(";")) {
                throw new Exception("Error parsing at " + current + "expected ';' but found " + tokenList.get(current));
            }

        }

    }

    private void constItem() throws Exception {
        System.out.println("constItem");
        //	const-item     "name"   =   "integer-value"
        if (!reservedWords.containsKey(getToken())) {
            System.out.println("Current token : " + currentToken);
            System.out.println("const item token is not reserved word");
            if (getToken().equals("=")) {
                System.out.println("token equals =");
                //Check if getToken can parse to integer value
                try {
                    Integer.parseInt(getToken());
                    System.out.println("token is integer value");
//                    if (getToken().equals(";")){
//                        System.out.println("const item is correct end with ;");
//                        if (getToken().equals("const")) {
//                            System.out.println("token equals const, declare another const item");
//                            constItem();
//                        }
//                    }else{
//                        throw new Exception("Error parsing at " + current + "expected ; but found " + tokenList.get(current));
//                    }
                } catch (NumberFormatException e) {
                    throw new Exception("Error parsing at " + current + "expected integer value but found " + tokenList.get(current));
                }
            } else {
                throw new Exception("Error parsing at " + current + "expected '=' but found " + tokenList.get(current));
            }
        } else {
            throw new Exception("Error parsing at " + current + "const name cant be a reserved word -> " + tokenList.get(current));
        }


    }

    private void projectHeading() throws Exception {
        System.out.println("projectHeading");
        try {

            //Check if project definition starts with project keyword
            if (getToken().equals("project")) {
                System.out.println("token equals project keyword");
                //Check if project name is not a reserved word
                if (!reservedWords.containsKey(getToken())) {
                    System.out.println("project name is not a reserved word");
                    if (getToken().equals(";")) {
                        System.out.println("Project heading is correct");
                    } else {
                        throw new Exception("Error parsing at " + current + "expected ; but found " + tokenList.get(current));
                    }
                } else {
                    throw new Exception("Error parsing at " + current + "project name cant be a reserved word -> " + tokenList.get(current));
                }
            } else {
                throw new Exception("Error parsing at " + current + "expected project keyword but found " + tokenList.get(current));
            }

        } catch (Exception e) {
            throw new Exception("Error in projectHeading");
        }
    }

    private static String getToken() throws Exception {

        if (current >= tokenList.size()) {
            throw new Exception("End of input expected, but more tokens found. at token " + current);
        }
        String token = tokenList.get(current);
        currentToken = token;

        System.out.println("Token Number " + current + " is " + tokenList.get(current));
        current++;
        return token;

    }
}
