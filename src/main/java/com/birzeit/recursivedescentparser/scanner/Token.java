package com.birzeit.recursivedescentparser.scanner;

public class Token {

    private  String value;
    private  String type;//non-terminal or terminal
    private Boolean isReservedWord;

    public Token(String value, String type, Boolean isReservedWord) {
        this.value = value;
        this.type = type;
        this.isReservedWord = isReservedWord;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getReservedWord() {
        return isReservedWord;
    }

    public void setReservedWord(Boolean reservedWord) {
        isReservedWord = reservedWord;
    }

    @Override
    public String toString() {
        return "Token{" +
                "value='" + value + '\'' +
                ", type='" + type + '\'' +
                ", isReservedWord=" + isReservedWord +
                '}';
    }
}
