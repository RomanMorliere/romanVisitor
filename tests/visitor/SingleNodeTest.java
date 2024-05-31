package tests.visitor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;

import ast.AST;
import ast.trees.ProgramTree;
import visitor.images.Offset;
import visitor.images.OffsetVisitor;

public class SingleNodeTest {

  @Test
  public void testSingleNodeOffset() {
    AST root = new ProgramTree();

    OffsetVisitor visitor = new OffsetVisitor();
    root.accept(visitor);

    Map<AST, Offset> results = visitor.getOffsets();
    Offset rootOffset = results.get(root);

    assertEquals(0, rootOffset.getX());
    assertEquals(0, rootOffset.getY());
  }
}
