package br.cin.ufpe.nesc2cpn.translator;

import br.cin.ufpe.nesc2cpn.nescModule.creator.AssignCreator;
import br.cin.ufpe.nesc2cpn.nescModule.creator.CaseCreator;
import br.cin.ufpe.nesc2cpn.nescModule.creator.CreatorFactory;
import br.cin.ufpe.nesc2cpn.nescModule.creator.OperationCreator;
import br.cin.ufpe.nesc2cpn.nescModule.instructions.DoWhile;
import br.cin.ufpe.nesc2cpn.nescModule.instructions.For;
import br.cin.ufpe.nesc2cpn.nescModule.instructions.IfElse;
import br.cin.ufpe.nesc2cpn.nescModule.instructions.Function;
import br.cin.ufpe.nesc2cpn.nescModule.instructions.Switch;
import br.cin.ufpe.nesc2cpn.nescModule.instructions.While;
import java.util.HashMap;

/**
 *
 * @author avld
 */
public class TranslatorMain
{

    public static Function getMethodTest()
    {
        Function method = new Function();       //event void AMSender.sendDone()
        method.setFunctionType("event");
        method.setReturnType("void");
        method.setInterfaceName("AMSender");
        method.setFunctionName("sendDone");

        method.getArguments().put("error", "error_t");
        method.getArguments().put("payload", "void*");

        //method.getInstructions().add( new Variable("int8_t value = 10;") );

        //method.getInstructions().add( getSwitch() );
        method.getInstructions().add( getIfElse() );
        //method.getInstructions().add( getWhile() );
        //method.getInstructions().add( getDoWhile() );
        //method.getInstructions().add( getFor() );
        //method.getInstructions().add( getWhile() );

        //method.getInstructions().add( new Variable("int8_t value = 10;") );

        System.out.println( method.toString() );

        System.out.println(" // ----------------------- // ");
        System.out.println(" // ----------------------- // ");

        return method;
    }

    private static Switch getSwitch()
    {
        Switch switchInst = new Switch();
        switchInst.setValue( "value" );
        switchInst.getInstructions().add( new CaseCreator().convertTo("case 1: value = 1; break;" ) );
        switchInst.getInstructions().add( new CaseCreator().convertTo("case 2: value = 2; break;" ) );
        switchInst.getInstructions().add( new CaseCreator().convertTo("case 3: value = 3; break;" ) );
        switchInst.getInstructions().add( new CaseCreator().convertTo("case 4: value = 4; break;" ) );
        
        return switchInst;
    }

    private static IfElse getIfElse()
    {
        IfElse ifElse = new IfElse();
        ifElse.setCondition( new OperationCreator().convertTo("value == 10") );
        ifElse.setProbability( 0.3 );
        ifElse.getInstructions().add( new AssignCreator().convertTo("value = 10 + 10;") );
        ifElse.setElses( new HashMap<Long, IfElse>() );

        IfElse ifElseIfElse = new IfElse();
        //ifElseIfElse.setCondition("value == 11");
        ifElseIfElse.setProbability( 0.25 );
        ifElseIfElse.getInstructions().add( new AssignCreator().convertTo("value = 10 + 10;") );
        ifElse.getElses().put( (long) 0 , ifElseIfElse );

        IfElse ifElseElse = new IfElse();
        ifElseElse.setProbability( 0.45 );
        ifElseElse.getInstructions().add( new AssignCreator().convertTo("value = 10 + 10;") );
        ifElse.getElses().put( (long) 1, ifElseElse );

        return ifElse;
    }

    private static For getFor()
    {
        For forInst = new For();
        forInst.setPart01( CreatorFactory.getInstance().convertToSimple("int value = 0;") );
        forInst.setPart02( CreatorFactory.getInstance().convertToSimple("value <= 10") );
        forInst.setPart03( CreatorFactory.getInstance().convertToSimple("value++") );
        forInst.getInstructions().add( new AssignCreator().convertTo("value = 11 + 11;") );
        forInst.setInterationNumber( 10 );

        return forInst;
    }

    private static While getWhile()
    {
        While whileInst = new While();
        whileInst.setProbability( 0.6 );
        //whileInst.setCondition( "value == 10" );
        whileInst.getInstructions().add( new AssignCreator().convertTo("value = 11 + 11;") );

        return whileInst;
    }

    private static DoWhile getDoWhile()
    {
        DoWhile doWhileInst = new DoWhile();
        //doWhileInst.setCondition( "value == 10" );
        doWhileInst.setProbability( 0.6 );
        doWhileInst.getInstructions().add( new AssignCreator().convertTo("value = 11 + 11;") );

        return doWhileInst;
    }

}
