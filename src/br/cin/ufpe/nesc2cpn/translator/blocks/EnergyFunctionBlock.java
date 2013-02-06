package br.cin.ufpe.nesc2cpn.translator.blocks;

import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Block;
import br.cin.ufpe.nesc2cpn.cpnModule.globbox.GlobRef;
import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Ml;

/**
 *
 * @author avld
 */
public class EnergyFunctionBlock implements FunctionBlock
{

    public Block getBlock()
    {
        Block energyBlock = new Block("Energy Function");
        energyBlock.add( new GlobRef("globref energyInst = 0.0;") );
        energyBlock.add( new GlobRef("globref energyTotal = 0.0;") );
        energyBlock.add( new GlobRef("globref energyRound = 0.0;") );

        energyBlock.add( new Ml("fun addEnergy( mean:real , variance:real ) =\n"+
                                "  let\n"+
                                "     val value = normal( mean , variance )\n"+
                                "  in\n"+
                                "     energyInst :=  value;\n"+
                                "     energyTotal := (!energyTotal) + value;\n"+
                                "     energyRound := (!energyRound) + value\n"+
                                "  end;") );

        return energyBlock;
    }

}
