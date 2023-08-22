package Scanner;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class Lexer {
    public static final Map<String, TokenType> keywordsMap = new HashMap<>(Map.of("let", TokenType.LET,"fn",TokenType.FUNC));
    public static List<Token> tokenList = new ArrayList<>(0);
    public static List<String> srcCode = new ArrayList<>(0);

    private static boolean isDigit(String ch) {
        int digit = ch.getBytes(StandardCharsets.UTF_8)[0];
        return digit >= "0".getBytes(StandardCharsets.UTF_8)[0] && digit <= "9".getBytes(StandardCharsets.UTF_8)[0];
    }

    private static boolean isAlpha(String ch) {
        return !ch.toLowerCase().equals(ch.toUpperCase());
    }

    public static List<Token> tokenize(String sourceCode) {
        List<String> restCode = new ArrayList<>(List.of(sourceCode.split("")));
        srcCode = Arrays.asList(sourceCode.split(""));

        // create List of Tokens from Code until end of Stream
        while (!restCode.isEmpty()) {

            // handle single-charakter Tokens
            if (Objects.equals(restCode.get(0), "=")) {
                if (!Objects.equals(restCode.get(1), "="))
                    tokenList.add(new Token(TokenType.ASSIGN, restCode.remove(0)));
                else tokenList.add(new Token(TokenType.EQUAL, restCode.remove(0) + restCode.remove(0)));
            }
            else if (Objects.equals(restCode.get(0), "-") || Objects.equals(restCode.get(0), "+") ||
                    Objects.equals(restCode.get(0), "*") || Objects.equals(restCode.get(0), "/"))
                tokenList.add(new Token(TokenType.BINARY, restCode.remove(0)));
            else if (Objects.equals(restCode.get(0), "("))
                tokenList.add(new Token(TokenType.LPAREN, restCode.remove(0)));
            else if (Objects.equals(restCode.get(0), ")"))
                tokenList.add(new Token(TokenType.RPAREN, restCode.remove(0)));
            else if (Objects.equals(restCode.get(0), "{"))
                tokenList.add(new Token(TokenType.LBRACE, restCode.remove(0)));
            else if (Objects.equals(restCode.get(0), "}"))
                tokenList.add(new Token(TokenType.RBRACE, restCode.remove(0)));
            else if (Objects.equals(restCode.get(0), ","))
                tokenList.add(new Token(TokenType.COMMA, restCode.remove(0)));
            else if (Objects.equals(restCode.get(0), ";"))
                tokenList.add(new Token(TokenType.SEMICOL, restCode.remove(0)));

            // handle multi-charakter Tokens
            else {
                // handle NumberToken
                if (isDigit(restCode.get(0))) {
                    StringBuilder numValue = new StringBuilder();
                    while (!restCode.isEmpty() && isDigit(restCode.get(0))) {
                        numValue.append(restCode.remove(0));
                    }
                    tokenList.add(new Token(TokenType.INT, numValue.toString()));
                }

                // handle IdentifierToken
                else if (isAlpha(restCode.get(0))) {
                    StringBuilder idnValue = new StringBuilder();
                    while (!restCode.isEmpty() && isAlpha(restCode.get(0))) {
                        idnValue.append(restCode.remove(0));
                    }
                    tokenList.add(new Token(keywordsMap.getOrDefault(idnValue.toString(), TokenType.IDENT),
                            idnValue.toString()));
                }
                else restCode.remove(0);
            }
        }
        return tokenList;
    }
}
