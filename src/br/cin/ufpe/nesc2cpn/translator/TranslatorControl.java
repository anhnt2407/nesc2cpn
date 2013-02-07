package br.cin.ufpe.nesc2cpn.translator;

import br.cin.ufpe.nesc2cpn.Nesc2CpnProperties;
import br.cin.ufpe.nesc2cpn.cpnModule.CPN;
import br.cin.ufpe.nesc2cpn.cpnModule.CPNXML;
import br.cin.ufpe.nesc2cpn.cpnModule.IndexNode.IndexNode;
import br.cin.ufpe.nesc2cpn.cpnModule.Page;
import br.cin.ufpe.nesc2cpn.cpnModule.Place;
import br.cin.ufpe.nesc2cpn.cpnModule.Trans;
import br.cin.ufpe.nesc2cpn.cpnModule.globbox.BlockItem;
import br.cin.ufpe.nesc2cpn.cpnModule.instances.InstanceItens;
import br.cin.ufpe.nesc2cpn.cpnModule.monitorblock.Monitor;
import br.cin.ufpe.nesc2cpn.cpnModule.monitorblock.MonitorBlock;
import br.cin.ufpe.nesc2cpn.nescModule.instructions.Function;
import br.cin.ufpe.nesc2cpn.repository.RepositoryControl;
import br.cin.ufpe.nesc2cpn.translator.cpn.SimpleCPN;
import br.cin.ufpe.nesc2cpn.translator.node.TranslatorNode;
import br.cin.ufpe.nesc2cpn.translator.nodeCreator.NodeCreatorFactory;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 *
 * @author avld
 */
public class TranslatorControl
{
    private Nesc2CpnProperties properties;

    private CPN cpn;
    private GlobboxMount globboxMount;

    private SchedulerPage scheduler;
    private List<Trans> appFunctionList;
    private List<SimpleCPN> simpleCpnList;
    private Set<String> pageNameSet;

    public TranslatorControl(Nesc2CpnProperties properties)
    {
        this.properties = properties;
        init();

//        String moldingType = System.getProperties().getProperty( "nesc2cpn.moldingType" );
//        this.translatorAFunction = Nesc2CpnMain.MOLDING_TYPE_FUN.equals( moldingType );

//        System.setProperty( "nesc2cpn.moldingType" , isFuction
//                               ? Nesc2CpnMain.MOLDING_TYPE_FUN
//                               : Nesc2CpnMain.MOLDING_TYPE_APP );
    }

    private void init()
    {
        globboxMount = new GlobboxMount();
        scheduler = new SchedulerPage( properties );
        appFunctionList = new ArrayList<Trans>();
        simpleCpnList = new ArrayList<SimpleCPN>();

        pageNameSet = new HashSet<String>();

        cpn = new CPN();
        cpn.setIndexNode( new IndexNode() );
        cpn.setMonitors( new MonitorBlock() );
        cpn.setInstances( new ArrayList<InstanceItens>() );
    }

    private void createSchedulerPage()
    {
        Page page = scheduler.getPage( appFunctionList );

        List<Page> pageList = new ArrayList<Page>();
        pageList.add( page );
        pageList.addAll( cpn.getPage() );

        cpn.setPage( pageList );

        cpn.getInstances().add( scheduler.getInstance() );

        long transID = scheduler.getSchendulerTrans().getId();
        long pageID = scheduler.getInstance().getInstanceId();

        Monitor monitor = globboxMount.monitorBreakPoint( transID , pageID );
        cpn.getMonitors().getMonitor().add( monitor );
        //cpn.getInstances().add( new InstanceItens( page.getId() ) );
    }

    public void createPage( List<Function> functionList ) throws Exception
    {
        createEventIdMap( functionList );

        for( Function function : functionList )
        {
            createPage( function );
        }
    }

    public void createPage( Function function ) throws Exception
    {
        TranslatorNode node = NodeCreatorFactory.convertTo( function );

        String pageName = TranslatorToCPN.getPageName( function , pageNameSet );

        TranslatorToCPN functTranslator = new TranslatorToCPN( cpn );
        functTranslator.createPage( pageName , node );
        
        simpleCpnList.addAll( functTranslator.getCpnItemList() );

        //Colocar apenas eventos na Scheduler Page
        if( !properties.isCreateApplicationModel()
                || ( function.getFunctionType() == null
                    ? false
                    : Function.EVENT.equals( function.getFunctionType() )
                   )
          )
        {
            int id = appFunctionList.size() + 1;
            Place in = scheduler.getInPlace();
            Place out = scheduler.getOutPlace();

            Trans trans = functTranslator.getTransScheduler( id , in , out );
            appFunctionList.add( trans );
        }
        
        //List<Object> globList = globboxMount.addGlobbox( getCpnItemList() );
        //cpn.getGlobbox().getListGlobBox().add( globList );
    }

    private void createEventIdMap( List<Function> functionList ) throws Exception
    {
        Map<Function, Long> eventMap = new HashMap<Function, Long>();

        for( Function function : functionList )
        {
            //Colocar apenas eventos na Scheduler Page
            if( !properties.isCreateApplicationModel()
                    || ( function.getFunctionType() == null
                        ? false
                        : Function.EVENT.equals( function.getFunctionType() )
                       )
              )
            {
                long id = eventMap.size() + 1;
                eventMap.put( function , id );
            }
        }

        EventId.getInstance().setLineList( RepositoryControl.getInstance().list() );
        EventId.getInstance().setEventMap( eventMap );
    }

    public CPN createCPN()
    {
        List<BlockItem> globList = globboxMount.addGlobbox( simpleCpnList );
        cpn.setGlobbox( globList );

        createSchedulerPage();

        return cpn;
    }

    public CPN getCPN()
    {
        return cpn;
    }
    
    public String getCpnString() throws Exception
    {
        StringBuilder builder = new StringBuilder();
        builder.append( "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n" );
        builder.append( CPNXML.convertTo( getCPN() ) );
        
        return builder.toString();
    }

    public void saveInFile(String name) throws Exception
    {
        FileWriter writer = new FileWriter( name );
        //writer.write( "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n" );
//        writer.write( "<!DOCTYPE workspaceElements PUBLIC "
//                + "\"-//CPN//DTD CPNXML 1.0//EN\" "
//                + "\"http://www.daimi.au.dk/~cpntools/bin/DTD/6/cpn.dtd\">\n\n" );
        //writer.write( CPNXML.convertTo( getCPN() ) );
        writer.write( getCpnString() );
        writer.close();
    }

}
