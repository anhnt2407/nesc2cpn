package br.cin.ufpe.nesc2cpn.translator.nodeCreator;

import br.cin.ufpe.nesc2cpn.nescModule.instructions.Instruction;
import br.cin.ufpe.nesc2cpn.nescModule.instructions.Operation;
import br.cin.ufpe.nesc2cpn.nescModule.instructions.Value;
import br.cin.ufpe.nesc2cpn.nescModule.instructions.Variable;
import br.cin.ufpe.nesc2cpn.repository.Line;
import br.cin.ufpe.nesc2cpn.repository.RepositoryControl;
import br.cin.ufpe.nesc2cpn.translator.node.TranslatorNode;

/**
 *
 * @author avld
 */
public class OperationNodeCreator extends ComposedNodeCreator<Operation>
{

    public OperationNodeCreator()
    {
        // do nothing
    }

    @Override
    public boolean identify(Instruction inst)
    {
        return inst instanceof Operation;
    }

    @Override
    public TranslatorNode convertTo(Operation inst) throws Exception
    {
        TranslatorNode leftNode = NodeCreatorFactory.convertTo( inst.getLeft() );

        if( leftNode == null )
        {
            leftNode = NodeCreatorFactory.convertTo( inst.getRight() );
        }
        else
        {
            TranslatorNode rightNode = NodeCreatorFactory.convertTo( inst.getRight() );
            getLastNode( leftNode ).setNextNode( rightNode );
        }

        TranslatorNode opNode = getRepository( inst );
        opNode.setNextNode( leftNode );

        return opNode;
    }

    private TranslatorNode getRepository(Operation inst) throws Exception
    {
        //TODO: pegar o tipo!
        //Line line = RepositoryControl.getInstance().get( BASIC , OPERATION , inst.getOperation() );

        String type = getType( inst );
        String key  = inst.getOperation();

        if( inst.getLeft() instanceof Value
                && inst.getRight() instanceof Value )
        {
            if( ((Value) inst.getLeft()).isVariable()
                    && ((Value) inst.getRight()).isVariable() )
            {
                boolean resp = RepositoryControl.getInstance().exist( "$" + OPERATION , key , type );

                if( resp )
                {
                    key += " (variable)";
                }
            }
        }

        Line line = RepositoryControl.getInstance().get( "$" + OPERATION , key , type );

        TranslatorNode node = new TranslatorNode();
        node.addLine( line );

        return node;
    }

    private String getType(Instruction inst)
    {
        if( inst instanceof Operation )
        {
            String t1 = getType( ((Operation) inst).getLeft() );
            String t2 = getType( ((Operation) inst).getRight() );

            return chooseType( t1 , t2 );
        }
        else if(inst instanceof Variable)
        {
            return ((Variable) inst).getVariableType();
        }
        else if(inst instanceof Value)
        {
            return ((Value) inst).getValueType();
        }
        else
        {
            return "int8_t";
        }
    }

    private String chooseType( String t1 , String t2 )
    {
        int v1 = typeNumber( t1 );
        int v2 = typeNumber( t2 );

        return (v1 > v2) ? t1 : t2;
    }

    private int typeNumber(String type)
    {
        if( "INT8_T".equalsIgnoreCase( type ) )
        {
            return 0;
        }
        else if( "INT16_T".equalsIgnoreCase(type) )
        {
            return 0;
        }
        else if( "INT32_T".equalsIgnoreCase(type) )
        {
            return 2;
        }
        else if( "INT64_T".equalsIgnoreCase(type) )
        {
            return 3;
        }
        else if( "FLOAT".equalsIgnoreCase(type) )
        {
            return 4;
        }
        else if( "DOUBLE".equalsIgnoreCase(type) )
        {
            return 5;
        }
        else
        {
            return -1;
        }
    }
}
