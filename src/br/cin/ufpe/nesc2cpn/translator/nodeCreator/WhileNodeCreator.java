package br.cin.ufpe.nesc2cpn.translator.nodeCreator;

import br.cin.ufpe.nesc2cpn.nescModule.instructions.Instruction;
import br.cin.ufpe.nesc2cpn.nescModule.instructions.While;
import br.cin.ufpe.nesc2cpn.translator.node.TranslatorNode;
import br.cin.ufpe.nesc2cpn.translator.node.WhileNode;

/**
 *
 * @author avld
 */
public class WhileNodeCreator extends ComposedNodeCreator<While>
{

    public WhileNodeCreator()
    {
        
    }

    @Override
    public boolean identify(Instruction inst)
    {
        return inst instanceof While;
    }

    @Override
    public TranslatorNode convertTo(While inst) throws Exception
    {
        WhileNode node = getRepository( inst );
        node.setBlockNode( convertToNodeList( (While) inst ) );

        return node;
    }

    private WhileNode getRepository(While inst) throws Exception
    {
        WhileNode node = new WhileNode();
        //node.setCondition( convertConditionToList( inst.getCondition() ) );
        node.setProbability( inst.getProbability() );

        return node;
    }
}
