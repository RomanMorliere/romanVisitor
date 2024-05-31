package tests.helpers.lexer;

import java.util.ArrayList;
import java.util.List;

import exceptions.LexicalException;
import lexer.ILexer;
import lexer.daos.Token;

public class TestLexer implements ILexer {
  private List<Token> tokens;
  private int index;

  public TestLexer() {
    tokens = new ArrayList<>();
    index = 0;
  }

  public TestLexer(List<Token> tokens) {
    this.tokens = tokens;
    index = 0;
  }

  public void addToken(Token token) {
    tokens.add(token);
  }

  @Override
  public Token nextToken() throws LexicalException {
    if (index == tokens.size()) {
      return null;
    } else {
      return tokens.get(index++);
    }
  }

  @Override
  public void close() throws Exception {
    // no-op
  }

}
