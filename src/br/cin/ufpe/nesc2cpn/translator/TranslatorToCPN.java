package br.cin.ufpe.nesc2cpn.translator;

import br.cin.ufpe.nesc2cpn.cpnModule.CPN;
import br.cin.ufpe.nesc2cpn.cpnModule.CPNItem;
import br.cin.ufpe.nesc2cpn.cpnModule.Page;
import br.cin.ufpe.nesc2cpn.cpnModule.Place;
import br.cin.ufpe.nesc2cpn.cpnModule.Trans;
import br.cin.ufpe.nesc2cpn.nescModule.instructions.Function;
import br.cin.ufpe.nesc2cpn.translator.cpn.ComposedCPN;
import br.cin.ufpe.nesc2cpn.translator.cpn.Position;
import br.cin.ufpe.nesc2cpn.translator.cpn.SimpleCPN;
import br.cin.ufpe.nesc2cpn.translator.node.TranslatorNode;
import java.util.Set;

/**
 *
 * @author avld
 */
public class TranslatorToCPN extends ComposedCPN
{
    private CPN cpn;

    private Page page;
    private Place inPlace;
    private Place outPlace;
    
    public TranslatorToCPN(CPN cpn)
    {
        this.cpn = cpn;
        
        setPos( new Position( 0 , 0 ) );
        setChildrenPos( new Position( 0 , 0 ) );
    }

    public Place getInPlace()
    {
        return inPlace;
    }

    public Place getOutPlace()
    {
        return outPlace;
    }

    public Trans getTransScheduler(int id, Place inSchedulerPlace , Place outSchedulerPlace )
    {
        Trans trans = new Trans( page.getPageattrName() );

        trans.setPortsockId( page.getId() );
        trans.addPortsock( inSchedulerPlace.getId() , inPlace.getId() );
        trans.addPortsock( outSchedulerPlace.getId() , outPlace.getId() );

        trans.setSubpageinfo( new CPNItem( page.getPageattrName() ) );

        // +++++++++++++++++++++++++++++++ // CONFIGURAR A PRIMEIRA TRANSICAO
        SimpleCPN lastCpnItem = getCpnItemList().get( 0 );

        Trans transFirst = lastCpnItem.getTransList().get( 0 );
        transFirst.setCond( new CPNItem("[ i = "+ id +" ]") );
        // +++++++++++++++++++++++++++++++ //

        return trans;
    }

    public Page createPage( String nome, TranslatorNode root )
    {
        page = new Page();
        page.setPageattrName( nome );

        getCpnItemList().clear();
        tratarInstructions( root );
        
        cpn.getPage().add( page );
        //cpn.getInstances().add( new InstanceItens( page.getId() ) );

        return page;
    }

    private Place createInPort()
    {
        SimpleCPN lastCpnItem = getCpnItemList().get( 0 );
        
        inPlace = lastCpnItem.getPlaceFirst();
        inPlace.setText( "in1" ); //in
        inPlace.setPortType( Place.PORT_TYPE_IN );

        return inPlace;
    }

    private Place createOutPort()
    {
        outPlace = new Place( "out" );

        outPlace.setPosattrX( 0 );
        outPlace.setPosattrY( getEndY() - DISTANCE );

        outPlace.setType( new CPNItem("INT") );
        outPlace.setPortType( Place.PORT_TYPE_OUT );

        SimpleCPN lastCpnItem = getCpnItemList().get( getCpnItemList().size() - 1 );
        lastCpnItem.createLink( outPlace );

        return outPlace;
    }

    @Override
    protected void tratarInstructions( TranslatorNode root )
    {
        if( root.getNextNode() == null )
        {
            root.setNextNode( new TranslatorNode() );
        }

        super.tratarInstructions( root );

        createInPort();
        page.getPlaces().add( createOutPort() );

        for( SimpleCPN item : getCpnItemList() )
        {
            page.getArcs().addAll( item.getArcList() );
            page.getPlaces().addAll( item.getPlaceList() );
            page.getTrans().addAll( item.getTransList() );
        }
    }

    public static String getPageName( Function function , Set<String> pageNameSet )
    {
        String name = getPageName( function );
        int counter = 0;

        String pageName = name;

        while( pageNameSet.contains( pageName ) )
        {
            counter++;
            pageName = name + counter;
        }

        pageNameSet.add( pageName );
        return pageName;
    }

    public static String getPageName( Function function )
    {
        StringBuilder builder = new StringBuilder();

        if( function.getInterfaceNick() == null
                ? false
                : !function.getInterfaceNick().isEmpty() )
        {
            builder.append( function.getInterfaceNick() );
            builder.append( "\n" );
        }

        builder.append( function.getFunctionName() );

        return builder.toString();
    }
}
