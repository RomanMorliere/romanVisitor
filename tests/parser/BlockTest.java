package tests.parser;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import exceptions.LexicalException;
import exceptions.SyntaxErrorException;
import parser.Parser;
import tests.helpers.ast.PseudoProgram;

public class BlockTest {

  private static final String BAD_BLOCK_PROGRAM = String.join(
      System.lineSeparator(),
      "program { int <id>",
      "  <id> = <int>",
      "  int <id>",
      "}");

  @Test
  public void testStatementBeforeDeclarationThrows() throws LexicalException, Exception {
    final Parser parser = new Parser(PseudoProgram.lexerFromPseudoProgram(BAD_BLOCK_PROGRAM));

    assertThrows(SyntaxErrorException.class, () -> {
      parser.execute();
    });
  }
}
