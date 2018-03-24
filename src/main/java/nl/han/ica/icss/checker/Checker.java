package nl.han.ica.icss.checker;

import java.util.ArrayList;
import java.util.HashMap;

import nl.han.ica.icss.ast.*;

public class Checker {

    private HashMap<String, ConstantDefinition> symboltable;

    public void check(AST ast) {
        //Clear symbol table
        symboltable = new HashMap<>();

        collectErrors(ast.root);

        //Save the symbol table.
        ast.symboltable = symboltable;
        //Save the verdict
        if (ast.getErrors().isEmpty()) {
            ast.checked = true;
        }
    }

    private void collectErrors(ASTNode node) {
        for (ASTNode child : node.getChildren()) {
            if (child instanceof ConstantDefinition) {
                symboltable.put(child.getNodeLabel(), (ConstantDefinition) child);
            }
            collectErrors(child);
        }
    }
}
