package nl.han.ica.icss.ast;

import java.util.ArrayList;

/*
 * A Declaration defines a style property. Declarations are things like "width: 100px"
 */
public class Declaration extends ASTNode {
	public String property;
	public Expression expression;

	public Declaration(String property) {
		this.property = property;
	}
	public Declaration(){}

	@Override
	public String getNodeLabel() {
	    return "Declaration (" + property + ")";
	}

	@Override
	public ArrayList<ASTNode> getChildren() {

		ArrayList<ASTNode> children = new ArrayList<>();
		children.add(expression);
		return children;
	}
	@Override
	public void addChild(ASTNode child) {
		expression = (Expression) child;
	}
}
