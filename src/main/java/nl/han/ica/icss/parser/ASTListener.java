package nl.han.ica.icss.parser;

import java.util.HashMap;
import java.util.Stack;

import nl.han.ica.icss.ast.*;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * This class extracts the ICSS Abstract Syntax Tree from the Antlr Parse tree.
 */
public class ASTListener extends ICSSBaseListener {
	
	//Accumulator attributes:
	private AST ast;
	private Stack<ASTNode> currentContainer; //This is a hint...
	private HashMap<String,String> properties;

	public ASTListener() {
		ast = new AST();
		currentContainer = new Stack<>();

		ASTNode astNode = new ASTNode();
//		astNode.;
		currentContainer.add(astNode);
	}

	@Override
	public void exitStylesheet(ICSSParser.StylesheetContext ctx) {
		super.exitStylesheet(ctx);
		properties.put(cxt.getChild(0).getText(), cxt.getChild(2).getText());

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
	public void exitSwitchcase(ICSSParser.SwitchcaseContext ctx) {
		super.exitSwitchcase(ctx);
	}

	@Override
	public void exitCaseOption(ICSSParser.CaseOptionContext ctx) {
		super.exitCaseOption(ctx);
	}

	@Override
	public void exitDefaultOptioin(ICSSParser.DefaultOptioinContext ctx) {
		super.exitDefaultOptioin(ctx);
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
		super.exitStyleAttribute(ctx);
	}

	@Override
	public void exitSelectoren(ICSSParser.SelectorenContext ctx) {
		super.exitSelectoren(ctx);
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
