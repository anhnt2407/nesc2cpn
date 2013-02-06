package br.cin.ufpe.nesc2cpn.translator.cpn;

import br.cin.ufpe.nesc2cpn.cpnModule.Arc;
import br.cin.ufpe.nesc2cpn.cpnModule.CPNItem;
import br.cin.ufpe.nesc2cpn.cpnModule.Place;
import br.cin.ufpe.nesc2cpn.cpnModule.Trans;
import br.cin.ufpe.nesc2cpn.translator.node.WhileNode;
import java.util.List;

/**
 *
 * @author avld
 */
public class WhileCPN extends ComposedCPN
{

    public WhileCPN( WhileNode node , Position pos )
    {
        setPos( pos );

        Place place01 = initPlace( pos.getX() , pos.getY() );
        place01.setText( place01.getText() + "-WHILE" );

        Trans trans01 = initTrans( pos.getX() , pos.getY() - DISTANCE );
        trans01.setText( trans01.getText() + "-WHILE" );
        trans01.setCode( getCode(node) );

        initArc( place01 , trans01 );

        // --------------------

        tratarInstructions( node.getBlockNode() );

        // --------------------

        List<SimpleCPN> lista = getCpnItemList();
        Place place = lista.get( 0 ).getPlaceFirst();
        Arc arc = initArc( place , trans01 );
        arc.setOrientation( Arc.TransitionToPlace );
        arc.getAnnot().setText( "if n = 1\n then 1`i\n else empty" );
        
        Trans trans = lista.get( 0 ).getTransLast();
        lista.add( new LinkCPN( place01 , trans ) );
    }

    // --------------------------------------------- //
    // --------------------------------------------- //
    // --------------------------------------------- //

    private CPNItem getCode(WhileNode node)
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

}
