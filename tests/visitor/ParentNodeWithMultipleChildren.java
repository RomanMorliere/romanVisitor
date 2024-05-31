package tests.visitor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;

import ast.AST;
import ast.trees.BlockTree;
import ast.trees.ProgramTree;
import visitor.images.Offset;
import visitor.images.OffsetVisitor;

public class ParentNodeWithMultipleChildren {

  @Test
  public void testParentNodeWithTwoChildren() {
    AST root = new ProgramTree();
    AST childOne = new BlockTree();
    AST childTwo = new BlockTree();
    root.addChild(childOne).addChild(childTwo);

    OffsetVisitor visitor = new OffsetVisitor();
    root.accept(visitor);

    Map<AST, Offset> results = visitor.getOffsets();
    Offset rootOffset = results.get(root);
    Offset childOneOffset = results.get(childOne);
    Offset childTwoOffset = results.get(childTwo);

    assertEquals(1, rootOffset.getX());
    assertEquals(0, rootOffset.getY());

    assertEquals(0, childOneOffset.getX());
    assertEquals(1, childOneOffset.getY());

    assertEquals(2, childTwoOffset.getX());
    assertEquals(1, childTwoOffset.getY());
  }

  @Test
  public void testParentNodeWithThreeChildren() {
    AST root = new ProgramTree();
    AST childOne = new BlockTree();
    AST childTwo = new BlockTree();
    AST childThree = new BlockTree();
    root.addChild(childOne).addChild(childTwo).addChild(childThree);

    OffsetVisitor visitor = new OffsetVisitor();
    root.accept(visitor);

    Map<AST, Offset> results = visitor.getOffsets();
    Offset rootOffset = results.get(root);
    Offset childOneOffset = results.get(childOne);
    Offset childTwoOffset = results.get(childTwo);
    Offset childThreeOffset = results.get(childThree);

    assertEquals(2, rootOffset.getX());
    assertEquals(0, rootOffset.getY());

    assertEquals(0, childOneOffset.getX());
    assertEquals(1, childOneOffset.getY());

    assertEquals(2, childTwoOffset.getX());
    assertEquals(1, childTwoOffset.getY());

    assertEquals(4, childThreeOffset.getX());
    assertEquals(1, childThreeOffset.getY());
  }
}
