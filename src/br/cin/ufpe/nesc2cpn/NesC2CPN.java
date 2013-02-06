package br.cin.ufpe.nesc2cpn;

import br.cin.ufpe.nesc2cpn.nescModule.Module;
import br.cin.ufpe.nesc2cpn.nescModule.ProjectFile;
import br.cin.ufpe.nesc2cpn.nescModule.instructions.Function;
import br.cin.ufpe.nesc2cpn.repository.file.DatabaseUtil;
import br.cin.ufpe.nesc2cpn.repository.gui.EnergyListJFrame;
import br.cin.ufpe.nesc2cpn.translator.TranslatorControl;
import java.io.File;
import java.util.Properties;

/**
 *
 * @author avld
 */
public class NesC2CPN
{
    //public static final String MOLDING_TYPE_APP = "APPLICATION";
    //public static final String MOLDING_TYPE_FUN = "FUNCTION";
    
    private Nesc2CpnProperties properties;
    private ProjectFile projectFile;
    private String requestID;
    
    public NesC2CPN()
    {
        properties = new Nesc2CpnProperties();
    }
    
    public NesC2CPN(Nesc2CpnProperties properties)
    {
        this.properties = properties;
    }
    
    public Nesc2CpnResult evaluateSync( String project , Properties prop ) throws Exception
    {
        requestID = evaluateAsync( project , prop );
        
        Nesc2CpnResult result = null;
        do
        {
            result = getResult( requestID );
            Thread.sleep( 500 );
        }
        while( result != null );
        
        return result;
    }
    
    public String evaluateAsync( String projectDir , Properties prop ) throws Exception
    {
        File arquivo = new File( projectDir );

        System.out.println( "file ..: " + projectDir );
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
            control = getModelingFunction( properties.getFunctionName() );
        }
        
//        String path = System.getProperty( "nesc2cpn.output" );
//        String name = System.getProperty( "nesc2cpn.modelname" , "model.cpn" );

        String fullname = properties.getOutputDir() + File.separator + properties.getModelName();
        
        control.saveInFile( fullname );

        if( properties.isOnlyCreateModel() )
        {
            return null;
        }

//        CpnExecute execute = CpnExecuteFactory.getInstance( "console" );
//        Nesc2CpnResult result = execute.executar( fullname );
        

        return requestID;
    }
    
    private TranslatorControl getModelingFunction(String functionName) throws Exception
    {
        Module module = projectFile.getModuleList().get( 0 );
        Function function = null;

        for( Function m : module.getFunctions()  )
        {
            if( m.getFunctionName().equalsIgnoreCase( functionName ) )
            {
                function = m;
                break;
            }
        }

        if( function == null )
        {
            throw new Exception( "Please, select a method." );
        }

        TranslatorControl control = new TranslatorControl( true );
        control.createPage( function );
        control.createCPN();

        return control;
    }

    private TranslatorControl getModelingApplication() throws Exception
    {
        Module module = projectFile.getModuleList().get( 0 );

        TranslatorControl control = new TranslatorControl( true );
        control.createPage( module.getFunctions() );
        control.createCPN();

        return control;
    }
    
    public Nesc2CpnResult getResult( String requestID ) throws Exception
    {
        Nesc2CpnResult result = null;
        
//        if( isApplication )
//        {
//            //result.setModuleName( function.getModuleName() );
//            result.setModuleName( module.getName() );
//            result.setInterfaceName( "" );
//            result.setMethodName( "" );
//            result.setPetriNet( "" );
//        }
//        else
//        {
//            //result.setModuleName( function.getModuleName() );
//            result.setModuleName( module.getName() );
//            result.setInterfaceName( function.getInterfaceName() );
//            result.setMethodName( function.getFunctionName() );
//            result.setPetriNet( "" );
//        }
//
//        RepositoryControl.getInstance().add( result );
//        
//        if( properties.isKeep() )
//        {
//            execute.deleteAllFile( fullname );
//        }
        
        return result;
    }
 
    public void showRepositoryManager()
    {
        EnergyListJFrame.showFrame();
    }

    public Nesc2CpnProperties getProperties()
    {
        return properties;
    }

    public String getRequestID()
    {
        return requestID;
    }
    
}
