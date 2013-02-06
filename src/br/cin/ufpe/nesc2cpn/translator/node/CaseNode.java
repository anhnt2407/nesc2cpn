package br.cin.ufpe.nesc2cpn.translator.node;

/**
 *
 * @author avld
 */
public class CaseNode extends ComposedNode {
    private String value;

    public CaseNode()
    {
        
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
