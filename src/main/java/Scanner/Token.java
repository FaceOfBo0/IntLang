package Scanner;

public class Token {
    final String value;
    final TokenType type;

    Token(TokenType pType, String pValue) {
        this.type = pType;
        this.value = pValue;
    }

    Token(TokenType pType) {
        this(pType, "");
    }

   @Override
    public String toString() {
        return "Token {" + this.type + ", '" + this.value + "'}";
    }
}
