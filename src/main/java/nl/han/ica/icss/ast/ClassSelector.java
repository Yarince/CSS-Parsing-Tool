package nl.han.ica.icss.ast;

public class ClassSelector extends Selector {
    public String cls;

    public ClassSelector(String cls) {
        this.cls = cls;
    }

    @Override
    public String getNodeLabel() {
        return "ClassSelector " + cls;
    }
    public String toString() {
        return cls;
    }
}
