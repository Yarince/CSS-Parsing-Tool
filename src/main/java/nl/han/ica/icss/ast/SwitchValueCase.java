package nl.han.ica.icss.ast;

import java.util.ArrayList;

public class SwitchValueCase extends ASTNode {

    public Expression value;
    public ArrayList<ASTNode> body = new ArrayList<>();

    @Override
    public String getNodeLabel() {
        return "Case";
    }
    @Override
    public ArrayList<ASTNode> getChildren() {
        ArrayList<ASTNode> children = new ArrayList<>();
        children.add(value);
        children.addAll(body);
        return children;
    }
    @Override
    public void addChild(ASTNode child) {
        if (child instanceof Expression)
            value = (Expression) child;
        else
            body.add(child);
    }
}
