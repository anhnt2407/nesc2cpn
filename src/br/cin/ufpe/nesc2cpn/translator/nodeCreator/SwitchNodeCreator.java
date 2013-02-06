package br.cin.ufpe.nesc2cpn.translator.nodeCreator;

import br.cin.ufpe.nesc2cpn.nescModule.instructions.Case;
import br.cin.ufpe.nesc2cpn.nescModule.instructions.Instruction;
import br.cin.ufpe.nesc2cpn.nescModule.instructions.Switch;
import br.cin.ufpe.nesc2cpn.repository.Line;
import br.cin.ufpe.nesc2cpn.translator.node.CaseNode;
import br.cin.ufpe.nesc2cpn.translator.node.SwitchNode;
import br.cin.ufpe.nesc2cpn.translator.node.TranslatorNode;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author avld
 */
public class SwitchNodeCreator extends ComposedNodeCreator<Switch>
{

    public SwitchNodeCreator()
    {
        
    }

    @Override
    public boolean identify(Instruction inst)
    {
        return inst instanceof Switch;
    }

    @Override
    public TranslatorNode convertTo(Switch inst) throws Exception
    {
        SwitchNode node = getRepository( inst );
        node.setCaseNode( convertCasesToNodeList( inst.getInstructions() ) );

        return node;
    }

    private SwitchNode getRepository(Switch inst) throws Exception
    {
        SwitchNode node = new SwitchNode();
        node.setValue( new Line() );

        return node;
    }

    private List<TranslatorNode> convertCasesToNodeList(List<Instruction> list) throws Exception
    {
        List<TranslatorNode> lista = new ArrayList<TranslatorNode>();

        for( Instruction inst : list )
        {
            Case caseInst = (Case) inst;

            CaseNode caseNode = new CaseNode();
            caseNode.setValue( caseInst.getValue() );
            caseNode.setProbability( caseInst.getProbability() );
            caseNode.setBlockNode( convertToNodeList( caseInst ) );

            lista.add( caseNode );
        }

        return lista;
    }
}
