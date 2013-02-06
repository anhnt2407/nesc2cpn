package br.cin.ufpe.nesc2cpn.translator.node;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author avld
 */
public class IfElseNode extends ComposedNode {
    private List<TranslatorNode> elseNode;

    public IfElseNode()
    {
        elseNode = new LinkedList<TranslatorNode>();
    }
    
    public List<TranslatorNode> getElseNode() {
        return elseNode;
    }

    public void setElseNode(List<TranslatorNode> elseNode) {
        this.elseNode = elseNode;
    }
    
}
