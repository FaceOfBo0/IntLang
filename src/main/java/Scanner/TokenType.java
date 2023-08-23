package Scanner;

public enum TokenType {
    // Literals & Identifiers
    INT, IDENT,
    //Operators
    ASSIGN,
    EQUAL, TILDE,
    PLUS, MINUS, ASTERISK, SLASH,
    // Delimiters
    LPAREN, RPAREN,
    LBRACE, RBRACE,
    COMMA, SEMICOL,
    //Keywords
    LET, FUNC,
    // Special
    ILLEGAL, EOF
}