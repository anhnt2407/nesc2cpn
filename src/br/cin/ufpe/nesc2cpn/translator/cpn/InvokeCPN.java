package br.cin.ufpe.nesc2cpn.translator.cpn;

import br.cin.ufpe.nesc2cpn.cpnModule.Place;
import br.cin.ufpe.nesc2cpn.cpnModule.Trans;

/**
 *
 * @author avld
 */
public class InvokeCPN extends SimpleCPN
{

    public InvokeCPN( Position pos )
    {
        Place place = initPlace( pos.getX() , pos.getY() );
        Trans trans = initTrans( pos.getX() + DISTANCE , pos.getY() );
        initArc( place , trans );
    }

}
