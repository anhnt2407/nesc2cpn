package br.cin.ufpe.nesc2cpn.translator.node;

import br.cin.ufpe.nesc2cpn.repository.Line;
import java.util.List;

/**
 *
 * @author avld
 */
public class SwitchNode extends ComposedNode {
    private Line value;
    private List<TranslatorNode> caseNode;

    public SwitchNode()
    {
        
    }

    public List<TranslatorNode> getCaseNode() {
        return caseNode;
    }

    public void setCaseNode(List<TranslatorNode> casesNode) {
        this.caseNode = casesNode;
    }

    public Line getValue() {
        return value;
    }

    public void setValue(Line value) {
        this.value = value;
    }

}
