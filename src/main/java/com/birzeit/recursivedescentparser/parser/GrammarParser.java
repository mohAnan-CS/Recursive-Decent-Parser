package com.birzeit.recursivedescentparser.parser;

import com.birzeit.recursivedescentparser.scanner.Tokenizer;
import com.birzeit.recursivedescentparser.scanner.TokenizerResult;

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
        TokenizerResult result = tokenizer.tokenize("project projects;\n" +
                "var\n" +
                "  total:int;\n" +
                "  \n" +
                "routine compute;\n" +
                "  \n" +
                "  var\n" +
                "    num:int;\n" +
                "start\n" +
                "  input(x);\n" +
                "  num:=n+10;\n" +
                "  output(num);\n" +
                "end;\n" +
                "  \n" +
                "start\n" +
                "  input(i);\n" +
                "  if (i< 100) then\n" +
                "    j:=5\n" +
                "  endif;\n" +
                "  loop (i<> min) do\n" +
                "    start\n" +
                "      i := i+1;\n" +
                "      output(i);\n" +
                "      total :=total+i;\n" +
                "    end;\n" +
                "  output(total);\n" +
                "end .");
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
        //var-decl
        System.out.println("Finished constDeclaration");
        System.out.println("current token : " + currentToken);
        varDeclaration();
        //subroutine-decl

    }

    private void varDeclaration() throws Exception {

        System.out.println("varDeclaration");
        System.out.println("current token : " + currentToken);
        if (currentToken.equals("var")) {
            System.out.println("token equals var");
            getToken();
            while (!currentToken.equals("routine") && !currentToken.equals("start") && !reservedWords.containsKey(currentToken)){
                System.out.println("while loop ");
                System.out.println("current token in loop : " + currentToken);
                varItem();
                getToken();
                if (currentToken.equals(";")) {
                    System.out.println("token equals ';' ");
                    getToken();
                }else{
                    throw new Exception("Error parsing at " + current + "expected ';' but found " + tokenList.get(current));
                }
            }
        }else{
            retrieveToken();
        }

    }

    private void varItem() throws Exception {
        System.out.println("varItem");
        //var-item    name-list  ":"  int
        nameList();
        System.out.println("current token ----> " + currentToken);
        if (currentToken.equals(":")) {
            getToken();
            if (!currentToken.equals("int")) {
                throw new Exception("Error parsing at " + current + "expected 'int' but found " + tokenList.get(current));
            }
        }else {
            throw new Exception("Error parsing at token number " + current + "expected ':' but found " + tokenList.get(current));
        }
    }


    private void nameList() throws Exception {
        //name-list   "name" (","  "name")^*
        System.out.println("current token in nameList is '" + currentToken+"'");
        if (!reservedWords.containsKey(currentToken)) {
            System.out.println("token equals name in nameList");
            getToken();
            System.out.println("current token in nameList is '" + currentToken+"'");
            while (currentToken.equals(",")) {
                getToken();
                if (!reservedWords.containsKey(currentToken)) {
                    System.out.println("token equals name in loop");
                    getToken();
                }else{
                    throw new Exception("Error parsing at " + current + "var name cant be a reserved word -> " + tokenList.get(current));
                }
            }
        } else {
            throw new Exception("Error parsing at " + current + "var name cant be a reserved word -> " + tokenList.get(current));
        }
    }


    private void constDeclaration() throws Exception {

        System.out.println("constDeclaration");
        //const-decl  const ( const-item  ";" )^+  | lambda

        getToken();
        if (currentToken.equals("const")) {
            System.out.println("token equals const");
            getToken();
            while (!currentToken.equals("var") && !currentToken.equals("routine") && !currentToken.equals("start") && !reservedWords.containsKey(currentToken)){
                System.out.println("while loop");
                System.out.println("current token in loop : " + currentToken);
                constItem();
                getToken();
                if (currentToken.equals(";")) {
                    System.out.println("token equals ';' ");
                    getToken();
                }else{
                    throw new Exception("Error parsing at " + current + "expected ';' but found " + tokenList.get(current));
                }
            }
        }else{
            System.out.println("retrieve token");
            retrieveToken();
            System.out.println("current token : " + currentToken);
        }

    }

    private void constItem() throws Exception {
        System.out.println("constItem");
        //	const-item     "name"   =   "integer-value"

        System.out.println("constItem in current token " + currentToken);
        if (!reservedWords.containsKey(currentToken)) {
            getToken();
            if (currentToken.equals("=")) {
                System.out.println("token equals =");
                //Check if getToken can parse to integer value
                try {
                    getToken();
                    Integer.parseInt(currentToken);
                    System.out.println("token is integer value");
                } catch (NumberFormatException e) {
                    throw new Exception("Error parsing at " + current + "expected integer value but found " + currentToken);
                }
            } else {
                System.out.println("Curren token in exception !! is  " + currentToken);
                throw new Exception("Error parsing at token number " + current + " expected '=' but found " + currentToken);
            }
        } else {
            throw new Exception("Error parsing at " + current + "const name cant be a reserved word -> " + currentToken);
        }


    }

    private void projectHeading() throws Exception {
        System.out.println("projectHeading");


            //Check if project definition starts with project keyword
            getToken();
            if (currentToken.equals("project")) {
                System.out.println("token equals project keyword");
                //Check if project name is not a reserved word
                getToken();
                if (!reservedWords.containsKey(currentToken)) {
                    System.out.println("project name is not a reserved word");
                    getToken();
                    if (currentToken.equals(";")) {
                        System.out.println("Project heading is correct");
                    } else {
                        throw new Exception("Error parsing at " + current + "expected ; but found " + tokenList.get(current));
                    }
                } else {
                    throw new Exception("Error parsing at token number " + current + " project name cant be a reserved word ");
                }
            } else {
                throw new Exception("Error parsing at " + current + "expected project keyword but found " + tokenList.get(current));
            }


    }

    private static String getToken() throws Exception {

        if (current >= tokenList.size()) {
            throw new Exception("End of input expected, but more tokens found. at token " + current);
        }
        String token = tokenList.get(current);
        currentToken = token;

        System.out.println("getToken()  '" + token + "'");
        current++;
        return token;

    }

    private static String retrieveToken() throws Exception {
        //write a code that decreeases current by 1 and returns the token at that position
        if (current == 0) {
            throw new Exception("Start of input expected, but more tokens found. at token " + current);
        }
        String token = tokenList.get(current - 1);
        currentToken = token;
        return token;

    }
}
