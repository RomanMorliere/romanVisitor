package tests.parser;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ast.AST;
import ast.trees.ProgramTree;
import parser.Parser;
import tests.helpers.ast.PseudoProgram;
import tests.helpers.visitor.TestVisitor;
import visitor.AstVisitor;

public class TypeTest {

  @ParameterizedTest
  @MethodSource("provideTypeStrings")
  public void testTypes(String typeString) throws Exception {

    final Parser parser = new Parser(PseudoProgram.lexerForType(typeString));
    AST ast = parser.execute();

    assertEquals(ProgramTree.class, ast.getClass());

    AstVisitor visitor = new TestVisitor(
        PseudoProgram.expectedAstForType(typeString));
    Object result = visitor.visitProgramTree(ast);

    assertNull(result);

  }

  private static Stream<Arguments> provideTypeStrings() {
    return Stream.of(
        Arguments.of("int"),
        Arguments.of("boolean"),
        Arguments.of("num"),
        Arguments.of("character"));
  }
}
