package tests.parser;

import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import ast.AST;
import ast.trees.*;
import parser.Parser;
import tests.helpers.ast.PseudoProgram;
import tests.helpers.ast.PseudoProgramAsts;
import tests.helpers.ast.PseudoProgramTokens;
import tests.helpers.visitor.TestVisitor;
import visitor.AstVisitor;

public class FromStatementTest {

  private static final String FROM_PROGRAM = String.join(
      System.lineSeparator(),
      "program {",
      "  from ( <int> to <int> ) step <int> {",
      "    <id> = <int>",
      "  }",
      "}");
  private static final List<AST> EXPECTED_ASTS = List.of(
      new ProgramTree(),
      new BlockTree(),
      new FromTree(),
      new RangeTree(),
      PseudoProgramAsts.get("<int>"),
      PseudoProgramAsts.get("<int>"),
      new StepTree(),
      PseudoProgramAsts.get("<int>"),
      new BlockTree(),
      new AssignmentTree(),
      new IdentifierTree(PseudoProgramTokens.getTestToken("<id>")),
      PseudoProgramAsts.get("<int>"));

  @Test
  public void testFromStatement() throws Exception {
    final Parser parser = new Parser(PseudoProgram.lexerFromPseudoProgram(FROM_PROGRAM));
    AST ast = parser.execute();

    AstVisitor visitor = new TestVisitor(EXPECTED_ASTS);
    Object result = ast.accept(visitor);

    assertNull(result);
  }

}
