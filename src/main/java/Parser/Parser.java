package Parser;
import Parser.AST.*;
import Token.*;
import Lexer.*;

public class Parser {
    Lexer lex;
    Token curToken;
    Token peekToken;

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

    private Statement parseLetStatement() {
        LetStatement stmt = new LetStatement(curToken);

        if (expectedPeekNot(TokenType.IDENT)) {
            System.out.println("Syntax Error: incorrect identifier");
            return null;
        }

        stmt.setName(parseIdentifier());


        if (expectedPeekNot(TokenType.ASSIGN)) {
            System.out.println("Syntax Error: incorrect assign operator");
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

    private Expression parseInt() {
        return null;
    }

    private Expression parseOperatorExp() {
        return null;
    }

    private Identifier parseIdentifier(){
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
        else return true;
    }

    private boolean peekTokenIs(TokenType pType) {
        return peekToken.type == pType;
    }

    private boolean curTokenIs(TokenType pType) {
        return curToken.type == pType;
    }
}
