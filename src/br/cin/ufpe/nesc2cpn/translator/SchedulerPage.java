package br.cin.ufpe.nesc2cpn.translator;

import br.cin.ufpe.nesc2cpn.Nesc2CpnProperties;
import br.cin.ufpe.nesc2cpn.cpnModule.CPNItem;
import br.cin.ufpe.nesc2cpn.cpnModule.Page;
import br.cin.ufpe.nesc2cpn.cpnModule.Place;
import br.cin.ufpe.nesc2cpn.cpnModule.Trans;
import br.cin.ufpe.nesc2cpn.cpnModule.instances.InstanceItens;
import br.cin.ufpe.nesc2cpn.cpnModule.instances.TransItens;
import br.cin.ufpe.nesc2cpn.translator.cpn.EndCPN;
import br.cin.ufpe.nesc2cpn.translator.cpn.FucntionCPN;
import br.cin.ufpe.nesc2cpn.translator.cpn.SchedulerCPN;
import br.cin.ufpe.nesc2cpn.translator.cpn.SleepCPN;
import br.cin.ufpe.nesc2cpn.translator.cpn.StartCPN;
import java.util.List;

/**
 *
 * @author avld
 */
public class SchedulerPage
{
    private Page page;
    
    private Place inPlace;
    private Place outPlace;

    private InstanceItens instance;
    private Trans schendulerTrans;
    
    private Nesc2CpnProperties properties;

    public SchedulerPage(Nesc2CpnProperties properties)
    {
        this.properties = properties;
        init();
    }

    private void init()
    {
        getInPlace();
        getOutPlace();

        instance = new InstanceItens();
    }

    public InstanceItens getInstance()
    {
        return instance;
    }

    public Page getPage(List<Trans> transList)
    {
        page = new Page();
        page.setPageattrName( "scheduler" );

        page.getPlaces().add( getInPlace() );
        page.getPlaces().add( getOutPlace() );

        createSchedulerTrans();

        createStartTrans();
        createEndTrans();
        createSleepTrans();

        instance.setInstancePage( page.getId() );
        for( int i = 0; i < transList.size(); i++ )
        {
            Trans trans = transList.get( i );
            
            FucntionCPN funCPN = new FucntionCPN( trans , i , inPlace , outPlace );
            page.getTrans().add( trans );
            page.getArcs().addAll( funCPN.getArcList() );

            instance.getInstanceTrans().add( new TransItens( trans.getId() ) );
        }

        return page;
    }

    // ---------------------- //

    public void createSchedulerTrans()
    {
        SchedulerCPN schedulerCPN = new SchedulerCPN( getInPlace() , getOutPlace() );
        page.getTrans().addAll( schedulerCPN.getTransList() );
        page.getArcs().addAll( schedulerCPN.getArcList() );

        schendulerTrans = schedulerCPN.getTransLast();
    }

    public void createStartTrans()
    {
        boolean isApplication = properties.isCreateApplicationModel();
        
        System.out.println( "isFunction: " + isApplication );
        
        StartCPN startCPN = new StartCPN( getInPlace() , getOutPlace() , !isApplication );
        page.getTrans().addAll( startCPN.getTransList() );
        page.getArcs().addAll( startCPN.getArcList() );
    }

    public void createEndTrans()
    {
        boolean isApplication = properties.isCreateApplicationModel();
        
        EndCPN endCPN = new EndCPN( getInPlace() , getOutPlace() , !isApplication );
        page.getTrans().addAll( endCPN.getTransList() );
        page.getArcs().addAll( endCPN.getArcList() );
    }

    public void createSleepTrans()
    {
        SleepCPN sleepCPN = new SleepCPN( getInPlace() , getOutPlace() );
        page.getTrans().addAll( sleepCPN.getTransList() );
        page.getArcs().addAll( sleepCPN.getArcList() );
    }

    // ---------------------- //

    public Place getInPlace()
    {
        if( inPlace != null )
        {
            return inPlace;
        }
        
        inPlace = new Place( "in1" ); //in
        inPlace.setPosattrX( 0 );
        inPlace.setPosattrY( -100 );

        inPlace.setType( new CPNItem( "INT" ) );
        inPlace.setInitmark( new CPNItem("[~2]") );

        return inPlace;
    }

    public Place getOutPlace()
    {
        if( outPlace != null )
        {
            return outPlace;
        }

        outPlace = new Place( "out" );
        outPlace.setPosattrX( 0 );
        outPlace.setPosattrY( -300 );

        outPlace.setType( new CPNItem( "INT" ) );
        
        return outPlace;
    }

    public Trans getSchendulerTrans()
    {
        return schendulerTrans;
    }
}
