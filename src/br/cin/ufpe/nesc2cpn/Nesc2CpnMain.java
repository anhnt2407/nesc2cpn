package br.cin.ufpe.nesc2cpn;

import br.cin.ufpe.nesc2cpn.nescModule.Module;
import br.cin.ufpe.nesc2cpn.nescModule.ProjectFile;
import br.cin.ufpe.nesc2cpn.nescModule.instructions.Function;
import br.cin.ufpe.nesc2cpn.repository.RepositoryControl;
import br.cin.ufpe.nesc2cpn.repository.file.DatabaseUtil;
import br.cin.ufpe.nesc2cpn.param.ParameterFactory;
import br.cin.ufpe.nesc2cpn.translator.TranslatorControl;
import java.io.File;

/**
 *
 * @author avld
 */
public class Nesc2CpnMain
{
    public static String filename;
    public static String functionname;
    public static boolean showRepositoryManager;
    public static boolean KEEP;
    public static boolean ONLY_CREATE_MODEL;
    public static boolean isModelingApplication;

    public static final String MOLDING_TYPE_APP = "APPLICATION";
    public static final String MOLDING_TYPE_FUN = "FUNCTION";

    public static Function function;
    public static Module module;

    private static void execute( String filename ) throws Exception
    {
        File arquivo = new File( filename );

        System.out.println( "file ..: " + filename );
        System.out.println( "diretory ..: " + (arquivo.getParent() == null ? "." : arquivo.getParent()) );

        ProjectFile projectFile = new ProjectFile();
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
        if( isModelingApplication )
        {
            control = getModelingApplication( projectFile );
        }
        else
        {
            control = getModelingFunction( projectFile );
        }
        
        String path = System.getProperty( "nesc2cpn.output" );
        String name = System.getProperty( "nesc2cpn.modelname" , "model.cpn" );

        String fullname = path + File.separator + name;
        
        control.saveInFile( fullname );

        if( ONLY_CREATE_MODEL )
        {
            return ;
        }

        //CpnExecute execute = CpnExecuteFactory.getInstance( "console" );
        //Nesc2CpnResult result = execute.executar( fullname );

        // ------------------------------

        if( isModelingApplication )
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
        
        if( !KEEP )
        {
            //execute.deleteAllFile( fullname );
        }
    }

    private static TranslatorControl getModelingFunction(ProjectFile projectFile) throws Exception
    {
        module = projectFile.getModuleList().get( 0 );
        function = null;

        for( Function m : module.getFunctions()  )
        {
            if( m.getFunctionName().equalsIgnoreCase( functionname ) )
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

        TranslatorControl control = new TranslatorControl( true );
        control.createPage( function );
        control.createCPN();

        return control;
    }

    private static TranslatorControl getModelingApplication(ProjectFile projectFile) throws Exception
    {
        module = projectFile.getModuleList().get( 0 );

        TranslatorControl control = new TranslatorControl( true );
        control.createPage( module.getFunctions() );
        control.createCPN();

        return control;
    }

    // ---------------------------------- //

    public static void process(String args[]) throws Exception
    {
        //defaultConfiguration();
        //processParaments( args );
        ParameterFactory.getInstance().configuration( args );

        if( showRepositoryManager )
        {
            // do nothig
        }
        else if( filename != null )
        {
            execute( filename );
            //System.exit( 0 );
        }
        else
        {
            String help = ParameterFactory.getInstance().help();
            System.out.println( help ) ;
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
