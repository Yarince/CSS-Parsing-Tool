package nl.han.ica.icss.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

import nl.han.ica.icss.ast.*;

/**
 * This class extracts the ICSS Abstract Syntax Tree from the Antlr Parse tree.
 */
public class ASTListener extends ICSSBaseListener {

    //Accumulator attributes:
    private AST ast;
    private Stack<ASTNode> currentContainer;

    public ASTListener() {
        ast = new AST();
        currentContainer = new Stack<>();
    }

    // I've chosen not to use the ".addChild" function too much for clarity of function.

    @Override
    public void exitStylesheet(ICSSParser.StylesheetContext ctx) {
        Stylesheet node = new Stylesheet(new ArrayList<>());

        while (!currentContainer.isEmpty())
            node.addChild(currentContainer.pop());

        ast.root = node;
    }

    @Override
    public void exitVariableInit(ICSSParser.VariableInitContext ctx) {
        ConstantDefinition definition = new ConstantDefinition();

        definition.name = new ConstantReference(ctx.getChild(1).getText());

        definition.addChild(currentContainer.pop());

        currentContainer.add(definition);
    }

    @Override
    public void exitSwitchCase(ICSSParser.SwitchCaseContext ctx) {
        SwitchRule switchRule = new SwitchRule();

        switchRule.defaultCase = (SwitchDefaultCase) currentContainer.pop();

        while (currentContainer.peek() instanceof SwitchValueCase)
            switchRule.addChild(currentContainer.pop());

        switchRule.selector = (Selector) currentContainer.pop();
        switchRule.match = new ConstantReference(ctx.VARIABLE().getText());

        currentContainer.add(switchRule);
    }

    @Override
    public void exitCaseOption(ICSSParser.CaseOptionContext ctx) {
        SwitchValueCase valueCase = new SwitchValueCase();

        while (currentContainer.peek() instanceof Declaration)
            valueCase.addChild(currentContainer.pop());

        valueCase.value = (Expression) currentContainer.pop();
        currentContainer.add(valueCase);
    }

    @Override
    public void exitDefaultOption(ICSSParser.DefaultOptionContext ctx) {
        SwitchDefaultCase defaultCase = new SwitchDefaultCase();

        while (currentContainer.peek() instanceof Declaration)
            defaultCase.addChild(currentContainer.pop());

        currentContainer.add(defaultCase);
    }

    @Override
    public void exitBlock(ICSSParser.BlockContext ctx) {
        StyleRule styleRule = new StyleRule();

        while (currentContainer.peek() instanceof Declaration)
            styleRule.addChild(currentContainer.pop());

        styleRule.selector = (Selector) currentContainer.pop();

        currentContainer.add(styleRule);
    }

    @Override
    public void exitRow(ICSSParser.RowContext ctx) {
        Declaration declaration = new Declaration(ctx.styleAttribute().getText());
        declaration.addChild(currentContainer.pop());

        currentContainer.add(declaration);
    }

    @Override
    public void exitSelectors(ICSSParser.SelectorsContext ctx) {
        Selector selector;
        String name = ctx.getText();
        if (ctx.CLASS() != null) selector = new ClassSelector(name);
        else if (ctx.ID() != null) selector = new IdSelector(name);
        else selector = new TagSelector(name);

        currentContainer.add(selector);
    }

    @Override
    public void exitValue(ICSSParser.ValueContext ctx) {
        Expression literal;

        if (ctx.COLOR() != null) literal = new ColorLiteral(ctx.COLOR().getText());
        else if (ctx.PERCENTAGE() != null) literal = new PercentageLiteral(ctx.PERCENTAGE().getText());
        else if (ctx.PIXEL() != null) literal = new PixelLiteral(ctx.PIXEL().getText());
        else if (ctx.VARIABLE() != null) literal = new ConstantReference(ctx.getChild(0).getText());
        else literal = new ScalarLiteral(ctx.DIGITS().getText());


        currentContainer.add(literal);
    }

    @Override
    public void exitValueCalc(ICSSParser.ValueCalcContext ctx) {
        Operation operation;
        if (ctx.MULTIPLICATION() != null) operation = new MultiplyOperation();
        else if (ctx.ADDITION() != null) operation = new AddOperation();
        else operation = new SubtractOperation();

        operation.rhs = (Expression) currentContainer.pop();
        operation.lhs = (Expression) currentContainer.pop();

        currentContainer.add(operation);
    }

    public AST getAST() {
        Collections.reverse(ast.root.getChildren());
        return ast;
    }
}
