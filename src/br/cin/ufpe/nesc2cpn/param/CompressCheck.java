package br.cin.ufpe.nesc2cpn.param;

import br.cin.ufpe.nesc2cpn.Nesc2CpnProperties;

/**
 *
 * @author avld
 */
public class CompressCheck extends AbstractCheckParameter
{

    public CompressCheck()
    {
        super( "-reduction" , 0 );
    }

    @Override
    public int execute(String[] arg , Nesc2CpnProperties properties ) throws Exception
    {
        properties.setReduction( true );
        return 0;
    }

    @Override
    public String help()
    {
        String txt = "-compress";
        txt += "\tif you want compress the CPN result";

        return txt;
    }

}
