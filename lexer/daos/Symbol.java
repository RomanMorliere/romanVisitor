package lexer.daos;

public class Symbol {
  private String lexeme;
  private TokenKind tokenKind;

  public Symbol(String lexeme, TokenKind tokenKind) {
    this.lexeme = lexeme;
    this.tokenKind = tokenKind;
  }

  public String getLexeme() {
    return this.lexeme;
  }

  public TokenKind getTokenKind() {
    return this.tokenKind;
  }
}
