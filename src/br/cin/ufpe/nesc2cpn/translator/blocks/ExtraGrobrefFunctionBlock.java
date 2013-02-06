package br.cin.ufpe.nesc2cpn.translator.blocks;

import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Block;
import br.cin.ufpe.nesc2cpn.cpnModule.globbox.GlobRef;
import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Ml;
import br.cin.ufpe.nesc2cpn.translator.cpn.SimpleCPN;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author avld
 */
public class ExtraGrobrefFunctionBlock implements FunctionBlock
{
    private List<SimpleCPN> simpleCpnList;
    private List<String> nameList;

    public ExtraGrobrefFunctionBlock(List<SimpleCPN> simpleCpnList)
    {
        this.simpleCpnList = simpleCpnList;
    }

    public Block getBlock()
    {
        Block block = new Block( "Extra GlobRef" );
        addGlobref( block );
        addFunctionClear( block );

        return block;
    }

    private void addGlobref(Block block)
    {
        nameList = new ArrayList<String>();

        for( SimpleCPN item : simpleCpnList )
        {
            for( String name : item.getVariableUsed() )
            {
                nameList.add( name );

                GlobRef ref = new GlobRef( "globref " + name + " = 0;" );
                block.add( ref );
            }
        }
    }

    private void addFunctionClear(Block block)
    {
        StringBuilder funClear = new StringBuilder();
        funClear.append("fun clearVariable() = (\n");
        funClear.append(" radioOn := false;\n");
        funClear.append(" radioTime := 0.0;\n");
        funClear.append(" energyRound := 0.0;\n");
        funClear.append(" powerRound := 0.0;\n");
        funClear.append(" timeRound := 0.0");
        funClear.append( !nameList.isEmpty() ? ";\n" : "" );

        Iterator<String> it = nameList.iterator();
        while( it.hasNext() )
        {
            funClear.append( " " ).append( it.next() ).append( " := 0" );
            funClear.append( it.hasNext() ? ";\n" : "\n" );
        }

        funClear.append(");");

        block.add( new Ml( funClear.toString() ) );
    }
}
