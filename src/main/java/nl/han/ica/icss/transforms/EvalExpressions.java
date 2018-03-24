package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.*;

import java.util.HashMap;

public class EvalExpressions implements Transform {

    private HashMap<String,ConstantDefinition> symboltable; //Another hint...

    @Override
    public void apply(AST ast) {
        symboltable = ast.symboltable;

    }
}
