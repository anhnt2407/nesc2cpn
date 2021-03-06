package br.cin.ufpe.nesc2cpn;

import br.cin.ufpe.nesc2cpn.param.ParameterFactory;
import br.cin.ufpe.nesc2cpn.repository.gui.EnergyListJFrame;
import java.net.URL;
import java.util.Properties;

/**
 *
 * @author avld
 */
public class Nesc2CpnMain
{
    public static boolean showRepositoryManager;
    
    public static void process(String args[]) throws Exception
    {
        //defaultConfiguration();
        //processParaments( args );
        Nesc2CpnProperties properties = ParameterFactory.getInstance().configuration( args );
        //properties.setEvaluateServiceUrl( new URL( "http://localhost:8080/EvaluationService/ws?wsdl" ) );

        if( showRepositoryManager )
        {
            EnergyListJFrame.showFrame();
        }
        else if( properties.getProjectDir() != null )
        {
            NesC2CPN nesc2cpn = new NesC2CPN( properties );
            nesc2cpn.evaluateSync( properties.getProjectDir() , new Properties() );
        }
    }

    public static void main(String args[]) throws Exception
    {
        //String dir = "/home/avld/pessoal/Doutorado/medicoes/Exemplo/exemplo04.1/ExampleAppC.nc";
        String dir = "/opt/tinyos-2.1.1/apps/RadioCountToLeds/RadioCountToLedsAppC.nc";

        //"-file" , "/home/avld/pessoal/Doutorado/medicoes/Exemplo/exemplo04.1/ExampleAppC.nc"
        ///home/avld/pessoal/Doutorado/medicoes/Exemplo/exemplo08/ExampleAppC.nc
        args = new String[]{ "-file" , dir
                           , "-function" , "stopDone"
                           //, "-function" , "modelagem"
                           //, "-function" , "fired"
                           //, "-function" , "readDone"
//                           , "-compress"
                           , "-keep"
                           //, "-output" , "/home/avld/Documentos/teste/"
                           //, "-output" , dir
                           //, "-onlycreator"
                           };

        //args = new String[]{ "-repositorymanager" };

        process( args );
    }
}
