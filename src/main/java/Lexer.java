import java.nio.charset.StandardCharsets;
import java.util.*;

enum TokenType {
    NUMBER, IDENTIFIER, EQUAL, OPENPAR, CLOSEDPAR, BINARY, LET
}

class Token {
    public String value;
    public TokenType type;

    Token(TokenType pType, String pValue) {
        this.type = pType;
        this.value = pValue;
    }

    @Override
    public String toString() {
        return "Token{" + "value='" + value + '\'' + ", type=" + type + '}';
    }
}

public abstract class Lexer {
    private static final Map<String, TokenType> identifierMap = new HashMap<>(Map.of("let", TokenType.LET));

    private static boolean isDigit(String ch) {
        int digit = ch.getBytes(StandardCharsets.UTF_8)[0];
        return digit >= "0".getBytes(StandardCharsets.UTF_8)[0] && digit <= "9".getBytes(StandardCharsets.UTF_8)[0];
    }

    private static boolean isAlpha(String ch) {
        return !ch.toLowerCase().equals(ch.toUpperCase());
    }

    public static List<Token> tokenize(String sourceCode) {
        List<Token> tokenList = new ArrayList<>(0);
        List<String> src = new ArrayList<>(List.of(sourceCode.split("")));
        // create List of Tokens from Code until end of Stream
        while (src.size() > 0) {
            // handle single-charakter Tokens
            if (Objects.equals(src.get(0), "=")){
                tokenList.add(new Token(TokenType.EQUAL, src.remove(0)));
            }
            else if (Objects.equals(src.get(0), "-") || Objects.equals(src.get(0), "+") ||
                    Objects.equals(src.get(0), "*") || Objects.equals(src.get(0), "/"))
                tokenList.add(new Token(TokenType.BINARY, src.remove(0)));
            else if (Objects.equals(src.get(0), "("))
                tokenList.add(new Token(TokenType.OPENPAR, src.remove(0)));
            else if (Objects.equals(src.get(0), ")"))
                tokenList.add(new Token(TokenType.CLOSEDPAR, src.remove(0)));
            // handle multi-charakter Tokens
            else {
                // handle NumberToken
                if (isDigit(src.get(0))) {
                    StringBuilder numValue = new StringBuilder();
                    while (src.size() > 0 && isDigit(src.get(0))) {
                        numValue.append(src.remove(0));
                    }
                    tokenList.add(new Token(TokenType.NUMBER, numValue.toString()));
                }
                // handle IdentifierToken
                else if (isAlpha(src.get(0))) {
                    StringBuilder idnValue = new StringBuilder();
                    while (src.size() > 0 && isAlpha(src.get(0))) {
                        idnValue.append(src.remove(0));
                    }
                    tokenList.add(new Token(identifierMap.getOrDefault(idnValue.toString(), TokenType.IDENTIFIER),
                            idnValue.toString()));
                }
                else src.remove(0);
            }
        }
        return tokenList;
    }
}
