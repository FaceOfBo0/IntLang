package Parser;
import Parser.AST.*;
import Token.*;
import Lexer.*;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    Lexer lex;
    Token curToken;
    Token peekToken;
    List<String> errors = new ArrayList<>(0);

    public Parser(Lexer pLex) {
        lex = pLex;
        nextToken();
        nextToken();
    }

    public Program parseProgram() {
        Program program = new Program();
        Statement statement;
        while (curToken.type != TokenType.EOF) {
            statement = parseStatement();
            if (statement != null)
                program.statements.add(statement);
            nextToken();
        }
        return program;
    }

    private Statement parseStatement(){
        switch (curToken.type){
            case LET -> { return parseLetStatement(); }
            case RETURN -> { return parseReturnStatement(); }
            // case IF -> { return parseIfStatement(); }
            default -> { return null; }
        }
    }

    private Statement parseLetStatement() {
        LetStatement stmt = new LetStatement(curToken);

        if (expectedPeekNot(TokenType.IDENT)) {
            System.out.println("Parser Error: " + errors.get(errors.size()-1));
            return null;
        }

        stmt.setName(parseIdent());


        if (expectedPeekNot(TokenType.ASSIGN)) {
            System.out.println("Parser Error: " + errors.get(errors.size()-1));
            return null;
        }
        nextToken();

        // Expression value = parseExpression();
        while (!curTokenIs(TokenType.SEMICOL)) {
            nextToken();
        }

        return stmt;
    }

    private Expression parseExpression() {
        if (curTokenIs(TokenType.INT)) {
            if (peekTokenIs(TokenType.PLUS))
                return parseOperatorExp();
            else if (peekToken.type == TokenType.SEMICOL)
                return parseInt();
        }
        return null;
    }

    private Statement parseReturnStatement() {
        ReturnStatement stmt = new ReturnStatement(curToken);

        return stmt;
    }

    private Statement parseIfStatement() {
        return null;
    }

    private Expression parseInt() {
        return null;
    }

    private Expression parseOperatorExp() {
        return null;
    }

    private Identifier parseIdent(){
        return new Identifier(curToken, curToken.literal);
    }

    public void nextToken() {
        curToken = peekToken;
        peekToken = lex.nextToken();
    }

    private boolean expectedPeekNot(TokenType pType) {
        if (peekToken.type == pType) {
            nextToken();
            return false;
        }
        else {
            peekError(pType);
            return true;
        }
    }

    private boolean peekTokenIs(TokenType pType) {
        return peekToken.type == pType;
    }

    private boolean curTokenIs(TokenType pType) {
        return curToken.type == pType;
    }

    private void peekError(TokenType pType) {
        errors.add("Expected next Token to be "+ pType +", got "+ peekToken.type +" instead!");
    }

    public List<String> getErrors(){ return errors; }
}
