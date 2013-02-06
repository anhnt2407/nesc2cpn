package br.cin.ufpe.nesc2cpn.translator.blocks;

import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Block;
import br.cin.ufpe.nesc2cpn.cpnModule.globbox.GlobRef;
import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Ml;
import java.util.List;

/**
 *
 * @author avld
 */
public class CriteriaStopFunctionBlock implements FunctionBlock
{
    private String error;
    private String batchMaxSize;
    private String tStudentAlfa;

    public CriteriaStopFunctionBlock()
    {
        error        = System.getProperties().getProperty( "nesc2cpn.errorMax"     , "0.5" );
        batchMaxSize = System.getProperties().getProperty( "nesc2cpn.batchMaxSize" , "100" );

        //75% 80% 85% 90% 95% 97.5% 99% 99.5% 99.75% 99.9% 99.95%
        tStudentAlfa = System.getProperties().getProperty( "nesc2cpn.tStudentAlfa" , "95"  );
    }

    public Block getBlock()
    {
        Block block = new Block("Criteria Stop Function");
        
        block.add( new GlobRef("globref errorMax = "+ error +";") );
        block.add( new GlobRef("globref batchMaxSize = "+ batchMaxSize +";") );
        block.add( new GlobRef("globref batchList = nil : real list;") );
        block.add( new GlobRef("globref batchMeanList = nil : real list;") );
        block.add( new GlobRef("globref tStudentList = nil : real list;") );

        block.add( new Ml( getAddBatchFunction() ) );
        block.add( new Ml( getGetTSudent() ) );

        try
        {
            block.add( new Ml( getInitTStudentList() ) );
        }
        catch(Exception err)
        {
            block.add( new Ml( getInitTStudentList_95() ) );
        }

        block.add( new Ml( getInitCriteriaStopFunction() ) );
        block.add( new Ml( getCalcErrorFunction() ) );
        block.add( new Ml( getCheckError() ) );

        return block;
    }

    private String getAddBatchFunction()
    {
        return "fun addBatch( power : real ) =\n"+
                "(\n"+
                "  batchList := ins (!batchList) power;\n"+
                "  if( length (!batchList) = !batchMaxSize ) then\n"+
                "    (\n"+
                "     batchMeanList := ins (!batchMeanList) (mean(!batchList));\n"+
                "     batchList := nil\n"+
                "    )\n"+
                "  else ()\n"+
                ");";
    }

    private String getCalcErrorFunction()
    {
        return "fun calcError() =\n"+
                "  let\n"+
                "     val n  = length (!batchMeanList);\n"+
                "     val t   = getTStudent( n - 1 );\n"+
                "     val sd = sd( !batchMeanList );\n"+
                "  in\n"+
                "     ( t * sd ) / Math.sqrt (real n)\n"+
                "  end;";
    }

    private String getInitCriteriaStopFunction()
    {
        return "fun initCriteriaStop() =\n"+
                "(\n"+
                "  batchList := nil;\n" +
                "  batchMeanList := nil;\n" +
                "  initTStudentList()\n" +
                ");";
    }

    private String getInitTStudentList() throws Exception
    {
        double alfa = Double.parseDouble( tStudentAlfa );

        String fun = "fun initTStudentList() = (\n";

        TStudent tstudent = new TStudent();
        List<Double> valueList = tstudent.getValueList( alfa );

        for( int i = 0 ; i < valueList.size() ; i++ )
        {
            double v = valueList.get( i );
            fun += "  tStudentList := ins (!tStudentList) " + v;

            if( i + 1 < valueList.size() ) { fun += ";\n"; }
            else { fun += "\n"; }
        }

        fun += ");";
        
        return fun;
    }
    
