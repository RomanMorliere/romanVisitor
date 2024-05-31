package visitor;

import ast.AST;

public abstract class AstVisitor {

    public void visitChildren(AST parentNode) {
        for (AST child : parentNode.getChildren()) {
            child.accept(this);
        }
    }

    public abstract Object visitProgramTree(AST node);

    public abstract Object visitBlockTree(AST node);

    public abstract Object visitDeclarationTree(AST node);

    public abstract Object visitFunctionDeclarationTree(AST node);

    public abstract Object visitFormalsTree(AST node);

    public abstract Object visitIntTypeTree(AST node);

    public abstract Object visitBoolTypeTree(AST node);

    public abstract Object visitIfTree(AST node);

    public abstract Object visitWhileTree(AST node);

    public abstract Object visitReturnTree(AST node);

    public abstract Object visitAssignmentTree(AST node);

    public abstract Object visitCallTree(AST node);

    public abstract Object visitActualArgumentsTree(AST node);

    public abstract Object visitRelOpTree(AST node);

    public abstract Object visitAddOpTree(AST node);

    public abstract Object visitMultOpTree(AST node);

    public abstract Object visitIntTree(AST node);

    public abstract Object visitIdentifierTree(AST node);

    public abstract Object visitNumberTree(AST node);

    public abstract Object visitNumberLitTree(AST node);

    public abstract Object visitCharTypeTree(AST node);

    public abstract Object visitCharLitTree(AST node);

    public abstract Object visitFromTree(AST node);

    public abstract Object visitRangeTree(AST node);

    public abstract Object visitStepTree(AST node);

}