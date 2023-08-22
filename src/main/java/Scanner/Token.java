package Scanner;

public class Token {
    final String value;
    final TokenType type;

    Token(TokenType pType, String pValue) {
        this.type = pType;
        this.value = pValue;
    }

   @Override
    public String toString() {
        return "Scanner.Token {" + this.type+", " + this.value + "}";
    }
}
