package visitor.images;

import ast.AST;
import ast.ISymbolTree;
import visitor.AstVisitor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Map;

public class DrawOffsetVisitor extends AstVisitor {
    private Map<AST, Offset> offsets;
    private int unitWidth;
    private int unitHeight;
    private BufferedImage image;
    private final int NODE_WIDTH = 100;
    private final int NODE_HEIGHT = 75;
    private final int HORIZONTAL_PADDING = 25;
    private final int VERTICAL_PADDING = 25;

    public DrawOffsetVisitor(Map<AST, Offset> offsets, int unitWidth, int unitHeight) {
        this.offsets = offsets;
        this.unitWidth = unitWidth;
        this.unitHeight = unitHeight;

        // Calculate maxWidth and maxHeight once and reuse the values
        int maxWidth = maxWidth(offsets);
        int maxHeight = maxHeight(offsets);

        int height = (maxHeight + 1) * this.unitHeight + ((maxHeight + 2) * VERTICAL_PADDING);
        int width = (maxWidth + 1) * this.unitWidth + ((maxWidth + 2) * HORIZONTAL_PADDING);

        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        initializeImageBackground(width, height);
    }

    private void initializeImageBackground(int width, int height) {
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.dispose(); // Ensure graphics resources are released after use
    }

    private int maxWidth(Map<AST, Offset> offsets) {
        return offsets.values().stream().mapToInt(Offset::getX).max().orElse(0);
    }

    private int maxHeight(Map<AST, Offset> offsets) {
        return offsets.values().stream().mapToInt(Offset::getY).max().orElse(0);
    }

    public void print(String Name, AST node) {

        int xOffset = offsets.get(node).getX();
        int yOffset = offsets.get(node).getY();

        // Calculate the actual position in pixels
        int xPixel = xOffset * unitWidth + ((xOffset + 1) * HORIZONTAL_PADDING);
        int yPixel = yOffset * unitHeight  + ((yOffset + 1) * VERTICAL_PADDING );

        // Draw the node
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.lightGray);
        graphics.fillOval(xPixel, yPixel, unitWidth, unitHeight);
        graphics.setColor(Color.BLACK);
        graphics.drawOval(xPixel, yPixel, unitWidth, unitHeight);
        graphics.drawString(Name, xPixel + 5, yPixel+unitHeight/2);
        //draw lines
        for (AST child : node.getChildren()) {
            int xPixelChild = offsets.get(child).getX() * unitWidth + ((offsets.get(child).getX() + 1) * HORIZONTAL_PADDING);
            int yPixelChild = offsets.get(child).getY() * unitHeight  + ((offsets.get(child).getY() + 1) * VERTICAL_PADDING );
            graphics.drawLine(xPixel + unitWidth/2, yPixel + unitHeight,
                    xPixelChild + unitWidth/2, yPixelChild);
        }

        visitChildren(node);

    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }


    public Object visitProgramTree(AST node) {
        print("Program", node);
        return null;
    }

    public Object visitBlockTree(AST node) {
        print("Block", node);
        return null;
    }

    public Object visitDeclarationTree(AST node) {
        print("Declaration", node);
        return null;
    }

    public Object visitFunctionDeclarationTree(AST node) {
        print("FunctionDeclaration", node);
        return null;
    }

    public Object visitFormalsTree(AST node) {
        print("Formals", node);
        return null;
    }

    public Object visitIntTypeTree(AST node) {
        print("IntType", node);
        return null;
    }

    public Object visitBoolTypeTree(AST node) {
        print("BoolType", node);
        return null;
    }

    public Object visitIfTree(AST node) {
        print("If", node);
        return null;
    }

    public Object visitWhileTree(AST node) {
        print("While", node);
        return null;
    }

    public Object visitReturnTree(AST node) {
        print("Return", node);
        return null;
    }

    public Object visitAssignmentTree(AST node) {
        print("Assignment", node);
        return null;
    }

    public Object visitCallTree(AST node) {
        print("Call", node);
        return null;
    }

    public Object visitActualArgumentsTree(AST node) {
        print("ActualArguments", node);
        return null;
    }

    public Object visitRelOpTree(AST node) {
        print(String.format("RelOp: %s", ((ISymbolTree) node).getSymbol().getLexeme()), node);
        return null;
    }

    public Object visitAddOpTree(AST node) {
        print(String.format("AddOp: %s", ((ISymbolTree) node).getSymbol().getLexeme()), node);
        return null;
    }

    public Object visitMultOpTree(AST node) {
        print(String.format("MultOp: %s", ((ISymbolTree) node).getSymbol().getLexeme()), node);
        return null;
    }

    public Object visitIntTree(AST node) {
        print(String.format("Int: %s", ((ISymbolTree) node).getSymbol().getLexeme()), node);
        return null;
    }

    public Object visitIdentifierTree(AST node) {
        print(String.format("Identifier: %s", ((ISymbolTree) node).getSymbol().getLexeme()), node);
        return null;
    }

    public Object visitNumberTree(AST node) {
        print("Number", node);
        return null;
    }

    public Object visitNumberLitTree(AST node) {
        print(String.format("NumberLit: %s", ((ISymbolTree) node).getSymbol().getLexeme()), node);
        return null;
    }

    public Object visitCharTypeTree(AST node) {
        print("CharType", node);
        return null;
    }

    public Object visitCharLitTree(AST node) {
        print(String.format("CharLit: %s", ((ISymbolTree) node).getSymbol().getLexeme()), node);
        return null;
    }

    public Object visitFromTree(AST node) {
        print("From", node);
        return null;
    }

    public Object visitRangeTree(AST node) {
        print("Range", node);
        return null;
    }

    public Object visitStepTree(AST node) {
        print("Step", node);
        return null;
    }
}

