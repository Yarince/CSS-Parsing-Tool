package nl.han.ica.icss.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import nl.han.ica.icss.ast.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * This class extracts the ICSS Abstract Syntax Tree from the Antlr Parse tree.
 */
public class ASTListener extends ICSSBaseListener {

    //Accumulator attributes:
    private AST ast;
    private Stack<ASTNode> currentContainer; //This is a hint...

    public ASTListener() {
        ast = new AST();
        currentContainer = new Stack<>();

//        ast.root = (Stylesheet) currentContainer.pop();

    }

    @Override
    public void exitStylesheet(ICSSParser.StylesheetContext ctx) {
        Stylesheet node = new Stylesheet();
    }

    @Override
    public void exitVariableInit(ICSSParser.VariableInitContext ctx) {
        super.exitVariableInit(ctx);
    }

    @Override
    public void exitVariable(ICSSParser.VariableContext ctx) {
        super.exitVariable(ctx);
    }

    @Override
    public void exitSwitchCase(ICSSParser.SwitchCaseContext ctx) {

    }

    @Override
    public void exitCaseOption(ICSSParser.CaseOptionContext ctx) {
        super.exitCaseOption(ctx);
    }

    @Override
    public void exitDefaultOption(ICSSParser.DefaultOptionContext ctx) {

    }

    @Override
    public void exitBlock(ICSSParser.BlockContext ctx) {
        super.exitBlock(ctx);
    }

    @Override
    public void exitBlockContent(ICSSParser.BlockContentContext ctx) {
        super.exitBlockContent(ctx);
    }

    @Override
    public void exitRow(ICSSParser.RowContext ctx) {
        super.exitRow(ctx);
    }

    @Override
    public void exitStyleAttribute(ICSSParser.StyleAttributeContext ctx) {
        Stylerule stylerule;
        String name = ctx.getText();
//        if (ctx.BACKGROUND_COLOR_PROP()
//                != null) stylerule = new ClassSelector(name);
//        else if (ctx.ID() != null) stylerule = new IdSelector(name);
//        else stylerule = new TagSelector(name);
//
//        currentContainer.add(stylerule);
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
        super.exitValue(ctx);
    }

    @Override
    public void exitValue_calc(ICSSParser.Value_calcContext ctx) {
        super.exitValue_calc(ctx);
    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
        super.exitEveryRule(ctx);
    }

    public AST getAST() {
        return ast;
    }
}
