package tests.lexer.daos;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import lexer.daos.Symbol;
import lexer.daos.Token;
import lexer.daos.TokenKind;

public class TokenRegressionTest {

  @Test
  public void testGetLexeme() {
    int left = 8, right = 42;
    String lexeme = "testlexeme";
    TokenKind kind = TokenKind.BogusToken;

    Symbol symbol = new Symbol(lexeme, kind);
    Token token = new Token(symbol, left, right);

    assertEquals(lexeme, token.getLexeme());
  }

  @Test
  public void testGetTokenKind() {
    int left = 8, right = 42;
    String lexeme = "testlexeme";
    TokenKind kind = TokenKind.BogusToken;

    Symbol symbol = new Symbol(lexeme, kind);
    Token token = new Token(symbol, left, right);

    assertEquals(kind, token.getTokenKind());
  }

  @Test
  public void testGetLeftPosition() {
    int left = 8, right = 42;
    String lexeme = "testlexeme";
    TokenKind kind = TokenKind.BogusToken;

    Symbol symbol = new Symbol(lexeme, kind);
    Token token = new Token(symbol, left, right);

    assertEquals(left, token.getLeftPosition());
  }

  @Test
  public void testGetRightPosition() {
    int left = 8, right = 42;
    String lexeme = "testlexeme";
    TokenKind kind = TokenKind.BogusToken;

    Symbol symbol = new Symbol(lexeme, kind);
    Token token = new Token(symbol, left, right);

    assertEquals(right, token.getRightPosition());
  }

  @Test
  public void testTestPrint() {
    int left = 8, right = 42;
    String lexeme = "testlexeme";
    TokenKind kind = TokenKind.BogusToken;

    Symbol symbol = new Symbol(lexeme, kind);
    Token token = new Token(symbol, left, right);

    String expected = String.format(
        "L: %d, R: %d, %s", left, right, lexeme);

    assertEquals(expected, token.testPrint());
  }

  @Test
  public void testLegacyConstructor() {
    int left = 8, right = 42;
    String lexeme = "testlexeme";
    TokenKind kind = TokenKind.BogusToken;

    Symbol symbol = new Symbol(lexeme, kind);
    Token token = new Token(symbol, left, right);

    assertEquals(-1, token.getLineNumber());
  }

  @Test
  public void testNewConstructor() {
    int left = 8, right = 42;
    int lineNumber = 1873;
    String lexeme = "testlexeme";
    TokenKind kind = TokenKind.BogusToken;

    Symbol symbol = new Symbol(lexeme, kind);
    Token token = new Token(symbol, left, right, lineNumber);

    assertEquals(lineNumber, token.getLineNumber());
  }

  @Test
  public void testTokenStringRepresentation() {
    int left = 8, right = 42;
    int lineNumber = 1873;
    String lexeme = "testlexeme";
    TokenKind kind = TokenKind.BogusToken;
    String expectedString = "testlexeme           left: 8        right: 42       line: 1873     BogusToken";

    Symbol symbol = new Symbol(lexeme, kind);
    Token token = new Token(symbol, left, right, lineNumber);

    assertEquals(expectedString, token.toString());
  }
}
