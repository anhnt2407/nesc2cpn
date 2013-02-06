package br.cin.ufpe.nesc2cpn.translator.cpn;

import br.cin.ufpe.nesc2cpn.cpnModule.Arc;
import br.cin.ufpe.nesc2cpn.cpnModule.CPNItem;
import br.cin.ufpe.nesc2cpn.cpnModule.Place;
import br.cin.ufpe.nesc2cpn.cpnModule.Trans;
import br.cin.ufpe.nesc2cpn.translator.node.DoWhileNode;
import java.util.List;

/**
 *
 * @author avld
 */
public class DoWhileCPN extends ComposedCPN
{
    public DoWhileCPN( DoWhileNode node , Position pos )
    {
        setPos( pos );
        setChildrenPos( new Position( pos.getX() + DISTANCE , pos.getY() ) );

        tratarInstructions( node.getBlockNode() );

        // --------------------

        pos.setY( getEndY() - DISTANCE );

        Place place = initPlace( pos.getX() , pos.getY() );
        place.setText( place.getText() + "-DO" );

        Trans trans = initTrans( pos.getX() , pos.getY() - DISTANCE );
        trans.setText( trans.getText() + "-DO" );
        trans.setCode( getCode( node ) );

        initArc( place , trans );

        // --------------------

        List<SimpleCPN> lista = getCpnItemList();
        Place placeItem = lista.get( 0 ).getPlaceFirst();
        Arc arc = initArc( placeItem , trans );
        arc.setOrientation( Arc.TransitionToPlace );
        arc.getAnnot().setText( "if n = 1\n then 1`i\n else empty" );

        Trans transItem = lista.get( 0 ).getTransLast();
        lista.add( new LinkCPN( place , transItem ) );
    }

    // --------------------------------------------- //
    // --------------------------------------------- //
    // --------------------------------------------- //

    private CPNItem getCode(DoWhileNode node)
    {
        double total = node.getProbability();
        
        StringBuilder builder = new StringBuilder();
        builder.append( "input(i);\n" );
        builder.append( "output(n);\n" );
        builder.append( "action\n" );
        builder.append( " let\n" );
        builder.append( "   val choice = uniform( 0.0 , 1.0 );\n" );
        builder.append( " in\n" );
        builder.append( "   if( choice < "+ total +" ) then 1\n" );
        builder.append( "   else 0\n" );
        builder.append( " end;" );

        CPNItem code = new CPNItem( builder.toString() );

        return code;
    }

    @Override
    public Arc createLink( Place place )
    {
        Arc arc = super.createLink( place );
        arc.getAnnot().setText( "if n = 0\n then 1`i\n else empty" );

        return arc;
    }

    @Override
    public Place getPlaceFirst()
    {
        return getCpnItemList().get( 0 ).getPlaceFirst();
    }

}
