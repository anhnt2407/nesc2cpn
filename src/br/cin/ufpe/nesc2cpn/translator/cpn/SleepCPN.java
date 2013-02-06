package br.cin.ufpe.nesc2cpn.translator.cpn;

import br.cin.ufpe.nesc2cpn.cpnModule.CPNItem;
import br.cin.ufpe.nesc2cpn.cpnModule.Place;
import br.cin.ufpe.nesc2cpn.cpnModule.Trans;

/**
 *
 * @author avld
 */
public class SleepCPN extends SimpleCPN
{
    public SleepCPN( Place inPlace , Place outPlace )
    {
        init( inPlace , outPlace );
    }

    private void init( Place inPlace , Place outPlace )
    {
        Trans trans = initTrans( DISTANCE * 6 , -200 );
        trans.setText( "sleep" );
        trans.setCond( new CPNItem("[ i = 0 ]") );

        initArc( inPlace , trans );
        createLink( outPlace );
    }

    private String getCodeSegments()
    {
        String code = "";
        return code;
    }
}
