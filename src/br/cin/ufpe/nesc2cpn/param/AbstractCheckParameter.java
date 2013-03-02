package br.cin.ufpe.nesc2cpn.param;

import br.cin.ufpe.nesc2cpn.Nesc2CpnProperties;

/**
 *
 * @author avld
 */
public abstract class AbstractCheckParameter
{
    private String param;
    private int argNumber;

    public AbstractCheckParameter(String paramName, int argNumber)
    {
        this.param = paramName;
        this.argNumber = argNumber;
    }

    public boolean checkParam(String name)
    {
        return param.equalsIgnoreCase( name );
    }

    public int getArgNumber() {
        return argNumber;
    }

    public String getParam() {
        return param;
    }

    public abstract int execute( String[] arg , Nesc2CpnProperties properties ) throws Exception;
    public abstract String help();
}
