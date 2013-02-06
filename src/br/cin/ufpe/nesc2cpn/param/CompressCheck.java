package br.cin.ufpe.nesc2cpn.param;

import br.cin.ufpe.nesc2cpn.translator.nodeCreator.NodeCreatorFactory;

/**
 *
 * @author avld
 */
public class CompressCheck extends AbstractCheckParameter
{

    public CompressCheck()
    {
        super( "-compress" , 0 );
        NodeCreatorFactory.COMPRESS = false;
    }

    @Override
    public int execute(String[] arg) throws Exception
    {
        NodeCreatorFactory.COMPRESS = true;
        
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
