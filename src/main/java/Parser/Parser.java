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

    private Statement parseLetStatement() {
        return null;
    }

}
