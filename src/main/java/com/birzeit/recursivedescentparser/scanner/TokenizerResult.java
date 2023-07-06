package com.birzeit.recursivedescentparser.scanner;

import java.util.HashMap;
import java.util.List;

public class TokenizerResult {

    private List<String> tokens;
    private HashMap<String, String> nonTerminals;
    private HashMap<String, String> terminals;
    private HashMap<String, String> reservedWords;

    public TokenizerResult(List<String> tokens, HashMap<String, String> nonTerminals, HashMap<String, String> terminals, HashMap<String, String> reservedWords) {
        this.tokens = tokens;
        this.nonTerminals = nonTerminals;
        this.terminals = terminals;
        this.reservedWords = reservedWords;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }

    public HashMap<String, String> getNonTerminals() {
        return nonTerminals;
    }

    public void setNonTerminals(HashMap<String, String> nonTerminals) {
        this.nonTerminals = nonTerminals;
    }

    public HashMap<String, String> getTerminals() {
        return terminals;
    }

    public void setTerminals(HashMap<String, String> terminals) {
        this.terminals = terminals;
    }

    public HashMap<String, String> getReservedWords() {
        return reservedWords;
    }

    public void setReservedWords(HashMap<String, String> reservedWords) {
        this.reservedWords = reservedWords;
    }

    @Override
    public String toString() {
        return "TokenizerResult{" + "tokens=" + tokens + ", nonTerminals=" + nonTerminals + ", terminals=" + terminals + ", reservedWords=" + reservedWords + '}';
    }
}
