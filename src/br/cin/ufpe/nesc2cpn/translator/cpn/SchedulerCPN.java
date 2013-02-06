package br.cin.ufpe.nesc2cpn.translator.cpn;

import br.cin.ufpe.nesc2cpn.cpnModule.Arc;
import br.cin.ufpe.nesc2cpn.cpnModule.CPNItem;
import br.cin.ufpe.nesc2cpn.cpnModule.Place;
import br.cin.ufpe.nesc2cpn.cpnModule.Trans;

/**
 *
 * @author avld
 */
public class SchedulerCPN extends SimpleCPN
{

    public SchedulerCPN( Place inPlace , Place outPlace )
    {
        init( inPlace , outPlace );
    }

    private void init( Place inPlace , Place outPlace )
    {
        Trans trans = new Trans( "scheduler" );
        trans.setCode( new CPNItem( getCodeSegments() ) );

        getTransList().add( trans );

        Arc arcOut = initArc( outPlace , trans );
        arcOut.getBendpointList().clear();
        arcOut.getBendpointList().add( new CPNItem( -100 , 0 ) );
        arcOut.getBendpointList().add( new CPNItem( -100 , -300 ) );

        Arc arcIn = createLink( inPlace );
        arcIn.setAnnot( new CPNItem("n") );
    }

    private String getCodeSegments()
    {
        String string = "";
        string += "input(i);\n";
        string += "output(n);\n";
        string += "action(\n";
        string += " if( List.null( !schedulerList ) )\n";
        string += "   then 0\n";
        string += " else (\n";
        string += "   iRef := List.nth(!schedulerList,0);\n";
        string += "   schedulerList := List.drop(!schedulerList,1);\n";
        string += "   !iRef\n";
        string += " ) );";

        return string;
    }
}