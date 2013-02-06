package br.cin.ufpe.nesc2cpn.param;

import br.cin.ufpe.nesc2cpn.Nesc2CpnMain;

/**
 *
 * @author avld
 */
public class KeepCheck extends AbstractCheckParameter
{

    public KeepCheck()
    {
        super( "-keep" , 0 );
        Nesc2CpnMain.KEEP = false;
    }

    @Override
    public int execute(String[] arg) throws Exception
    {
        Nesc2CpnMain.KEEP = true;
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
