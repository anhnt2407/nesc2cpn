package br.cin.ufpe.nesc2cpn.param;

/**
 *
 * @author avld
 */
public class ModelNameCheck extends AbstractCheckParameter
{

    public ModelNameCheck()
    {
        super( "-modelname" , 1 );
        System.setProperty( "nesc2cpn.modelname" , "model.cpn" );
    }

    @Override
    public int execute(String[] arg) throws Exception
    {
        System.setProperty( "nesc2cpn.modelname" , arg[0] );
        return 1;
    }

    @Override
    public String help()
    {
        return "-modelname name.cpn";
    }

    

}
