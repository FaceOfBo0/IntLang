package Parser;
import Parser.AST.*;
import Token.*;
import Lexer.*;

public class Parser {
    Lexer lex;
    Token curToken;
    Token peekToken;

    public Parser(Lexer pLex) {
        this.lex = pLex;
        this.nextToken();
        this.nextToken();
    }

    public void nextToken() {
        this.curToken = this.peekToken;
        this.peekToken = this.lex.nextToken();
    }

    public Program parseProgram() {
        Program program = new Program();
        Statement statement;
        while (this.curToken.type != TokenType.EOF) {
            statement = this.parseStatement();
            if (statement != null)
                program.statements.add(statement);
            this.nextToken();
        }
        return program;
    }

    private Statement parseStatement(){
        switch (this.curToken.type){
            case LET -> { return parseLetStatement(); }
            case RETURN -> { return parseReturnStatement(); }
            case IF -> { return parseIfStatement(); }
            default -> { return null; }
        }
    }

    private Statement parseIfStatement() {
        return null;
    }

    private Statement parseReturnStatement() {
        return null;
    }

    private boolean peekIsNot(TokenType pType) {
        if (this.peekToken.type == pType) {
            this.nextToken();
            return false;
        }
        else return true;
    }

    private Statement parseLetStatement() {
        LetStatement stmt = new LetStatement(this.curToken);

        if (this.peekIsNot(TokenType.IDENT)) {
            System.out.println("Error: incorrect identifier");
            return null;
        }

        stmt.setName(parseIdentifier());


        if (this.peekIsNot(TokenType.ASSIGN)) {
            System.out.println("Error: incorrect assign operator");
            return null;
        }
        this.nextToken();

        // Expression value = parseExpression();
        while (!(this.curToken.type == TokenType.SEMICOL)) {
            this.nextToken();
        }

        return stmt;
    }

    private Expression parseExpression() {
        if (this.curToken.type == TokenType.INT) {
            if (this.peekToken.type == TokenType.PLUS) {
                return parseOperatorExp();
            } else if (this.peekToken.type == TokenType.SEMICOL) {
                return parseInt();
            }
        }
        return null;
    }

    private Expression parseInt() {
        return null;
    }

    private Expression parseOperatorExp() {
        return null;
    }

    private Identifier parseIdentifier(){
        return new Identifier(this.curToken, this.curToken.literal);
    }
}
