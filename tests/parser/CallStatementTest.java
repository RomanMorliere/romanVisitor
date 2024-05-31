package tests.parser;

import static org.junit.Assert.assertNull;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ast.AST;
import ast.trees.*;
import parser.Parser;
import tests.helpers.ast.PseudoProgram;
import tests.helpers.ast.PseudoProgramAsts;
import tests.helpers.ast.PseudoProgramTokens;
import tests.helpers.visitor.TestVisitor;
import visitor.AstVisitor;

public class CallStatementTest {

    @ParameterizedTest
    @MethodSource("provideCallPrograms")
    public void testCallStatementWithoutArguments(String program, List<AST> expectedAsts)
            throws Exception {
        final Parser parser = new Parser(PseudoProgram.lexerFromPseudoProgram(program));
        AST ast = parser.execute();

        AstVisitor visitor = new TestVisitor(expectedAsts);
        Object result = ast.accept(visitor);

        assertNull(result);
    }

    private static final String CALL_TEST_PROGRAM_NO_ARGS = String.join(
            System.lineSeparator(),
            List.of(
                    "program {",
                    "  <id> = <id> ( )",
                    "}"));
    private static final List<AST> CALL_TEST_EXPECTED_AST_NO_ARGS = List.of(
            new ProgramTree(),
            new BlockTree(),
            new AssignmentTree(),
            new IdentifierTree(PseudoProgramTokens.getTestToken("<id>")),
            new CallTree(),
            new IdentifierTree(PseudoProgramTokens.getTestToken("<id>")),
            new ActualArgumentsTree());

    private static final String CALL_TEST_PROGRAM_ONE_ARGS = String.join(
            System.lineSeparator(),
            List.of(
                    "program {",
                    "  <id> = <id> ( <int> )",
                    "}"));
    private static final List<AST> CALL_TEST_EXPECTED_AST_ONE_ARGS = List.of(
            new ProgramTree(),
            new BlockTree(),
            new AssignmentTree(),
            new IdentifierTree(PseudoProgramTokens.getTestToken("<id>")),
            new CallTree(),
            new IdentifierTree(PseudoProgramTokens.getTestToken("<id>")),
            new ActualArgumentsTree(),
            PseudoProgramAsts.get("<int>"));

    private static final String CALL_TEST_PROGRAM_MORE_ARGS = String.join(
            System.lineSeparator(),
            List.of(
                    "program {",
                    "  <id> = <id> ( <int> , <int> + <int> )",
                    "}"));
    private static final List<AST> CALL_TEST_EXPECTED_AST_MORE_ARGS = List.of(
            new ProgramTree(),
            new BlockTree(),
            new AssignmentTree(),
            new IdentifierTree(PseudoProgramTokens.getTestToken("<id>")),
            new CallTree(),
            new IdentifierTree(PseudoProgramTokens.getTestToken("<id>")),
            new ActualArgumentsTree(),
            PseudoProgramAsts.get("<int>"),
            new AddOpTree(PseudoProgramTokens.getTestToken("+")),
            PseudoProgramAsts.get("<int>"),
            PseudoProgramAsts.get("<int>"));

    private static Stream<Arguments> provideCallPrograms() {
        return Stream.of(
                Arguments.of(CALL_TEST_PROGRAM_NO_ARGS, CALL_TEST_EXPECTED_AST_NO_ARGS),
                Arguments.of(CALL_TEST_PROGRAM_ONE_ARGS, CALL_TEST_EXPECTED_AST_ONE_ARGS),
                Arguments.of(CALL_TEST_PROGRAM_MORE_ARGS, CALL_TEST_EXPECTED_AST_MORE_ARGS));
    }
}
