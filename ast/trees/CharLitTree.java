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
public class CharLitTree extends AST implements ISymbolTree {

    private Symbol symbol;

    public CharLitTree(Token token) {
        this.symbol = SymbolTable.symbol(token.getLexeme(), TokenKind.BogusToken);
    }

    public Symbol getSymbol() {
        return this.symbol;
    }

    @Override
    public Object accept(AstVisitor visitor) {
        return visitor.visitCharLitTree(this);
    }
}