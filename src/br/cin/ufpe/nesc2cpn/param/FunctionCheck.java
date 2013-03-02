package br.cin.ufpe.nesc2cpn.param;

import br.cin.ufpe.nesc2cpn.Nesc2CpnProperties;

/**
 *
 * @author avld
 */
public class FunctionCheck extends AbstractCheckParameter
{

    public FunctionCheck()
    {
        super( "-function" , 1 );
        
        //Nesc2CpnMain.isModelingApplication = true;
        //System.setProperty( "nesc2cpn.moldingType" , Nesc2CpnMain.MOLDING_TYPE_APP );
    }

    @Override
    public int execute(String[] arg , Nesc2CpnProperties properties ) throws Exception
    {
        properties.setFunctionName( arg[ 0 ] );
        //properties.setCreateApplicationModel( false );

        //System.setProperty( "nesc2cpn.moldingType" , Nesc2CpnMain.MOLDING_TYPE_FUN );
        System.out.println( "nome da função..." );

        return 1;
    }

    @Override
    public String help()
    {
        String txt = "-function name";
        txt += "\tselect the method will be translated";

        return txt;
    }

}
