package br.cin.ufpe.nesc2cpn.translator.nodeCreator;

import br.cin.ufpe.nesc2cpn.nescModule.instructions.Instruction;
import br.cin.ufpe.nesc2cpn.nescModule.instructions.Invoke;
import br.cin.ufpe.nesc2cpn.repository.Line;
import br.cin.ufpe.nesc2cpn.repository.RepositoryControl;
import br.cin.ufpe.nesc2cpn.translator.node.TranslatorNode;

/**
 *
 * @author avld
 */
public class InvokeNodeCreator extends NodeCreator<Invoke>
{

    public InvokeNodeCreator()
    {
        
    }

    @Override
    public boolean identify(Instruction inst)
    {
        return inst instanceof Invoke;
    }

    @Override
    public TranslatorNode convertTo(Invoke inst) throws Exception
    {
        return getRepository( (Invoke) inst );
    }

    private TranslatorNode getRepository(Invoke inst) throws Exception
    {
//        System.out.print( "invoke: " + inst.getMethod().getModuleName()  );
//        System.out.print( "." + inst.getMethod().getInterfaceName()  );
//        System.out.print( "[as " + inst.getMethod().getInterfaceNick() + "]"  );
//        System.out.println( "." + inst.getMethod().getFunctionName()  );

        Line line = RepositoryControl.getInstance().get( inst.getMethod().getModuleName()
                                                       , inst.getMethod().getInterfaceName()
                                                       , inst.getMethod().getFunctionName() );
        line.setInterfaceNick( inst.getMethod().getInterfaceNick() );

        TranslatorNode node = new TranslatorNode();
        node.addLine( line );

        return node;
    }
}
