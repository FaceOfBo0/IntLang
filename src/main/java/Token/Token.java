package Token;

public record Token(TokenType type, String literal) {

    public Token(TokenType pType) {
        this(pType, "");
    }

   @Override
    public String toString() {
        return "Token:{" + this.type + ", '" + this.literal + "'}";
    }
}
