package br.cin.ufpe.nesc2cpn.translator.cpn;

import br.cin.ufpe.nesc2cpn.cpnModule.Arc;
import br.cin.ufpe.nesc2cpn.cpnModule.Place;
import br.cin.ufpe.nesc2cpn.cpnModule.Trans;
import br.cin.ufpe.nesc2cpn.translator.node.CaseNode;
import br.cin.ufpe.nesc2cpn.translator.node.TranslatorNode;

/**
 *
 * @author avld
 */
public class CaseCPN extends ComposedCPN
{

    public CaseCPN( TranslatorNode node, Position pos )
    {
        setPos( pos );
        setChildrenPos( pos );
        tratarInstructions( node );
    }

    public CaseCPN( CaseNode node, Position pos )
    {
        setPos( pos );
        setChildrenPos( pos );
        tratarInstructions( node.getBlockNode() );
    }
    
    // --------------------------------------------- //
    // --------------------------------------------- //
    // --------------------------------------------- //

    @Override
    public Place getPlaceFirst() {
        return getCpnItemList().get( 0 ).getPlaceFirst();
    }

    @Override
    public Trans getTransLast() {
        return getCpnItemList().get( getCpnItemList().size() - 1 ).getTransLast();
    }

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

        return cpnItem.createLink( place );
    }
}
