package lexer;

import exceptions.LexicalException;
import lexer.daos.Symbol;
import lexer.daos.SymbolTable;
import lexer.daos.Token;
import lexer.daos.TokenKind;
import lexer.readers.IReader;
import lexer.readers.SourceFileReader;

/**
 * I generally despise JavaDoc since it tends to be much
 * more verbose than the code itself, but including it in this
 * class to facilitate student understanding.
 */
public class Lexer implements ILexer {
  private IReader reader;
  private int startPosition;
  private int endPosition;
  private char ch;

  private boolean isInErrorState;
  private LexicalException errorStateException;

  public Lexer(IReader reader) {
    this.reader = reader;
    this.ch = ' ';
    this.isInErrorState = false;
  }

  public Lexer(String sourceFilePath) {
    this(new SourceFileReader(sourceFilePath));
  }

  /**
   * This is where the bulk of the Lexer's work is done.
   *
   * Since whitespace characters are not a part of any possible
   * lexeme, the Lexer first scans past all whitespace characters
   * (spaces, tabs, etc.).
   *
   * After whitespace has been scanned past, the Lexer's ch field
   * holds the beginning of a new possible lexeme. Because this is
   * a new lexeme, the Lexer needs to update the fields used to track
   * the location of the lexeme in the file (startPosition and
   * endPosition).
   *
   * The Lexer then proceeds to check for the different categories
   * of lexeme, starting with the set of identifiers and keywords.
   *
   * If a character that can not begin an identifier or keyword is
   * found, the Lexer then checks for a character that could begin
   * an integer literal.
   *
   * If a character that can not begin an integer literal is found,
   * the only remaining possible lexemes are the operators and
   * separators.
   */
  @Override
  public Token nextToken() throws LexicalException {
    if (this.isInErrorState) {
      throw this.errorStateException;
    }

    ignoreWhiteSpace();

    beginNewToken();

    if (this.ch == '"') {
      return character();
    }

    if (this.ch == '"') {
      return character();
    }

    if (Character.isJavaIdentifierStart(this.ch)) {
      return identifierOrKeyword();
    }

    if (Character.isDigit(this.ch)) {
      return integerOrNumber();
    }

    return operatorOrSeparator();
  }

  /**
   * Continue to read characters until a non-whitespace
   * character is encountered, or the end of the file is
   * reached.
   */
  private void ignoreWhiteSpace() {
    while (Character.isWhitespace(this.ch) && !atEof()) {
      advance();
    }
  }

  /**
   * Prepare to process a new lexeme by setting the start
   * and end positions of the lexeme to the IReader's
   * current column.
   */
  private void beginNewToken() {
    this.startPosition = this.reader.getColumn();
    this.endPosition = this.startPosition;
  }

  private Token character() throws LexicalException {
    String lexeme = this.ch + "";
    advance();

    lexeme += this.ch;
    advance();

    if (this.ch != '"') {
      error(this.ch + "", this.reader.getColumn());
    } else {
      lexeme += this.ch;
      advance();
    }

    return createToken(SymbolTable.symbol(lexeme, TokenKind.CharLit));
  }

  /**
   * This method must only be entered when a character that
   * may begin an identifier or keyword is read from
   * the IReader.
   *
   * This method continues to read characters from the IReader
   * to build a lexeme for an identifier or keyword, stopping when
   * the character read is not a possible identifier or
   * keyword character, or the end of the file has been reached.
   *
   * Note that the createToken method that is used to create
   * the Token, along with its Symbol, attempts to look up the
   * symbol in the SymbolTable. Since we populated the SymbolTable
   * with all of the known keywords, if a keyword is found, the
   * corresponding symbol will be returned. If the lexeme is not
   * found, that means this must be an Identifier.
   *
   * @return
   */
  private Token identifierOrKeyword() {
    String lexeme = "";

    do {
      lexeme += this.ch;
      advance();
    } while (Character.isJavaIdentifierPart(this.ch) && !atEof());

    return createToken(lexeme, TokenKind.Identifier);
  }

