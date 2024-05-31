package tests.visitor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;

import ast.AST;
import ast.trees.BlockTree;
import ast.trees.ProgramTree;
import visitor.images.Offset;
import visitor.images.OffsetVisitor;

public class ShiftingNodesTest {

  @Test
  public void testComplexTree() {
    // Root - level 0
    AST a = new ProgramTree();
    // Level 1
    AST b = new BlockTree(), c = new BlockTree();
    a.addChild(b).addChild(c);
    // Level 2, b's children
    AST d = new BlockTree(), e = new BlockTree(), f = new BlockTree();
    b.addChild(d).addChild(e).addChild(f);
    // Level 2, c's child
    AST g = new BlockTree();
    c.addChild(g);
    // Level 2, g's children
    AST h = new BlockTree(), i = new BlockTree();
    g.addChild(h).addChild(i);

    OffsetVisitor visitor = new OffsetVisitor();
    a.accept(visitor);

    Map<AST, Offset> results = visitor.getOffsets();

    assertEquals(4, results.get(a).getX());
    assertEquals(0, results.get(a).getY());

    assertEquals(2, results.get(b).getX());
    assertEquals(1, results.get(b).getY());

    assertEquals(6, results.get(c).getX());
    assertEquals(1, results.get(c).getY());

    assertEquals(0, results.get(d).getX());
    assertEquals(2, results.get(d).getY());

    assertEquals(2, results.get(e).getX());
    assertEquals(2, results.get(e).getY());

    assertEquals(4, results.get(f).getX());
    assertEquals(2, results.get(f).getY());

    assertEquals(6, results.get(g).getX());
    assertEquals(2, results.get(g).getY());

    assertEquals(5, results.get(h).getX());
    assertEquals(3, results.get(h).getY());

    assertEquals(7, results.get(i).getX());
    assertEquals(3, results.get(i).getY());
  }
}
