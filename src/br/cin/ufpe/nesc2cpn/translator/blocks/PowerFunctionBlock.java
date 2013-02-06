package br.cin.ufpe.nesc2cpn.translator.blocks;

import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Block;
import br.cin.ufpe.nesc2cpn.cpnModule.globbox.GlobRef;
import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Ml;

/**
 *
 * @author avld
 */
public class PowerFunctionBlock implements FunctionBlock
{

    public Block getBlock()
    {
        Block powerBlock = new Block("Power Function");
        powerBlock.add( new GlobRef("globref powerTotal = 0.0;") );
        powerBlock.add( new GlobRef("globref powerRound = 0.0;") );
        powerBlock.add( new Ml( getPowerFunction() ) );
        powerBlock.add( new Ml( getPowerCalcFunction() ) );

        return powerBlock;
    }

    private String getPowerFunction()
    {
        return "fun addPower( mean:real , variance:real ) =\n"+
                "  let\n"+
                "     val value = normal( mean , variance )\n"+
                "  in\n"+
                "     powerTotal := (!powerTotal) + value;\n"+
                "     powerRound := (!powerRound) + value\n"+
                "  end;";
    }

    private String getPowerCalcFunction()
    {
        return "fun calcPower() =\n"+
                "  let\n"+
                "     val value = (!energyInst) / (!timeInst)\n"+
                "  in\n"+
                "     if( Real.==(!timeInst,0.0) ) then ()\n" +
                "     else addBatch( value );\n"+
                "     checkError()\n"+
                //"     powerTotal := (!powerTotal) + value;\n"+
                //"     powerRound := value\n"+
                "  end;";
    }
}
