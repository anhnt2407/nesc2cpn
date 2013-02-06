package br.cin.ufpe.nesc2cpn;

import br.cin.ufpe.nesc2cpn.nescModule.Configuration;
import br.cin.ufpe.nesc2cpn.nescModule.ConfigurationFile;
import br.cin.ufpe.nesc2cpn.nescModule.Interface;
import br.cin.ufpe.nesc2cpn.nescModule.InterfaceFile;
import br.cin.ufpe.nesc2cpn.nescModule.Module;
import br.cin.ufpe.nesc2cpn.nescModule.ModuleFile;
import br.cin.ufpe.nesc2cpn.nescModule.NescIdentifyFile;
import br.cin.ufpe.nesc2cpn.nescModule.instructions.Function;
import br.cin.ufpe.nesc2cpn.repository.Line;
import br.cin.ufpe.nesc2cpn.repository.RepositoryControl;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author avld
 */
public class ModulosDisponiveis {

    public ModulosDisponiveis()
    {

    }


    private static Object convert(File arquivo) throws Exception
    {
        NescIdentifyFile nescUnkownFile = new NescIdentifyFile( arquivo );
        nescUnkownFile.open();

        //System.out.print( "abrindo o arquivo... " + arquivo.getName() );
        //System.out.println( "\t" + nescUnkownFile.getType() );

        if( NescIdentifyFile.CONFIGURATION_FILE.equalsIgnoreCase( nescUnkownFile.getType() ) )
        {
            ConfigurationFile confFile = new ConfigurationFile();
            confFile.convertTo( nescUnkownFile.getConteudo() );

            return confFile.getConfiguration();
        }
        else if( NescIdentifyFile.INTERFACE_FILE.equalsIgnoreCase( nescUnkownFile.getType() ) )
        {
            InterfaceFile interfFile = new InterfaceFile();
            interfFile.convertTo( nescUnkownFile.getConteudo() );

            return interfFile.getInterface();
        }
        else
        {
            ModuleFile moduleFile = new ModuleFile();
            moduleFile.convertTo( nescUnkownFile.getConteudo() );
            
            return moduleFile.getModule();
        }
    }

    private static Configuration getConfiguration(File arquivo) throws Exception
    {
        Object obj = convert( arquivo );

        if( obj instanceof Configuration )
        {
            return (Configuration) obj;
        }
        else
        {
            return null;
        }
    }

    private static Interface getInterface(File arquivo) throws Exception
    {
        Object obj = convert( arquivo );

        if( obj instanceof Interface )
            return (Interface) obj;
        else
            return null;
    }

    private static Module getModule(File arquivo) throws Exception
    {
        Object obj = convert( arquivo );

        if( obj instanceof Module )
            return (Module) obj;
        else
            return null;
    }

    // ---------------------------------------------------------------- //
    // ---------------------------------------------------------------- //
    // ---------------------------------------------------------------- //

    private final static String TOS_ROOT = "/opt/tinyos-2.x/";
    private final static String TOS_SYSTEM = TOS_ROOT + "tos/system/";
    private final static String TOS_INTERFACE = TOS_ROOT + "tos/interfaces/";
    private final static String TOS_TIMER = TOS_ROOT + "tos/lib/timer/";

    // ---------------------------------------------------------------- //
    // ---------------------------------------------------------------- //
    // ---------------------------------------------------------------- //
    
    public static void main(String args[]) throws Exception
    {
        geral();
    }

    public static void verificarFcfsResourceQueueC() throws Exception
    {
        String FcfsResourceQueueC = TOS_SYSTEM + "FcfsResourceQueueC.nc";
        getModule( new File( FcfsResourceQueueC ) );
    }

    public static void geral() throws Exception
    {
        List<Configuration> confList = new ArrayList<Configuration>();
        Map<String, Interface> intList = new HashMap<String, Interface>();

        File systemFile = new File( TOS_SYSTEM );

        System.out.println("\n\nConfigurations");
        for( File arquivo : systemFile.listFiles() )
        {
            try
            {
                Configuration conf = getConfiguration( arquivo );
                if( conf != null ) confList.add( conf );
            }
            catch(Exception err)
            {
                //System.err.println("[error] " + err.getMessage() );
                System.err.println("arquivo: " + arquivo.getName() );
                err.printStackTrace();
            }
        }

        System.out.println("\n\nInterfaces");

        for( Configuration conf : confList )
        {
            for( String intName : conf.getInterfaceProviders().values() )
            {
                if( intName.indexOf("<") >= 0 )
                {
                    intName = intName.substring( 0 , intName.indexOf("<") );
                }
                else if( intName.indexOf("[") >= 0 )
                {
                    intName = intName.substring( 0 , intName.indexOf("[") );
                }

                if( intList.containsKey( intName ) )
                {
                    continue ;
                }

                File intFile = new File( TOS_INTERFACE + intName + ".nc" );

                if( !intFile.exists() )
                {
                    intFile = new File( TOS_TIMER + intName + ".nc" );
                }

                //System.out.println( intFile.getAbsolutePath() );
                intList.put( intName , getInterface( intFile ) );
            }
        }

        System.out.println( "\n" );
        System.out.println( "Modulo N°: " + confList.size() );
        System.out.println( "Interface N°: " + intList.size() );

        int total = 0;

        for( Configuration conf : confList )
        {
            int subTotal = 0;
            System.out.println( conf.getName() );

            for( String intName : conf.getInterfaceProviders().values() )
            {
                if( intName.indexOf("<") >= 0 )
                {
                    intName = intName.substring( 0 , intName.indexOf("<") );
                }
                else if( intName.indexOf("[") >= 0 )
                {
                    intName = intName.substring( 0 , intName.indexOf("[") );
                }

                Interface interf = intList.get( intName );

                createEnergy( conf.getName(), intName , interf.getMethods() );

                System.out.print("method N°: " + interf.getMethods().size() );
                System.out.println( "\t\t" + interf.getName() );

                subTotal += interf.getMethods().size();
            }

            total += subTotal;
            System.out.println( "------------------- SUBTOTAL: " + subTotal );
        }

        System.out.println( "------------------- TOTAL: " + total );
    }

    public static void createEnergy(String moduleName, String interfaceName, List<Function> methodList) throws Exception
    {
        if( moduleName.indexOf("(") > 0 )
        {
            moduleName = moduleName.substring( 0 , moduleName.indexOf("(") );
        }

        for( Function method : methodList )
        {
            Line line = new Line();
            line.setModuleName( moduleName );
            line.setInterfaceName( interfaceName );
            line.setMethodName( method.getFunctionName() );

            line.setEnergyMean( getRandom() );
            line.setEnergyVariance( getRandom() );

            line.setPowerMean( getRandom() );
            line.setPowerVariance( getRandom() );

            line.setTimeMean( getRandom() );
            line.setTimeVariance( getRandom() );

            RepositoryControl.getInstance().add( line );
        }
    }

    public static double getRandom()
    {
        return (int)( 10000000 * Math.random() ) * 0.0000001;
    }
}
