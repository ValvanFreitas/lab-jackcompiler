package br.ufma.ecp;
import br.ufma.ecp.token.Token;
import br.ufma.ecp.token.TokenType;
import static br.ufma.ecp.token.TokenType.*;

public class Parser {

    private static class ParseError extends RuntimeException {}
 
     private Scanner scan;
     private Token currentToken;
     private Token peekToken;
     //Possibilita concatenar, através do Stringbuilder, para formar string
     private StringBuilder xmlOutput = new StringBuilder();

     //Constructor - receives the inputs
     public Parser(byte[] input) {
         scan = new Scanner(input);
         nextToken();}
    
     //Passa o futuro Token, PeekToken é o proximo token
     private void nextToken() {
         currentToken = peekToken;
         peekToken = scan.nextToken();}
 
 
     public void parse () {}
   
     // 'do' subrotineCall ';'
     public void parseDo() {
        printNonTerminal("doStatement");
        expectPeek(DO);
        expectPeek(IDENT);
        parseSubroutineCall();
        expectPeek(SEMICOLON);
        printNonTerminal("/doStatement");}

    //casos da estrutura necessária para leitura da subroutineCall -> subroutineName '(' expressionList ')' | (className|varName)
    // '.' subroutineName '(' expressionList ')
    void parseSubroutineCall() {
        if (peekTokenIs(LPAREN)) {
            expectPeek(LPAREN);
            parseExpressionList();
            expectPeek(RPAREN);} 
        else {
            expectPeek(DOT);
            expectPeek(IDENT);
            expectPeek(LPAREN);
            parseExpressionList();
            expectPeek(RPAREN);}}
    
    //Pode ser vazio ou ter uma expressão
    void parseExpressionList() {
        printNonTerminal("expressionList");

        if (!peekTokenIs(RPAREN)) // verifica se há pelo menos uma expressão
        {
            parseExpression();}

        // procurando as demais expressões
        while (peekTokenIs(COMMA)) {
            expectPeek(COMMA);
            parseExpression();}

        printNonTerminal("/expressionList");}    

     //estrutura necessária para leitura do letStatement -> 'let' identifier( '[' expression ']' )? '=' expression ';’
     void parseLet() {
        printNonTerminal("letStatement");
        expectPeek(LET);
        expectPeek(IDENT);

        if (peekTokenIs(LBRACKET)) {
            expectPeek(LBRACKET);
            parseExpression();
            expectPeek(RBRACKET);}

        expectPeek(EQ);
        parseExpression();
        expectPeek(SEMICOLON);
        printNonTerminal("/letStatement");}

     void parseExpression() {
        printNonTerminal("expression"); //Teste de unidade
        parseTerm ();
        while (isOperator(peekToken.lexeme)) {
            expectPeek(peekToken.type);
            parseTerm();}
        printNonTerminal("/expression");}

     void parseTerm() {
        printNonTerminal("term");
        switch (peekToken.type) {
          case NUMBER:
            expectPeek(TokenType.NUMBER);
            break;
          case STRING:
            expectPeek(TokenType.STRING);
            break;
          case FALSE:
          case NULL:
          case TRUE:
            expectPeek(TokenType.FALSE, TokenType.NULL, TokenType.TRUE);
            break;
          case THIS:
            expectPeek(TokenType.THIS);
            break;
          case IDENT:
            expectPeek(TokenType.IDENT);
            break;
          default:
            throw error(peekToken, "term expected");}
    
        printNonTerminal("/term");}
 
     // Funções Auxiliares
    
     //Verifica se é operador
     static public boolean isOperator(String op) {
        return op!= "" && "+-*/<>=~&|".contains(op);}
     
     //Transforma em string
     public String XMLOutput() {
         return xmlOutput.toString();}
    
     //Imprime strings não terminais
     private void printNonTerminal(String nterminal) {
         xmlOutput.append(String.format("<%s>\r\n", nterminal));}
 
     //Método para comparar tokens
     boolean peekTokenIs(TokenType type) {
         return peekToken.type == type;}
     //Método para comparar tokens
     boolean currentTokenIs(TokenType type) {
         return currentToken.type == type;}
    
     //Verificar os toekns a frente
     private void expectPeek(TokenType... types) {  //se for o token esperado, ele avança e imprime esse token
         for (TokenType type : types) {
             if (peekToken.type == type) {
                 expectPeek(type);
                 return;}}
 
        throw error(peekToken, "Expected a statement");}
 
     private void expectPeek(TokenType type) {
         if (peekToken.type == type) {
             nextToken();
             xmlOutput.append(String.format("%s\r\n", currentToken.toString()));
         } else {
             throw error(peekToken, "Expected "+type.name());}}
 
     //Geração de mensagens de error 
     private static void report(int line, String where,
         String message) {
             System.err.println(
             "[line " + line + "] Error" + where + ": " + message);} 
     //Geração de mensagens de error 
     private ParseError error(Token token, String message) {
         if (token.type == TokenType.EOF) {
             report(token.line, " at end", message);} 
         else {
             report(token.line, " at '" + token.lexeme + "'", message);}
         return new ParseError();}}
