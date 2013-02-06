package br.cin.ufpe.nesc2cpn.translator.blocks;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author avld
 */
public class TStudent
{
    //75% 80% 85% 90% 95% 97.5% 99% 99.5% 99.75% 99.9% 99.95%
    private Map<Double,List<Double>> valuesMap;

    public TStudent() throws Exception
    {
        init( true );
    }

    public TStudent(boolean process) throws Exception
    {
        init( process );
    }

    private void init(boolean process) throws Exception
    {
        if( process )
        {
            treatDataInFile();
        }
    }

    private List<String> getDataInFile() throws Exception
    {
        URL filename = TStudent.class.getResource( "t_student.txt" );

        FileReader fileReader = new FileReader( filename.getPath() );
        BufferedReader buffer = new BufferedReader( fileReader );

        String line = null;
        List<String> lineList = new LinkedList<String>();
        
        while( (line = buffer.readLine()) != null )
        {
            lineList.add( line );
        }

        buffer.close();
        fileReader.close();

        return lineList;
    }

    private void treatDataInFile() throws Exception
    {
        List<String> lineList = getDataInFile();

        List<Double> keyList = new LinkedList<Double>();
        valuesMap = new HashMap<Double, List<Double>>();
        for( String keyStr : lineList.get( 0 ).split( "\t" ) )
        {
            if( keyStr.indexOf( "%" ) == -1 )
            {
                continue ;
            }

            keyStr = keyStr.substring( 0 , keyStr.length() - 1 );
            double key = Double.parseDouble( keyStr );

            keyList.add( key );
            valuesMap.put( key , new LinkedList<Double>() );
        }

        for( int i = 1; i < lineList.size(); i++ )
        {
            String[] part = lineList.get( i ).split( "\t" );

            for( int j = 1; j < part.length; j++ )
            {
                double key = keyList.get( j - 1 );
                valuesMap.get( key ).add( Double.parseDouble( part[j] ) );
            }
        }

        keyList.clear();
        keyList = null;

        lineList.clear();
        lineList = null;
    }

    public List<Double> getValueList(double prob)
    {
        return valuesMap.get( prob );
    }

    public boolean containsKey(double prob)
    {
        return valuesMap.containsKey( prob );
    }

    public List<Double> getKeyList()
    {
        List<Double> keyList = new LinkedList<Double>( valuesMap.keySet() );
        Collections.sort( keyList );

        return keyList;
    }
}
