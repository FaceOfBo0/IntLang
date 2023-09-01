package Parser;
import Parser.AST.*;
import Parser.AST.Statements.*;
import Parser.AST.Expressions.*;
import Token.*;
import Lexer.*;

import java.util.*;

enum Operator {
    BLANK,
    LOWEST,
    EQUALS,
    LESSGREATER,
    SUM,
    PRODUCT,
    PREFIX,
    CALL
}

interface PrefixParseFn {
    Expression parse();
}

interface InfixParseFn {
    Expression parse(Expression leftOp);
}

public class Parser {
    Lexer lex;
    Token curToken;
    Token peekToken;
    List<String> errors = new ArrayList<>(0);
    Map<TokenType, PrefixParseFn> prefixParseMap = new HashMap<>(0);
    Map<TokenType, InfixParseFn> infixParseMap = new HashMap<>(0);
    Map<TokenType, Operator> precedences = new HashMap<>(Map.of(TokenType.EQ, Operator.EQUALS,
            TokenType.BANG_EQ, Operator.EQUALS, TokenType.GREATER, Operator.LESSGREATER,
            TokenType.GREATER_EQ, Operator.LESSGREATER, TokenType.LESS, Operator.LESSGREATER,
            TokenType.LESS_EQ, Operator.LESSGREATER, TokenType.PLUS, Operator.SUM,
            TokenType.MINUS, Operator.SUM, TokenType.ASTERISK, Operator.PRODUCT,
            TokenType.SLASH, Operator.PRODUCT));

    public Parser(Lexer pLex) {
        this.lex = pLex;
        this.nextToken();
        this.nextToken();
        this.initParseFns();
    }

    private void initParseFns(){
        this.setPrefix(TokenType.IDENT, () -> new Identifier(this.curToken));
        this.setPrefix(TokenType.INT, () -> {
            IntegerLiteral intLit = new IntegerLiteral(this.curToken);
            intLit.setValue(Long.valueOf(this.curToken.literal));
            return intLit;
        });

        PrefixParseFn parsePrefixExpr = () -> {
            PrefixExpression prefExp = new PrefixExpression(this.curToken);
            prefExp.setOp(this.curToken.literal);
            this.nextToken();
            prefExp.setRight(this.parseExpression(Operator.PREFIX));
            return prefExp;
        };
        this.setPrefix(TokenType.MINUS, parsePrefixExpr);
        this.setPrefix(TokenType.BANG, parsePrefixExpr);

        InfixParseFn parseInfixExpr = (Expression left) -> {
            InfixExpression infExp = new InfixExpression(this.curToken);
            infExp.setOp(this.curToken.literal);
            infExp.setLeft(left);
            Operator precedence = this.curPrecedence();
            this.nextToken();
            infExp.setRight(this.parseExpression(precedence));
            return infExp;
        };

        this.setInfix(TokenType.MINUS, parseInfixExpr);
        this.setInfix(TokenType.PLUS, parseInfixExpr);
        this.setInfix(TokenType.LESS, parseInfixExpr);
        this.setInfix(TokenType.GREATER, parseInfixExpr);
        this.setInfix(TokenType.ASTERISK, parseInfixExpr);
        this.setInfix(TokenType.SLASH, parseInfixExpr);
        this.setInfix(TokenType.EQ, parseInfixExpr);
        this.setInfix(TokenType.BANG_EQ, parseInfixExpr);
    }

    private void setPrefix(TokenType pType, PrefixParseFn fn) {
        this.prefixParseMap.put(pType, fn);
    }

    private void setInfix(TokenType pType, InfixParseFn fn) {
        this.infixParseMap.put(pType, fn);
    }

    public Program parseProgram() {
        Program program = new Program();
        Statement statement;
        while (!this.curTokenIs(TokenType.EOF)) {
            if (this.curTokenIs(TokenType.SEMICOL)) {
                this.nextToken();
                continue;
            }
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
            default -> { return this.parseExpressionStatement(); }
        }
    }

    private Statement parseLetStatement() {
        LetStatement stmt = new LetStatement(curToken);

        if (expectedPeekNot(TokenType.IDENT)) {
            System.out.println("Parser Error: " + this.errors.get(this.errors.size()-1));
            return null;
        }

        stmt.setName(new Identifier(this.curToken));

        if (expectedPeekNot(TokenType.ASSIGN)) {
            System.out.println("Parser Error: " + this.errors.get(this.errors.size()-1));
            return null;
        }
        this.nextToken();

        stmt.setValue(this.parseExpression(Operator.LOWEST));

        return stmt;
    }

    private Expression parseExpression(Operator op) {
        PrefixParseFn prefix = this.prefixParseMap.get(this.curToken.type);
        if (prefix == null) {
            this.noPrefixParseFnError(this.curToken.type);
            return null;
        }
        Expression leftExp = prefix.parse();

        while(!this.peekTokenIs(TokenType.SEMICOL) && op.ordinal() < this.peekPrecedence().ordinal()) {
            InfixParseFn infix = this.infixParseMap.get(this.peekToken.type);
            if (infix == null)
                return null;
            this.nextToken();
            leftExp = infix.parse(leftExp);
        }
        return leftExp;
    }

    private ExpressionStatement parseExpressionStatement() {
        ExpressionStatement stmt = new ExpressionStatement(this.curToken);

        stmt.setValue(this.parseExpression(Operator.LOWEST));

        if (this.peekTokenIs(TokenType.SEMICOL))
            this.nextToken();

        return stmt;
    }

    private Statement parseReturnStatement() {
        ReturnStatement stmt = new ReturnStatement(curToken);
        this.nextToken();

        stmt.setValue(this.parseExpression(Operator.LOWEST));

        return stmt;
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

    private Operator peekPrecedence() {
        return this.precedences.getOrDefault(this.peekToken.type, Operator.LOWEST);
    }

    private Operator curPrecedence() {
        return this.precedences.getOrDefault(this.curToken.type, Operator.LOWEST);
    }

    private boolean peekTokenIs(TokenType pType) {
        return this.peekToken.type == pType;
    }

    private boolean curTokenIs(TokenType pType) { return this.curToken.type == pType; }

    private void noPrefixParseFnError(TokenType pTokenType) {
        this.errors.add("no prefix parse function for "+ pTokenType + "found");
    }

    private void peekError(TokenType pType) {
        this.errors.add("Expected next Token to be "+ pType +", got "+ this.peekToken.type +" instead!");
    }

    // public List<String> getErrors(){ return this.errors; }
}
