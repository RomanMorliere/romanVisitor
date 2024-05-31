package lexer.readers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SourceFileReader implements IReader {

  private BufferedReader reader;
  private int column;
  private int lineNumber;
  private char lastCharacter;

  private StringBuffer buffer;

  public SourceFileReader(String sourceFilePath) {
    this.lineNumber = 1;
    this.column = -1;
    this.lastCharacter = ' ';

    this.buffer = new StringBuffer();
    this.buffer.append(String.format("%3d: ", 1));

    try {
      this.reader = new BufferedReader(new FileReader(sourceFilePath));
    } catch (FileNotFoundException e) {
      System.err.println(String.format("Failed to find source file [%s].", sourceFilePath));
      System.exit(1);
    }

  }

  @Override
  public void close() throws IOException {
    this.reader.close();
  }

  @Override
  public char read() {
    if (this.lastCharacter == '\n') {
      this.column = -1;
      this.lineNumber++;

      this.buffer.append(String.format("%3d: ", this.lineNumber));
    }

    this.lastCharacter = getNextChar();

    if (this.lastCharacter != '\0') {
      this.buffer.append(this.lastCharacter);
    }

    this.column++;

    return this.lastCharacter;
  }

  private char getNextChar() {
    int next;

    try {
      next = this.reader.read();

      if (next == -1) {
        return '\0';
      }

    } catch (IOException e) {
      next = '\0';
    }

    return (char) next;
  }

  @Override
  public int getColumn() {
    return this.column;
  }

  @Override
  public int getLineNumber() {
    return this.lineNumber;
  }

  @Override
  public String toString() {
    return this.buffer.toString();
  }
}
