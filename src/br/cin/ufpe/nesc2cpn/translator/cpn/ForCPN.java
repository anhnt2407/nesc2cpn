package br.cin.ufpe.nesc2cpn.translator.cpn;

import br.cin.ufpe.nesc2cpn.cpnModule.Arc;
import br.cin.ufpe.nesc2cpn.cpnModule.CPNItem;
import br.cin.ufpe.nesc2cpn.cpnModule.Place;
import br.cin.ufpe.nesc2cpn.cpnModule.Trans;
import br.cin.ufpe.nesc2cpn.repository.Line;
import br.cin.ufpe.nesc2cpn.translator.node.ForNode;
import java.util.List;

/**
 *
 * @author avld
 */
public class ForCPN extends ComposedCPN
{
    private String variableName;

    private Place placePart01;
    private Trans transPart01;
    
    private Place placePart02;
    private Trans transPart02;

    private Place placePart03;
    private Trans transPart03;
    

    /**
     * 
     * @param node
     * @param pos
     */
    public ForCPN( ForNode node , Position pos )
    {
        setPos( pos );

        createVariable();
        
        initPart01( node , pos );
        initPart03( node , pos );
        initPart02( node , pos );
        linkParts();

        setChildrenPos( new Position( getPos().getX() + DISTANCE
                                    , getPos().getY() - DISTANCE * 6 ) );

        tratarInstructions( node.getBlockNode() );

        // --------------------

        List<SimpleCPN> lista = getCpnItemList();
        Place place = lista.get( 0 ).getPlaceFirst();
        lista.add( new LinkCPN( place , transPart03 ) );

        Trans trans = lista.get( 0 ).getTransLast();
        lista.add( new LinkCPN( placePart02 , trans ) );
    }

    private void createVariable()
    {
        variableName = "For" + idControl.getForNextId() + "Counter";
        addVariableUsed( variableName );
    }

    private void initPart01( ForNode node , Position pos )
    {
        placePart01 = initPlace( pos.getX() , pos.getY() );
        placePart01.setText( placePart01.getText() + "-FOR" );

        transPart01 = initTrans( pos.getX() , pos.getY() - DISTANCE );
        transPart01.setText( transPart01.getText() + "-FOR" );
        transPart01.setCode( getCodeAddEnergy( node.getAssign() ) );

        initArc( placePart01 , transPart01 );
    }

    private void initPart02( ForNode node , Position pos )
    {
        placePart02 = initPlace( pos.getX() , pos.getY() - DISTANCE * 2 );
        placePart02.setText( placePart02.getText() + "-FOR" );

        transPart02 = initTrans( pos.getX() , pos.getY() - DISTANCE * 3 );
        transPart02.setText( transPart02.getText() + "-FOR" );
        transPart02.setCode( getCodePart02( node ) );

        initArc( placePart02 , transPart02 );
    }

    private void initPart03( ForNode node , Position pos )
    {
        placePart03 = initPlace( pos.getX() + DISTANCE , pos.getY() - DISTANCE * 4 );
        placePart03.setText( placePart03.getText() + "-FOR" );

        transPart03 = initTrans( pos.getX() + DISTANCE , pos.getY() - DISTANCE * 5 );
        transPart03.setText( transPart03.getText() + "-FOR" );
        transPart03.setCode( getCodePart03( node.getPlus() ) );
        
        initArc( placePart03 , transPart03 );
    }

    private void linkParts()
    {
        Arc arc01 = initArc( placePart02 , transPart01 );
        arc01.setOrientation( Arc.TransitionToPlace );

        // ----------------------

        Arc arc02 = initArc( placePart03 , transPart02 );
        arc02.setOrientation( Arc.TransitionToPlace );
        arc02.getAnnot().setText( "if( n = 1 )\n then 1`i\n else empty" );
    }

    // --------------------------------------------- //
    // --------------------------------------------- //
    // --------------------------------------------- //

    private CPNItem getCodePart02(ForNode node)
    {
        int max = node.getInterationNumber();

        if( max < 0 )
        {
            max = 0;
        }
        
        StringBuilder builder = new StringBuilder();
        builder.append( "input(i);\n" );
        builder.append( "output(n);\n" );
        builder.append( "action(\n" );
        builder.append( getCode( node.getCondition() ) );
        builder.append(";\n  if( (!").append(variableName).append(") < ").append(max).append( " ) then 1\n");
        builder.append( "  else 0\n" );
        builder.append( " );" );

        return new CPNItem( builder.toString() );
    }
    
    private CPNItem getCodePart03(Line line)
    {
        StringBuilder builder = new StringBuilder();
        builder.append( "action(\n" );
        builder.append("  if( (!").append(variableName).append(") <= 0 ) then ").append(variableName).append( " := 1\n");
        builder.append( "  else (\n     ");
        builder.append( getCode( line ) );
        builder.append(";\n     ").append(variableName).append(" := (!").append(variableName).append( ") + 1)\n");
        builder.append( " );" );

        return new CPNItem( builder.toString() );
    }

    // --------------------------------------------- //
    // --------------------------------------------- //
    // --------------------------------------------- //

    @Override
    public Arc createLink( Place place )
    {
        Arc arc = super.createLink( place );
        arc.getAnnot().setText( "if n = 0\n then 1`i\n else empty" );

        return arc;
    }

}
