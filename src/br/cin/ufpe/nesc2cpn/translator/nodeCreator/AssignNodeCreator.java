package br.cin.ufpe.nesc2cpn.translator.nodeCreator;

import br.cin.ufpe.nesc2cpn.nescModule.instructions.Assign;
import br.cin.ufpe.nesc2cpn.nescModule.instructions.Instruction;
import br.cin.ufpe.nesc2cpn.repository.Line;
import br.cin.ufpe.nesc2cpn.repository.RepositoryControl;
import br.cin.ufpe.nesc2cpn.translator.node.TranslatorNode;

/**
 *
 * @author avld
 */
public class AssignNodeCreator extends NodeCreator<Assign>
{

    public AssignNodeCreator()
    {
        // do nothing
    }

    @Override
    public boolean identify(Instruction inst)
    {
        return inst instanceof Assign;
    }

    @Override
    public TranslatorNode convertTo(Assign inst) throws Exception
    {
        TranslatorNode node = getRepository( inst );
        TranslatorNode valueNode = NodeCreatorFactory.convertTo( inst.getValue() );
        
        node.setNextNode( valueNode );
        
        return node;
    }

    private TranslatorNode getRepository(Assign inst) throws Exception
    {
        String assignType = inst.getVariableType();

        if( inst.isArray() )
        {
            assignType += "[]";
        }
        else if( inst.isPointer() )
        {
            assignType += "*";
        }

        //System.out.println( "assign type: " + assignType + " " + inst.getVariableName() );
        Line line = RepositoryControl.getInstance().get( BASIC , ASSIGN , assignType );

        //System.out.println( "energy mean: " + line.getEnergyMean() );

        TranslatorNode node = new TranslatorNode();
        node.addLine( line );

        return node;
    }


    public static void main(String args[]) throws Exception
    {
        String assignType = "int16_t";
        Line line = RepositoryControl.getInstance().get( BASIC , ASSIGN , assignType );

        //System.out.println( "energy mean: " + line.getEnergyMean() );
    }

}
