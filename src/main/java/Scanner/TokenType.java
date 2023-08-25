package Scanner;

public enum TokenType {
    // Literals & Identifiers
    INT, IDENT,
    //Operators
    ASSIGN,
    BANG, EQ, NEQ, TILDE, LESS, GREATER,
    PLUS, MINUS, ASTERISK, SLASH,
    // Delimiters
    LPAREN, RPAREN,
    LBRACE, RBRACE,
    COMMA, SEMICOL,
    //Keywords
    LET, FUNC, IF, ELSE, RETURN, TRUE, FALSE,
    // Special
    ILLEGAL, EOF
}