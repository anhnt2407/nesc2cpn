package br.cin.ufpe.nesc2cpn.translator.nodeCreator;

import br.cin.ufpe.nesc2cpn.nescModule.instructions.Instruction;
import br.cin.ufpe.nesc2cpn.nescModule.instructions.Value;
import br.cin.ufpe.nesc2cpn.repository.Line;
import br.cin.ufpe.nesc2cpn.repository.RepositoryControl;
import br.cin.ufpe.nesc2cpn.translator.node.TranslatorNode;

/**
 *
 * @author avld
 */
public class ValueNodeCreator extends NodeCreator<Value>
{

    public ValueNodeCreator()
    {
        
    }

    @Override
    public boolean identify(Instruction inst)
    {
        return inst instanceof Value;
    }

    @Override
    public TranslatorNode convertTo(Value inst) throws Exception
    {
        if( inst.isPointer() )
        {
            return getRepository( inst );
        }

        return null;
    }

    private TranslatorNode getRepository(Value inst) throws Exception
    {
        Line line = RepositoryControl.getInstance().get( BASIC , POINTER , "float" );

        //System.out.println( inst.getValue() + " | energy mean: " + line.getEnergyMean() );

        TranslatorNode node = new TranslatorNode();
        node.addLine( line );

        return node;
    }
}
