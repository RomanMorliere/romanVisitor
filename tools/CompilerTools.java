package tools;

import exceptions.CompilerToolException;
import tools.ast.TreeGenerator;
import tools.configuration.LexerConfiguration;
import tools.configuration.ParserConfiguration;
import tools.lexer.SymbolTableGenerator;
import tools.lexer.TokenKindGenerator;
import tools.visitor.PrintVisitorGenerator;
import tools.visitor.TestVisitorGenerator;
import tools.visitor.VisitorBaseClassGenerator;

public class CompilerTools {
  public static void main(String[] args) {
    try {
      FileGeneratorTool[] tools = new FileGeneratorTool[] {
          new TokenKindGenerator(LexerConfiguration.TOKEN_FILE_PATH),
          new SymbolTableGenerator(LexerConfiguration.TOKEN_FILE_PATH),
          new TreeGenerator(ParserConfiguration.AST_FILE_PATH),
          new VisitorBaseClassGenerator(ParserConfiguration.AST_FILE_PATH),
          new PrintVisitorGenerator(ParserConfiguration.AST_FILE_PATH),
          new TestVisitorGenerator(ParserConfiguration.AST_FILE_PATH)
      };

      for (FileGeneratorTool tool : tools) {
        System.out.println(
            String.format("> Regenerating [%s]", tool.getGeneratedFilePath().toString()));

        tool.ensureDirectoryExists();
        tool.generate();

      }
    } catch (CompilerToolException e) {
      System.err.println(e.getMessage());
      e.printStackTrace();

      System.exit(1);
    }
  }
}
