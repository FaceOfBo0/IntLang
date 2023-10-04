package Lexer.Token;

public enum TokenType {
    // Literals & Identifiers
    INT, IDENT, STRING,
    //Operators
    ASSIGN,
    BANG, EQ, BANG_EQ, TILDE, LESS, GREATER,
    PLUS, MINUS, ASTERISK, SLASH, LESS_EQ, GREATER_EQ,
    // Delimiters
    LPAREN, RPAREN, LBRACE, LBRACKET,
    RBRACKET, RBRACE, COMMA, SEMICOL,
    //Keywords
    LET, FUNC, IF, ELSE, RETURN, TRUE, FALSE,
    // Special
    ILLEGAL, COLON, EOF
}