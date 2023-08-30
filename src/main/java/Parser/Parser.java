package Parser;
import Parser.AST.*;
import Parser.AST.Statements.*;
import Parser.AST.Expressions.*;
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
        this.lex = pLex;
        this.nextToken();
        this.nextToken();
    }

    public Program parseProgram() {
        Program program = new Program();
        Statement statement;
        while (this.curToken.type != TokenType.EOF) {
            statement = parseStatement();
            if (statement != null)
                program.statements.add(statement);
            this.nextToken();
        }
        return program;
    }

    private Statement parseStatement(){
        switch (this.curToken.type){
            case LET -> { return this.parseLetStatement(); }
            case RETURN -> { return this.parseReturnStatement(); }
            default -> { return null; }
        }
    }

    private Statement parseLetStatement() {
        LetStatement stmt = new LetStatement(curToken);

        if (expectedPeekNot(TokenType.IDENT)) {
            System.out.println("Parser Error: " + this.errors.get(this.errors.size()-1));
            return null;
        }

        stmt.setName(parseIdent());


        if (expectedPeekNot(TokenType.ASSIGN)) {
            System.out.println("Parser Error: " + this.errors.get(this.errors.size()-1));
            return null;
        }
        this.nextToken();

        // TODO: We're skipping the expressions until we encounter a semicolon
        // Expression value = parseExpression();

        while (!this.curTokenIs(TokenType.SEMICOL)) {
            this.nextToken();
        }

        return stmt;
    }

    private Expression parseExpression() {
        if (this.curTokenIs(TokenType.INT)) {
            if (this.peekTokenIs(TokenType.PLUS))
                return this.parseOpExpression();
            else if (this.peekToken.type == TokenType.SEMICOL)
                return this.parseInt();
        }
        return null;
    }

    private Statement parseReturnStatement() {
        ReturnStatement stmt = new ReturnStatement(curToken);
        this.nextToken();

        // TODO: We're skipping the expressions until we encounter a semicolon
        // Expression value = parseExpression();

        while (!this.curTokenIs(TokenType.SEMICOL)) {
            this.nextToken();
        }
        return stmt;
    }

    private Expression parseIfExpression() {
        return null;
    }

    private Expression parseInt() {
        return null;
    }

    private Expression parseOpExpression() {
        return null;
    }

    private Identifier parseIdent(){
        return new Identifier(this.curToken);
    }

    public void nextToken() {
        this.curToken = this.peekToken;
        this.peekToken = this.lex.nextToken();
    }

    private boolean expectedPeekNot(TokenType pType) {
        if (peekToken.type == pType) {
            this.nextToken();
            return false;
        }
        else {
            this.peekError(pType);
            return true;
        }
    }

    private boolean peekTokenIs(TokenType pType) {
        return this.peekToken.type == pType;
    }

    private boolean curTokenIs(TokenType pType) {
        return this.curToken.type == pType;
    }

    private void peekError(TokenType pType) {
        this.errors.add("Expected next Token to be "+ pType +", got "+ this.peekToken.type +" instead!");
    }

    public List<String> getErrors(){ return this.errors; }
}
