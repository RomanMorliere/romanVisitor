package tests.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import org.junit.jupiter.api.Test;
import ast.AST;
import ast.trees.BlockTree;
import ast.trees.DeclarationTree;
import ast.trees.IdentifierTree;
import ast.trees.IntTypeTree;
import ast.trees.ProgramTree;
import parser.Parser;
import tests.helpers.ast.PseudoProgram;
import tests.helpers.ast.PseudoProgramTokens;
import tests.helpers.visitor.TestVisitor;
import visitor.AstVisitor;

public class VariableDeclarationTest {
  private static final String VALID_DECLARATION = String.join(
      System.lineSeparator(),
      List.of(
          "program { int <id> }"));

  @Test
  public void testVariableDeclaration() throws Exception {
    final Parser parser = new Parser(PseudoProgram.lexerFromPseudoProgram(VALID_DECLARATION));

    AST ast = parser.execute();
    AstVisitor visitor = new TestVisitor(List.of(
        new ProgramTree(),
        new BlockTree(),
        new DeclarationTree(),
        new IntTypeTree(),
        new IdentifierTree(PseudoProgramTokens.getTestToken("<id>"))));

    Object result = ast.accept(visitor);

    assertEquals(null, result);
  }
}
