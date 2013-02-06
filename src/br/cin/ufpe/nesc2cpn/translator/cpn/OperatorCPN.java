package br.cin.ufpe.nesc2cpn.translator.cpn;

import br.cin.ufpe.nesc2cpn.cpnModule.Place;
import br.cin.ufpe.nesc2cpn.cpnModule.Trans;
import br.cin.ufpe.nesc2cpn.translator.node.TranslatorNode;

/**
 *
 * @author avld
 */
public class OperatorCPN extends SimpleCPN
{
    public OperatorCPN( TranslatorNode node , Position pos )
    {
        Place place = initPlace( pos.getX() , pos.getY() );
        Trans trans = initTrans( pos.getX() , pos.getY() - DISTANCE );
        trans.setCode( getCodeAddEnergy( node.getLine() ) );
        
        initArc( place , trans );
    }

}
