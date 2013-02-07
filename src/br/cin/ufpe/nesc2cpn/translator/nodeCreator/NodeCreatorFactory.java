package br.cin.ufpe.nesc2cpn.translator.nodeCreator;

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
    private List<NodeCreator> creatorList;
    public boolean reduction;
    
    private NodeCreatorFactory()
    {
        init();
        this.reduction = false;
    }
    
    private NodeCreatorFactory(boolean reduction)
    {
        init();
        this.reduction = reduction;
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

        creatorList.add( new ForNodeCreator( reduction ) );
        creatorList.add( new WhileNodeCreator( reduction ) );
        creatorList.add( new DoWhileNodeCreator( reduction ) );
        creatorList.add( new IfElseNodeCreator( reduction ) );
        creatorList.add( new SwitchNodeCreator( reduction ) );

        creatorList.add( new OperationNodeCreator( reduction ) );
        creatorList.add( new ValueNodeCreator() );

        creatorList.add( new CastNodeCreator() );
        creatorList.add( new ParenteseNodeCreator() );
        creatorList.add( new FunctionNodeCreator( reduction ) );
    }

    public TranslatorNode convert(Instruction inst) throws Exception
    {
        for( NodeCreator creator : creatorList )
        {
            if( creator.identify( inst ) )
            {
                TranslatorNode node = creator.convertTo( inst );

                if( reduction && !(creator instanceof ComposedNodeCreator) )
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
        NodeCreatorFactory factory = new NodeCreatorFactory();
        TranslatorNode node = factory.convert( inst );
        factory = null;
        
        return node;
    }

    public static TranslatorNode groupNode(TranslatorNode node) throws Exception
    {
        NodeCreatorFactory factory = new NodeCreatorFactory();
        TranslatorNode nodeGroup = factory.group( node );
        factory = null;
        
        return nodeGroup;
    }

}
