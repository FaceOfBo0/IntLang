package Parser;
import Parser.AST.*;
import Parser.AST.Statements.*;
import Parser.AST.Expressions.*;
import Token.*;
import Lexer.*;

import java.util.*;

enum Precedence {
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
    Token curToken = null;
    Token peekToken = null;
    List<Token> tokens = new ArrayList<>(0);
    List<String> errors = new ArrayList<>(0);
    Map<TokenType, PrefixParseFn> prefixParseMap = new HashMap<>(0);
    Map<TokenType, InfixParseFn> infixParseMap = new HashMap<>(0);
    Map<TokenType, Precedence> precedences = new HashMap<>(Map.of(TokenType.EQ, Precedence.EQUALS,
            TokenType.BANG_EQ, Precedence.EQUALS, TokenType.GREATER, Precedence.LESSGREATER,
            TokenType.GREATER_EQ, Precedence.LESSGREATER, TokenType.LESS, Precedence.LESSGREATER,
            TokenType.LESS_EQ, Precedence.LESSGREATER, TokenType.PLUS, Precedence.SUM,
            TokenType.MINUS, Precedence.SUM, TokenType.ASTERISK, Precedence.PRODUCT,
            TokenType.SLASH, Precedence.PRODUCT));

    public Parser(Lexer pLex) {
        this.lex = pLex;
        this.precedences.put(TokenType.LPAREN, Precedence.CALL);
        this.nextToken();
        this.nextToken();
        this.initParseFns();
    }

