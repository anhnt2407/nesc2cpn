package br.cin.ufpe.nesc2cpn.translator.blocks;

import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Block;
import br.cin.ufpe.nesc2cpn.cpnModule.globbox.GlobRef;
import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Ml;

/**
 *
 * @author avld
 */
public class FileFunctionBlock implements FunctionBlock
{

    public FileFunctionBlock()
    {
        
    }

    public Block getBlock()
    {
        Block fileBlock = new Block("File Function");

        fileBlock.add( new GlobRef("globref fileidE = (NONE : TextIO.outstream option);") );
        fileBlock.add( new GlobRef("globref fileidP = (NONE : TextIO.outstream option);") );
        fileBlock.add( new GlobRef("globref fileidT = (NONE : TextIO.outstream option);") );

        fileBlock.add( new Ml( "fun getfidE () = Option.valOf(!fileidE)" ) );
        fileBlock.add( new Ml( "fun getfidP () = Option.valOf(!fileidP)" ) );
        fileBlock.add( new Ml( "fun getfidT () = Option.valOf(!fileidT)" ) );

        fileBlock.add( new Ml("fun file_open ( name ) =\n" +
                              "  let\n" +
                              "     val dirOut = Output.getSimOutputDir()\n" +
                              "     val f = OS.Path.concat ( dirOut , name )\n" +
                              "     val f_energy = f ^ &quot;_energy.dat&quot;\n" +
                              "     val f_time     = f ^ &quot;_time.dat&quot;\n" +
                              "     val f_power  = f ^ &quot;_power.dat&quot;\n" +
                              "  in\n" +
                              "     Output.initSimOutputDir();\n" +
                              "     fileidE := SOME (TextIO.openAppend f_energy);\n" +
                              "     fileidT := SOME (TextIO.openAppend f_time);\n" +
                              "     fileidP := SOME (TextIO.openAppend f_power)\n" +
                              "end;") );

        fileBlock.add( new Ml("fun file_write ( eValue , tValue , pValue ) = (\n"+
                              "  TextIO.output(getfidE(), Real.toString( !eValue ) ^ \"\\n\");\n"+
                              "  TextIO.output(getfidT(), Real.toString( !tValue ) ^ \"\\n\");\n"+
                              "  TextIO.output(getfidP(), Real.toString( !pValue ) ^ \"\\n\")\n"+
                              " )") );

        fileBlock.add( new Ml("fun file_close () = (\n"+
                              "  TextIO.closeOut( getfidE() );\n"+
                              "  fileidE := NONE;\n"+
                              "  TextIO.closeOut( getfidT() );\n"+
                              "  fileidT := NONE;\n"+
                              "  TextIO.closeOut( getfidP() );\n"+
                              "  fileidP := NONE\n"+
                              ")") );

        return fileBlock;
    }

}
