package br.cin.ufpe.nesc2cpn.translator.blocks;

import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Block;
import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Color;
import br.cin.ufpe.nesc2cpn.cpnModule.globbox.GlobRef;

/**
 *
 * @author avld
 */
public class SchedulerBlock implements FunctionBlock
{

    public Block getBlock()
    {
        Block block = new Block("Scheduler Function");

        Color intListColor = new Color();
        intListColor.setNameId( "INTs" );
        intListColor.setList( "INT" );
        intListColor.setTimed( true );
        intListColor.setLayout( "colset INTs = list INT timed;" );
        block.add( intListColor );

        GlobRef schedulerList = new GlobRef( "globref schedulerList = [] : INTs;" );
        block.add( schedulerList );

        GlobRef iRef = new GlobRef( "globref iRef = 0;" );
        block.add( iRef );

        return block;
    }

}
