package nl.han.ica.icss.generator;

import nl.han.ica.icss.ast.*;

public class Generator {

    private StringBuilder build = new StringBuilder();

    public String generate(AST ast) {

        loopChildren(ast.root);

        return build.toString();
    }

    private void loopChildren(ASTNode node) {
        for (ASTNode child : node.getChildren()) {
            getString(child);
//            loopChildren(child);
        }
    }

    private void getString(ASTNode node) {
        if (node instanceof StyleRule) {
            build.append(((StyleRule) node).selector).append(" {\n");
            loopChildren(node);
            build.append("} \n \n");
        }
        for (ASTNode child : node.getChildren()) {

            if (child instanceof Declaration)
                build.append(((Declaration) child).property).append(": ");
            else if (child instanceof Expression) {
                if (child instanceof PixelLiteral)
                    build.append(((PixelLiteral) child).value).append("px;");
                if (child instanceof PercentageLiteral)
                    build.append(((PercentageLiteral) child).value).append("%;");
                if (child instanceof ColorLiteral)
                    build.append(((ColorLiteral) child).value).append(";");
                build.append("\n");
            }
        }


    }
}
