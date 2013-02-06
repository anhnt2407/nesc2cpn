package br.cin.ufpe.nesc2cpn.translator.nodeCreator;

import br.cin.ufpe.nesc2cpn.nescModule.instructions.Instruction;
import br.cin.ufpe.nesc2cpn.nescModule.instructions.Function;
import br.cin.ufpe.nesc2cpn.repository.Line;
import br.cin.ufpe.nesc2cpn.translator.node.TranslatorNode;

/**
 *
 * @author avld
 */
public class FunctionNodeCreator extends ComposedNodeCreator<Function>
{

    public FunctionNodeCreator()
    {
        // do nothing
    }

    @Override
    public boolean identify(Instruction inst)
    {
        return inst instanceof Function;
    }

    @Override
    public TranslatorNode convertTo(Function method) throws Exception
    {
        Line line = new Line();
        line.setInterfaceName( method.getInterfaceName() );
        line.setMethodName( method.getFunctionName() );

        TranslatorNode node = convertToNodeList( method );
        node.addLine( line );

        return node;
    }

}
