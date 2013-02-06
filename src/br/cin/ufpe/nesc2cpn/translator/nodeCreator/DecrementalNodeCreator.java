package br.cin.ufpe.nesc2cpn.translator.nodeCreator;

import br.cin.ufpe.nesc2cpn.nescModule.instructions.Decremental;
import br.cin.ufpe.nesc2cpn.nescModule.instructions.Incremental;
import br.cin.ufpe.nesc2cpn.nescModule.instructions.Instruction;
import br.cin.ufpe.nesc2cpn.repository.Line;
import br.cin.ufpe.nesc2cpn.repository.RepositoryControl;
import br.cin.ufpe.nesc2cpn.translator.node.TranslatorNode;

/**
 *
 * @author avld
 */
public class DecrementalNodeCreator extends NodeCreator<Decremental>
{

    public DecrementalNodeCreator()
    {
        // do nothing
    }

    @Override
    public boolean identify(Instruction inst)
    {
        return inst instanceof Decremental;
    }

    @Override
    public TranslatorNode convertTo(Decremental inst) throws Exception
    {
        return getRepository( inst );
    }

    private TranslatorNode getRepository(Decremental inst) throws Exception
    {
        String assignType = inst.getVariableType();
        Line line = RepositoryControl.getInstance().get( BASIC , DECREMENTAL , assignType );

        TranslatorNode node = new TranslatorNode();
        node.addLine( line );

        return node;
    }
}
