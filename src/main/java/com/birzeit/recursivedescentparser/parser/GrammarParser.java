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
    public static char currentChar ;
    public static int currentLine = 1;

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
                "  num:int;\n" +
                "  \n" +
                "routine loop;\n" +
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

        //project-declaration  project-def     "."

        System.out.println("enter projectDeclaration");
        projectDef();
        getToken();
        if (currentToken.equals(".")) {
            System.out.println("input successfully parsed ...");
        } else {
            throw new Exception("Error at line " + currentLine + " parsing at token number " + current + " expected '.' but found " + currentToken);
        }


    }

    private void projectDef() throws Exception {

        //project-def     project-heading   declarations   compound-stmt

        projectHeading();
        declarations();
        compoundStatement();

    }

    private void declarations() throws Exception {

        //declarations  const-decl  var-decl  subroutine-decl

        if (currentToken.equals("start")) {
            System.out.println("token equals start in declarations");
            compoundStatement();
        } else {
            System.out.println("enter declarations");
            constDeclaration();
            System.out.println("finished constDeclaration");
            varDeclaration();
            System.out.println("finished varDeclaration");
            subroutineDeclaration();
        }


    }

    private void subroutineDeclaration() throws Exception {

        //subroutine-decl  subroutine-heading  declarations  compound-stmt  “;”  |  lambda

        System.out.println("enter subroutineDeclaration");
        //subroutine-heading
        subroutineHeading();
        declarations();
        //compound-statement
        compoundStatement();
        System.out.println("current token after compound statement is " + currentToken);
        if (currentToken.equals(";")) {
            System.out.println("subroutine declaration valid");
        } else {
            throw new Exception("Error at line " + currentLine + " : error parsing at token number " + current + " expected ';' but found " + currentToken);
        }


    }

    private void compoundStatement() throws Exception {

        //compound-stmt  start  stmt-list end

        System.out.println("enter compoundStatement");
        if (currentToken.equals("start")) {
            getToken();
            stmtList();
            System.out.println("current token after stmt list is " + currentToken);
            if (currentToken.equals(";")) {
                return;
            }
            if (currentToken.equals("end")) {
                System.out.println("compound statement end with 'end'");
                getToken();
                System.out.println("current token is " + currentToken);

            } else {
                throw new Exception("Error at line " + currentLine + " : error parsing at token number " + current + " expected 'end' but found " + currentToken);
            }
        } else {
            throw new Exception("Error parsing at token number " + current + " expected 'start' but found " + currentToken);
        }

    }

    private void stmtList() throws Exception {

        //stmt-list    ( statement    ";" )*

        if (currentToken.equals("end")) {
            System.out.println("stmt list is empty");
        } else {
            while (!currentToken.equals("end")) {
                statement();
                getToken();
                System.out.println("current token after stmt list is " + currentToken);
                if (currentToken.equals(";")) {
                    System.out.println("stmt list valid");
                    getToken();
                } else {
                    throw new Exception("Error at line " + currentLine + " : error parsing at token number " + current + " expected ';' but found " + currentToken);
                }
            }
        }

    }

    private void statement() throws Exception {

        //statement  ass-stmt  |  inout-stmt  | if-stmt | loop-stmt | compound-stmt | lambda

        System.out.println("enter statement");
        System.out.println("current token in statement is " + currentToken);
        if (currentToken.equals("input") || currentToken.equals("output")) {
            inoutStmt();
        } else if (currentToken.equals("if")) {
            ifStmt();
        } else if (currentToken.equals("loop")) {
            loopStmt();
        } else if (currentToken.equals("start")) {
            compoundStatement();
        } else {
            if (!reservedWords.containsKey(currentToken)) {
                assStmt();
            } else {
                throw new Exception("Error at line " + currentLine + " : error parsing at token number " + current + " cant resolve statement name value as reserved word ");
            }
        }

        //inout-stmt
        inoutStmt();
        //ass-stmt
        assStmt();
        //if-stmt
        ifStmt();
        System.out.println("end of if stmt");
        //loop-stmt
        //compound-stmt

    }

    private void loopStmt() throws Exception{

        //	loop-stmt  loop   “(“    bool-exp   “)”  do   statement

        if(currentToken.equals("loop")){
            getToken();
            if (currentToken.equals("(")){
                boolExp();
                getToken();
                if (currentToken.equals(")")){
                    getToken();
                    if (currentToken.equals("do")){
                        statement();
                    }else{
                        throw new Exception("Error at line " + currentLine + " : error parsing at token number " + current + " expected 'do' but found '" + currentToken+"'");
                    }
                }else {
                    throw new Exception("Error at line " + currentLine + " : error parsing at token number " + current + " expected ')' but found '" + currentToken+"'");
                }
            }else {
                throw new Exception("Error at line " + currentLine +  " : error parsing at token number " + current + " expected '(' but found '" + currentToken+"'");
            }
        }

    }

    private void ifStmt() throws Exception {

        //if-stmt  if  “(“ bool-exp   “)”  then  statement  else-part  endif

        System.out.println("if statement enter");
        if (currentToken.equals("if")) {
            getToken();
            if (currentToken.equals("(")) {
                boolExp();
                getToken();
                if (currentToken.equals(")")) {
                    getToken();
                    if (currentToken.equals("then")) {
                        statement();
                        elsePart();
                        getToken();
                        if (currentToken.equals("endif")) {
                            System.out.println("valid if stmt");
                        } else {
                            throw new Exception("Error at line " + currentLine + " : error parsing at token number " + current + " expected 'endif' but found " + currentToken);
                        }
                    } else {
                        throw new Exception("Error at line " + currentLine + " : error parsing at token number " + current + " expected 'then' but found " + currentToken);
                    }
                } else {
                    throw new Exception("Error at line " + currentLine + " : error parsing at token number " + current + " expected ')' but found " + currentToken);
                }
            }
        }
    }

    private void elsePart() throws Exception {

        //	else-part   else     statement   |   lambda

        System.out.println("enter else part");
        if (currentToken.equals("else")) {
            getToken();
            statement();
        } else {
            System.out.println("else part is empty");
        }

    }

    private void boolExp() throws Exception {

        //	bool-exp  name-value relational-oper  name-value

        System.out.println("enter bool exp");
        nameValue();
        relationalOper();
        nameValue();

    }

    private void relationalOper() throws Exception {

        //	relational-oper       "="     |     "<>"     |     "<"    |     "<="     |     ">"    |     ">="
        getToken();
        if (currentToken.equals("=") || currentToken.equals("<>") || currentToken.equals("<") || currentToken.equals("<=") || currentToken.equals(">") || currentToken.equals(">=")) {
            System.out.println("relational operator is valid");
        } else {
            throw new Exception("Error at line " + currentLine +  " : error parsing at token number " + current + " expected relational operator but found " + currentToken);
        }

    }

    private void assStmt() throws Exception {

        //	ass-stmt  ”name”  ":="  arith-exp

        System.out.println("enter assStmt");
        getToken();
        getToken();
        if (!currentToken.equals("loop") && !currentToken.equals("start") && !currentToken.equals("if") && !currentToken.equals("end")) {
            System.out.println("curren token in assStmt if statement is " + currentToken);
            if (!reservedWords.containsKey(currentToken)) {
                System.out.println("stmt name is not a reserved word");
                getToken();
                if (currentToken.equals(":=")) {
                    System.out.println("token after stmt name is ':='");
                    arthExp();
                } else {
                    throw new Exception("Error at line " + currentLine + " : error parsing at token number " + current + " expected is ':=' but found " + currentToken);
                }
            } else {
                throw new Exception("Error at line " + currentLine + " : error parsing at token number " + current + " cant define ass-stmt name as reserved word");
            }
        } else {
            retrieveToken();
            retrieveToken();
        }
        System.out.println("current token in assStmt is " + currentToken);


    }

    private void arthExp() throws Exception {

        //arith-exp  term    ( add-sign      term )*

        System.out.println("enter athExp");
        term();
        while (currentToken.equals("+") || currentToken.equals("-")) {
            addSign();
            term();
        }


    }

    private void addSign() throws Exception {

        System.out.println("enter addSign");
        getToken();
        System.out.println("current token in addSign " + currentToken);
        if (currentToken.equals("+") || currentToken.equals("-")) {
            System.out.println("add sign found");
        } else {
            retrieveToken();
        }

    }

    private void term() throws Exception {

        //	term  factor    ( mul-sign       factor  )*

        System.out.println("enter term");
        while (currentToken.equals("*") || currentToken.equals("/") || !currentToken.equals("%") ) {
            factor();
            mulSign();
        }

    }

    private void mulSign() throws Exception{

        // mul-sign  "*"    |  "/"  |  “%”
        getToken();
        if (currentToken.equals("*") || currentToken.equals("/") || currentToken.equals("%")) {
            System.out.println("mul sign found");
        } else {
            throw new Exception("Error at line " + currentLine +  " : error parsing at token number " + current + " expected mul-sign but found " + currentToken);
        }

    }

    private void factor() throws Exception {

        //factor   "("   arith-exp  ")"   |     name-value

        getToken();
        System.out.println("enter factor");
        System.out.println("current token in factor is " + currentToken);
        if (currentToken.equals("(")) {
            arthExp();
            getToken();
            if (currentToken.equals(")")) {
                System.out.println("factor end with )");
            } else {
                throw new Exception("Error at line " + currentLine + " : error parsing at token number " + current + " expected ')' but found " + currentToken);
            }
        } else {
            nameValue();
        }

    }


    private void inoutStmt() throws Exception {

        //	inout-stmt  input "("    "name"     ")"    |    output  "("   name-value   ")"

        System.out.println("enter inoutStmt");
        System.out.println("curren token is " + currentToken);
        if (currentToken.equals("input")) {
            System.out.println("if current equal input");
            getToken();
            if (currentToken.equals("(")) {
                getToken();
                if (!reservedWords.containsKey(currentToken)) {
                    getToken();
                    if (currentToken.equals(")")) {
                        System.out.println("input stmt valid");
                    } else {
                        throw new Exception("Error at line " + currentLine + " : error parsing at token number " + current + " expected ')' but found " + currentToken);
                    }
                } else {
                    throw new Exception("Error at line " + currentLine + " : error parsing at token number " + current + " input name cant be reserved word");
                }
            } else {
                throw new Exception("Error at line " + currentLine + " error parsing at token number " + current + " expected '(' but found '" + currentToken + "'");
            }
        } else if (currentToken.equals("output")) {
            System.out.println("else if current equal output");
            getToken();
            if (currentToken.equals("(")) {
                getToken();
                nameValue();
                getToken();
                if (currentToken.equals(")")) {
                    System.out.println("output stmt valid");
                } else {
                    throw new Exception("Error at line " + currentLine + " : error parsing at token number " + current + " expected ')' but found " + currentToken);
                }

            } else {
                throw new Exception("Error at line " + currentLine + " : error parsing at token number " + current + " expected '(' but found '" + currentToken + "'");
            }
        }

    }


    private void subroutineHeading() throws Exception {

        //subroutine-heading  routine  "name"    ";"

        System.out.println("enter subroutineHeading");
        System.out.println("current token is : " + currentToken);
        if (currentToken.equals("routine")) {
            System.out.println("token equals routine");
            getToken();
            if (!reservedWords.containsKey(currentToken)) {
                System.out.println("token equals name");
                getToken();
                if (currentToken.equals(";")) {
                    System.out.println("token equals ';'");
                    //getToken();
                } else {
                    throw new Exception("Error at line " + currentLine + " : error parsing at " + current + "expected ';' but found " + currentToken);
                }
            } else {
                throw new Exception("Error at line " + currentLine + " : error parsing at token number " + current + " routine name can be reserved word ");

            }
        } else {
            System.out.println("token not equals routine");
        }

    }

    private void varDeclaration() throws Exception {

        System.out.println("varDeclaration");
        System.out.println("current token : " + currentToken);
        if (currentToken.equals("var")) {
            System.out.println("token equals var");
            getToken();
            while (!currentToken.equals("routine") && !currentToken.equals("start") && !reservedWords.containsKey(currentToken)) {
                System.out.println("while loop ");
                System.out.println("current token in loop : " + currentToken);
                varItem();
                getToken();
                if (currentToken.equals(";")) {
                    System.out.println("token equals ';' ");
                    getToken();
                } else {
                    throw new Exception("Error at line " + currentLine + " : error parsing at " + current + "expected ';' but found " + tokenList.get(current));
                }
            }
            System.out.println("end of while loop in varDeclaration");
            //retrieveToken();
        } else {
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
                throw new Exception("Error at line " + currentLine + " : error parsing at " + current + "expected 'int' but found " + tokenList.get(current));
            }
            System.out.println("end of varItem");
        } else {
            throw new Exception("Error at line " + currentLine +  " : error parsing at token number " + current + "expected ':' but found " + tokenList.get(current));
        }
    }

    private void nameValue() throws Exception {

        //name-value   "name"  |  "int-value"
        System.out.println("current token in nameValue is '" + currentToken + "'");

        getToken();
        if (isParsableToInt(currentToken)) {
            System.out.println("token equals int-value in nameValue");
        } else if (!reservedWords.containsKey(currentToken)) {
            System.out.println("token equals name in nameValue");
        } else {
            throw new Exception("Error at line " + currentLine + " : error parsing at " + current + "name value cant be a reserved word -> " + tokenList.get(current));
        }

    }


    private void nameList() throws Exception {
        //name-list   "name" (","  "name")^*
        System.out.println("current token in nameList is '" + currentToken + "'");
        if (!reservedWords.containsKey(currentToken)) {
            System.out.println("token equals name in nameList");
            getToken();
            System.out.println("current token in nameList is '" + currentToken + "'");
            while (currentToken.equals(",")) {
                getToken();
                if (!reservedWords.containsKey(currentToken)) {
                    System.out.println("token equals name in loop");
                    getToken();
                } else {
                    throw new Exception("Error at line " + currentLine + " : error parsing at " + current + "var name cant be a reserved word -> " + tokenList.get(current));
                }
            }
        } else {
            throw new Exception("Error at line " + currentLine + " : error parsing at " + current + "var name cant be a reserved word -> " + tokenList.get(current));
        }
    }


    private void constDeclaration() throws Exception {

        //const-decl  const ( const-item  ";" )^+  | lambda

        System.out.println("constDeclaration");
        getToken();
        if (currentToken.equals("const")) {
            System.out.println("token equals const");
            getToken();
            while (!currentToken.equals("var") && !currentToken.equals("routine") && !currentToken.equals("start") && !reservedWords.containsKey(currentToken)) {
                System.out.println("while loop");
                System.out.println("current token in loop : " + currentToken);
                constItem();
                getToken();
                if (currentToken.equals(";")) {
                    System.out.println("token equals ';' ");
                    getToken();
                } else {
                    throw new Exception("Error at line " + currentLine + " : error parsing at " + current + "expected ';' but found " + tokenList.get(current));
                }
            }
        } else {
            System.out.println("retrieve token");
            retrieveToken();
            System.out.println("current token : " + currentToken);
        }

    }

    private void constItem() throws Exception {

        //	const-item     "name"   =   "integer-value"

        System.out.println("enter constItem");
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
                    throw new Exception("Error at line " + currentLine + " : error parsing at " + current + "expected integer value but found " + currentToken);
                }
            } else {
                System.out.println("Curren token in exception !! is  " + currentToken);
                throw new Exception("Error at line " + currentLine + " error parsing at token number " + current + " expected '=' but found " + currentToken);
            }
        } else {
            throw new Exception("Error at line " + currentLine +  " : error parsing at " + current + "const name cant be a reserved word -> " + currentToken);
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
                    throw new Exception("Error at line " + currentLine + " : error parsing at " + current + "expected ; but found " + tokenList.get(current));
                }
            } else {
                throw new Exception("Error at line " + currentLine + " : error parsing at token number " + current + " project name cant be a reserved word ");
            }
        } else {
            throw new Exception("Error at line " + currentLine + " : error parsing at " + current + "expected project keyword but found " + tokenList.get(current));
        }


    }

    private static void getToken() throws Exception {

        if (current >= tokenList.size()) {
            throw new Exception("End of input expected, but more tokens found. at token " + current);
        }
        String token = tokenList.get(current);
        currentToken = token;

        if (currentChar == '\n') {
            currentLine++;
        }

        System.out.println("getToken()  '" + token + "'" + " number of token is " + current);
        current++;

    }

    private static void retrieveToken() throws Exception {

        if (current == 0) {
            throw new Exception("Start of input expected, but more tokens found. at token " + current);
        }
        String token = tokenList.get(current - 1);
        currentToken = token;

    }

    public static boolean isParsableToInt(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
