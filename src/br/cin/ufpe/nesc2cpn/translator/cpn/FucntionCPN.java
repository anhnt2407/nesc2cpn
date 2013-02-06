package br.cin.ufpe.nesc2cpn.translator.cpn;

import br.cin.ufpe.nesc2cpn.cpnModule.Place;
import br.cin.ufpe.nesc2cpn.cpnModule.Trans;

/**
 *
 * @author avld
 */
public class FucntionCPN extends SimpleCPN
{

    public FucntionCPN( Trans trans, int i, Place in, Place out )
    {
        init( trans , i , in , out );
    }

    private void init(Trans trans, int i, Place in, Place out)
    {
        trans.setPosattrX( DISTANCE * 3 * (3 + i) );
        trans.setPosattrY( -200 );

        getTransList().add( trans );

        initArc( in , trans );
        createLink( out );
    }
}
