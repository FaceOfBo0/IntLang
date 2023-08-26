package Scanner;

import java.util.*;

public class Lexer {
    private final Map<String, TokenType> keywords = new HashMap<>(Map.of("let", TokenType.LET, "fn",TokenType.FUNC,"true",TokenType.TRUE,
            "false",TokenType.FALSE,"return",TokenType.RETURN,"if",TokenType.IF,"else",TokenType.ELSE));
    public  List<Token> tokens = new ArrayList<>(0);
    public String source;
    public char curChar;
    public int readPos = 0;
    public int curPos = 0;

    public Lexer (String source) {
        this.source = source;
        this.readChar();
    }

    private void skipWhitespaces() {
        while (this.curChar == '\n' || this.curChar == '\t' || this.curChar == '\r' || this.curChar == ' ') {
            this.readChar();
        }
    }

    private void readChar() {
        if (this.readPos >= this.source.length())
            this.curChar = 0;
        else
            this.curChar = this.source.charAt(this.readPos);
        this.curPos = this.readPos;
        this.readPos++;
    }

    private char peekChar() {
        if (this.readPos >= this.source.length())
            return 0;
        else return this.source.charAt(this.readPos);
    }

    private String readIdent() {
        int pos = this.curPos;
        while (this.isAlpha(this.curChar)) {
            this.readChar();
        }
        return this.source.substring(pos, this.curPos);
    }

    private String readInt() {
        int pos = this.curPos;
        while (this.isDigit(this.curChar)) {
            this.readChar();
        }
        return this.source.substring(pos, this.curPos);
    }

    private TokenType lookupIdent (String tag) {
        return keywords.getOrDefault(tag, TokenType.IDENT);
    }

    private boolean isDigit(char ch) {
        return '0' <= ch && ch <= '9';
    }

    private boolean isAlpha(char ch) {
        return ('a' <= ch && ch <= 'z') || ('A' <= ch && ch <= 'Z') || ch == '_';
    }

    private Token nextToken () {
        Token tok;
        this.skipWhitespaces();
        switch (this.curChar) {
            case '=' -> {
                if (this.peekChar() == '=') {
                    this.readChar();
                    tok = new Token(TokenType.EQ, "==");
                }
                else tok = new Token(TokenType.ASSIGN, String.valueOf(this.curChar));
            }
            case '!' -> {
                if (this.peekChar() == '=') {
                    this.readChar();
                    tok = new Token(TokenType.NEQ, "!=");
                }
                else tok = new Token(TokenType.BANG, String.valueOf(this.curChar));
            }
            case ';' -> tok = new Token(TokenType.SEMICOL, String.valueOf(this.curChar));
            case ',' -> tok = new Token(TokenType.COMMA, String.valueOf(this.curChar));
            case '(' -> tok = new Token(TokenType.LPAREN, String.valueOf(this.curChar));
            case ')' -> tok = new Token(TokenType.RPAREN, String.valueOf(this.curChar));
            case '{' -> tok = new Token(TokenType.LBRACE, String.valueOf(this.curChar));
            case '}' -> tok = new Token(TokenType.RBRACE, String.valueOf(this.curChar));
            case '+' -> tok = new Token(TokenType.PLUS, String.valueOf(this.curChar));
            case '-' -> tok = new Token(TokenType.MINUS, String.valueOf(this.curChar));
            case '*' -> tok = new Token(TokenType.ASTERISK, String.valueOf(this.curChar));
            case '/' -> tok = new Token(TokenType.SLASH, String.valueOf(this.curChar));
            case '~' -> tok = new Token(TokenType.TILDE, String.valueOf(this.curChar));
            case '<' -> tok = new Token(TokenType.LESS, String.valueOf(this.curChar));
            case '>' -> tok = new Token(TokenType.GREATER, String.valueOf(this.curChar));
            case 0 -> tok = new Token(TokenType.EOF);
            default -> {
                String ident;
                String num;
                if (isAlpha(this.curChar)) {
                    ident = this.readIdent();
                    return new Token(this.lookupIdent(ident), ident);
                }
                else if (isDigit(this.curChar)) {
                    num = this.readInt();
                    return new Token(TokenType.INT, num);
                }
                else tok = new Token(TokenType.ILLEGAL, String.valueOf(this.curChar));
            }
        }
        this.readChar();
        return tok;
    }

    public List<Token> tokenize() {
        Token tok = new Token(TokenType.ILLEGAL);
        while (!(tok.type == TokenType.EOF)) {
            tok = this.nextToken();
            tokens.add(tok);
        }
        return this.tokens;
    }

}
