package br.cin.ufpe.nesc2cpn.translator.blocks;

import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Block;
import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Ml;

/**
 *
 * @author avld
 */
public class OtherFunctionBlock implements FunctionBlock
{

    public Block getBlock()
    {
        Block otherBlock = new Block("Other");
        otherBlock.add( new Ml("val d = 20;") );
        otherBlock.add( new Ml("fun realToInt r = IntInf.toInt(RealToIntInf d r);") );
        otherBlock.add( new Ml("fun intToReal i = (IntInfToReal d(IntInf.fromInt i));") );

        return otherBlock;
    }

}
