package Lexer;

public class Token {
    public String value;
    public TokenType type;

    public Token(TokenType pType, String pValue) {
        this.type = pType;
        this.value = pValue;
    }

    @Override
    public String toString() {
        return "Lexer.Token{" + "value='" + this.value + '\'' + ", type=" + this.type + '}';
    }
}
