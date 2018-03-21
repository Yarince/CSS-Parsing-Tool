package nl.han.ica.icss.ast;

import java.util.ArrayList;

public class Switchrule extends ASTNode {

    public Selector selector;
    public Expression match;
    public ArrayList<SwitchValueCase> valueCases = new ArrayList<>();
    public SwitchDefaultCase defaultCase;

    @Override
    public String getNodeLabel() {
        return "Switch";
    }
    @Override
    public ArrayList<ASTNode> getChildren() {
        ArrayList<ASTNode> children = new ArrayList<>();

        children.add(selector);
        children.add(match);
        children.addAll(valueCases);
        children.add(defaultCase);

        return children;
    }
    @Override
    public void addChild(ASTNode child) {
        if(child instanceof Selector)
            selector = (Selector) child;
        if(child instanceof Expression)
            match = (Expression) child;
        if(child instanceof SwitchValueCase)
            valueCases.add((SwitchValueCase) child);
        if(child instanceof SwitchDefaultCase)
            defaultCase = (SwitchDefaultCase) child;
    }
}
