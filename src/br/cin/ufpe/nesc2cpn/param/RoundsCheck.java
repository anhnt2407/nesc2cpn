package br.cin.ufpe.nesc2cpn.param;

import br.cin.ufpe.nesc2cpn.Nesc2CpnProperties;

/**
 *
 * @author avld
 */
public class RoundsCheck extends AbstractCheckParameter
{

    public RoundsCheck()
    {
        super( "-rounds" , 1 );
        System.getProperties().setProperty( "nesc2cpn.rounds" , "100" );
    }

    @Override
    public int execute(String[] arg , Nesc2CpnProperties properties ) throws Exception
    {
        int rounds = Integer.parseInt( arg[0] );
        System.getProperties().setProperty( "nesc2cpn.rounds" , rounds + "" );

        return 1;
    }

    @Override
    public String help()
    {
        String txt = "-rounds number";
        txt += "\tinforma quantos rounds deve executar [default: 100]";

        return txt;
    }

}
