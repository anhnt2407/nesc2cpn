package br.cin.ufpe.nesc2cpn.param;

import br.cin.ufpe.nesc2cpn.Nesc2CpnMain;

/**
 *
 * @author avld
 */
public class HelpCheck extends AbstractCheckParameter
{

    public HelpCheck()
    {
        super( "-help" , 0 );
    }

    @Override
    public int execute(String[] arg) throws Exception
    {
        String help = ParameterFactory.getInstance().help();
        System.out.println( help );
        
        System.exit( 0 );

        return 0;
    }

    @Override
    public String help()
    {
        String txt = "-help";
        txt += "\tprint this help";

        return txt;
    }

}
