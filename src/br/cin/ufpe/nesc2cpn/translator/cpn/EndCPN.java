package br.cin.ufpe.nesc2cpn.translator.cpn;

import br.cin.ufpe.nesc2cpn.cpnModule.CPNItem;
import br.cin.ufpe.nesc2cpn.cpnModule.Place;
import br.cin.ufpe.nesc2cpn.cpnModule.Trans;

/**
 *
 * @author avld
 */
public class EndCPN extends SimpleCPN
{
    private boolean function;
    
    public EndCPN( Place inPLace , Place outPlace , boolean function )
    {
        this.function = function;
        init( inPLace , outPlace );
    }

    private void init(Place inPLace , Place outPlace)
    {
        Trans trans = initTrans( DISTANCE * 3 , -200 );
        trans.setText( "end" );
        trans.setCond( new CPNItem("[i = ~1]") );
        trans.setCode( new CPNItem( getCodeSegments() ) );
        //trans.setTime( new CPNItem("@+1") );

        initArc( inPLace , trans );
        createLink( outPlace );
    }

    private String getCodeSegments()
    {
        String code = "";
        code += "input( i );\n";
        code += "output( n );\n";
        code += "action(\n";
        code += "  addRound();\n";
        
        // --------------------------------------- SCHEDULER
        code += "  schedulerList := [];\n";
        code += "  schedulerList := (ins (!schedulerList) 1);\n";

        //String moldingType = System.getProperties().getProperty( "nesc2cpn.moldingType" );
        if( function ) //Nesc2CpnMain.MOLDING_TYPE_FUN.equals( moldingType )
        {
            code += "  schedulerList := (ins (!schedulerList) ~1);\n";
        }

        code += "  0\n";
        code += ");";

        return code;
    }
}