  /**
   * This method must only be entered when a character that
   * may begin an integer (i.e. a digit) is read from
   * the IReader.
   *
   * This method continues to read characters from the IReader
   * to build a lexeme for an integer, stopping when the
   * character read is not a digit character, or the end of
   * the file has been reached.
   *
   * @return
   * @throws LexicalException
   */
  private Token integerOrNumber() throws LexicalException {
    String lexeme = wholeNumber();

    if (this.ch == '.') {
      advance();

      if (!Character.isDigit(this.ch)) {
        this.isInErrorState = true;
        this.errorStateException = new LexicalException(
            ".",
            this.reader.getLineNumber(),
            this.reader.getColumn() - 1);

        return createToken(SymbolTable.symbol(lexeme, TokenKind.IntLit), this.startPosition, this.endPosition - 2);
      } else {
        String decimalPart = wholeNumber();

        return createToken(SymbolTable.symbol(String.format("%s.%s", lexeme, decimalPart), TokenKind.NumberLit));
      }
    }

    return createToken(lexeme, TokenKind.IntLit);
  }

  private String wholeNumber() {
    String number = "";

    do {
      number += this.ch;
      advance();
    } while (Character.isDigit(this.ch) && !atEof());

    return number;
  }

  /**
   * This method is the final step in attempting to
   * recognize a lexeme in the source code. This method
   * must only be entered when all other possible lexemes
   * have been checked for (identifiers, keywords, integer
   * literals).
   *
   * First, the Lexer checks to see if the end of the source
   * code file was reached. If so, the EOF token is created
   * and returned.
   *
   * If not, there are two possible cases:
   * 1. A single character operator or separator was read (i.e. <, =, etc.)
   * 2. A double character operator or separator was read (i.e. <=, !=, etc.)
   *
   * To check for a double character operator or separator, the Lexer
   * must read ahead one character. It then uses the two characters
   * to check the symbol table for a two character operator. If the
   * SymbolTable returns a non-null value (i.e. it found the Symbol for
   * the lexeme), then a double character operator or separator was found.
   *
   * If a double character operator or separator was found, the Lexer
   * first checks to see if Symbol is a Comment. If it is, it simply
   * ignores all the rest of the characters until the end of the source
   * code line is encountered. Otherwise a Token is created from the
   * Symbol.
   *
   * If a double character operator or separator is not found, the
   * Lexer checks the SymbolTable for the initial character that was read.
   * If a Symbol is returned, then a valid single character operator
   * or separator was discovered, and a Token is created.
   *
   * If neither of these cases result in a Symbol being returned from
   * the SymbolTable, a lexical error has occurred - the character(s)
   * can not be used to create a valid Token.
   *
   * @return
   * @throws LexicalException
   */
  private Token operatorOrSeparator() throws LexicalException {

    if (atEof()) {
      return createToken(
          SymbolTable.symbol(this.ch + "", TokenKind.EOF),
          this.startPosition,
          this.endPosition);
    }

    String singleCharacter = "" + this.ch;
    advance();

    String doubleCharacter = singleCharacter + this.ch;

    Symbol symbol = SymbolTable.symbol(doubleCharacter, TokenKind.BogusToken);

    if (symbol == null) {
      return singleCharacterOperatorOrSeparator(singleCharacter);
    } else if (symbol.getTokenKind() == TokenKind.Comment) {
      ignoreComment();
      return nextToken();
    } else {
      advance();

      return createToken(symbol);
    }
  }

