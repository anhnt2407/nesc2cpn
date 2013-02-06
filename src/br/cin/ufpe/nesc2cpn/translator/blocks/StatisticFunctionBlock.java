package br.cin.ufpe.nesc2cpn.translator.blocks;

import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Block;
import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Ml;

/**
 *
 * @author avld
 */
public class StatisticFunctionBlock implements FunctionBlock
{
    
    public Block getBlock()
    {
        Block powerBlock = new Block("Statistic Function");
        powerBlock.add( new Ml( getSumFunction() ) );
        powerBlock.add( new Ml( getMeanFunction() ) );
        powerBlock.add( new Ml( getSumSdFunction() ) );
        powerBlock.add( new Ml( getVarianceFunction() ) );
        powerBlock.add( new Ml( getSdFunction() ) );

        return powerBlock;
    }

    private String getSumFunction()
    {
        return "fun sum( dataList:real list, i:int, s:real ) =\n"+
                "(\n"+
                "  if( i < length dataList ) then\n"+
                "    (\n"+
                "     sum( dataList , i + 1 ,  s + List.nth(dataList,i) )\n"+
                "    )\n"+
                "  else s\n"+
                ");";
    }

    private String getMeanFunction()
    {
        return "fun mean( dataList : real list ) =\n"+
                "(\n"+
                "  sum( dataList , 0 , 0.0 ) / real (length dataList)\n"+
                ");";
    }

    private String getSumSdFunction()
    {
        return "fun sumSD( dataList:real list,i:int,s:real,m:real ) =\n"+
                "(\n"+
                "  if( i < length dataList ) then\n"+
                "    (\n"+
                "     sumSD( dataList\n"+
                "          , i  + 1\n"+
                "          , s + (List.nth(dataList,i) - m)\n"+
                "              * (List.nth(dataList,i) - m)\n"+
                "          , m)\n"+
                "    )\n"+
                "  else s\n"+
                ");";
    }

    private String getVarianceFunction()
    {
        return "fun variance( dataList : real list ) = \n"+
                "  let\n"+
                "     val m = mean( dataList );\n"+
                "  in\n"+
                "     sumSD( dataList , 0 , 0.0 , m )\n"+
                "        / real (length dataList - 1)\n"+
                "  end;";
    }

    private String getSdFunction()
    {
        return "fun sd( dataList : real list ) =\n"+
                "  (\n"+
                "     Math.sqrt( variance( dataList ) )\n"+
                "  );";
    }
}
