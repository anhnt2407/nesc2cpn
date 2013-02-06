package br.cin.ufpe.nesc2cpn.translator.cpn;

import br.cin.ufpe.nesc2cpn.Nesc2CpnMain;
import br.cin.ufpe.nesc2cpn.cpnModule.CPNItem;
import br.cin.ufpe.nesc2cpn.cpnModule.Place;
import br.cin.ufpe.nesc2cpn.cpnModule.Trans;

/**
 *
 * @author avld
 */
public class StartCPN extends SimpleCPN
{
    
    public StartCPN( Place inPlace, Place outPlace )
    {
        //Place place = initPlace( pos.getX() - DISTANCE * 2 , pos.getY() + DISTANCE * 2 );
        //place.setInitmark( new CPNItem("0") );
        
        init( inPlace , outPlace );
    }

    private void init( Place inPlace , Place outPlace )
    {
        //pos.getX() - DISTANCE | pos.getY() + DISTANCE
        Trans trans = initTrans( 0 , -200 );
        trans.setText( "start" );
        trans.setCond( new CPNItem("[i = ~2]") );
        trans.setCode( new CPNItem( getCodeSegments() ) );

        initArc( inPlace , trans );
        createLink( outPlace );
    }

    private String getCodeSegments()
    {
        String code = "";
        code += "input( i );\n";
        code += "output( n );\n";
        code += "action(\n";
        code += "  clearVariableAll();\n";
        code += "  file_open(\"simulation\");\n";

        // --------------------------------------- SCHEDULER
        code += "  schedulerList := [];\n";
        code += "  schedulerList := (ins (!schedulerList) 1);\n";

        String moldingType = System.getProperties().getProperty( "nesc2cpn.moldingType" );
        if( Nesc2CpnMain.MOLDING_TYPE_FUN.equals( moldingType ) )
        {
            code += "  schedulerList := (ins (!schedulerList) ~1);\n";
        }
        
        code += "  0\n";
        code += ");";

        return code;
    }
}
