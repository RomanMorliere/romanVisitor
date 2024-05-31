package lexer.readers;

public interface IReader extends AutoCloseable {
  public void close() throws Exception;

  public char read();

  public int getColumn();

  public int getLineNumber();
}
