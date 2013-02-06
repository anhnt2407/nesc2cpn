package br.cin.ufpe.nesc2cpn.translator.nodeCreator;

import br.cin.ufpe.nesc2cpn.nescModule.instructions.Incremental;
import br.cin.ufpe.nesc2cpn.nescModule.instructions.Instruction;
import br.cin.ufpe.nesc2cpn.repository.Line;
import br.cin.ufpe.nesc2cpn.repository.RepositoryControl;
import br.cin.ufpe.nesc2cpn.translator.node.TranslatorNode;

/**
 *
 * @author avld
 */
public class IncrementalNodeCreator extends NodeCreator<Incremental>
{

    public IncrementalNodeCreator()
    {
        // do nothing
    }

    @Override
    public boolean identify(Instruction inst)
    {
        return inst instanceof Incremental;
    }

    @Override
    public TranslatorNode convertTo(Incremental inst) throws Exception
    {
        return getRepository( inst );
    }

    private TranslatorNode getRepository(Incremental inst) throws Exception
    {
        String assignType = inst.getVariableType();

        assignType = "int8_t";

        Line line = RepositoryControl.getInstance().get( BASIC , INCREMENTAL , assignType );

        TranslatorNode node = new TranslatorNode();
        node.addLine( line );

        return node;
    }
}
