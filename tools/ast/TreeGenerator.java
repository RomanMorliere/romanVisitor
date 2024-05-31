package tools.ast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import exceptions.CompilerToolException;
import tools.FileGeneratorTool;
import tools.configuration.LexerConfiguration;
import tools.configuration.ParserConfiguration;
import tools.configuration.VisitorConfiguration;

public class TreeGenerator extends FileGeneratorTool {
  private static final int CLASS_NAME_INDEX = 0;
  private static final int OPTIONAL_ARGS_INDEX = 1;

  private final String PACKAGE_AND_IMPORTS = String.join(
      "",
      this.formatLine(
          String.format("package %s.%s;",
              ParserConfiguration.AST_PACKAGE,
              ParserConfiguration.TREE_PACKAGE),
          0, 2),
      this.formatLine(
          String.format("import %s.AST;", ParserConfiguration.AST_PACKAGE), 0, 1),
      this.formatLine(
          String.format("import %s.%s;",
              VisitorConfiguration.VISITOR_PACKAGE,
              VisitorConfiguration.VISITOR_BASE_CLASS_NAME),
          0, 2));

  private final String SYMBOL_TREE_IMPORTS = String.join(
      "",
      this.formatLine(
          String.format("import %s.ISymbolTree;",
              ParserConfiguration.AST_PACKAGE),
          0, 1),
      this.formatLine(
          String.format("import %s.%s.SymbolTable;",
              LexerConfiguration.LEXER_PACKAGE,
              LexerConfiguration.DAO_PACKAGE),
          0, 1),
      this.formatLine(
          String.format("import %s.%s.*;",
              LexerConfiguration.LEXER_PACKAGE,
              LexerConfiguration.DAO_PACKAGE),
          0, 2));

  public TreeGenerator(Path specificationFile) throws CompilerToolException {
    super(specificationFile);
  }

  @Override
  public void generate() throws CompilerToolException {
    deleteExistingTrees();

    while (this.hasNext()) {
      String[] spec = this.next().split("\\s+");

      String className = String.format("%sTree", spec[CLASS_NAME_INDEX]);
      boolean implementsSymbolTree = spec.length == 2 && spec[OPTIONAL_ARGS_INDEX].contains("s");

      try {
        createTreeFile(className, implementsSymbolTree);
      } catch (IOException e) {
        System.err.println(String.format("Failed to create AST %s", className));
      }
    }
  }

  private void createTreeFile(String className, boolean implementsSymbolTree) throws IOException {
    File treeFile = Paths.get(
        ParserConfiguration.AST_PACKAGE,
        ParserConfiguration.TREE_PACKAGE,
        String.format("%s.java", className)).toFile();

    try (FileWriter writer = new FileWriter(treeFile)) {
      writer.write(PACKAGE_AND_IMPORTS);

      if (implementsSymbolTree) {
        writer.write(SYMBOL_TREE_IMPORTS);
      }

      writer.write(this.getAutoGeneratedWarning());

      writer.write(String.format("public class %s extends AST ", className));
      if (implementsSymbolTree) {
        writer.write("implements ISymbolTree ");
      }
      writer.write(this.formatLine("{", 0, 2));

      if (implementsSymbolTree) {
        writer.write(this.formatLine("private Symbol symbol;", 1, 2));

        writer.write(this.formatLine(
            String.format("public %s(Token token) {", className), 1, 1));

        writer.write(this.formatLine(
            "this.symbol = SymbolTable.symbol(token.getLexeme(), TokenKind.BogusToken);", 2, 1));
        writer.write(this.formatLine("}", 1, 2));

        writer.write(this.formatLine("public Symbol getSymbol() {", 1, 1));
        writer.write(this.formatLine("return this.symbol;", 2, 1));
        writer.write(this.formatLine("}", 1, 2));
      }

      writer.write(this.formatLine("@Override", 1, 1));
      writer.write(this.formatLine(
          String.format("public Object accept(%s visitor) {",
              VisitorConfiguration.VISITOR_BASE_CLASS_NAME),
          1, 1));
      writer.write(this.formatLine(
          String.format("return visitor.visit%s(this);", className), 2, 1));
      writer.write(this.formatLine("}", 1, 1));
      writer.write("}");
    }
  }

  /**
   * Utility method to delete existing tree files (to ensure they get regenerated
   * each time this tool is executed)
   */
  private void deleteExistingTrees() {
    File treeDirectory = getGeneratedFilePath().getParent().toFile();

    File[] oldTrees = treeDirectory.listFiles();
    for (File file : oldTrees) {
      file.delete();
    }
  }

  @Override
  public Path getGeneratedFilePath() {
    return Paths.get(
        ParserConfiguration.AST_PACKAGE,
        ParserConfiguration.TREE_PACKAGE,
        // We aren't generating a single file, but this is only used
        // to ensure the parent directory exists. A little hacky, but I'm
        // OK with that trade-off for a tool
        "doesntmatter.java");
  }

}