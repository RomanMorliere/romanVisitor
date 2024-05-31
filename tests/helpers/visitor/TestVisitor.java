package tests.helpers.visitor;

import ast.*;
import java.util.List;
import visitor.AstVisitor;

public class TestVisitor extends AstVisitor {

    private List<AST> expected;
    private int index;

    public TestVisitor(final List<AST> expected) {
        this.expected = expected;
        this.index = 0;
    }

    public Object test(AST t) {

        try {
            expected.get(index).getClass().cast(t);

            index++;

            return testChildren(t);
        } catch (ClassCastException exception) {
            return String.format(
                "Expected [%s] but got [%s]",
                expected.get(index).getClass().getSimpleName(),
                t.getClass().getSimpleName());
        }
    }

    private Object test(AST t, String expectedSymbol, String actualSymbol) {
        try {
            expected.get(index).getClass().cast(t);

            if (!expectedSymbol.equals(actualSymbol)) {
                throw new Exception(
                    String.format(
                        "Expected [%s] but got [%s]",
                            expectedSymbol,
                            actualSymbol));
            }

            index++;
            return testChildren(t);
        } catch (ClassCastException exception) {
            return String.format(
                "Expected [%s] but got [%s]",
                expected.get(index).getClass().getSimpleName(),
                t.getClass().getSimpleName());
        } catch (Exception exception) {
            return exception.getMessage();
        }
    }

    private Object testChildren(AST t) {
        for (AST child : t.getChildren()) {
            Object result = child.accept(this);

            if (result != null) {
                return result;
            }
        }

        return null;
    }

    @Override
    public Object visitProgramTree(AST node) {
        return test(node);
    }

    @Override
    public Object visitBlockTree(AST node) {
        return test(node);
    }

    @Override
    public Object visitDeclarationTree(AST node) {
        return test(node);
    }

    @Override
    public Object visitFunctionDeclarationTree(AST node) {
        return test(node);
    }

    @Override
    public Object visitFormalsTree(AST node) {
        return test(node);
    }

    @Override
    public Object visitIntTypeTree(AST node) {
        return test(node);
    }

    @Override
    public Object visitBoolTypeTree(AST node) {
        return test(node);
    }

    @Override
    public Object visitIfTree(AST node) {
        return test(node);
    }

    @Override
    public Object visitWhileTree(AST node) {
        return test(node);
    }

    @Override
    public Object visitReturnTree(AST node) {
        return test(node);
    }

    @Override
    public Object visitAssignmentTree(AST node) {
        return test(node);
    }

    @Override
    public Object visitCallTree(AST node) {
        return test(node);
    }

    @Override
    public Object visitActualArgumentsTree(AST node) {
        return test(node);
    }

    @Override
    public Object visitRelOpTree(AST node) {
        String actualSymbol = ((ISymbolTree) node).getSymbol().toString();
        String expectedSymbol = ((ISymbolTree) expected.get(index)).getSymbol().toString();

        return test(node, expectedSymbol, actualSymbol);
    }

    @Override
    public Object visitAddOpTree(AST node) {
        String actualSymbol = ((ISymbolTree) node).getSymbol().toString();
        String expectedSymbol = ((ISymbolTree) expected.get(index)).getSymbol().toString();

        return test(node, expectedSymbol, actualSymbol);
    }

    @Override
    public Object visitMultOpTree(AST node) {
        String actualSymbol = ((ISymbolTree) node).getSymbol().toString();
        String expectedSymbol = ((ISymbolTree) expected.get(index)).getSymbol().toString();

        return test(node, expectedSymbol, actualSymbol);
    }

    @Override
    public Object visitIntTree(AST node) {
        String actualSymbol = ((ISymbolTree) node).getSymbol().toString();
        String expectedSymbol = ((ISymbolTree) expected.get(index)).getSymbol().toString();

        return test(node, expectedSymbol, actualSymbol);
    }

    @Override
    public Object visitIdentifierTree(AST node) {
        String actualSymbol = ((ISymbolTree) node).getSymbol().toString();
        String expectedSymbol = ((ISymbolTree) expected.get(index)).getSymbol().toString();

        return test(node, expectedSymbol, actualSymbol);
    }

    @Override
    public Object visitNumberTree(AST node) {
        return test(node);
    }

    @Override
    public Object visitNumberLitTree(AST node) {
        String actualSymbol = ((ISymbolTree) node).getSymbol().toString();
        String expectedSymbol = ((ISymbolTree) expected.get(index)).getSymbol().toString();

        return test(node, expectedSymbol, actualSymbol);
    }

    @Override
    public Object visitCharTypeTree(AST node) {
        return test(node);
    }

    @Override
    public Object visitCharLitTree(AST node) {
        String actualSymbol = ((ISymbolTree) node).getSymbol().toString();
        String expectedSymbol = ((ISymbolTree) expected.get(index)).getSymbol().toString();

        return test(node, expectedSymbol, actualSymbol);
    }

    @Override
    public Object visitFromTree(AST node) {
        return test(node);
    }

    @Override
    public Object visitRangeTree(AST node) {
        return test(node);
    }

    @Override
    public Object visitStepTree(AST node) {
        return test(node);
    }

}