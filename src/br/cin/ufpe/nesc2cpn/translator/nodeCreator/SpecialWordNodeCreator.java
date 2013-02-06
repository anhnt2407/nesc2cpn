package br.cin.ufpe.nesc2cpn.translator.nodeCreator;

import br.cin.ufpe.nesc2cpn.nescModule.instructions.Instruction;
import br.cin.ufpe.nesc2cpn.nescModule.instructions.SpecialWord;
import br.cin.ufpe.nesc2cpn.repository.Line;
import br.cin.ufpe.nesc2cpn.translator.node.TranslatorNode;

/**
 *
 * @author avld
 */
public class SpecialWordNodeCreator extends NodeCreator<SpecialWord>
{

    public SpecialWordNodeCreator()
    {
        // do nothing
    }

    @Override
    public boolean identify(Instruction inst)
    {
        return inst instanceof SpecialWord;
    }

    @Override
    public TranslatorNode convertTo(SpecialWord inst) throws Exception
    {
        return getRepository( inst );
    }

    public TranslatorNode getRepository(SpecialWord inst) throws Exception
    {
        Line line = new Line();
        line.setModuleName( "*basic*" );
        line.setInterfaceName( "SpecialWord" );
        line.setMethodName( inst.getWord() );

        TranslatorNode node = new TranslatorNode();
        node.addLine( line );

        return node;
    }
}
