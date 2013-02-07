/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.cin.ufpe.nesc2cpn.translator.nodeCreator;

import br.cin.ufpe.nesc2cpn.nescModule.instructions.DoWhile;
import br.cin.ufpe.nesc2cpn.nescModule.instructions.Instruction;
import br.cin.ufpe.nesc2cpn.translator.node.DoWhileNode;
import br.cin.ufpe.nesc2cpn.translator.node.TranslatorNode;

/**
 *
 * @author avld
 */
public class DoWhileNodeCreator extends ComposedNodeCreator<DoWhile>
{

    public DoWhileNodeCreator( boolean reduction )
    {
        super( reduction );
    }

    @Override
    public boolean identify(Instruction inst)
    {
        return inst instanceof DoWhile;
    }

    @Override
    public TranslatorNode convertTo(DoWhile inst) throws Exception
    {
        DoWhileNode node = getRepository( inst );
        node.setBlockNode( convertToNodeList( (DoWhile) inst ) );
        
        return node;
    }

    public DoWhileNode getRepository(DoWhile inst) throws Exception
    {
        DoWhileNode node = new DoWhileNode();
        //node.setCondition( convertConditionToList( inst.getCondition() ) );
        node.setProbability( inst.getProbability() );

        return node;
    }
}
