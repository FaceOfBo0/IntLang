package Scanner;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class Lexer {
    private final Map<String, TokenType> keywords = new HashMap<>(Map.of("let", TokenType.LET,"fn",TokenType.FUNC));
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
        while (this.curChar == '\n' || this.curChar == '\t' || this.curChar == ' ') {
            this.readChar();
        }
    }

    private void readChar () {
        if (this.readPos >= this.source.length())
            this.curChar = 0;
        else
            this.curChar = this.source.charAt(this.readPos);
        this.curPos = this.readPos;
        this.readPos++;
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
        Token tok = null;
        this.skipWhitespaces();
        switch (this.curChar) {
            case '=' -> {
                if (this.source.charAt(this.readPos) == '=') {
                    this.readChar();
                    tok = new Token(TokenType.EQUAL, "==");
                }
                else tok = new Token(TokenType.ASSIGN, String.valueOf(this.curChar));
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
                else tok = new Token(TokenType.ILLEGAL);
            }
        }
        this.readChar();
        return tok;
    }

    public List<Token> tokenize() {
        while (this.curPos < this.source.length()) {
            tokens.add(this.nextToken());
        }
        return this.tokens;
    }

//    public List<Token> tokenize() {
//        // create List of Tokens from Code until end of Stream
//        while (this.curPos < this.source.length()) {
//
//            // handle single-charakter Tokens
//            if (this.source.charAt(curPos) == '=') {
//                if (!(this.source.charAt(readPos) == '='))
//                    tokens.add(new Token(TokenType.ASSIGN, String.valueOf(this.source.charAt(curPos))));
//                else tokens.add(new Token(TokenType.EQUAL, "=="));
//            }
//            else if (Objects.equals(this.source.charAt(0), '-') || Objects.equals(this.source.charAt(0), '+') ||
//                    Objects.equals(this.source.charAt(0), '*') || Objects.equals(this.source.charAt(0), '/'))
//                tokens.add(new Token(TokenType.BINARY, String.valueOf(this.source.charAt(curPos))));
//            else if (Objects.equals(this.source.charAt(0), '('))
//                tokens.add(new Token(TokenType.LPAREN, String.valueOf(this.source.charAt(curPos))));
//            else if (Objects.equals(this.source.charAt(0), ')'))
//                tokens.add(new Token(TokenType.RPAREN, String.valueOf(this.source.charAt(curPos))));
//            else if (Objects.equals(this.source.charAt(0), '{'))
//                tokens.add(new Token(TokenType.LBRACE, String.valueOf(this.source.charAt(curPos))));
//            else if (Objects.equals(this.source.charAt(0), '}'))
//                tokens.add(new Token(TokenType.RBRACE, String.valueOf(this.source.charAt(curPos))));
//            else if (Objects.equals(this.source.charAt(0), ','))
//                tokens.add(new Token(TokenType.COMMA, String.valueOf(this.source.charAt(curPos))));
//            else if (Objects.equals(this.source.charAt(0), ';'))
//                tokens.add(new Token(TokenType.SEMICOL, String.valueOf(this.source.charAt(curPos))));
//
//            // handle multi-charakter Tokens
//            else {
//                // handle NumberToken
//                if (isDigit(String.valueOf(this.source.charAt(0)))) {
//                    StringBuilder numValue = new StringBuilder();
//                    while (!this.source.isEmpty() && isDigit(String.valueOf(this.source.charAt(0)))) {
//                        numValue.append(String.valueOf(this.source.charAt(curPos)));
//                    }
//                    tokens.add(new Token(TokenType.INT, numValue.toString()));
//                }
//
//                // handle IdentifierToken
//                else if (isAlpha(String.valueOf(this.source.charAt(0)))) {
//                    StringBuilder idnValue = new StringBuilder();
//                    while (!this.source.isEmpty() && isAlpha(String.valueOf(this.source.charAt(0)))) {
//                        idnValue.append(String.valueOf(this.source.charAt(curPos)));
//                    }
//                    tokens.add(new Token(keywords.getOrDefault(idnValue.toString(), TokenType.IDENT),
//                            idnValue.toString()));
//                }
//                else tokens.add(new Token(TokenType.ILLEGAL));
//            }
//        }
//        return tokens;
//    }
}