    private String getInitTStudentList_95()
    {
        String fun = "fun initTStudentList() = (\n"
                    + "  tStudentList := ins (!tStudentList) 6.314;\n"
                    + "  tStudentList := ins (!tStudentList) 2.92;\n"
                    + "  tStudentList := ins (!tStudentList) 2.353;\n"
                    + "  tStudentList := ins (!tStudentList) 2.132;\n"
                    + "  tStudentList := ins (!tStudentList) 2.015;\n"
                    + "  tStudentList := ins (!tStudentList) 1.943;\n"
                    + "  tStudentList := ins (!tStudentList) 1.895;\n"
                    + "  tStudentList := ins (!tStudentList) 1.86;\n"
                    + "  tStudentList := ins (!tStudentList) 1.833;\n"
                    + "  tStudentList := ins (!tStudentList) 1.812;\n"
                    + "  tStudentList := ins (!tStudentList) 1.796;\n"
                    + "  tStudentList := ins (!tStudentList) 1.782;\n"
                    + "  tStudentList := ins (!tStudentList) 1.771;\n"
                    + "  tStudentList := ins (!tStudentList) 1.761;\n"
                    + "  tStudentList := ins (!tStudentList) 1.753;\n"
                    + "  tStudentList := ins (!tStudentList) 1.746;\n"
                    + "  tStudentList := ins (!tStudentList) 1.74;\n"
                    + "  tStudentList := ins (!tStudentList) 1.734;\n"
                    + "  tStudentList := ins (!tStudentList) 1.729;\n"
                    + "  tStudentList := ins (!tStudentList) 1.725;\n"
                    + "  tStudentList := ins (!tStudentList) 1.721;\n"
                    + "  tStudentList := ins (!tStudentList) 1.717;\n"
                    + "  tStudentList := ins (!tStudentList) 1.714;\n"
                    + "  tStudentList := ins (!tStudentList) 1.711;\n"
                    + "  tStudentList := ins (!tStudentList) 1.708;\n"
                    + "  tStudentList := ins (!tStudentList) 1.706;\n"
                    + "  tStudentList := ins (!tStudentList) 1.703;\n"
                    + "  tStudentList := ins (!tStudentList) 1.701;\n"
                    + "  tStudentList := ins (!tStudentList) 1.699;\n"
                    + "  tStudentList := ins (!tStudentList) 1.697;\n"
                    + "  tStudentList := ins (!tStudentList) 1.684;\n"
                    + "  tStudentList := ins (!tStudentList) 1.676;\n"
                    + "  tStudentList := ins (!tStudentList) 1.671;\n"
                    + "  tStudentList := ins (!tStudentList) 1.664;\n"
                    + "  tStudentList := ins (!tStudentList) 1.66;\n"
                    + "  tStudentList := ins (!tStudentList) 1.658;\n"
                    + "  tStudentList := ins (!tStudentList) 1.645\n"
                    +");";

        return fun;
    }

    private String getGetTSudent()
    {
        String fun = "fun getTStudent( degrees : int ) =\n (\n";
        
        fun += "if( degrees <= 0 )\n   then 0.0\n";
        fun += "else if(degrees <= 30 ) \n   then List.nth( (!tStudentList) , degrees - 1 )\n";
        fun += "else if(degrees <= 40 ) \n   then List.nth( (!tStudentList) , 30 )\n";
        fun += "else if(degrees <= 50 ) \n   then List.nth( (!tStudentList) , 31 )\n";
        fun += "else if(degrees <= 60 ) \n   then List.nth( (!tStudentList) , 32 )\n";
        fun += "else if(degrees <= 80 ) \n   then List.nth( (!tStudentList) , 33 )\n";
        fun += "else if(degrees <= 100 ) \n   then List.nth( (!tStudentList) , 34 )\n";
        fun += "else if(degrees <= 120 ) \n   then List.nth( (!tStudentList) , 35 )\n";
        fun += "else \n        List.nth( (!tStudentList) , 36 )\n";

        fun += ");";

        return fun;
    }

    private String getCheckError()
    {
        String fun = "fun checkError() =\n (\n";

        fun += "if( length (!batchMeanList) < 2 ) then false\n";
        fun += "else (calcError() <= !errorMax)\n";

        fun += ");";

        return fun;
    }
    
}