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

public class WhileStatementTest {

  @Test
  public void testWhileStatement() throws Exception {
    ILexer lexer = PseudoProgram.lexerFromPseudoProgram(WHILE_TEST_PROGRAM);
    List<AST> expectedAst = WHILE_TEST_AST;

    final Parser parser = new Parser(lexer);
    AST ast = parser.execute();

    AstVisitor visitor = new TestVisitor(expectedAst);
    Object result = ast.accept(visitor);

    assertEquals(null, result);
  }

  private static final String WHILE_TEST_PROGRAM = String.join(
      System.lineSeparator(),
      List.of(
          "program { int <id>",
          "  while ( <id> < <int> ) {",
          "    <id> = <id> + <int>",
          "  }",
          "}"));

  private static final List<AST> WHILE_TEST_AST = List.of(
      new ProgramTree(),
      new BlockTree(),
      new DeclarationTree(),
      new IntTypeTree(),
      new IdentifierTree(PseudoProgramTokens.getTestToken("<id>")),
      new WhileTree(),
      new RelOpTree(PseudoProgramTokens.getTestToken("<")),
      new IdentifierTree(PseudoProgramTokens.getTestToken("<id>")),
      new IntTree(PseudoProgramTokens.getTestToken("<int>")),
      new BlockTree(),
      new AssignmentTree(),
      new IdentifierTree(PseudoProgramTokens.getTestToken("<id>")),
      new AddOpTree(PseudoProgramTokens.getTestToken("+")),
      new IdentifierTree(PseudoProgramTokens.getTestToken("<id>")),
      new IntTree(PseudoProgramTokens.getTestToken("<int>")));
}
