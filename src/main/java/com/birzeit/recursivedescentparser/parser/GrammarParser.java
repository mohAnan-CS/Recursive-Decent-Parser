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
                "const\n" +
                "  len=100;\n" +
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
                "end;");
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
        compoundStatement();
    }

    private void declarations() throws Exception {

        if (currentToken.equals("start")){
            System.out.println("token equals start in declarations");
            compoundStatement();
        }else {
            System.out.println("enter declarations");
            //const-declaration
            constDeclaration();
            //var-decl
            System.out.println("finished constDeclaration");
            varDeclaration();
            System.out.println("finished varDeclaration");
            //subroutine-decl
            subroutineDeclaration();
        }


    }

    private void subroutineDeclaration() throws Exception{

        //subroutine-decl  subroutine-heading  declarations  compound-stmt  “;”  |  lambda

        System.out.println("enter subroutineDeclaration");
        //subroutine-heading
        subroutineHeading();
        declarations();
        //compound-statement
        //compoundStatement();
        System.out.println("current token after compound statement is " + currentToken);
        if (currentToken.equals(";")) {
            System.out.println("subroutine declaration valid");
        } else {
            throw new Exception("Error parsing at token number " + current + " expected ';' but found " + currentToken);
        }


    }

    private void compoundStatement() throws Exception{

        //compound-stmt  start  stmt-list end
        System.out.println("enter compoundStatement");
        if (currentToken.equals("start")) {
            getToken();
            stmtList();
            System.out.println("current token after stmt list is " + currentToken);
            if (currentToken.equals("end")) {
                System.out.println("compound statement end with 'end'");
                getToken();
                System.out.println("current token is " + currentToken);

            } else {
                throw new Exception("Error parsing at token number " + current + " expected 'end' but found " + currentToken);
            }
        }else{
            throw new Exception("Error parsing at token number " + current + " expected 'start' but found " + currentToken);
        }

    }

    private void stmtList() throws Exception{

        //stmt-list    ( statement    ";" )*
        if (currentToken.equals("end")) {
            System.out.println("stmt list is empty");
        }else {
            while (!currentToken.equals("end")) {
                statement();
                getToken();
                System.out.println("current token after stmt list is " + currentToken);
                if (currentToken.equals(";")) {
                    System.out.println("stmt list valid");
                } else {
                    throw new Exception("Error parsing at token number " + current + " expected ';' but found " + currentToken);
                }
            }
        }

    }

    private void statement() throws Exception{

        System.out.println("enter statement");
        //statement  ass-stmt  |  inout-stmt  | if-stmt | loop-stmt | compound-stmt | lambda
        //inout-stmt
        inoutStmt();
        //ass-stmt
        //assStmt();
        //if-stmt
        //loop-stmt
        //compound-stmt

    }

    private void inoutStmt() throws Exception{

        //	inout-stmt  input "("    "name"     ")"    |    output  "("   name-value   ")"
        System.out.println("enter inoutStmt");
        System.out.println("curren token is " + currentToken);
        if (currentToken.equals("input")){
            System.out.println("if current equal input");
            getToken();
            if (currentToken.equals("(")){
                getToken();
                if (!reservedWords.containsKey(currentToken)){
                    getToken();
                    if (currentToken.equals(")")){
                        System.out.println("input stmt valid");
                    }else{
                        throw new Exception("Error parsing at token number " + current + " expected ')' but found " + currentToken);
                    }
                }else{
                    throw new Exception("Error parsing at token number " + current + " input name cant be reserved word");
                }
            }else{
                throw new Exception("Error parsing at token number " + current + " expected '(' but found '" + currentToken+"'");
            }
        }else if (currentToken.equals("output")){
            System.out.println("else if current equal output");
            getToken();
            if (currentToken.equals("(")){
                getToken();
                if (!reservedWords.containsKey(currentToken)){
                    getToken();
                    if (currentToken.equals(")")){
                        System.out.println("output stmt valid");
                    }else{
                        throw new Exception("Error parsing at token number " + current + " expected ')' but found " + currentToken);
                    }
                }else{
                    throw new Exception("Error parsing at token number " + current + " input name cant be reserved word");
                }
            }else{
                throw new Exception("Error parsing at token number " + current + " expected '(' but found '" + currentToken+"'");
            }
        }

    }


    private void subroutineHeading() throws Exception {

        //subroutine-heading  routine  "name"    ";"
        System.out.println("enter subroutineHeading");
        System.out.println("current token is : " + currentToken);
        if (currentToken.equals("routine")){
            System.out.println("token equals routine");
            getToken();
            if (!reservedWords.containsKey(currentToken)) {
                System.out.println("token equals name");
                getToken();
                if (currentToken.equals(";")) {
                    System.out.println("token equals ';'");
                    //getToken();
                }else{
                    throw new Exception("Error parsing at " + current + "expected ';' but found " + currentToken);
                }
            }else {
                throw new Exception("Error parsing at token number " + current + " routine name can be reserved word ");

            }
        }else {
            System.out.println("token not equals routine");
        }

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
            System.out.println("end of while loop in varDeclaration");
            //retrieveToken();
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
            System.out.println("end of varItem");
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

    private static void getToken() throws Exception {

        if (current >= tokenList.size()) {
            throw new Exception("End of input expected, but more tokens found. at token " + current);
        }
        String token = tokenList.get(current);
        currentToken = token;

        System.out.println("getToken()  '" + token + "'" + " number of token is " + current);
        current++;

    }

    private static void retrieveToken() throws Exception {
        //write a code that decreases current by 1 and returns the token at that position
        if (current == 0) {
            throw new Exception("Start of input expected, but more tokens found. at token " + current);
        }
        String token = tokenList.get(current - 1);
        currentToken = token;

    }
}