    private void initParseFns(){
        // Identifiers & Integer Literals
        this.setPrefix(TokenType.IDENT, () -> new Identifier(this.curToken, this.curToken.literal()));
        this.setPrefix(TokenType.INT, () -> new IntegerLiteral(this.curToken, Long.parseLong(this.curToken.literal())));
        // Boolean Literals
        this.setPrefix(TokenType.FALSE, () -> new BooleanLiteral(this.curToken, false));
        this.setPrefix(TokenType.TRUE, () -> new BooleanLiteral(this.curToken, true));
        // Grouped Expressions
        this.setPrefix(TokenType.LPAREN, () -> {
            this.nextToken();
            Expression exp = this.parseExpression(Precedence.LOWEST);
            if (this.expectedPeekNot(TokenType.RPAREN))
                return null;
            return exp;
        });
        // --- [Call Expression] ---
        this.setInfix(TokenType.LPAREN, (Expression function) -> {
            Token callTok = this.curToken;
            this.nextToken();
            List<Expression> params = this.parseCallParameters();
            return new CallExpression(callTok, function, params);
        });
        // --- [If Expression] ---
        this.setPrefix(TokenType.IF, () -> {
            Token ifToken = this.curToken;
            if (this.expectedPeekNot(TokenType.LPAREN))
                return null;
            this.nextToken();
            Expression condition = this.parseExpression(Precedence.LOWEST);
            if (this.expectedPeekNot(TokenType.RPAREN))
                return null;
            if (this.expectedPeekNot(TokenType.LBRACE))
                return null;
            BlockStatement consequence = this.parseBlockStatement();
            BlockStatement alternative = null;
            if (this.peekTokenIs(TokenType.ELSE)) {
                this.nextToken();
                if (this.expectedPeekNot(TokenType.LBRACE))
                    return null;
                alternative = this.parseBlockStatement();
            }
            return new IfExpression(ifToken, condition, consequence, alternative);
        });
        // --- [Function Expression] ---
        this.setPrefix(TokenType.FUNC, () -> {
            Token fnTok = this.curToken;
            if (this.expectedPeekNot(TokenType.LPAREN))
                return null;
            this.nextToken();
            List<Identifier> params = this.parseFunctionParameters();
            if (this.expectedPeekNot(TokenType.LBRACE))
                return null;
            BlockStatement body = this.parseBlockStatement();
            return new FunctionLiteral(fnTok, params, body);
        });
        // --- [Prefix Expressions] ---
        PrefixParseFn parsePrefixExpr = () -> {
            Token prefixTok = this.curToken;
            String op = this.curToken.literal();
            this.nextToken();
            Expression right = this.parseExpression(Precedence.PREFIX);
            return new PrefixExpression(prefixTok, op, right);
        };
        this.setPrefix(TokenType.MINUS, parsePrefixExpr);
        this.setPrefix(TokenType.BANG, parsePrefixExpr);
        // --- [Infix Expressions] ---
        InfixParseFn parseInfixExpr = (Expression left) -> {
            Token infixTok = this.curToken;
            String op = this.curToken.literal();
            Precedence precedence = this.curPrecedence();
            this.nextToken();
            Expression right = this.parseExpression(precedence);
            return new InfixExpression(infixTok, left, op, right);
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

    private List<Expression> parseCallParameters() {
        List<Expression> exprs = new ArrayList<>(0);
        if (this.curTokenIs(TokenType.RPAREN))
            return exprs;
        exprs.add(this.parseExpression(Precedence.LOWEST));
        while (this.peekTokenIs(TokenType.COMMA)) {
            this.nextToken();
            this.nextToken();
            exprs.add(this.parseExpression(Precedence.LOWEST));
        }
        if (this.expectedPeekNot(TokenType.RPAREN))
            return null;
        return exprs;
    }

    private List<Identifier> parseFunctionParameters() {
        List<Identifier> idents = new ArrayList<>(0);
        if (this.curTokenIs(TokenType.RPAREN))
            return idents;
        idents.add(new Identifier(this.curToken, this.curToken.literal()));
        while (this.peekTokenIs(TokenType.COMMA)) {
            this.nextToken();
            this.nextToken();
            idents.add(new Identifier(this.curToken, this.curToken.literal()));
        }
        if (this.expectedPeekNot(TokenType.RPAREN))
            return null;
        return idents;
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
                program.getStatements().add(statement);
            this.nextToken();
        }
        return program;
    }

    private Statement parseStatement(){
        switch (this.curToken.type()){
            case LET -> { return this.parseLetStatement(); }
            case RETURN -> { return this.parseReturnStatement(); }
            default -> { return this.parseExpressionStatement(); }
        }
    }

    private BlockStatement parseBlockStatement() {
        List<Statement> stmts = new ArrayList<>(0);
        this.nextToken();
        while (!(this.curTokenIs(TokenType.RBRACE) || this.curTokenIs(TokenType.EOF))) {
            Statement stmt = this.parseStatement();
            if (stmt != null)
                stmts.add(stmt);
            this.nextToken();
        }
        return new BlockStatement(this.curToken, stmts);
    }

    private Statement parseLetStatement() {
        Token letTok = this.curToken;

        if (this.expectedPeekNot(TokenType.IDENT)) {
            return null;
        }

        Identifier name = new Identifier(this.curToken, this.curToken.literal());

        if (this.expectedPeekNot(TokenType.ASSIGN)) {
            return null;
        }
        this.nextToken();

        Expression value = this.parseExpression(Precedence.LOWEST);

        if(this.peekTokenIs(TokenType.SEMICOL))
            this.nextToken();

        return new LetStatement(letTok, name, value);
    }

    private Statement parseReturnStatement() {
        Token returnTok = this.curToken;
        this.nextToken();

        Expression value = this.parseExpression(Precedence.LOWEST);

        if(this.peekTokenIs(TokenType.SEMICOL))
            this.nextToken();

        return new ReturnStatement(returnTok, value);
    }

    private Expression parseExpression(Precedence precedence) {
        PrefixParseFn prefix = this.prefixParseMap.get(this.curToken.type());
        if (prefix == null) {
            this.noPrefixParseFnError(this.curToken.type());
            return null;
        }
        Expression leftExp = prefix.parse();

        while(!this.peekTokenIs(TokenType.SEMICOL) && precedence.ordinal() < this.peekPrecedence().ordinal()) {
            InfixParseFn infix = this.infixParseMap.get(this.peekToken.type());
            if (infix == null)
                return null;
            this.nextToken();
            leftExp = infix.parse(leftExp);
        }
        return leftExp;
    }

    private ExpressionStatement parseExpressionStatement() {
        ExpressionStatement stmt = new ExpressionStatement(this.curToken, this.parseExpression(Precedence.LOWEST));

        if (this.peekTokenIs(TokenType.SEMICOL))
            this.nextToken();

        return stmt;
    }

    public void nextToken() {
        this.curToken = this.peekToken;
        this.peekToken = this.lex.nextToken();
        this.tokens.add(this.peekToken);
    }

    private boolean expectedPeekNot(TokenType pType) {
        if (this.peekToken.type() == pType) {
            this.nextToken();
            return false;
        }
        else {
            this.peekError(pType);
            return true;
        }
    }

    private Precedence peekPrecedence() {
        return this.precedences.getOrDefault(this.peekToken.type(), Precedence.LOWEST);
    }

    private Precedence curPrecedence() {
        return this.precedences.getOrDefault(this.curToken.type(), Precedence.LOWEST);
    }

    private boolean peekTokenIs(TokenType pType) {
        return this.peekToken.type() == pType;
    }

    private boolean curTokenIs(TokenType pType) { return this.curToken.type() == pType; }

    private void noPrefixParseFnError(TokenType pTokenType) {
        this.errors.add("no prefix parse function for "+ pTokenType + "found");
    }

    private void peekError(TokenType pType) {
        this.errors.add("Parse Error: Expected next Token to be "+ pType +", got "+ this.peekToken.type() +" instead!");
    }

    public List<String> getErrors(){ return this.errors; }
}
