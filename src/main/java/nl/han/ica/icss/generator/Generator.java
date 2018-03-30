package nl.han.ica.icss.generator;

import nl.han.ica.icss.ast.*;

public class Generator {

    private StringBuilder build;

    public String generate(AST ast) {

        build = new StringBuilder();

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

            for (ASTNode declarationNode : node.getChildren()) {
                if (declarationNode instanceof Declaration) {
                    Declaration declaration = (Declaration) declarationNode;
                    build.append("\t").append(declaration.property).append(": ");
                    Expression literal = declaration.expression;

                    if (literal instanceof PixelLiteral)
                        build.append(((PixelLiteral) literal).value).append("px;");
                    else if (literal instanceof PercentageLiteral)
                        build.append(((PercentageLiteral) literal).value).append("%;");
                    else if (literal instanceof ColorLiteral)
                        build.append(((ColorLiteral) literal).value).append(";");
                    build.append("\n");
                }
            }

            build.append("} \n \n");
        }
    }
}
