package tests.parser;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ast.AST;
import ast.trees.AddOpTree;
import ast.trees.MultOpTree;
import ast.trees.ProgramTree;
import exceptions.LexicalException;
import parser.Parser;
import tests.helpers.ast.PseudoProgram;
import tests.helpers.ast.PseudoProgramTokens;
import tests.helpers.visitor.TestVisitor;
import visitor.AstVisitor;

public class MathematicalOperatorTest {

  @ParameterizedTest
  @MethodSource("provideOperatorTrees")
  public void testOperator(String operator, AST operatorTree) throws LexicalException, Exception {
    final Parser parser = new Parser(
        PseudoProgram.lexerForMathematicalOperator(operator));
    AST ast = parser.execute();

    assertEquals(ProgramTree.class, ast.getClass());

    AstVisitor visitor = new TestVisitor(
        PseudoProgram.expectedAstForMathematicalOperator(operatorTree));
    Object result = visitor.visitProgramTree(ast);

    assertNull(result);
  }

  private static Stream<Arguments> provideOperatorTrees() {
    return Stream.of(
        Arguments.of("+", new AddOpTree(PseudoProgramTokens.getTestToken("+"))),
        Arguments.of("-", new AddOpTree(PseudoProgramTokens.getTestToken("-"))),
        Arguments.of("|", new AddOpTree(PseudoProgramTokens.getTestToken("|"))),
        Arguments.of("*", new MultOpTree(PseudoProgramTokens.getTestToken("*"))),
        Arguments.of("/", new MultOpTree(PseudoProgramTokens.getTestToken("/"))),
        Arguments.of("&", new MultOpTree(PseudoProgramTokens.getTestToken("&"))));
  }
}
