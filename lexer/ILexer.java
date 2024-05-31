package lexer;

import exceptions.LexicalException;
import lexer.daos.Token;

public interface ILexer extends AutoCloseable {
  public Token nextToken() throws LexicalException;
}
