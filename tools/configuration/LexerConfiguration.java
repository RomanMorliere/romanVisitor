package tools.configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

public class LexerConfiguration {
  public static final Path TOKEN_FILE_PATH = Paths.get(
      "tools", "specifications", "tokens.txt");

  public static final String LEXER_PACKAGE = "lexer";
  public static final String DAO_PACKAGE = "daos";
}
