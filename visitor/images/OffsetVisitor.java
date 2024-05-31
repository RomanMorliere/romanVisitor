package visitor.images;

import ast.AST;
import visitor.AstVisitor;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OffsetVisitor extends AstVisitor {

    private Map<AST, Offset> nodePositions;
    private int[] nextAvailableOffset;
    private int currentDepth;

    public OffsetVisitor() {
        nodePositions = new HashMap<>();
        nextAvailableOffset = new int[100]; // Assuming maximum tree depth is 100
        currentDepth = 0;
    }




    public void process(AST node, int depth) {
        // Base case for leaf nodes
        if (node.getChildren().isEmpty()) {
            int offset = nextAvailableOffset[depth];
            nodePositions.put(node, new Offset(offset, depth));
            nextAvailableOffset[depth] += 2; // Prepare for the next sibling
        } else {
            // Set a placeholder offset for non-leaf nodes; actual value will be calculated later
            nodePositions.put(node, new Offset(0, depth));
        }

        // Recursively visit children to adjust their offsets first
        currentDepth++;
        for (AST child : node.getChildren()) {
            process(child, currentDepth);
        }
        currentDepth--;

        // Post-order processing: calculate and adjust offsets after visiting children
        if (!node.getChildren().isEmpty()) {
            int leftOffset = nodePositions.get(node.getChildren().get(0)).getX();
            int rightOffset = nodePositions.get(node.getChildren().get(node.getChildren().size() - 1)).getX();
            int centerOffset = (leftOffset + rightOffset) / 2;

            // Check if the centered offset needs adjustment to avoid overlap
            if (centerOffset < nextAvailableOffset[depth]) {
                int adjustment = nextAvailableOffset[depth] - centerOffset;
                shiftTree(node, adjustment);
                centerOffset += adjustment; // Adjust the centered offset after shifting
            }

            // Update the node's offset and prepare the offset for the next node at this depth
            nodePositions.get(node).setX(centerOffset);
            nextAvailableOffset[depth] = centerOffset + 2; // Ensure space for the next node
        }
    }

    private void shiftTree(AST node, int delta) {
        // Apply the shift to the current node
        Offset nodeOffset = nodePositions.get(node);
        nodeOffset.setX(nodeOffset.getX() + delta);

        // Recursively apply the shift to all children
        for (AST child : node.getChildren()) {
            shiftTree(child, delta);
        }
    }

    public Map<AST, Offset> getOffsets() {
        return nodePositions;
    }


    @Override
    public Object visitProgramTree(AST node) {
        process(node,currentDepth);
        return null;
    }

    @Override
    public Object visitBlockTree(AST node) {
        process(node,currentDepth);
        return null;
    }

    @Override
    public Object visitDeclarationTree(AST node) {
        process(node,currentDepth);
        return null;
    }

    @Override
    public Object visitFunctionDeclarationTree(AST node) {
        process(node,currentDepth);
        return null;
    }

    @Override
    public Object visitFormalsTree(AST node) {
        process(node,currentDepth);
        return null;
    }

    @Override
    public Object visitIntTypeTree(AST node) {
        process(node,currentDepth);
        return null;
    }

    @Override
    public Object visitBoolTypeTree(AST node) {
        process(node,currentDepth);
        return null;
    }

    @Override
    public Object visitIfTree(AST node) {
        process(node,currentDepth);
        return null;
    }

    @Override
    public Object visitWhileTree(AST node) {
        process(node,currentDepth);
        return null;
    }

    @Override
    public Object visitReturnTree(AST node) {
        process(node,currentDepth);
        return null;
    }

    @Override
    public Object visitAssignmentTree(AST node) {
        process(node,currentDepth);
        return null;
    }

    @Override
    public Object visitCallTree(AST node) {
        process(node,currentDepth);
        return null;
    }

    @Override
    public Object visitActualArgumentsTree(AST node) {
        process(node,currentDepth);
        return null;
    }

    @Override
    public Object visitRelOpTree(AST node) {
        process(node,currentDepth);
        return null;
    }

    @Override
    public Object visitAddOpTree(AST node) {
        process(node,currentDepth);
        return null;
    }

    @Override
    public Object visitMultOpTree(AST node) {
        process(node,currentDepth);
        return null;
    }

    @Override
    public Object visitIntTree(AST node) {
        process(node,currentDepth);
        return null;
    }

    @Override
    public Object visitIdentifierTree(AST node) {
        process(node,currentDepth);
        return null;
    }

    @Override
    public Object visitNumberTree(AST node) {
        process(node,currentDepth);
        return null;
    }

    @Override
    public Object visitNumberLitTree(AST node) {
        process(node,currentDepth);
        return null;
    }

    @Override
    public Object visitCharTypeTree(AST node) {
        process(node,currentDepth);
        return null;
    }

    @Override
    public Object visitCharLitTree(AST node) {
        process(node,currentDepth);
        return null;
    }

    @Override
    public Object visitFromTree(AST node) {
        process(node,currentDepth);
        return null;
    }

    @Override
    public Object visitRangeTree(AST node) {
        process(node,currentDepth);
        return null;
    }

    @Override
    public Object visitStepTree(AST node) {
        process(node,currentDepth);
        return null;
    }

    public int getUnitWidth() {
        return 100;
    }

    public int getUnitHeight() {
        return 50;
    }
}
