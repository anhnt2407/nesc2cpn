package br.cin.ufpe.nesc2cpn.param;

import br.cin.ufpe.nesc2cpn.Nesc2CpnMain;

/**
 *
 * @author avld
 */
public class OnlyCreatorCheck extends AbstractCheckParameter
{

    public OnlyCreatorCheck()
    {
        super( "-onlycreator" , 0 );
    }

    @Override
    public int execute(String[] arg) throws Exception
    {
        Nesc2CpnMain.ONLY_CREATE_MODEL = true;
        
        return 0;
    }

    @Override
    public String help()
    {
        //pode informar se é apenas para criar a CPN [não deve simular]
        String txt = "-onlycreator";
        txt += "\tapenas criar o arquivo CPN e não simular";

        return txt;
    }

}
