package br.cin.ufpe.nesc2cpn.translator.cpn;

import br.cin.ufpe.nesc2cpn.cpnModule.Arc;
import br.cin.ufpe.nesc2cpn.cpnModule.CPNItem;
import br.cin.ufpe.nesc2cpn.cpnModule.Place;
import br.cin.ufpe.nesc2cpn.cpnModule.Trans;
import br.cin.ufpe.nesc2cpn.translator.node.CaseNode;
import br.cin.ufpe.nesc2cpn.translator.node.SwitchNode;
import br.cin.ufpe.nesc2cpn.translator.node.TranslatorNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author avld
 */
public class SwitchCPN extends ComposedCPN
{
    private Map<String,SimpleCPN> otherList;

    public SwitchCPN( SwitchNode node , Position pos )
    {
        setPos( pos );

        Place place = initPlace( pos.getX() , pos.getY() );
        place.setText( place.getText() + "-SWITCH" );

        Trans trans = initTrans( pos.getX() , pos.getY() - DISTANCE );
        trans.setText( trans.getText() + "-SWITCH" );
        trans.setCode( getCode(node) );

        initArc( place , trans );

        // --------------------

        initCaseList( trans , node.getCaseNode() );
    }

    // --------------------------------------------- //
    // --------------------------------------------- //
    // --------------------------------------------- //

    private CPNItem getCode(SwitchNode node)
    {
        StringBuilder builder = new StringBuilder();
        builder.append( "input (i);\n" );
        builder.append( "output (n);\n" );

        double total = 0;

        builder.append( "action\n" );
        builder.append( " let\n" );
        builder.append( "   val choice = uniform( 0.0 , 1.0 );\n" );
        builder.append( " in\n" );

        for( int i = 0; i < node.getCaseNode().size(); i++ )
        {
            CaseNode elseNode = (CaseNode) node.getCaseNode().get( i );
            total += elseNode.getProbability();

            builder.append( "   " + ( i > 0 ? "else " : "" ) );
            builder.append( "if( choice < "+ total +" ) then "+ i +"\n" );
        }

        builder.append( "   else 0\n" );
        builder.append( " end;" );

        CPNItem code = new CPNItem( builder.toString() );

        return code;
    }

    private void initCaseList( Trans trans , List<TranslatorNode> lista )
    {
        otherList = new HashMap<String, SimpleCPN>();

        setChildrenPos( new Position( getPos().getX() + DISTANCE * 3
                                    , getPos().getY() - DISTANCE * 2 ) );

        for( int i = 0; i < lista.size(); i++ )
        {

            CaseNode caseNode = (CaseNode) lista.get( i );

            CaseCPN caseCpn = new CaseCPN( caseNode , getChildrenPos() );
            otherList.put( i + "" , caseCpn );

            // -----

            Arc arc = caseCpn.initArc( caseCpn.getPlaceFirst() , trans );
            arc.setOrientation( Arc.TransitionToPlace );
            arc.getAnnot().setText("if n = "+ i +" \nthen 1`i \nelse empty");

            // -----

            getChildrenPos().setX( caseCpn.getStartX() + DISTANCE * 3 );
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

    @Override
    public Arc createLink( Place place )
    {
        if( otherList.isEmpty() )
        {
            super.createLink( place );
        }
        else
        {
            for( SimpleCPN item : otherList.values() )
            {
                Arc arcIfElse = item.initArc( place , item.getTransLast() );
                arcIfElse.setOrientation( Arc.TransitionToPlace );
            }
        }

        return null;
    }

    @Override
    public List<String> getVariableUsed()
    {
        List<String> list = new ArrayList<String>();
        list.addAll( super.getVariableUsed() );

        for( SimpleCPN item : otherList.values() )
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

        for( TranslatorCPN item : otherList.values() )
        {
            lista.addAll( item.getArcList() );
        }

        return lista;
    }

    @Override
    public List<Place> getPlaceList() {
        List<Place> lista = new ArrayList<Place>();
        lista.addAll( super.getPlaceList() );

        for( TranslatorCPN item : otherList.values() )
        {
            lista.addAll( item.getPlaceList() );
        }

        return lista;
    }

    @Override
    public List<Trans> getTransList() {
        List<Trans> lista = new ArrayList<Trans>();
        lista.addAll( super.getTransList() );

        for( TranslatorCPN item : otherList.values() )
        {
            lista.addAll( item.getTransList() );
        }

        return lista;
    }
}
