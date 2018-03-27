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

        findConstants(ast.root);

        //Save the symbol table.
        ast.symboltable = symbolTable;

        checkNode(ast.root);

        //Save the verdict
        if (ast.getErrors().isEmpty()) {
            ast.checked = true;
        }
    }

    private void findConstants(ASTNode node) {
        if (node instanceof ConstantDefinition) {
            checkConstants((ConstantDefinition) node);
        }

        if (node.getChildren().size() > 0) {
            node.getChildren().forEach(this::findConstants);
        }
    }

    private void checkNode(ASTNode node) {
        if (node instanceof ConstantDefinition) {
            checkConstants((ConstantDefinition) node);
        } else if (node instanceof ConstantReference) {
            checkReference((ConstantReference) node);
        } else if (node instanceof Operation) {
            checkOperation((Operation) node);
        } else if (node instanceof Declaration) {
            checkDeclaration((Declaration) node);
        }

        for (ASTNode astNode : node.getChildren()) {
            checkNode(astNode);
        }
    }

    /**
     * TODO ADD MORE THINGS
     *
     * @param node
     */
    private void checkConstants(ConstantDefinition node) {
        String nodeName = node.name.name;
        boolean error = false;

        // check whether assignment would cause a loop
        if (node.expression instanceof ConstantReference) {
            ConstantReference reference = (ConstantReference) node.expression;
            if (reference.name.equals(nodeName)) {
                node.setError("You can't assign a constant to itself.");
                error = true;
            } else {
                isCircularReference(reference);
            }
        }
//        TODO FIX
        // if assignment is an operation, check the operation
//        if (node.expression instanceof Operation) {
//            checkOperation((Operation) node.expression);
//        }

        // if no error has been found add it to symbolTable.
        if (!error) {
            symbolTable.put(nodeName, node);
        }
    }

    /**
     * Checks if a reference is circular
     *
     * TODO Maybe remove
     *
     * @param node Reference to check.
     * @return true if reference is circular, else: false.
     */
    private boolean isCircularReference(ConstantReference node) {
        ArrayList<String> trail = new ArrayList<>();
        trail.add(node.name);

        String previousName = node.name;
        boolean running = true;
        boolean isCircular = false;
        while ((symbolTable.get(previousName) != null) && running) {
            ConstantReference newReference = symbolTable.get(previousName).name;
            if (!trail.contains(newReference.name)) {
                trail.add(newReference.name);
                previousName = newReference.name;
            } else {
                node.setError(String.format("Circular reference detected. %s", previousName));
                running = false;
                isCircular = true;
            }
        }

        return isCircular;
    }

    /**
     * TODO
     *
     * @param node
     * @return ExpressionType of operation.
     */
    private ExpressionType checkOperation(Operation node) {

        ExpressionType left;
        ExpressionType right;

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
        if (left != right) {
            node.setError(String.format("Operation error at %s", node.getNodeLabel()));
            return ExpressionType.UNDEFINED;
        } else {
            return left;
        }

    }

    /**
     * Returns the valueType
     *
     * @param expression value to check type of
     * @return ExpressionType of expression
     */
    private ExpressionType checkValueType(Expression expression) {

        if (expression instanceof ConstantReference) {
            ConstantReference value = (ConstantReference) expression;
            if (!isCircularReference(value)) {
                if (symbolTable.get(value.name) == null) {
                    return Expression Type.UNDEFINED;
                } else {
                    return checkValueType(symbolTable.get(value.name).expression);
                }
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
     * @param node Declaration where type assignment is checked.
     */
    private void checkDeclaration(Declaration node) {
        if (node.expression instanceof ColorLiteral) {
            if (!colorProperties.contains(node.property)) {
                node.setError("You can only assign a colour value to a colour property.");
            }
        } else if (node.expression instanceof PercentageLiteral || node.expression instanceof PixelLiteral) {
            if (!pixelProperties.contains(node.property)) {
                node.setError("You can only assign a size value to a size property.");
            }
        } else if (node.expression instanceof ConstantReference) {
            Declaration tempDeclaration = new Declaration();
            tempDeclaration.property = node.property;
            tempDeclaration.expression = symbolTable.get(((ConstantReference) node.expression).name).expression;
            checkDeclaration(tempDeclaration);
        }
    }

    /**
     * Check if a reference is set
     *
     * @param node reference to check
     */
    private void checkReference(ConstantReference node) {
        if (!symbolTable.containsKey(node.name)) {
            node.setError(String.format("Constant %s used but not assigned", node.name));
        }
    }

}
