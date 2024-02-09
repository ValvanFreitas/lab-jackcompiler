package br.ufma.ecp.token;

import java.util.List;
import java.util.Map;

public enum TokenType {
    PLUS,MINUS,

     // Literals.
    NUMBER,STRING,IDENT,COMMA,SEMICOLON,RBRACKET,LBRACKET,
    RBRACE,LBRACE,RPAREN,LPAREN,EQ,LT,GT,NOT,OR,AND,
    ASTERISK,DOT,SLASH,
     // keywords
    WHILE, CLASS,CONSTRUCTOR,FUNCTION,
    METHOD,FIELD,STATIC,VAR,INT,
    CHAR,BOOLEAN,VOID,TRUE,FALSE,
    NULL,THIS,LET,DO,IF,ELSE, RETURN,/**
     *
     */
    EOF,ILLEGAL;

    static public boolean isSymbol (char c) {
        String symbols = "{}()[].,;+-*/&|<>=~";
        return symbols.indexOf(c) > -1;}

    static public boolean isKeyword (TokenType type) {
        List<TokenType> keywords  = 
            List.of(
                WHILE, CLASS,CONSTRUCTOR,FUNCTION,
                METHOD,FIELD,STATIC,VAR,INT,
                CHAR,BOOLEAN,VOID,TRUE,FALSE,
                NULL,THIS,LET,DO,IF,ELSE,RETURN);
            return keywords.contains(type);} }
}
