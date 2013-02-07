package br.cin.ufpe.nesc2cpn.translator.nodeCreator;

import br.cin.ufpe.nesc2cpn.nescModule.instructions.ComposedInstruction;
import br.cin.ufpe.nesc2cpn.nescModule.instructions.Instruction;
import br.cin.ufpe.nesc2cpn.translator.node.TranslatorNode;
import java.util.List;

/**
 *
 * @author avld
 */
public abstract class ComposedNodeCreator<type> extends NodeCreator<type>
{
    private boolean reduction;
    
    public ComposedNodeCreator( boolean reduction )
    {
        this.reduction = reduction;
    }

    protected TranslatorNode convertToNodeList(ComposedInstruction ci) throws Exception
    {
        List<Instruction> list = ci.getInstructions();

        if( list == null ? true : list.isEmpty() )
        {
            return new TranslatorNode();
        }
        else
        {
            return convertToNodeList( list );
        }
    }

    protected TranslatorNode convertToNodeList(List<Instruction> list) throws Exception
    {
        TranslatorNode root = new TranslatorNode();
        TranslatorNode next = root;

        for( Instruction inst : list )
        {
            TranslatorNode node = NodeCreatorFactory.convertTo( inst );

            if( node != null )
            {
                next.setNextNode( node );
                next = getLastNode( node );
            }
        }

        if( reduction )
        {
            next = NodeCreatorFactory.groupNode( root.getNextNode() );
            root.setNextNode( next );
        }

        return root;
    }

    protected TranslatorNode getLastNode(TranslatorNode node)
    {
        if( node != null )
        {
            if( node.getNextNode() != null )
            {
                TranslatorNode next = node.getNextNode();
                return getLastNode( next );
            }
        }

        return node;
    }
}
