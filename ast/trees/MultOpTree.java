package ast.trees;

import ast.AST;
import visitor.AstVisitor;

import ast.ISymbolTree;
import lexer.daos.SymbolTable;
import lexer.daos.*;

/**
 * This file is automatically generated
 * Do not manually update! (Use the CompilerTools to regenerate.)
 **/
public class MultOpTree extends AST implements ISymbolTree {

    private Symbol symbol;

    public MultOpTree(Token token) {
        this.symbol = SymbolTable.symbol(token.getLexeme(), TokenKind.BogusToken);
    }

    public Symbol getSymbol() {
        return this.symbol;
    }

    @Override
    public Object accept(AstVisitor visitor) {
        return visitor.visitMultOpTree(this);
    }
}