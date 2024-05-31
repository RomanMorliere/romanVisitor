package tests.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import ast.AST;
import ast.trees.*;
import parser.Parser;
import tests.helpers.ast.PseudoProgram;
import tests.helpers.ast.PseudoProgramTokens;
import tests.helpers.visitor.TestVisitor;
import visitor.AstVisitor;

public class ReturnStatementTest {

  private static final String returnProgram = String.join(
      System.lineSeparator(),
      List.of(
          "program {",
          "  int <id> ( ) {",
          "    return <int>",
          "  }",
          "}"));

  private static final List<AST> expectedAst = List.of(
      new ProgramTree(),
      new BlockTree(),
      new FunctionDeclarationTree(),
      new IntTypeTree(),
      new IdentifierTree(PseudoProgramTokens.getTestToken("<id>")),
      new FormalsTree(),
      new BlockTree(),
      new ReturnTree(),
      new IntTree(PseudoProgramTokens.getTestToken("<int>")));

  @Test
  public void returnStatementTest() throws Exception {
    final Parser parser = new Parser(PseudoProgram.lexerFromPseudoProgram(returnProgram));

    AST ast = parser.execute();

    AstVisitor visitor = new TestVisitor(expectedAst);
    Object result = ast.accept(visitor);

    assertEquals(null, result);
  }
}
