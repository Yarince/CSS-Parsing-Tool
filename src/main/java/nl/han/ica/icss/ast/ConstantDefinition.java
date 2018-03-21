package nl.han.ica.icss.ast;

import java.util.ArrayList;

/**
 * An assignment binds a expression to an identifier.
 *
 */
public class ConstantDefinition extends ASTNode {
	
	public ConstantReference name;
	public Expression expression;

	@Override
	public String getNodeLabel() {
		return "ConstantDefinition (" + name.name + ")";
	}

	@Override
	public void addChild(ASTNode child) {
		if(name == null) {
			name = (ConstantReference) child;
		} else if(expression == null) {
			expression = (Expression) child;
		}
	}

	@Override
	public ArrayList<ASTNode> getChildren() {

		ArrayList<ASTNode> children = new ArrayList<>();
		children.add(name);
		children.add(expression);
		return children;
	}
}
