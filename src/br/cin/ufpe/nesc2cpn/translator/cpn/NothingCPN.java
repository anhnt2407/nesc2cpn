package br.cin.ufpe.nesc2cpn.translator.cpn;

import br.cin.ufpe.nesc2cpn.cpnModule.Place;
import br.cin.ufpe.nesc2cpn.cpnModule.Trans;

/**
 *
 * @author avld
 */
public class NothingCPN extends SimpleCPN
{
    public NothingCPN( Position pos )
    {
        Place place = initPlace( pos.getX() , pos.getY() );
        Trans trans = initTrans( pos.getX() , pos.getY() - DISTANCE );
        initArc( place , trans );
    }

}

