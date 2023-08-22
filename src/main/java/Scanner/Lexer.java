package Scanner;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class Lexer {
    private final Map<String, TokenType> keywordsMap = new HashMap<>(Map.of("let", TokenType.LET,"fn",TokenType.FUNC));
    public  List<Token> tokenList = new ArrayList<>(0);
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

    private boolean isDigit(String ch) {
        int digit = ch.getBytes(StandardCharsets.UTF_8)[0];
        return digit >= "0".getBytes(StandardCharsets.UTF_8)[0] && digit <= "9".getBytes(StandardCharsets.UTF_8)[0];
    }

    private boolean isAlpha(String ch) {
        return !ch.toLowerCase().equals(ch.toUpperCase());
    }

    public List<Token> tokenize() {
        // create List of Tokens from Code until end of Stream
        while (this.curPos < this.source.length()) {

            // handle single-charakter Tokens
            if (this.source.charAt(curPos) == '=') {
                if (!(this.source.charAt(readPos) == '='))
                    tokenList.add(new Token(TokenType.ASSIGN, String.valueOf(this.source.charAt(curPos))));
                else tokenList.add(new Token(TokenType.EQUAL, "=="));
            }
            else if (Objects.equals(this.source.charAt(0), '-') || Objects.equals(this.source.charAt(0), '+') ||
                    Objects.equals(this.source.charAt(0), '*') || Objects.equals(this.source.charAt(0), '/'))
                tokenList.add(new Token(TokenType.BINARY, String.valueOf(this.source.charAt(curPos))));
            else if (Objects.equals(this.source.charAt(0), '('))
                tokenList.add(new Token(TokenType.LPAREN, String.valueOf(this.source.charAt(curPos))));
            else if (Objects.equals(this.source.charAt(0), ')'))
                tokenList.add(new Token(TokenType.RPAREN, String.valueOf(this.source.charAt(curPos))));
            else if (Objects.equals(this.source.charAt(0), '{'))
                tokenList.add(new Token(TokenType.LBRACE, String.valueOf(this.source.charAt(curPos))));
            else if (Objects.equals(this.source.charAt(0), '}'))
                tokenList.add(new Token(TokenType.RBRACE, String.valueOf(this.source.charAt(curPos))));
            else if (Objects.equals(this.source.charAt(0), ','))
                tokenList.add(new Token(TokenType.COMMA, String.valueOf(this.source.charAt(curPos))));
            else if (Objects.equals(this.source.charAt(0), ';'))
                tokenList.add(new Token(TokenType.SEMICOL, String.valueOf(this.source.charAt(curPos))));

            // handle multi-charakter Tokens
            else {
                // handle NumberToken
                if (isDigit(String.valueOf(this.source.charAt(0)))) {
                    StringBuilder numValue = new StringBuilder();
                    while (!this.source.isEmpty() && isDigit(String.valueOf(this.source.charAt(0)))) {
                        numValue.append(String.valueOf(this.source.charAt(curPos)));
                    }
                    tokenList.add(new Token(TokenType.INT, numValue.toString()));
                }

                // handle IdentifierToken
                else if (isAlpha(String.valueOf(this.source.charAt(0)))) {
                    StringBuilder idnValue = new StringBuilder();
                    while (!this.source.isEmpty() && isAlpha(String.valueOf(this.source.charAt(0)))) {
                        idnValue.append(String.valueOf(this.source.charAt(curPos)));
                    }
                    tokenList.add(new Token(keywordsMap.getOrDefault(idnValue.toString(), TokenType.IDENT),
                            idnValue.toString()));
                }
                else tokenList.add(new Token(TokenType.ILLEGAL));
            }
        }
        return tokenList;
    }
}
