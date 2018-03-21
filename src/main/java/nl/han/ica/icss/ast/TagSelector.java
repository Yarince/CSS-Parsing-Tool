package nl.han.ica.icss.ast;

public class TagSelector extends Selector {
    public String tag;

    public TagSelector(String tag) {
        this.tag = tag;
    }

    public String getNodeLabel() {
        return "TagSelector " + tag;
    }
    public String toString() {
        return tag;
    }
}
