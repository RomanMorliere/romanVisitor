package ast.trees;

import ast.AST;
import visitor.AstVisitor;

/**
 * This file is automatically generated
 * Do not manually update! (Use the CompilerTools to regenerate.)
 **/
public class CharTypeTree extends AST {

    @Override
    public Object accept(AstVisitor visitor) {
        return visitor.visitCharTypeTree(this);
    }
}