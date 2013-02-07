package br.cin.ufpe.nesc2cpn;

import br.cin.ufpe.evaluateservice.EvaluateService;
import br.cin.ufpe.evaluateservice.EvaluateServiceService;
import br.cin.ufpe.nesc2cpn.nescModule.Module;
import br.cin.ufpe.nesc2cpn.nescModule.ProjectFile;
import br.cin.ufpe.nesc2cpn.nescModule.instructions.Function;
import br.cin.ufpe.nesc2cpn.repository.RepositoryControl;
import br.cin.ufpe.nesc2cpn.repository.file.DatabaseUtil;
import br.cin.ufpe.nesc2cpn.repository.gui.EnergyListJFrame;
import br.cin.ufpe.nesc2cpn.translator.TranslatorControl;
import java.io.File;
import java.io.StringReader;
import java.util.Properties;

/**
 *
 * @author avld
 */
public class NesC2CPN
{
    private Nesc2CpnProperties properties;
    private ProjectFile projectFile;
    private String requestID;
    
    private EvaluateService port;
    private Module module;
    private Function function;
    
    public NesC2CPN()
    {
        properties = new Nesc2CpnProperties();
        
        EvaluateServiceService ess = new EvaluateServiceService();
        port = ess.getEvaluateServicePort();
    }
    
    public NesC2CPN( Nesc2CpnProperties properties )
    {
        this.properties = properties;
        
        EvaluateServiceService ess = null; 
        
        if( properties == null 
                ? false 
                : properties.getEvaluateServiceUrl() != null )
        {
            ess = new EvaluateServiceService( properties.getEvaluateServiceUrl() );
        }
        else
        {
            ess = new EvaluateServiceService();
        }
        
        port = ess.getEvaluateServicePort();
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
        
        String email = prop.getProperty( "email" , "" );
        requestID = port.evaluateApplication( fullname , email );
                
        return requestID;
    }
    
    private TranslatorControl getModelingFunction(String functionName) throws Exception
    {
        module = projectFile.getModuleList().get( 0 );
        function = null;

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

        TranslatorControl control = new TranslatorControl( properties );
        control.createPage( function );
        control.createCPN();

        return control;
    }

    private TranslatorControl getModelingApplication() throws Exception
    {
        module = projectFile.getModuleList().get( 0 );

        TranslatorControl control = new TranslatorControl( properties );
        control.createPage( module.getFunctions() );
        control.createCPN();

        return control;
    }
    
    public Nesc2CpnResult getResult( String requestID ) throws Exception
    {
        String resultStr = port.getResult( requestID );
        
        if( resultStr == null 
                ? true 
                : resultStr.isEmpty() )
        {
            throw new Exception( "there is still no result." );
        }
        else if( resultStr.contains( "erro" ) 
                || resultStr.contains( "ERRO" ) )
        {
            throw new Exception( resultStr );
        }
        
        Properties prop = new Properties();
        prop.load( new StringReader( resultStr ) );
        
        Nesc2CpnResult result = new Nesc2CpnResult();
        result.setEnergyMean( Double.parseDouble( prop.getProperty( "energy.mean" , "0.0" ) ) );
        result.setEnergyError(Double.parseDouble( prop.getProperty( "energy.error" , "0.0" ) ) );
        result.setEnergyVariance(Double.parseDouble( prop.getProperty( "energy.variance" , "0.0" ) ) );
        
        result.setPowerMean( Double.parseDouble( prop.getProperty( "power.mean" , "0.0" ) ) );
        result.setPowerError(Double.parseDouble( prop.getProperty( "power.error" , "0.0" ) ) );
        result.setPowerVariance(Double.parseDouble( prop.getProperty( "power.variance" , "0.0" ) ) );
        
        result.setTimeMean( Double.parseDouble( prop.getProperty( "time.mean" , "0.0" ) ) );
        result.setTimeError(Double.parseDouble( prop.getProperty( "time.error" , "0.0" ) ) );
        result.setTimeVariance(Double.parseDouble( prop.getProperty( "time.variance" , "0.0" ) ) );
        
        if( properties.isCreateApplicationModel() )
        {
            //result.setModuleName( function.getModuleName() );
            result.setModuleName( module.getName() );
            result.setInterfaceName( "" );
            result.setMethodName( "" );
            result.setPetriNet( "" );
        }
        else
        {
            //result.setModuleName( function.getModuleName() );
            result.setModuleName( module.getName() );
            result.setInterfaceName( function.getInterfaceName() );
            result.setMethodName( function.getFunctionName() );
            result.setPetriNet( "" );
        }

        RepositoryControl.getInstance().add( result );
        
        if( properties.isKeep() )
        {
            //execute.deleteAllFile( fullname );
        }
        
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
