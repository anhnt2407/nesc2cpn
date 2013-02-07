package br.cin.ufpe.nesc2cpn.param;

import br.cin.ufpe.nesc2cpn.Nesc2CpnMain;
import br.cin.ufpe.nesc2cpn.Nesc2CpnProperties;
import br.cin.ufpe.nesc2cpn.repository.gui.EnergyListJFrame;

/**
 *
 * @author avld
 */
public class ShowRepositoryCheck extends AbstractCheckParameter
{

    public ShowRepositoryCheck()
    {
        super( "-repositorymanager" , 0 );
        Nesc2CpnMain.showRepositoryManager = false;
    }

    @Override
    public int execute( String[] arg , Nesc2CpnProperties properties ) throws Exception
    {
        Nesc2CpnMain.showRepositoryManager = true;
        EnergyListJFrame.showFrame();
        
        return 0;
    }

    @Override
    public String help()
    {
        String txt = "-repositorymanager";
        txt += "\tshow the Repository Manager";

        return txt;
    }

}
