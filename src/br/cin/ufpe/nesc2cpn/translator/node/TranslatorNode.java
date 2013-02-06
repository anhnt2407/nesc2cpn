package br.cin.ufpe.nesc2cpn.translator.node;

import br.cin.ufpe.nesc2cpn.repository.Line;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author avld
 */
public class TranslatorNode {
    private List<Line> lineList;
    private TranslatorNode nextNode;

    public TranslatorNode()
    {
        lineList = new LinkedList<Line>();
    }

    public List<Line> getLine()
    {
        return lineList;
    }

    public void addLine(Line line)
    {
        this.lineList.add( line );
    }

    public void addLine(List<Line> line)
    {
        this.lineList.addAll( line );
    }

    public TranslatorNode getNextNode() {
        return nextNode;
    }

    public void setNextNode(TranslatorNode nextNode) {
        this.nextNode = nextNode;
    }

    @Override
    public String toString()
    {
        return toString( "" );
    }

    public String toString(String init)
    {
        StringBuilder builder = new StringBuilder();

        for( int i = 0; i <  lineList.size(); i++)
        {
            Line l = lineList.get( i );

            builder.append( init );

            builder.append( l.getModuleName() ).append(".");
            builder.append( l.getInterfaceName() ).append(".");
            builder.append( l.getMethodName() );
            builder.append( "(energy mean: " ).append( l.getEnergyMean() ).append(")\n");
        }

        init += " ";

        if( getNextNode() != null )
        {
            builder.append( init ).append( "---- Next\n" );
            builder.append( getNextNode().toString( init ) );
        }

        return builder.toString();
    }
}
