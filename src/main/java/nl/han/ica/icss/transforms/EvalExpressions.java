package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.*;

import java.util.HashMap;
import java.util.Iterator;

public class EvalExpressions implements Transform {

    private HashMap<String, ConstantDefinition> symbolTable;

    @Override
    public void apply(AST ast) {
        symbolTable = ast.symbolTable;

        makeLiteral(ast.root);


        removeConstantDeclaration(ast.root);
    }

    private void makeLiteral(ASTNode node) {

        for (ASTNode child : node.getChildren()) {
            if (child instanceof ConstantDefinition)
                continue;
            if (child instanceof Expression) {
                node.addChild(getLiteral((Expression) child));
            }
            if (child.getChildren().size() > 0)
                makeLiteral(child);
        }
    }

    private void removeConstantDeclaration(Stylesheet root) {
        root.getChildren().removeIf(node -> node instanceof ConstantDefinition);//   || node instanceof SwitchRule);
    }

    private Expression getLiteral(Expression node) {
        Expression literal = node;

        if (node instanceof ConstantReference) {
            literal = symbolTable.get(((ConstantReference) node).name).expression;
        } else if (node instanceof Operation) {
            literal = execOperation((Operation) node);
        }
        //TODO switch case

        if (!(literal instanceof Literal))
            literal = getLiteral(literal);

        return literal;
    }

    /**
     * Execute operation
     *
     * @param node left and right side checked for
     * @return ExpressionType of operation.
     */
    private Literal execOperation(Operation node) {

        // Check if left side of the operation is OperationalLiteral
        OperationalLiteral lhs;
        if (node.lhs instanceof OperationalLiteral) lhs = (OperationalLiteral) node.lhs;
        else lhs = (OperationalLiteral) getLiteral(node.lhs);

        // Check if right side of the operation is a OperationalLiteral
        OperationalLiteral rhs;
        if (node.rhs instanceof OperationalLiteral) rhs = (OperationalLiteral) node.rhs;
        else rhs = (OperationalLiteral) getLiteral(node.rhs);

        // Check the specific Operations
        if (node instanceof MultiplyOperation) {
            if (lhs instanceof ScalarLiteral) {
                rhs.value = lhs.value * rhs.value;
                return rhs;
            }
            lhs.value = lhs.value * rhs.value;
            return lhs;
        } else if (node instanceof SubtractOperation) {
            lhs.value = lhs.value - rhs.value;
            return lhs;
        } else { // Then it must be a AddOperation.
            lhs.value = lhs.value + rhs.value;
            return lhs;
        }
    }
}
