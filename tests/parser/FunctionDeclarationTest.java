package tests.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ast.AST;
import ast.trees.*;
import parser.Parser;
import tests.helpers.ast.PseudoProgram;
import tests.helpers.ast.PseudoProgramTokens;
import tests.helpers.visitor.TestVisitor;
import visitor.AstVisitor;

public class FunctionDeclarationTest {

    @ParameterizedTest
    @MethodSource("provideFunctionDeclarations")
    public void testFunctionDeclaration(String program, List<AST> expectedAsts) throws Exception {
        final Parser parser = new Parser(PseudoProgram.lexerFromPseudoProgram(program));

        AST ast = parser.execute();
        AstVisitor visitor = new TestVisitor(expectedAsts);

        Object result = ast.accept(visitor);

        assertEquals(null, result);
    }

    private static final String NO_FORMALS_FUNCTION_DECLARATION = String.join(
            System.lineSeparator(),
            List.of(
                    "program {",
                    "  int <id> ( ) {",
                    "    int <id>",
                    "    <id> = <int>",
                    "  }",
                    "}"));
    private static final List<AST> EXPECTED_AST_NO_FORMALS_FUNCTION_DECLARATION = List.of(
            new ProgramTree(),
            new BlockTree(),
            new FunctionDeclarationTree(),
            new IntTypeTree(),
            new IdentifierTree(PseudoProgramTokens.getTestToken("<id>")),
            new FormalsTree(),
            new BlockTree(),
            new DeclarationTree(),
            new IntTypeTree(),
            new IdentifierTree(PseudoProgramTokens.getTestToken("<id>")),
            new AssignmentTree(),
            new IdentifierTree(PseudoProgramTokens.getTestToken("<id>")),
            new IntTree(PseudoProgramTokens.getTestToken("<int>")));

    private static final String ONE_FORMALS_FUNCTION_DECLARATION = String.join(
            System.lineSeparator(),
            List.of(
                    "program {",
                    "  int <id> ( int <id> ) {",
                    "    int <id>",
                    "    <id> = <int>",
                    "  }",
                    "}"));
    private static final List<AST> EXPECTED_AST_ONE_FORMALS_FUNCTION_DECLARATION = List.of(
            new ProgramTree(),
            new BlockTree(),
            new FunctionDeclarationTree(),
            new IntTypeTree(),
            new IdentifierTree(PseudoProgramTokens.getTestToken("<id>")),
            new FormalsTree(),
            new DeclarationTree(),
            new IntTypeTree(),
            new IdentifierTree(PseudoProgramTokens.getTestToken("<id>")),
            new BlockTree(),
            new DeclarationTree(),
            new IntTypeTree(),
            new IdentifierTree(PseudoProgramTokens.getTestToken("<id>")),
            new AssignmentTree(),
            new IdentifierTree(PseudoProgramTokens.getTestToken("<id>")),
            new IntTree(PseudoProgramTokens.getTestToken("<int>")));

    private static final String MULTIPLE_FORMALS_FUNCTION_DECLARATION = String.join(
            System.lineSeparator(),
            List.of(
                    "program {",
                    "  int <id> ( int <id> , int <id> ) {",
                    "    int <id>",
                    "    <id> = <int>",
                    "  }",
                    "}"));
    private static final List<AST> EXPECTED_AST_MULTIPLE_FORMALS_FUNCTION_DECLARATION = List.of(
            new ProgramTree(),
            new BlockTree(),
            new FunctionDeclarationTree(),
            new IntTypeTree(),
            new IdentifierTree(PseudoProgramTokens.getTestToken("<id>")),
            new FormalsTree(),
            new DeclarationTree(),
            new IntTypeTree(),
            new IdentifierTree(PseudoProgramTokens.getTestToken("<id>")),
            new DeclarationTree(),
            new IntTypeTree(),
            new IdentifierTree(PseudoProgramTokens.getTestToken("<id>")),
            new BlockTree(),
            new DeclarationTree(),
            new IntTypeTree(),
            new IdentifierTree(PseudoProgramTokens.getTestToken("<id>")),
            new AssignmentTree(),
            new IdentifierTree(PseudoProgramTokens.getTestToken("<id>")),
            new IntTree(PseudoProgramTokens.getTestToken("<int>")));

    private static Stream<Arguments> provideFunctionDeclarations() {
        return Stream.of(
                Arguments.of(NO_FORMALS_FUNCTION_DECLARATION, EXPECTED_AST_NO_FORMALS_FUNCTION_DECLARATION),
                Arguments.of(ONE_FORMALS_FUNCTION_DECLARATION,
                        EXPECTED_AST_ONE_FORMALS_FUNCTION_DECLARATION),
                Arguments.of(MULTIPLE_FORMALS_FUNCTION_DECLARATION,
                        EXPECTED_AST_MULTIPLE_FORMALS_FUNCTION_DECLARATION));
    }
}
