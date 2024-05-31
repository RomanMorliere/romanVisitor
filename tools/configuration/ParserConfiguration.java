package tools.configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ParserConfiguration {
  public static final Path AST_FILE_PATH = Paths.get(
      "tools", "specifications", "asts.txt");

  public static final String AST_PACKAGE = "ast";
  public static final String TREE_PACKAGE = "trees";
}
