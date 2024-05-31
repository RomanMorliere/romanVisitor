package tests.parser;

import static org.junit.jupiter.api.Assertions.assertNull;
import java.util.List;
import org.junit.jupiter.api.Test;

import ast.AST;
import ast.trees.BlockTree;
import ast.trees.ProgramTree;
import exceptions.LexicalException;
import parser.Parser;
import tests.helpers.ast.PseudoProgram;
import tests.helpers.visitor.TestVisitor;
import visitor.AstVisitor;

public class ProgramTest {
  @Test
  public void testProgram() throws LexicalException, Exception {
    final Parser parser = new Parser(
        PseudoProgram.lexerFromPseudoProgram("program { }"));

    AST ast = parser.execute();
    AstVisitor visitor = new TestVisitor(
        List.of(new ProgramTree(), new BlockTree()));

    Object result = ast.accept(visitor);

    assertNull(result);
  }
}
