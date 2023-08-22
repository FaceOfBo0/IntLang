package Scanner;

public enum TokenType {
    // Literals & Identifiers
    INT, IDENT,
    //Operators
    ASSIGN, EQUAL, BINARY,
    // Delimiters
    LPAREN, RPAREN, LBRACE, RBRACE, COMMA, SEMICOL,
    //Keywords
    LET, FUNC,
    // Special
    ILLEGAL, EOF
}