package exceptions;

public class LexicalException extends Exception {
  public static final String MESSAGE = "Invalid character encountered: %s (code: %d) on line %d, column %d";

  public LexicalException(String invalidCharacter, int line, int column) {
    super(
        String.format(
            MESSAGE,
            invalidCharacter,
            (int) invalidCharacter.charAt(0),
            line,
            column));
  }
}
