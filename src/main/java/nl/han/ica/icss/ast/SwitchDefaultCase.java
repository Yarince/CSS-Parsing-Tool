package nl.han.ica.icss.ast;

import java.util.ArrayList;

public class SwitchDefaultCase extends ASTNode {

    public ArrayList<ASTNode> body = new ArrayList<>();

    @Override
    public String getNodeLabel() {
        return "Default";
    }
    @Override
    public ArrayList<ASTNode> getChildren() {
        ArrayList<ASTNode> children = new ArrayList<>();
        children.addAll(body);
        return children;
    }
    @Override
    public void addChild(ASTNode child) {
        body.add(child);
    }
}
