package br.cin.ufpe.nesc2cpn.translator.blocks;

import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Block;
import br.cin.ufpe.nesc2cpn.cpnModule.globbox.GlobRef;
import br.cin.ufpe.nesc2cpn.cpnModule.globbox.Ml;
import br.cin.ufpe.nesc2cpn.repository.Line;
import br.cin.ufpe.nesc2cpn.repository.RepositoryControl;
import br.cin.ufpe.nesc2cpn.translator.nodeCreator.NodeCreator;

/**
 *
 * @author avld
 */
public class DeviceFunctionBlock implements FunctionBlock
{

    public Block getBlock()
    {
        Block block = new Block( "Device Function" );
        block.add( new GlobRef("globref radioTime = 0.0;") );
        block.add( new GlobRef("globref radioOn = false;") );
//        block.getGlobRef().add( new GlobRef("globref led0On = false;") );
//        block.getGlobRef().add( new GlobRef("globref led1On = false;") );
//        block.getGlobRef().add( new GlobRef("globref led2On = false;") );

        Ml radioTimeMl = new Ml( getRadioTimeFunction() );
        block.add( radioTimeMl );

        Ml radioMl = new Ml( getRadioFunction() );
        block.add( radioMl );
        
//        Ml led0Ml = new Ml( getLed0Function() );
//        block.getMl().add( led0Ml );
//
//        Ml led1Ml = new Ml( getLed1Function() );
//        block.getMl().add( led1Ml );
//
//        Ml led2Ml = new Ml( getLed2Function() );
//        block.getMl().add( led2Ml );

        //Ml deviceMl = new Ml( getDeviceFunction() );
        //block.getMl().add( deviceMl );

        return block;
    }

    // ----------------------------------- //
    // ----------------------------------- //
    // ----------------------------------- //

    private String getRadioTimeFunction()
    {
        return getTimeFunction( "addRadioTime" , "radioOn" );
    }

    private String getRadioFunction()
    {
        Line line = getEnergy( NodeCreator.RADIO );
        return getCalcFunction( "addRadioEnergy" , "radioOn" , line );
    }

    private String getLed0Function()
    {
        Line line = getEnergy( NodeCreator.LED0 );
        return getCalcFunction( "addLed0Energy" , "led0On" , line );
    }

    private String getLed1Function()
    {
        Line line = getEnergy( NodeCreator.LED1 );
        return getCalcFunction( "addLed1Energy" , "led1On" , line );
    }
    
    private String getLed2Function()
    {
        Line line = getEnergy( NodeCreator.LED2 );
        return getCalcFunction( "addLed2Energy" , "led2On" , line );
    }

    // ----------------------------------- //
    // ----------------------------------- //
    // ----------------------------------- //

    private String getCalcFunction(String name, String grobref, Line line)
    {
        String m = format( line.getPowerMean() );
        String v = format( line.getPowerVariance() );

        StringBuilder function = new StringBuilder();
        function.append("fun ").append(name).append( "( t:real ) =\n");
        function.append( "  let\n" );
        function.append("     val value = t * normal( ").append(m).append(" , ").append(v).append( " )\n");
        function.append( "  in\n" );
        function.append("     if( (!" ).append(grobref).append( ") ) then (\n");
        //function.append( "     energyInst := (!energyInst) + value;\n" );
        function.append( "     energyTotal := (!energyTotal) + value;\n" );
        function.append( "     energyRound := (!energyRound) + value\n" );
        function.append( "     ) else ()\n" );
        function.append( "  end;" );

        return function.toString();
    }

    private String getTimeFunction(String name, String grobref)
    {
        StringBuilder function = new StringBuilder();
        function.append("fun ").append( name ).append( "( t:real ) = (\n");
        function.append("     if( (!" ).append(grobref).append( ") ) then \n");
        function.append( "       radioTime := (!radioTime) + t\n" );
        function.append( "    else ()\n" );
        function.append( "  );" );

        return function.toString();
    }

    private Line getEnergy(String type)
    {
        Line line = new Line();
        try
        {
            line = RepositoryControl.getInstance().get( NodeCreator.BASIC
                                                      , NodeCreator.DEVICE
                                                      , type );
        } catch (Exception ex)
        {
            
        }

        return line;
    }

    private String format(Double value)
    {
        String valueStr = value.toString();
        return valueStr.replace('-', '~');
    }
}
