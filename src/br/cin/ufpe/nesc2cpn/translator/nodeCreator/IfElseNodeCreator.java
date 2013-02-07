package br.cin.ufpe.nesc2cpn.translator.nodeCreator;

import br.cin.ufpe.nesc2cpn.nescModule.instructions.IfElse;
import br.cin.ufpe.nesc2cpn.nescModule.instructions.Instruction;
import br.cin.ufpe.nesc2cpn.translator.node.IfElseNode;
import br.cin.ufpe.nesc2cpn.translator.node.TranslatorNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author avld
 */
public class IfElseNodeCreator extends ComposedNodeCreator<IfElse>
{

    public IfElseNodeCreator( boolean reduction )
    {
        super( reduction );
    }

    @Override
    public boolean identify(Instruction inst)
    {
        return inst instanceof IfElse;
    }

    @Override
    public TranslatorNode convertTo(IfElse inst) throws Exception
    {
        inst.calculeProbability();
        
        IfElseNode node = getRepository( inst );
        node.setElseNode( convertElsesToNodeList( inst.getElses() ) );

        TranslatorNode conditionNode = NodeCreatorFactory.convertTo( inst.getCondition() );

        if( conditionNode != null )
        {
            getLastNode( conditionNode ).setNextNode( node );
            return conditionNode;
        }
        else
        {
            return node;
        }
    }

    private IfElseNode getRepository(IfElse inst) throws Exception
    {

        IfElseNode node = new IfElseNode();
        //node.setCondition( convertConditionToList( inst.getCondition() ) );
        node.setProbability( inst.getProbability() );
        node.setBlockNode( convertToNodeList( inst ) );

        return node;
    }

    private List<TranslatorNode> convertElsesToNodeList(Map<Long, IfElse> map) throws Exception
    {
        List<TranslatorNode> lista = new ArrayList<TranslatorNode>();

        for( IfElse ifElse : map.values() )
        {
            //TranslatorNode node = NodeCreatorFactory.convertTo( ifElse );
            IfElseNode node = getRepository( ifElse );
            lista.add( node );
        }

        return lista;
    }
}
