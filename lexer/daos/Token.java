package lexer.daos;

public class Token {
  private int leftPosition;
  private int rightPosition;
  private Symbol symbol;
  private int lineNumber;

  public Token(Symbol symbol, int leftPosition, int rightPosition) {
    this(symbol, leftPosition, rightPosition, -1);
  }

  public Token(Symbol symbol, int leftPosition, int rightPosition, int lineNumber) {
    this.symbol = symbol;
    this.leftPosition = leftPosition;
    this.rightPosition = rightPosition;
    this.lineNumber = lineNumber;
  }

  public int getLeftPosition() {
    return this.leftPosition;
  }

  public int getRightPosition() {
    return this.rightPosition;
  }

  public String getLexeme() {
    return this.symbol.getLexeme();
  }

  public TokenKind getTokenKind() {
    return this.symbol.getTokenKind();
  }

  public int getLineNumber() {
    return this.lineNumber;
  }

  public String testPrint() {
    return String.format(
        "L: %d, R: %d, %s",
        this.getLeftPosition(),
        this.getRightPosition(),
        this.getLexeme());
  }

  @Override
  public String toString() {
    return String.format("%-20s left: %-8d right: %-8d line: %-8d %s",
        this.symbol.getLexeme(),
        this.getLeftPosition(),
        this.getRightPosition(),
        this.getLineNumber(),
        this.symbol.getTokenKind());
  }
}
