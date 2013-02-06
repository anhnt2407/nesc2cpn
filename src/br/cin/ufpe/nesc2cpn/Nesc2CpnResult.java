package br.cin.ufpe.nesc2cpn;

import br.cin.ufpe.nesc2cpn.repository.Line;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author avld
 */
public class Nesc2CpnResult extends Line
{    
    public Nesc2CpnResult()
    {
        
    }

    public void load(String file) throws FileNotFoundException, IOException
    {
        Properties prop = new Properties();

        FileInputStream fis = new FileInputStream( file );
        prop.loadFromXML( fis );
        fis.close();

        String eMean = prop.getProperty( "energy.mean" , "0.0" );
        setEnergyMean( Double.parseDouble( eMean ) );

        String eVar = prop.getProperty( "energy.variance" , "0.0" );
        setEnergyVariance( Double.parseDouble( eVar ) );

        String pMean = prop.getProperty( "power.mean" , "0.0" );
        setPowerMean( Double.parseDouble( pMean ) );

        String pVar = prop.getProperty( "power.variance" , "0.0" );
        setPowerVariance( Double.parseDouble( pVar ) );

        String tMean = prop.getProperty( "time.mean" , "0.0" );
        setTimeMean( Double.parseDouble( tMean ) );

        String tVar = prop.getProperty( "time.variance" , "0.0" );
        setTimeVariance( Double.parseDouble( tVar ) );
    }

    public void save(String filename) throws FileNotFoundException, IOException
    {
        Properties prop = new Properties();

        prop.setProperty("energy.mean", getEnergyMean() + "" );
        prop.setProperty("energy.variance", getEnergyVariance() + "" );

        prop.setProperty("power.mean", getPowerMean() + "" );
        prop.setProperty("power.variance", getPowerVariance() + "" );

        prop.setProperty("time.mean", getTimeMean() + "" );
        prop.setProperty("time.variance", getTimeVariance() + "" );


        File file = new File( filename );
        
        if( !file.exists() )
        {
            file.createNewFile();
        }

        FileOutputStream fos = new FileOutputStream( file );
        prop.storeToXML( fos , "" );
        fos.close();
    }
}
