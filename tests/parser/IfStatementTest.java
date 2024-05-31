package tests.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ast.*;
import ast.trees.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import lexer.ILexer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import parser.Parser;
import tests.helpers.ast.PseudoProgram;
import tests.helpers.ast.PseudoProgramTokens;
import tests.helpers.visitor.TestVisitor;
import visitor.AstVisitor;

public class IfStatementTest {

  @ParameterizedTest
  @MethodSource("provideIfStatements")
  void testIfStatement(ILexer lexer, List<AST> expectedAst) throws Exception {
    final Parser parser = new Parser(lexer);
    AST ast = parser.execute();

    AstVisitor visitor = new TestVisitor(expectedAst);
    Object result = ast.accept(visitor);

    assertEquals(null, result);
  }

  private static Stream<Arguments> provideIfStatements() throws Exception {
    return Stream.of(
        Arguments.of(PseudoProgram.lexerFromPseudoProgram(IF_TEST_PROGRAM), IF_TEST_AST));
  }

  private static final String IF_TEST_PROGRAM = String.join(
      System.lineSeparator(),
      List.of(
          "program { int <id>",
          "    if ( <int> == <int> ) then {",
          "        <id> = <int>",
          "    }",
          "}"));

  private static final List<AST> IF_TEST_AST = Arrays.asList(
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
      new IntTree(PseudoProgramTokens.getTestToken("<int>")));
}
