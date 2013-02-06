package br.cin.ufpe.nesc2cpn.translator.nodeCreator;

import br.cin.ufpe.nesc2cpn.nescModule.instructions.Instruction;
import br.cin.ufpe.nesc2cpn.nescModule.instructions.Parentese;
import br.cin.ufpe.nesc2cpn.repository.Line;
import br.cin.ufpe.nesc2cpn.repository.RepositoryControl;
import br.cin.ufpe.nesc2cpn.translator.node.TranslatorNode;

/**
 *
 * @author avld
 */
public class ParenteseNodeCreator extends NodeCreator<Parentese>
{

    @Override
    public boolean identify(Instruction inst)
    {
        return inst instanceof Parentese;
    }

    @Override
    public TranslatorNode convertTo(Parentese inst) throws Exception
    {
        TranslatorNode node = getRepository( inst );
        TranslatorNode valueNode = NodeCreatorFactory.convertTo( inst.getInstruction() );

        node.setNextNode( valueNode );

        return node;
    }

    private TranslatorNode getRepository(Parentese inst) throws Exception
    {
        Line line = RepositoryControl.getInstance().get( BASIC , PARENTESE , "-" );

        //System.out.println( inst.getValue() + " | energy mean: " + line.getEnergyMean() );

        TranslatorNode node = new TranslatorNode();
        node.addLine( line );

        return node;
    }
}
