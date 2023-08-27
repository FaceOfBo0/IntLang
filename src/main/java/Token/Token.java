package Token;

import java.util.Objects;

public class Token {
    public final String literal;
    public final TokenType type;
    final int line;

    public Token(TokenType pType, String pValue) {
        this.type = pType;
        this.literal = pValue;
        this.line = 0;
    }

     public Token(TokenType pType) {
        this(pType, "");
    }

   @Override
    public String toString() {
        if (Objects.equals(this.literal, ""))
            return "Token {"+this.type+"}";
        else
            return "Token {" + this.type + ", '" + this.literal + "'}";
    }
}
