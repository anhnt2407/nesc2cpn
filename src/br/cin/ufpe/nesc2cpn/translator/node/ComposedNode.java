package br.cin.ufpe.nesc2cpn.translator.node;

/**
 *
 * @author avld
 */
public class ComposedNode extends TranslatorNode
{
    private TranslatorNode blockNode;
    private double probability;

    public ComposedNode()
    {
        
    }

    public TranslatorNode getBlockNode() {
        return blockNode;
    }

    public void setBlockNode(TranslatorNode blockNode) {
        this.blockNode = blockNode;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    @Override
    public String toString()
    {
        return toString( "" );
    }

    @Override
    public String toString(String init)
    {
        StringBuilder builder = new StringBuilder();
        builder.append( init ).append( "Composed Node\n" );

        if( getBlockNode() != null )
        {
            builder.append( init ).append( "---- Block Node\n" );
            builder.append( getBlockNode().toString( init ) );
        }

        if( getNextNode() != null )
        {
            builder.append( init ).append( "---- Next\n" );
            builder.append( getNextNode().toString( init ) );
        }

        return builder.toString();
    }
}
