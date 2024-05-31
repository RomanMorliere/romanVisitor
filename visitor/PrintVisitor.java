package visitor;

import ast.*;

public class PrintVisitor extends AstVisitor {

    private final int INDENT_BY = 2;
    private int indentation = INDENT_BY;

    private void print(String description, AST node) {
        String nodeNumber = String.format("%3d:", node.getNodeNumber());
        System.out.println(String.format("%-3s %-35.35s",
                nodeNumber,
                String.format("%" + indentation + "s%s", "", description)));

        this.indentation += INDENT_BY;
        this.visitChildren(node);
        this.indentation -= INDENT_BY;
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