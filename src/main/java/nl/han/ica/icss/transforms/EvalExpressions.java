package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class EvalExpressions implements Transform {

    private HashMap<String, ConstantDefinition> symbolTable;

    @Override
    public void apply(AST ast) {
        symbolTable = ast.symbolTable;

        workoutExpression(ast.root);

        removeExpressions(ast.root);
    }

    private void workoutExpression(ASTNode node) {
        ArrayList<ASTNode> toBeAdded = new ArrayList<>();
        for (ASTNode child : node.getChildren()) {
            if (!(child instanceof ConstantDefinition)) {
                if (child instanceof Expression) {
                    node.addChild(getLiteral((Expression) child));
                } else if (child instanceof SwitchRule) {
                    toBeAdded.add(workoutSwitchCase((SwitchRule) child));
                }

                if (child.getChildren().size() > 0)
                    workoutExpression(child);
            }
        }

        node.getChildren().addAll(toBeAdded);
    }

    private StyleRule workoutSwitchCase(SwitchRule node) {

        StyleRule rule = new StyleRule();

        rule.selector = node.selector;

        boolean found = false;
        for (SwitchValueCase valueCase : node.valueCases) {
            OperationalLiteral right = (OperationalLiteral) getLiteral(node.match);
            OperationalLiteral left = (OperationalLiteral) getLiteral(valueCase.value);
            if (left.value == right.value) {
                rule.body.addAll(valueCase.body);
                found = true;
            }
        }

        if (!found)
            rule.body.addAll(node.defaultCase.body);

        return rule;
    }

    private void removeExpressions(Stylesheet root) {
        root.getChildren().removeIf(node -> node instanceof ConstantDefinition || node instanceof SwitchRule);
    }

    private Expression getLiteral(Expression node) {
        if (node instanceof ConstantReference) {
            node = symbolTable.get(((ConstantReference) node).name).expression;
        } else if (node instanceof Operation) {
            node = execOperation((Operation) node);

        }
        if (!(node instanceof Literal))
            node = getLiteral(node);

        return node;
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
            if (lhs instanceof ScalarLiteral)
                return getNewLiteral(rhs, lhs.value * rhs.value);
            return getNewLiteral(lhs, lhs.value * rhs.value);

        } else if (node instanceof SubtractOperation)
            return getNewLiteral(lhs, lhs.value - rhs.value);
        else // Then it must be a AddOperation.
            return getNewLiteral(lhs, lhs.value + rhs.value);
    }

    private OperationalLiteral getNewLiteral(OperationalLiteral node, int value) {
        if (node instanceof PixelLiteral)
            return new PixelLiteral(value);
        else if (node instanceof ScalarLiteral)
            return new ScalarLiteral(value);
        else // Then it must be a Percentage literal
            return new PercentageLiteral(value);
    }
}