  /**
   * This method checks the SymbolTable to see if a Symbol exists for
   * the single character lexeme.
   *
   * If the Symbol exists, a Token is created and returned.
   *
   * If the Symbol does not exist, the Lexer has exhausted all
   * possible options to create a lexeme, so throws a LexicalException.
   *
   * @param lexeme The single character lexeme
   * @return
   * @throws LexicalException
   */
  private Token singleCharacterOperatorOrSeparator(String lexeme) throws LexicalException {
    Symbol symbol = SymbolTable.symbol(lexeme + "", TokenKind.BogusToken);

    if (symbol == null) {
      error(lexeme, this.reader.getColumn() - 1);
      return null;
    } else {
      return createToken(symbol);
    }
  }

  /**
   * This utility method should only be called immediately
   * after a Comment token was found. It scans past all characters
   * remaining on the current source line until a new line is
   * encountered, or the end of the file is reached.
   */
  private void ignoreComment() {
    int currentLine = this.reader.getLineNumber();

    while (currentLine == this.reader.getLineNumber() && !atEof()) {
      advance();
    }
  }

  /**
   * A utility method to aid the Lexer in creating new tokens,
   * dispatches to the createToken(Symbol) version of the method
   *
   * @param lexeme The String lexeme that was read from the source file
   * @param kind   The TokenKind description of the Token
   * @return
   */
  private Token createToken(String lexeme, TokenKind kind) {
    return createToken(SymbolTable.symbol(lexeme, kind));
  }

  /**
   * A utility method to aid the Lexer in creating new tokens.
   * Since I happen to know that we are going to be adding line
   * numbers to Token creation, this method was created to allow
   * us to make a single source code update to add the line number.
   *
   * Note that, since the Lexer has already advanced past the end of
   * the Token (it must have read a character that does not belong
   * to the lexeme), 1 is subtracted from the current endPosition
   * UNLESS we have reached the end of the file (since the Lexer
   * can not scan past the end).
   *
   * @param symbol The symbol that should be used for the new Token
   * @return
   */
  private Token createToken(Symbol symbol) {
    return createToken(symbol, this.startPosition, this.endPosition - 1);
  }

  private Token createToken(Symbol symbol, int start, int end) {
    return new Token(symbol, start, end, this.reader.getLineNumber());
  }

  /**
   * The Lexer needs to check if the end of the IReader's
   * source has been reached to prevent reading past the end
   * of a file, or in some cases, to stop processing tokens
   * in the event of an error.
   *
   * @return
   */
  private boolean atEof() {
    return this.ch == '\0';
  }

  /**
   * As the Lexer reads characters from the IReader, it
   * needs to keep track of the position of the last character
   * that was read, so that it can accurately report the
   * beginning and ending columns of a lexeme.
   */
  private void advance() {
    if (!atEof()) {
      this.ch = this.reader.read();
      this.endPosition++;
    }
  }

  private void error(String lexeme, int errorColumn) throws LexicalException {
    throw new LexicalException(
        lexeme,
        this.reader.getLineNumber(),
        errorColumn);
  }

  /**
   * Delegate to the IReader's close method to free up
   * the IReader's resources; used to automatically clean
   * up resources used by the Lexer when the Lexer is created
   * in a try with resources block.
   *
   * @throws Exception
   */
  @Override
  public void close() throws Exception {
    this.reader.close();
  }

  @Override
  public String toString() {
    return this.reader.toString();
  }

  /**
   * Since we don't have a compiler yet to make use of our Lexer,
   * using this main method for testing for Assignment 1.
   *
   * @param args
   */
  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.println("usage: java lexer.Lexer <path>");
      System.exit(1);
    }

    try (Lexer lexer = new Lexer(args[0])) {
      Token token;

      while ((token = lexer.nextToken()).getTokenKind() != TokenKind.EOF) {
        System.out.println(token);
      }

      System.out.println();
      System.out.println(lexer);

      System.out.println();
      System.out.println(lexer);
    } catch (LexicalException lexception) {
      System.err.println(lexception.getMessage());
      System.exit(1);
    } catch (Exception exception) {
      System.err.println("Failed to close the Lexer");
      System.exit(1);
    }
  }
}
