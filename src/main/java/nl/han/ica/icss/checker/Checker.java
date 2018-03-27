package nl.han.ica.icss.checker;

import java.util.ArrayList;
import java.util.HashMap;

import nl.han.ica.icss.ast.*;

public class Checker {

    private HashMap<String, ConstantDefinition> symbolTable;
    private ArrayList<String> colorProperties = new ArrayList<>();
    private ArrayList<String> pixelProperties = new ArrayList<>();

    public Checker() {
        // Set available properties.
        // Could be loaded in as a file if more properties are added.
        colorProperties.add("color");
        colorProperties.add("background-color");

        pixelProperties.add("width");
        pixelProperties.add("height");
    }

    public void checkNode(AST ast) {
        //Clear symbol table
        symbolTable = new HashMap<>();


//        findConstants(ast.root);

        //Save the symbol table.
        ast.symboltable = symbolTable;

        checkNode(ast.root);

        //Save the verdict
        if (ast.getErrors().isEmpty()) {
            ast.checked = true;
        }
    }

    /**
     * Add all constant definitions to the symbolTable
     *
     * @param node to be checked
     */
    private void findConstants(ASTNode node) {


        if (node.getChildren().size() > 0) {
            node.getChildren().forEach(this::findConstants);
        }
    }

    /**
     * Check the different states a node can have.
     *
     * @param node to be checked
     */
    private void checkNode(ASTNode node) {

        if (node instanceof ConstantDefinition)
            checkConstants((ConstantDefinition) node);
        else if (node instanceof ConstantReference)
            checkReference((ConstantReference) node);
        else if (node instanceof Operation)
            checkOperation((Operation) node);
        else if (node instanceof Declaration)
            checkDeclaration((Declaration) node);
        else if (node instanceof SwitchRule)
            checkSwitch((SwitchRule) node);

        // Recursively for all children of every node
        node.getChildren().forEach(this::checkNode);
    }

    /**
     * Check if a switch exists of the same matching types as the to be matched.
     *
     * @param node to be checked
     */
    private void checkSwitch(SwitchRule node) {
        node.valueCases.forEach(valueCase -> {
            Expression value = node.match;
            if (node.match instanceof ConstantReference) {
                if (checkReference((ConstantReference) node.match))
                    value = symbolTable.get(((ConstantReference) node.match).name).expression;
                else return;
            }
            if (valueCase.value.getClass() != value.getClass()) {
                node.setError(String.format("All condition cases should be of the same type as the matched. At %s %s", node.getNodeLabel(), node.selector));
            }
        });
    }

    /**
     * Check if the constant definition is correct.
     * - Check duplicate
     * - Check circular reference
     * <p>
     * Add the correct constants to the symbol table.
     *
     * @param node to be checked
     */
    private void checkConstants(ConstantDefinition node) {
        String nodeName = node.name.name;

        // Check if a assignment would cause a loop
        if (node.expression instanceof ConstantReference) {
            ConstantReference reference = (ConstantReference) node.expression;
            if (reference.name.equals(nodeName)) {
                node.setError("You can't assign a constant to itself.");
            }
        }

        // Check if a constant with the same name is already defined.
        if (symbolTable.get(nodeName) != null) {
            node.setError("You can't double assign a constant");
        }

        // Check if assignment is an operation, check the operation
        if (node.expression instanceof Operation) {
            checkOperation((Operation) node.expression);
        }

        // if no error has been found add it to symbolTable.
        if (!node.hasError()) {
            symbolTable.put(nodeName, node);
        }
    }

    /**
     * Check if a operation is valid.
     *
     * @param node left and right side checked for
     * @return ExpressionType of operation.
     */
    private ExpressionType checkOperation(Operation node) {

        ExpressionType left;
        ExpressionType right;

        // TODO CHECK AGAIN @ Level 2
        // Fs up when the calculation rules are invalid! Why is this in the program.

        // Check if left side of operation is another operation
        if (node.lhs instanceof Operation) {
            left = checkOperation((Operation) node.lhs);
        } else {
            left = checkValueType(node.lhs);
        }
        if (node.rhs instanceof Operation) {
            right = checkOperation((Operation) node.rhs);
        } else {
            right = checkValueType(node.rhs);
        }

        if (node instanceof MultiplyOperation) {
            if (left != ExpressionType.SCALAR && right != ExpressionType.SCALAR) {
                node.setError(String.format("You can only multiply with scalar types. At %s ", node.getNodeLabel()));
                return ExpressionType.UNDEFINED;
            }
            return right != ExpressionType.SCALAR ? right : left;
        } else if (node instanceof SubtractOperation || node instanceof AddOperation) {
            if (left != right) {
                node.setError(String.format("You can only %s the same types.", node.getNodeLabel()));
                return ExpressionType.UNDEFINED;
            }
        }

        return left;
    }

    /**
     * Returns the ExpressionType of a expression
     *
     * @param expression node to check type of
     * @return ExpressionType of expression
     */
    private ExpressionType checkValueType(Expression expression) {

        if (expression instanceof ConstantReference) {
            ConstantReference value = (ConstantReference) expression;
            if (symbolTable.get(value.name) == null) {
                return ExpressionType.UNDEFINED;
            } else {
                return checkValueType(symbolTable.get(value.name).expression);
            }
        } else if (expression instanceof Operation) {
            return checkOperation((Operation) expression);
        } else {
            if (expression instanceof PercentageLiteral) {
                return ExpressionType.PERCENTAGE;
            } else if (expression instanceof PixelLiteral) {
                return ExpressionType.PIXEL;
            } else if (expression instanceof ColorLiteral) {
                return ExpressionType.COLOR;
            } else if (expression instanceof ScalarLiteral) {
                return ExpressionType.SCALAR;
            }
        }

        return ExpressionType.UNDEFINED;
    }


    /**
     * Check if a declaration is valid.
     *
     * @param node Declaration where type assignment is checked.
     */
    private void checkDeclaration(Declaration node) {
        if (node.expression instanceof ColorLiteral) {
            if (!colorProperties.contains(node.property))
                node.setError(String.format("You can only assign a colour value to a colour property. At %s", node.property));

        } else if (node.expression instanceof PercentageLiteral || node.expression instanceof PixelLiteral) {
            if (!pixelProperties.contains(node.property))
                node.setError(String.format("You can only assign a size value to a size property. At %s", node.property));

        } else if (node.expression instanceof ConstantReference) {
            // If declaration exists of a ConstantReference search backwards for the type of the reference.
            Declaration tempDeclaration = new Declaration();
            tempDeclaration.property = node.property;
            ConstantDefinition definition = symbolTable.get(((ConstantReference) node.expression).name);
            if (definition != null) {
                tempDeclaration.expression = definition.expression;
                checkDeclaration(tempDeclaration);
            }
        }
    }

    /**
     * Check if a reference is set before being used.
     *
     * @param node reference to check
     */
    private boolean checkReference(ConstantReference node) {
        if (!symbolTable.containsKey(node.name)) {
            node.setError(String.format("Constant %s used but not assigned", node.name));
            return false;
        }
        return true;
    }
}
