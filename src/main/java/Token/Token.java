package Token;

import java.util.Objects;

public class Token {
    public final String literal;
    public final TokenType type;

    public Token(TokenType pType, String pValue) {
        this.type = pType;
        this.literal = pValue;
    }

     public Token(TokenType pType) {
        this(pType, "");
    }

   @Override
    public String toString() {
        return "Token:{" + this.type + ", '" + this.literal + "'}";
    }
}
