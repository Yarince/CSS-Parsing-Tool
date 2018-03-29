package nl.han.ica.icss.ast;

public class ScalarLiteral extends OperationalLiteral {
    public ScalarLiteral(int value) {
        this.value = value;
    }
    public ScalarLiteral(String text) {
        this.value = Integer.parseInt(text);
    }
    @Override
    public String getNodeLabel() {
        return "Scalar literal (" + value + ")";
    }
}
