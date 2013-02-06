package br.cin.ufpe.nesc2cpn.param;

import br.cin.ufpe.nesc2cpn.repository.file.DatabaseUtil;
import java.util.Arrays;

/**
 *
 * @author avld
 */
public class OutputCheck extends AbstractCheckParameter
{

    public OutputCheck()
    {
        super( "-output" , 1 );
        System.getProperties().setProperty( "nesc2cpn.output" , "./" );
    }

    @Override
    public int execute(String[] args) throws Exception
    {
        if( args.length == 0 )
        {
            String error = "sintax: -output directory\n";
            error += "Faltou especificar o directory no parametro -output;";

            throw new Exception( error );
        }

        System.getProperties().setProperty( "nesc2cpn.output" , args[ 0 ] );
        System.setProperty( DatabaseUtil.DB_PREFER , args[ 0 ] );

        return 1;
    }

    @Override
    public String help()
    {
        //pode informar a pasta onde deve salvar o projeto
        String help = "-ouput directory";
        help += "\tset the diretory where save";

        return help;
    }

}
