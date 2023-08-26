package Scanner;

public class Token {
    String value = null;
    TokenType type;

    Token(TokenType pType, String pValue) {
        this.type = pType;
        this.value = pValue;
    }

    Token(TokenType pType) {
        this.type = pType;
    }

   @Override
    public String toString() {
        return "Token {" + this.type + ", '" + this.value + "'}";
    }
}
