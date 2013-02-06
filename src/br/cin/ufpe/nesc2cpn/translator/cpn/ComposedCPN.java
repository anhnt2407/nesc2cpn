package br.cin.ufpe.nesc2cpn.translator.cpn;

import br.cin.ufpe.nesc2cpn.cpnModule.Arc;
import br.cin.ufpe.nesc2cpn.cpnModule.Place;
import br.cin.ufpe.nesc2cpn.cpnModule.Trans;
import br.cin.ufpe.nesc2cpn.translator.node.DoWhileNode;
import br.cin.ufpe.nesc2cpn.translator.node.ForNode;
import br.cin.ufpe.nesc2cpn.translator.node.IfElseNode;
import br.cin.ufpe.nesc2cpn.translator.node.SwitchNode;
import br.cin.ufpe.nesc2cpn.translator.node.TranslatorNode;
import br.cin.ufpe.nesc2cpn.translator.node.WhileNode;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author avld
 */
public class ComposedCPN extends SimpleCPN
{
    private Position pos;
    private Position childrenPos;
    
    private List<SimpleCPN> cpnItemList;

    public ComposedCPN()
    {
        cpnItemList = new ArrayList<SimpleCPN>();
    }

    // ------------------------------------------ //
    // ------------------------------------------ //
    // ------------------------------------------ //

    protected int calculePosition( Position pos )
    {
        return 0;
    }

    protected void tratarInstructions( TranslatorNode root )
    {
        TranslatorNode node = root.getNextNode();

        if( childrenPos == null )
        {
            childrenPos = new Position( pos.getX() + DISTANCE
                                      , pos.getY() - DISTANCE * 2 );
        }

        while( node != null )
        {
            SimpleCPN newCpnItem = convertTo( node );

            if( !cpnItemList.isEmpty() )
            {
                Place place = newCpnItem.getPlaceFirst();

                SimpleCPN beforeCpnItem = cpnItemList.get( cpnItemList.size() - 1 );
                beforeCpnItem.createLink( place );
            }

            cpnItemList.add( newCpnItem );

            childrenPos.setY( newCpnItem.getEndY() - DISTANCE );
            
            setEndY( newCpnItem.getEndY() );
            setMaxX( newCpnItem.getMaxX() );

            if( getMaxY() < newCpnItem.getMaxY() )
            {
                setMaxY( newCpnItem.getMaxY() );
            }

            if( getMinY() > newCpnItem.getMinY() )
            {
                setMinY( newCpnItem.getMinY() );
            }

            node = node.getNextNode();
        }

        setStartX( getPos().getX() );
        setEndX( getPos().getX() );
        setMinX( getPos().getX() );
    }

    protected SimpleCPN convertTo( TranslatorNode node )
    {
        if( node instanceof IfElseNode )
        {
            return new IfElseCPN( (IfElseNode) node, childrenPos );
        }
        else if( node instanceof ForNode )
        {
            return new ForCPN( (ForNode) node , childrenPos );
        }
        else if( node instanceof WhileNode )
        {
            return new WhileCPN( (WhileNode) node , childrenPos );
        }
        else if( node instanceof DoWhileNode )
        {
            return new DoWhileCPN( (DoWhileNode) node , childrenPos );
        }
        else if( node instanceof SwitchNode )
        {
            return new SwitchCPN( (SwitchNode) node , childrenPos );
        }
        else if( node instanceof TranslatorNode )
        {
            return new OperatorCPN( node , childrenPos );
        }

        return new NothingCPN( pos );
    }

    // ------------------------------------------ //
    // ------------------------------------------ //
    // ------------------------------------------ //

    @Override
    public List<Arc> getArcList() {
        List<Arc> lista = new ArrayList<Arc>();
        lista.addAll( super.getArcList() );

        for( TranslatorCPN item : getCpnItemList() )
        {
            lista.addAll( item.getArcList() );
        }

        return lista;
    }

    @Override
    public List<Place> getPlaceList() {
        List<Place> lista = new ArrayList<Place>();
        lista.addAll( super.getPlaceList() );

        for( TranslatorCPN item : getCpnItemList() )
        {
            lista.addAll( item.getPlaceList() );
        }

        return lista;
    }

    @Override
    public List<Trans> getTransList() {
        List<Trans> lista = new ArrayList<Trans>();
        lista.addAll( super.getTransList() );

        for( TranslatorCPN item : getCpnItemList() )
        {
            lista.addAll( item.getTransList() );
        }

        return lista;
    }

    @Override
    public List<String> getVariableUsed() {
        List<String> list = new ArrayList<String>();
        list.addAll( super.getVariableUsed() );

        for( SimpleCPN item : cpnItemList )
        {
            list.addAll( item.getVariableUsed() );
        }

        return list;
    }

    // ------------------------------------------ //
    // ------------------------------------------ //
    // ------------------------------------------ //

    public Position getChildrenPos() {
        return childrenPos;
    }

    public void setChildrenPos(Position childrenPos) {
        this.childrenPos = childrenPos;
    }

    public List<SimpleCPN> getCpnItemList() {
        return cpnItemList;
    }

    public void setCpnItemList(List<SimpleCPN> cpnItemList) {
        this.cpnItemList = cpnItemList;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;

        setStartX( pos.getX() );
        setStartY( pos.getY() );
    }

}