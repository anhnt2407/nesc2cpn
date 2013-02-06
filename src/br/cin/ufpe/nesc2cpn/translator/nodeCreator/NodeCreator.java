/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.cin.ufpe.nesc2cpn.translator.nodeCreator;

import br.cin.ufpe.nesc2cpn.nescModule.instructions.Instruction;
import br.cin.ufpe.nesc2cpn.translator.node.TranslatorNode;

/**
 *
 * @author avld
 */
public abstract class NodeCreator<type>
{
    public static final String BASIC = "$basic";
    public static final String ASSIGN = "assign";
    public static final String POINTER = "pointer";
    public static final String OPERATION = "operation";
    public static final String PARENTESE = "parentese";
    public static final String CAST = "cast";
    public static final String INCREMENTAL = "incremental";
    public static final String DECREMENTAL = "decremental";

    public static final String DEVICE = "device";
    public static final String RADIO = "radio";
    public static final String LED0 = "led0";
    public static final String LED1 = "led1";
    public static final String LED2 = "led2";

    public NodeCreator()
    {
        
    }

    public abstract boolean identify(Instruction inst);
    public abstract TranslatorNode convertTo(type inst) throws Exception;
}
