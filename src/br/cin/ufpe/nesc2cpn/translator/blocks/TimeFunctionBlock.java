package br.cin.ufpe.nesc2cpn.translator.blocks;

import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Block;
import br.cin.ufpe.nesc2cpn.cpnModule.globbox.GlobRef;
import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Ml;

/**
 *
 * @author avld
 */
public class TimeFunctionBlock implements FunctionBlock
{

    public Block getBlock()
    {
        Block timeBlock = new Block("Time Function");
        timeBlock.add( new GlobRef("globref timeInst = 0.0;") );
        timeBlock.add( new GlobRef("globref timeTotal = 0.0;") );
        timeBlock.add( new GlobRef("globref timeRound = 0.0;") );

        timeBlock.add( new Ml("fun addTime( mean:real , variance:real ) =\n"+
                              "  let\n"+
                              "     val value = normal( mean , variance )\n"+
                              "  in\n"+
                              "     timeInst := value;\n"+
                              "     timeTotal := (!timeTotal) + value;\n"+
                              "     timeRound := (!timeRound) + value\n"+
                              "  end;") );

        return timeBlock;
    }

}
