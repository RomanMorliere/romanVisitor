package tests.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import org.junit.jupiter.api.Test;
import ast.AST;
import ast.trees.AssignmentTree;
import ast.trees.BlockTree;
import ast.trees.DeclarationTree;
import ast.trees.IdentifierTree;
import ast.trees.IntTree;
import ast.trees.IntTypeTree;
import ast.trees.ProgramTree;
import parser.Parser;
import tests.helpers.ast.PseudoProgram;
import tests.helpers.ast.PseudoProgramTokens;
import tests.helpers.visitor.TestVisitor;
import visitor.AstVisitor;

public class AssignmentStatementTest {

  private static final String ASSIGNMENT_PROGRAM = String.join(
      System.lineSeparator(),
      List.of(
          "program { int <id>",
          "  <id> = <int>",
          "}"));

  private static final List<AST> EXPECTED_AST = List.of(
      new ProgramTree(),
      new BlockTree(),
      new DeclarationTree(),
      new IntTypeTree(),
      new IdentifierTree(PseudoProgramTokens.getTestToken("<id>")),
      new AssignmentTree(),
      new IdentifierTree(PseudoProgramTokens.getTestToken("<id>")),
      new IntTree(PseudoProgramTokens.getTestToken("<int>")));

  @Test
  public void testAssignmentStatement() throws Exception {
    final Parser parser = new Parser(PseudoProgram.lexerFromPseudoProgram(ASSIGNMENT_PROGRAM));

    AST ast = parser.execute();

    AstVisitor visitor = new TestVisitor(EXPECTED_AST);
    Object result = ast.accept(visitor);

    assertEquals(null, result);
  }
}
