package br.cin.ufpe.nesc2cpn.translator.nodeCreator;

import br.cin.ufpe.nesc2cpn.nescModule.instructions.Cast;
import br.cin.ufpe.nesc2cpn.nescModule.instructions.Instruction;
import br.cin.ufpe.nesc2cpn.repository.Line;
import br.cin.ufpe.nesc2cpn.repository.RepositoryControl;
import br.cin.ufpe.nesc2cpn.translator.node.TranslatorNode;

/**
 *
 * @author avld
 */
public class CastNodeCreator extends NodeCreator<Cast>
{

    @Override
    public boolean identify(Instruction inst)
    {
        return inst instanceof Cast;
    }

    @Override
    public TranslatorNode convertTo(Cast inst) throws Exception
    {
        TranslatorNode node = getRepository( inst );
        TranslatorNode valueNode = NodeCreatorFactory.convertTo( inst.getInst() );

        node.setNextNode( valueNode );

        return node;
    }

    private TranslatorNode getRepository(Cast inst) throws Exception
    {
        Line line = RepositoryControl.getInstance().get( BASIC , CAST , "-" );

        //System.out.println( inst.getValue() + " | energy mean: " + line.getEnergyMean() );

        TranslatorNode node = new TranslatorNode();
        node.addLine( line );

        return node;
    }
}
