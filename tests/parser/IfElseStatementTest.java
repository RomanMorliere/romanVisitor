package tests.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import org.junit.jupiter.api.Test;
import ast.AST;
import ast.trees.*;
import lexer.ILexer;
import parser.Parser;
import tests.helpers.ast.PseudoProgram;
import tests.helpers.ast.PseudoProgramTokens;
import tests.helpers.visitor.TestVisitor;
import visitor.AstVisitor;

public class IfElseStatementTest {

  @Test
  public void testIfStatement() throws Exception {
    ILexer lexer = PseudoProgram.lexerFromPseudoProgram(IF_ELSE_TEST_PROGRAM);
    List<AST> expectedAst = IF_ELSE_TEST_AST;

    final Parser parser = new Parser(lexer);
    AST ast = parser.execute();
    assertEquals(ast.getClass(), ProgramTree.class);

    AstVisitor visitor = new TestVisitor(expectedAst);

    Object result = ast.accept(visitor);
    assertEquals(null, result);
  }

  private static final String IF_ELSE_TEST_PROGRAM = String.join(
      System.lineSeparator(),
      List.of(
          "program { int <id>",
          "    if ( <int> == <int> ) then {",
          "        <id> = <int>",
          "    } else {",
          "        <id> = <int>",
          "    }",
          "}"));

  private static final List<AST> IF_ELSE_TEST_AST = List.of(
      new ProgramTree(),
      new BlockTree(),
      new DeclarationTree(),
      new IntTypeTree(),
      new IdentifierTree(PseudoProgramTokens.getTestToken("<id>")),
      new IfTree(),
      new RelOpTree(PseudoProgramTokens.getTestToken("==")),
      new IntTree(PseudoProgramTokens.getTestToken("<int>")),
      new IntTree(PseudoProgramTokens.getTestToken("<int>")),
      new BlockTree(),
      new AssignmentTree(),
      new IdentifierTree(PseudoProgramTokens.getTestToken("<id>")),
      new IntTree(PseudoProgramTokens.getTestToken("<int>")),
      new BlockTree(),
      new AssignmentTree(),
      new IdentifierTree(PseudoProgramTokens.getTestToken("<id>")),
      new IntTree(PseudoProgramTokens.getTestToken("<int>")));
}
