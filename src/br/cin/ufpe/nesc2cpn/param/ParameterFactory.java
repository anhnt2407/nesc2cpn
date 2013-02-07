/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.cin.ufpe.nesc2cpn.param;

import br.cin.ufpe.nesc2cpn.Nesc2CpnProperties;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author avld
 */
public class ParameterFactory
{
    private static ParameterFactory instance;
    private Map<String,AbstractCheckParameter> checkMap;

    private ParameterFactory()
    {
        List<AbstractCheckParameter> checkList = new ArrayList<AbstractCheckParameter>();
        checkList.add( new OutputCheck() );
        checkList.add( new FileCheck() );
        checkList.add( new CompressCheck() );
        checkList.add( new FunctionCheck() );
        checkList.add( new KeepCheck() );
        checkList.add( new OnlyCreatorCheck() );
        checkList.add( new RoundsCheck() );
        checkList.add( new ShowRepositoryCheck() );
        checkList.add( new ModelNameCheck() );

        createMap( checkList );
    }

    private void createMap(List<AbstractCheckParameter> list)
    {
        checkMap = new HashMap<String, AbstractCheckParameter>();
        
        for( AbstractCheckParameter check : list )
        {
            checkMap.put( check.getParam() , check );
        }
    }

    public static ParameterFactory getInstance()
    {
        if( instance == null )
        {
            instance = new ParameterFactory();
        }

        return instance;
    }

    public Nesc2CpnProperties configuration(String args[]) throws Exception
    {
        int pos = 0;
        Nesc2CpnProperties properties = new Nesc2CpnProperties();

        while( pos < args.length )
        {
            String param = args[ pos ];

            System.out.println( "configuration: " + param );

            if( !checkMap.containsKey( param ) )
            {
                pos += 1;
                continue ;
            }

            AbstractCheckParameter abs = checkMap.get( param );
            int argNumber = abs.getArgNumber();

            if( argNumber == -1 )
            {
                argNumber = pos - args.length;
            }

            if( pos + argNumber >= args.length )
            {
                throw new Exception( "O parametro " + param
                                   + " necessita de " + argNumber
                                   + " argumento(s)." );
            }

            String[] subArg = Arrays.copyOfRange( args , pos + 1 , pos + 1 + argNumber );
            int saltos = abs.execute( subArg , properties );

            if( saltos < 0 )
            {
                saltos = 0;
            }

            pos += saltos + 1;
        }
        
        return properties;
    }

    public String help()
    {
        StringBuilder builder = new StringBuilder();
        builder.append( "nesc2cpn [options]\n\n" );
        builder.append( "Options\n" );
        
        for( AbstractCheckParameter acp : checkMap.values() )
        {
            builder.append( acp.help() );
            builder.append( "\n" );
        }

        return builder.toString();
    }
    
}
