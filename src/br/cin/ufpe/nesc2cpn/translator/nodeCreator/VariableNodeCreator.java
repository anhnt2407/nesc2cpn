package br.cin.ufpe.nesc2cpn.translator.nodeCreator;

import br.cin.ufpe.nesc2cpn.nescModule.instructions.Instruction;
import br.cin.ufpe.nesc2cpn.nescModule.instructions.Variable;
import br.cin.ufpe.nesc2cpn.repository.Line;
import br.cin.ufpe.nesc2cpn.repository.RepositoryControl;
import br.cin.ufpe.nesc2cpn.translator.node.TranslatorNode;

/**
 *
 * @author avld
 */
public class VariableNodeCreator extends NodeCreator<Variable>
{

    public VariableNodeCreator()
    {
        // do nothing
    }

    @Override
    public boolean identify(Instruction inst)
    {
        return inst instanceof Variable;
    }

    @Override
    public TranslatorNode convertTo(Variable inst) throws Exception
    {
        TranslatorNode node = getRepository( inst );
        TranslatorNode valueNode = NodeCreatorFactory.convertTo( inst.getValue() );

        node.setNextNode( valueNode );

        //System.out.println( "variable ... " + inst.toString() );

        return node;
    }

    private TranslatorNode getRepository(Variable inst) throws Exception
    {
        String assignType = inst.getVariableType();
        Line line = RepositoryControl.getInstance().get( BASIC , ASSIGN , assignType );

        //System.out.println( "variable: " + assignType );

        TranslatorNode node = new TranslatorNode();
        node.addLine( line );

        return node;
    }

}
