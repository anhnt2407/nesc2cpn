package br.cin.ufpe.nesc2cpn;

import br.cin.ufpe.nesc2cpn.nescModule.Module;
import br.cin.ufpe.nesc2cpn.nescModule.ProjectFile;
import br.cin.ufpe.nesc2cpn.nescModule.instructions.Function;
import br.cin.ufpe.nesc2cpn.repository.file.DatabaseUtil;
import br.cin.ufpe.nesc2cpn.param.ParameterFactory;
import br.cin.ufpe.nesc2cpn.repository.gui.EnergyListJFrame;
import br.cin.ufpe.nesc2cpn.translator.TranslatorControl;
import java.io.File;

/**
 *
 * @author avld
 */
public class Nesc2CpnMain
{
    private static Nesc2CpnProperties properties;
    private static ProjectFile projectFile;
    public static boolean showRepositoryManager;
    
    private static void execute( String filename ) throws Exception
    {
        File arquivo = new File( filename );

        System.out.println( "file ..: " + filename );
        System.out.println( "diretory ..: " + (arquivo.getParent() == null ? "." : arquivo.getParent()) );

        projectFile = new ProjectFile();
        projectFile.addDiretory( ( arquivo.getParent() == null ? "." : arquivo.getParent() ) );
        projectFile.processFile( arquivo.getName() );

        String diretory = "";
        for(String dir : projectFile.getDiretory() )
        {
            if( !diretory.isEmpty() ) diretory += ";";
            diretory += dir;
        }
        System.setProperty( DatabaseUtil.DB_EXTRA , diretory );

        // -------------------------------- //

        TranslatorControl control = null;
        if( properties.isCreateApplicationModel() )
        {
            control = getModelingApplication();
        }
        else
        {
            control = getModelingFunction();
        }
        
        String path = System.getProperty( "nesc2cpn.output" );
        String name = System.getProperty( "nesc2cpn.modelname" , "model.cpn" );

        String fullname = path + File.separator + name;
        
        control.saveInFile( fullname );

        if( properties.isOnlyCreateModel() )
        {
            return ;
        }

        //CpnExecute execute = CpnExecuteFactory.getInstance( "console" );
        //Nesc2CpnResult result = execute.executar( fullname );

        // ------------------------------

        if( properties.isCreateApplicationModel() )
        {
            // ---- NOT USED! result.setModuleName( function.getModuleName() );
            //result.setModuleName( module.getName() );
            //result.setInterfaceName( "" );
            //result.setMethodName( "" );
            //result.setPetriNet( "" );
        }
        else
        {
            // ---- NOT USED! result.setModuleName( function.getModuleName() );
            //result.setModuleName( module.getName() );
            //result.setInterfaceName( function.getInterfaceName() );
            //result.setMethodName( function.getFunctionName() );
            //result.setPetriNet( "" );
        }

        //RepositoryControl.getInstance().add( result );
        
        if( !properties.isKeep() )
        {
            //execute.deleteAllFile( fullname );
        }
    }

    private static TranslatorControl getModelingFunction() throws Exception
    {
        Module module = projectFile.getModuleList().get( 0 );
        Function function = null;

        for( Function m : module.getFunctions()  )
        {
            if( m.getFunctionName().equalsIgnoreCase( properties.getFunctionName() ) )
            {
                function = m;
                break;
            }
        }

        if( function == null )
        {
            throw new Exception("Please, select a method.");
        }

//        System.out.println("=======----------------------=======");
//        System.out.println( function.toString() );
//        System.out.println("=======----------------------=======\n");

        TranslatorControl control = new TranslatorControl( new Nesc2CpnProperties() );
        control.createPage( function );
        control.createCPN();

        return control;
    }

    private static TranslatorControl getModelingApplication() throws Exception
    {
        Module module = projectFile.getModuleList().get( 0 );

        TranslatorControl control = new TranslatorControl( new Nesc2CpnProperties() );
        control.createPage( module.getFunctions() );
        control.createCPN();

        return control;
    }

    // ---------------------------------- //

    public static void process(String args[]) throws Exception
    {
        //defaultConfiguration();
        //processParaments( args );
        properties = ParameterFactory.getInstance().configuration( args );

        if( showRepositoryManager )
        {
            EnergyListJFrame.showFrame();
        }
        else if( properties.getProjectDir() != null )
        {
            execute( properties.getProjectDir() );
            //System.exit( 0 );
        }
    }

    public static void main(String args[]) throws Exception
    {
        String dir = "/home/avld/pessoal/Doutorado/medicoes/Exemplo/exemplo04.1/";

        //"-file" , "/home/avld/pessoal/Doutorado/medicoes/Exemplo/exemplo04.1/ExampleAppC.nc"
        ///home/avld/pessoal/Doutorado/medicoes/Exemplo/exemplo08/ExampleAppC.nc
        args = new String[]{ "-file" , dir + "ExampleAppC.nc"
                           , "-function" , "modelagem"
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
