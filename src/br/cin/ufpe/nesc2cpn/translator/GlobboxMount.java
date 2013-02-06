package br.cin.ufpe.nesc2cpn.translator;

import br.cin.ufpe.nesc2cpn.cpnModule.globbox.BlockItem;
import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Color;
import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Ml;
import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Var;
import br.cin.ufpe.nesc2cpn.cpnModule.globbox.With;
import br.cin.ufpe.nesc2cpn.cpnModule.monitorblock.Monitor;
import br.cin.ufpe.nesc2cpn.cpnModule.monitorblock.MonitorBlock;
import br.cin.ufpe.nesc2cpn.cpnModule.monitorblock.MonitorBlockXML;
import br.cin.ufpe.nesc2cpn.cpnModule.monitorblock.MonitorXML;
import br.cin.ufpe.nesc2cpn.translator.blocks.CriteriaStopFunctionBlock;
import br.cin.ufpe.nesc2cpn.translator.cpn.SimpleCPN;
import br.cin.ufpe.nesc2cpn.translator.blocks.DeviceFunctionBlock;
import br.cin.ufpe.nesc2cpn.translator.blocks.FunctionBlock;
import br.cin.ufpe.nesc2cpn.translator.blocks.EnergyFunctionBlock;
import br.cin.ufpe.nesc2cpn.translator.blocks.ExtraGrobrefFunctionBlock;
import br.cin.ufpe.nesc2cpn.translator.blocks.FileFunctionBlock;
import br.cin.ufpe.nesc2cpn.translator.blocks.OtherFunctionBlock;
import br.cin.ufpe.nesc2cpn.translator.blocks.PowerFunctionBlock;
import br.cin.ufpe.nesc2cpn.translator.blocks.RoundFunctionBlock;
import br.cin.ufpe.nesc2cpn.translator.blocks.SchedulerBlock;
import br.cin.ufpe.nesc2cpn.translator.blocks.StatisticFunctionBlock;
import br.cin.ufpe.nesc2cpn.translator.blocks.TimeFunctionBlock;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author avld
 */
public class GlobboxMount
{
    private List<FunctionBlock> functionBlockList;
    
    public GlobboxMount()
    {
        functionBlockList = new ArrayList<FunctionBlock>();
        functionBlockList.add( new SchedulerBlock() );
        functionBlockList.add( new OtherFunctionBlock() );
        
        functionBlockList.add( new StatisticFunctionBlock() );
        functionBlockList.add( new CriteriaStopFunctionBlock() );
        
        functionBlockList.add( new EnergyFunctionBlock() );
        functionBlockList.add( new TimeFunctionBlock() );
        functionBlockList.add( new PowerFunctionBlock() );
        functionBlockList.add( new DeviceFunctionBlock() );

        functionBlockList.add( new FileFunctionBlock() );
    }

    public List<BlockItem> addGlobbox(List<SimpleCPN> simpleCpnList)
    {
        List<BlockItem> objList = new ArrayList<BlockItem>();
        addColorAndVariable( objList );
        
        for( FunctionBlock block : functionBlockList )
        {
            objList.add( block.getBlock() );
        }

        FunctionBlock extraBlock = new ExtraGrobrefFunctionBlock( simpleCpnList );
        objList.add( extraBlock.getBlock() );

        FunctionBlock roundBlock = new RoundFunctionBlock();
        objList.add( roundBlock.getBlock() );
        
        return objList;
    }

    private void addColorAndVariable(List<BlockItem> objList)
    {
        Color intColor = new Color();
        intColor.setNameId( "INT" );
        intColor.setInteiro( new With() );
        intColor.setLayout( "colset INT = int;" );  //timed
        intColor.setTimed( false );                 //true

        Var iVar = new Var();
        iVar.setNameId("INT");
        iVar.getTypeId().add("i");
        iVar.getTypeId().add("n");
        iVar.setLayout( "var i , n : INT;" );

        Color boolColor = new Color();
        boolColor.setNameId( "BOOL" );
        boolColor.setBool( new With() );
        boolColor.setLayout( "colset BOOL = bool;" );  //timed

        Var respVar = new Var();
        respVar.setNameId("BOOL");
        respVar.getTypeId().add("resp");
        respVar.setLayout( "var resp : BOOL;" );

        objList.add( intColor );
        objList.add( iVar );

        objList.add( boolColor );
        objList.add( respVar );
    }

    public Monitor monitorBreakPoint( long transID , long pageID )
    {
        Monitor monitor = new Monitor();
        monitor.setName( "Schenduler BP" );
        monitor.setType( 1 );
        monitor.setTypeDescription( "Breakpoint" );
        monitor.setDisable( false );
        monitor.setNode( transID , pageID );
        monitor.setDeclaration( new Ml( getBreakPointFunction() ) );

        return monitor;
    }

    private String getBreakPointFunction()
    {
        return "fun pred ( bindelem ) =\n"
             + "(\n"
             + "  checkError()\n"
             + ")\n";
    }

    public static void main(String arg[]) throws Exception
    {
        GlobboxMount mount = new GlobboxMount();
        Monitor monitor = mount.monitorBreakPoint( 1244 , 1006 );

        MonitorBlock block = new MonitorBlock();
        block.getMonitor().add( monitor );

        //System.out.println( MonitorBlockXML.convert( block ) );
        System.out.println( MonitorXML.convert( monitor ) );
    }
}
