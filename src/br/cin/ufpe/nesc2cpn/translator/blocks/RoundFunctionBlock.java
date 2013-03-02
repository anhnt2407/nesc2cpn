package br.cin.ufpe.nesc2cpn.translator.blocks;

import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Block;
import br.cin.ufpe.nesc2cpn.cpnModule.globbox.GlobRef;
import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Ml;

/**
 *
 * @author avld
 */
public class RoundFunctionBlock implements FunctionBlock
{

    public RoundFunctionBlock()
    {
        // do nothing
    }
    
    @Override
    public Block getBlock()
    {
        Block roundBlock = new Block("Round Function");
        roundBlock.add( new GlobRef("globref rounds = 0;") );

        roundBlock.add( new Ml("fun clearVariableAll() = (\n"+
                               "  energyTotal := 0.0;\n"+
                               "  powerTotal := 0.0;\n"+
                               "  timeTotal := 0.0;\n"+
                               "  rounds := 0;\n"+
                               "  initCriteriaStop();\n"+
                               "  clearVariable()\n"+
                               "  );") );

        roundBlock.add( new Ml("fun initMeasure( n:int ) = (\n"+
                               "  clearVariableAll();\n"+
                               "  file_open( \"measure\" );\n"+
                               "  CPN&apos;Replications.nreplications n\n"+ //;
                               //"  file_close()\n"+
                               "  );") );

        roundBlock.add( new Ml("fun addRound() = (\n"+
                               "  inc rounds;\n"+
                               "  addRadioEnergy( (!radioTime) );\n"+
                               //"  powerCalc();\n"+
                               //TODO: salvar no List
                               "  file_write( energyRound, timeRound, powerRound );\n"+
                               "  clearVariable()\n"+
                               "  );") );

        return roundBlock;
    }

}
