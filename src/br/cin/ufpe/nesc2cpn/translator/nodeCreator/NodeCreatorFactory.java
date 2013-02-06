package br.cin.ufpe.nesc2cpn.translator.nodeCreator;

import br.cin.ufpe.nesc2cpn.nescModule.creator.CreatorFactory;
import br.cin.ufpe.nesc2cpn.nescModule.instructions.Instruction;
import br.cin.ufpe.nesc2cpn.translator.node.ComposedNode;
import br.cin.ufpe.nesc2cpn.translator.node.TranslatorNode;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author avld
 */
public class NodeCreatorFactory
{
    private static NodeCreatorFactory instance;
    public static boolean COMPRESS = false;

    private List<NodeCreator> creatorList;

    private NodeCreatorFactory()
    {
        init();
    }

    private void init()
    {
        creatorList = new LinkedList<NodeCreator>();
        
        creatorList.add( new VariableNodeCreator() );
        creatorList.add( new AssignNodeCreator() );
        creatorList.add( new SpecialWordNodeCreator() );
        creatorList.add( new InvokeNodeCreator() );
        creatorList.add( new DecrementalNodeCreator() );
        creatorList.add( new IncrementalNodeCreator() );

        creatorList.add( new ForNodeCreator() );
        creatorList.add( new WhileNodeCreator() );
        creatorList.add( new DoWhileNodeCreator() );
        creatorList.add( new IfElseNodeCreator() );
        creatorList.add( new SwitchNodeCreator() );

        creatorList.add( new OperationNodeCreator() );
        creatorList.add( new ValueNodeCreator() );

        creatorList.add( new CastNodeCreator() );
        creatorList.add( new ParenteseNodeCreator() );
        creatorList.add( new FunctionNodeCreator() );
    }

    private static NodeCreatorFactory getInstance()
    {
        if( instance == null )
        {
            instance = new NodeCreatorFactory();
        }

        return instance;
    }

    public TranslatorNode convert(Instruction inst) throws Exception
    {
        for( NodeCreator creator : creatorList )
        {
            if( creator.identify( inst ) )
            {
                TranslatorNode node = creator.convertTo( inst );

                if( COMPRESS && !(creator instanceof ComposedNodeCreator) )
                {
                    return group( node );
                }
                
                return node;
            }
        }

        return null;
    }

    public TranslatorNode group(TranslatorNode receivedNode) throws Exception
    {
        if( receivedNode == null )
        {
            return receivedNode;
        }
        else if( receivedNode.getNextNode() == null )
        {
            return receivedNode;
        }

        // ---------------------------------------- //

        List<TranslatorNode> nodesList = new LinkedList<TranslatorNode>();
        nodesList.add( new TranslatorNode() );

        TranslatorNode node = nodesList.get( 0 );
        TranslatorNode nextNode = receivedNode;

        // ---------------------------------------- //

        while( nextNode != null )
        {
            if( nextNode instanceof ComposedNode )
            {
                nodesList.add( nextNode );

                nodesList.add( new TranslatorNode() );
                node = nodesList.get( nodesList.size() - 1 );
            }
            else if( nextNode.getLine().size() > 0 )
            {
                node.addLine( nextNode.getLine() );
            }
            
            nextNode = nextNode.getNextNode();
        }

        // ---------------------------------------- //

        TranslatorNode rootNode = null;
        node = rootNode;

        for( int i = 0; i < nodesList.size(); i++ )
        {
            TranslatorNode n = nodesList.get( i );

            if( n.getLine().isEmpty()
                    && !(n instanceof ComposedNode) )
            {
                continue ;
            }

            if( node != null )
            {
                node.setNextNode( n );
            }

            node = n;

            if( rootNode == null )
            {
                rootNode = n;
            }
        }

        // ---------------------------------------- //

        return rootNode;
    }

    public static TranslatorNode convertTo(Instruction inst) throws Exception
    {
        return NodeCreatorFactory.getInstance().convert( inst );
    }

    public static TranslatorNode groupNode(TranslatorNode node) throws Exception
    {
        return NodeCreatorFactory.getInstance().group( node );
    }


    public static void main(String args[]) throws Exception
    {
        COMPRESS = false;

        String instTxt = "while( 1 == 1 ){ int total = a + b + c + d + e / f; int media = 2; }";

        System.out.println( "------------------------- instrucao" );
        System.out.println( instTxt + "\n" );


        Instruction instObj = CreatorFactory.getInstance().convertTo( instTxt );

        System.out.println( "------------------------- Objeto" );
        System.out.println( instObj.toString() + "\n" );

        TranslatorNode instNode = convertTo( instObj );

        System.out.println( "------------------------- Node" );
        System.out.println( instNode.toString() );
    }

}
