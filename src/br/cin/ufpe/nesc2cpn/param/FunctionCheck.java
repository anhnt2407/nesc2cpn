package br.cin.ufpe.nesc2cpn.param;

import br.cin.ufpe.nesc2cpn.Nesc2CpnMain;

/**
 *
 * @author avld
 */
public class FunctionCheck extends AbstractCheckParameter
{

    public FunctionCheck()
    {
        super( "-function" , 1 );
        
        Nesc2CpnMain.isModelingApplication = true;
        System.setProperty( "nesc2cpn.moldingType" , Nesc2CpnMain.MOLDING_TYPE_APP );
    }

    @Override
    public int execute(String[] arg) throws Exception
    {
        Nesc2CpnMain.functionname = arg[ 0 ];
        Nesc2CpnMain.isModelingApplication = false;

        System.setProperty( "nesc2cpn.moldingType" , Nesc2CpnMain.MOLDING_TYPE_FUN );

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
