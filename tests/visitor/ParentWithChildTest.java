package tests.visitor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;

import ast.AST;
import ast.trees.BlockTree;
import ast.trees.ProgramTree;
import visitor.images.Offset;
import visitor.images.OffsetVisitor;

public class ParentWithChildTest {

  @Test
  public void testParentWithChild() {
    AST root = new ProgramTree();
    AST block = new BlockTree();
    root.addChild(block);

    OffsetVisitor visitor = new OffsetVisitor();
    root.accept(visitor);

    Map<AST, Offset> results = visitor.getOffsets();

    Offset programOffset = results.get(root);
    Offset blockOffset = results.get(block);

    assertEquals(0, programOffset.getX());
    assertEquals(0, programOffset.getY());

    assertEquals(0, blockOffset.getX());
    assertEquals(1, blockOffset.getY());

  }
}
