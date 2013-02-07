package br.cin.ufpe.nesc2cpn.param;

import br.cin.ufpe.nesc2cpn.Nesc2CpnProperties;

/**
 *
 * @author avld
 */
public class KeepCheck extends AbstractCheckParameter
{

    public KeepCheck()
    {
        super( "-keep" , 0 );
    }

    @Override
    public int execute(String[] arg , Nesc2CpnProperties properties ) throws Exception
    {
        properties.setKeep( true );
        return 0;
    }

    @Override
    public String help()
    {
        //salvar o arquivo .CPN e os dados da simulação
        String txt = "-keep";
        txt += "\tapós a simulação, irá deletar o arquivo .CPN e a pasta output/";

        return txt;
    }

}
