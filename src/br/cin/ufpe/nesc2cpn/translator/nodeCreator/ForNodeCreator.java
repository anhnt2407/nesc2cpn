package br.cin.ufpe.nesc2cpn.translator.nodeCreator;

import br.cin.ufpe.nesc2cpn.nescModule.instructions.For;
import br.cin.ufpe.nesc2cpn.nescModule.instructions.Instruction;
import br.cin.ufpe.nesc2cpn.repository.Line;
import br.cin.ufpe.nesc2cpn.translator.node.ForNode;
import br.cin.ufpe.nesc2cpn.translator.node.TranslatorNode;

/**
 *
 * @author avld
 */
public class ForNodeCreator extends ComposedNodeCreator<For>
{

    public ForNodeCreator()
    {
        
    }

    @Override
    public boolean identify(Instruction inst)
    {
        return inst instanceof For;
    }

    @Override
    public TranslatorNode convertTo(For inst) throws Exception
    {
        ForNode node = (ForNode) getRepository( (For) inst );
        node.setBlockNode( convertToNodeList( (For) inst ) );

        return node;
    }

    private TranslatorNode getRepository(For inst) throws Exception
    {
        Line assignLine = getPartRepository( inst.getPart01() );

        if( assignLine == null )
        {
            assignLine = new Line();
            assignLine.setModuleName( "*basic*" );
            assignLine.setMethodName( "assign" );
        }

        Line conditionLine = getPartRepository( inst.getPart02() );
        if( conditionLine == null )
        {
            conditionLine = new Line();
            conditionLine.setModuleName( "*basic*" );
            conditionLine.setMethodName( "condition" );
        }

        Line plusLine = getPartRepository( inst.getPart03() );
        if( plusLine == null )
        {
            plusLine = new Line();
            plusLine.setModuleName( "*basic*" );
            plusLine.setMethodName( "assign-plus" );
        }

        // ------------------------------------ //

        ForNode node = new ForNode();
        node.setAssign( assignLine );
        node.setCondition( conditionLine );
        node.setPlus( plusLine );
        node.setInterationNumber( inst.getInterationNumber() );

        return node;
    }

    private Line getPartRepository(Instruction inst) throws Exception
    {
        TranslatorNode node = NodeCreatorFactory.convertTo( inst );

        if( node == null )
        {
            return null;
        }
        else
        {
            return node.getLine().get( 0 );
        }
    }
}
