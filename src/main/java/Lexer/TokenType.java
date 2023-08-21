package Lexer;

public enum TokenType {
    // Literals & Identifiers
    INT, IDENT,
    //Operators
    ASSIGN, BINARY,
    // Delimiters
    LPAREN,
    RPAREN,
    LBRACE,
    RBRACE,
    COMMA, SEMICOL,
    //Keywords
    LET, FUNC,
    // Special
    ILLEGAL, EOF
}
