package br.cin.ufpe.nesc2cpn.param;

import br.cin.ufpe.nesc2cpn.Nesc2CpnMain;

/**
 *
 * @author avld
 */
public class FileCheck extends AbstractCheckParameter
{

    public FileCheck()
    {
        super( "-file" , 1 );
    }

    @Override
    public int execute(String[] arg) throws Exception
    {
        Nesc2CpnMain.filename = arg[ 0 ];
        return 1;
    }

    @Override
    public String help()
    {
        String txt = "-file file";
        txt += "\tselect the configuration file";
        
        return txt;
    }

}
