package nl.han.ica.icss.checker;

import java.util.HashMap;

import nl.han.ica.icss.ast.*;

public class Checker {

    private HashMap<String, ConstantDefinition> symboltable;

    public void check(AST ast) {
        //Clear symbol table
        symboltable = new HashMap<>();

        check(ast.root);

        //Save the symbol table.
        ast.symboltable = symboltable;
        //Save the verdict
        if (ast.getErrors().isEmpty()) {
            ast.checked = true;
        }
    }

    private void check(ASTNode node) {
        for (ASTNode child : node.getChildren()) {
            if (child instanceof ConstantDefinition) {
                ConstantDefinition definition = (ConstantDefinition) child;

                if (symboltable.get(definition.name.name) != null)
                    child.setError("Constant is already defined!");
                else
                    symboltable.put(definition.name.name, definition);
            }

            if (child instanceof ConstantReference) {
                checkIfReferenceExists(child);
            }

            if (child instanceof Operation) {
                checkOperationType(child);
            }




            check(child);
        }
    }

    private void checkIfReferenceExists(ASTNode child) {
        ConstantReference reference = (ConstantReference) child;
        if (symboltable.get(reference.name) == null) {
            child.setError(String.format("Constant reference is not initialized! %s", child.getNodeLabel()));
        }
    }

    private void checkOperationType(ASTNode node) {

        Operation operation = (Operation) node;

        if (operation.lhs instanceof Operation) {
            checkOperationType(operation.lhs); //TODO FIX
        } else if (operation.rhs instanceof Operation) {
            checkOperationType(operation.rhs); //TODO FIX
        }



    }
}
