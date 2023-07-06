package com.birzeit.recursivedescentparser.scanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {

    public static List<String> tokenize(String input) {
        List<String> tokens = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\w+|<|<=|>|>=|<>|\\*|/|%|\\+|-|=|:=|;|,|:|\\(|\\)|\\.");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String token = matcher.group();
            tokens.add(token);
        }

        return tokens;
    }

    public static void main(String[] args) {

        List<String> list = tokenize("project MyProject;\n" +
                "const PI = 3;\n" +
                "const MAX_VALUE= 100;\n" +
                "\n" +
                "routine myRoutine;\n" +
                "var x,y,z: int;\n" +
                "start\n" +
                "    ;\n" +
                "    x:=5;\n" +
                "    y:=10;\n" +
                "           \n" +
                "    output(x);\n" +
                "    input(y);\n" +
                "\n" +
                "    if (x<y)\n" +
                "        then\n" +
                "            output(x)\n" +
                "        else\n" +
                "            output(y)\n" +
                "    endif;\n" +
                "\n" +
                "    loop (x<MAX_VALUE)\n" +
                "        do\n" +
                "            x := x + 1 + y * (3 / y);\n" +
                "\n" +
                "end;\n" +
                "\n" +
                "start\n" +
                "    output(myRoutine);\n" +
                "    output(0);\n" +
                "end.");
        for (String s : list) {
            System.out.println(s);
        }


    }



}
