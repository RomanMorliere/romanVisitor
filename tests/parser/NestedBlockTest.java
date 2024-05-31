package tests.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import org.junit.jupiter.api.Test;
import ast.AST;
import ast.trees.BlockTree;
import ast.trees.ProgramTree;
import parser.Parser;
import tests.helpers.ast.PseudoProgram;
import tests.helpers.visitor.TestVisitor;
import visitor.AstVisitor;

public class NestedBlockTest {
  private static final String NESTED_BLOCK_PROGRAM = String.join(
      System.lineSeparator(),
      List.of(
          "program {",
          "  { }",
          "}"));

  private static final List<AST> EXPECTED_AST = List.of(
      new ProgramTree(),
      new BlockTree(),
      new BlockTree());

  @Test
  public void testNestedBlocks() throws Exception {
    final Parser parser = new Parser(PseudoProgram.lexerFromPseudoProgram(NESTED_BLOCK_PROGRAM));

    AST ast = parser.execute();

    AstVisitor visitor = new TestVisitor(EXPECTED_AST);
    Object result = ast.accept(visitor);

    assertEquals(null, result);
  }
}
