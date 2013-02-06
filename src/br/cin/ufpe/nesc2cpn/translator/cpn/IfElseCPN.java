package br.cin.ufpe.nesc2cpn.translator.cpn;

import br.cin.ufpe.nesc2cpn.cpnModule.Arc;
import br.cin.ufpe.nesc2cpn.cpnModule.CPNItem;
import br.cin.ufpe.nesc2cpn.cpnModule.Place;
import br.cin.ufpe.nesc2cpn.cpnModule.Trans;
import br.cin.ufpe.nesc2cpn.translator.node.IfElseNode;
import br.cin.ufpe.nesc2cpn.translator.node.TranslatorNode;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author avld
 */
public class IfElseCPN extends ComposedCPN
{
    private List<SimpleCPN> otherList;
    private List<String> elsesBlockEmpty;

    public IfElseCPN( IfElseNode node , Position pos )
    {
        elsesBlockEmpty = new LinkedList<String>();

        setPos( pos );
        otherList = new ArrayList<SimpleCPN>();

        Place place = initPlace( pos.getX() , pos.getY() );
        place.setText( place.getText() + "-IF" );

        Trans trans = initTrans( pos.getX() , pos.getY() - DISTANCE );
        trans.setText( trans.getText() + "-IF" );
        trans.setCode( getCode( node ) );
        
        initArc( place , trans );
        tratarInstructions( node.getBlockNode() );
        initIfElseList( trans , node.getElseNode() );

        List<SimpleCPN> lista = getCpnItemList();
        if( lista.size() >= 1 )
        {
            Place place2 = lista.get( 0 ).getPlaceFirst();

            Arc arc = initArc( place2 , trans );
            arc.setOrientation( Arc.TransitionToPlace );
            arc.getAnnot().setText( "if n = 0 \nthen 1`i \nelse empty" );
        }
        
        //lista.add( new LinkCPN( place2 , trans ) );
        //createLink( place2 );
    }

    // --------------------------------------------- //
    // --------------------------------------------- //
    // --------------------------------------------- //

    private CPNItem getCode(IfElseNode node)
    {
        StringBuilder builder = new StringBuilder();
        builder.append( "input(i);\n" );
        builder.append( "output(n);\n" );
        
        double total = node.getProbability();

        builder.append( "action\n" );
        builder.append( " let\n" );
        builder.append( "   val choice = uniform( 0.0 , 1.0 );\n" );
        builder.append( " in\n" );
        builder.append( "   if( choice < "+ total +" ) then 0\n" );
        
        for( int i = 0; i < node.getElseNode().size(); i++ )
        {
            IfElseNode elseNode = (IfElseNode) node.getElseNode().get( i );
            total += elseNode.getProbability();

            builder.append( "   else if( choice < "+ total +" )" );
            builder.append( " then "+ (i + 1) +"\n" );
        }

        if( otherList.isEmpty() )
        {
            builder.append( "   else 1\n" );
        }
        else
        {
            builder.append("   else ").append( otherList.size() ).append( "\n");
        }
        
        builder.append( " end;" );

        CPNItem code = new CPNItem( builder.toString() );
        
        return code;
    }

    private void initIfElseList( Trans trans , List<TranslatorNode> lista )
    {
        setChildrenPos( new Position( getPos().getX() + getMaxX() + DISTANCE
                                    , getPos().getY() - DISTANCE * 2 ) );

        for( int i = 0; i < lista.size(); i++ )
        {
            IfElseNode node = (IfElseNode) lista.get( i );
            CaseCPN caseCpn = new CaseCPN( node.getBlockNode() , getChildrenPos() );
            otherList.add( caseCpn );

            // -----

            String annot = "if n = "+ (i + 1) +" \nthen 1`i \nelse empty";

            if( !caseCpn.getPlaceList().isEmpty() )
            {
                Arc arc = caseCpn.initArc( caseCpn.getPlaceFirst() , trans );
                arc.setOrientation( Arc.TransitionToPlace );
                arc.getAnnot().setText( annot );
            }
            else
            {
                elsesBlockEmpty.add( annot );
            }

            // -----

            getChildrenPos().setX( caseCpn.getStartX() + DISTANCE );
            getChildrenPos().setY( caseCpn.getStartY() );

            // -----

            if( getMaxX() < caseCpn.getMaxX() )
            {
                setMaxX( caseCpn.getMaxX() );
            }

            if( getMaxY() < caseCpn.getMaxY() )
            {
                setMaxY( caseCpn.getMaxY() );
            }

            if( getMinY() > caseCpn.getMinY() )
            {
                setMinY( caseCpn.getMinY() );
            }
        }

        setStartX( getPos().getX() );
        setEndX( getPos().getX() );
        setMinX( getPos().getX() );

        setEndY( getMinY() - DISTANCE );
    }

    // --------------------------------------------- //
    // --------------------------------------------- //
    // --------------------------------------------- //

    @Override
    public Arc createLink( Place place )
    {
        SimpleCPN cpnItem = null;

        for( int i = getCpnItemList().size() - 1; i >= 0; i-- )
        {
            cpnItem = getCpnItemList().get( i );

            if( !( cpnItem instanceof LinkCPN ) )
                break;
        }

        Arc arc = null;

        if( cpnItem == null )
        {
            arc = null;
        }
        else
        {
            arc = cpnItem.createLink( place );
        }

        if( otherList.isEmpty() )
        {
            arc = super.createLink( place );
            arc.getAnnot().setText( "if n = 1 \nthen 1`i \nelse empty" );
        }
        else
        {
            for( SimpleCPN item : otherList )
            {
                if( !item.getTransList().isEmpty() )
                {
                    item.createLink( place );
                }

                //Arc arcIfElse = item.initArc( place , item.getTransLast() );
                //arcIfElse.setOrientation( Arc.TransitionToPlace );
            }

            for( String annot : elsesBlockEmpty )
            {
                arc = super.createLink( place );
                arc.getAnnot().setText( annot );
            }
        }

        return arc;
    }

    @Override
    public List<String> getVariableUsed()
    {
        List<String> list = new ArrayList<String>();
        list.addAll( super.getVariableUsed() );

        for( SimpleCPN item : otherList )
        {
            list.addAll( item.getVariableUsed() );
        }

        return list;
    }

    // --------------------------------------------- //
    // --------------------------------------------- //
    // --------------------------------------------- //

    @Override
    public List<Arc> getArcList() {
        List<Arc> lista = new ArrayList<Arc>();
        lista.addAll( super.getArcList() );

        for( TranslatorCPN item : otherList )
        {
            lista.addAll( item.getArcList() );
        }

        return lista;
    }

    @Override
    public List<Place> getPlaceList() {
        List<Place> lista = new ArrayList<Place>();
        lista.addAll( super.getPlaceList() );

        for( TranslatorCPN item : otherList )
        {
            lista.addAll( item.getPlaceList() );
        }

        return lista;
    }

    @Override
    public List<Trans> getTransList() {
        List<Trans> lista = new ArrayList<Trans>();
        lista.addAll( super.getTransList() );

        for( TranslatorCPN item : otherList )
        {
            lista.addAll( item.getTransList() );
        }
        
        return lista;
    }
}
