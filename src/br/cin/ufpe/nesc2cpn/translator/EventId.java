package br.cin.ufpe.nesc2cpn.translator;

import br.cin.ufpe.nesc2cpn.nescModule.instructions.Function;
import br.cin.ufpe.nesc2cpn.repository.Line;
import br.cin.ufpe.nesc2cpn.repository.RepositoryControl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author avld
 */
public class EventId
{
    private static EventId instance;
    private Map<Function,Long> eventMap;
    private List<Line> lineList;

    private EventId()
    {
        lineList = new ArrayList<Line>();
    }

    public static EventId getInstance()
    {
        if( instance == null )
        {
            instance = new EventId();
        }

        return instance;
    }

    public Map<Function, Long> getEventMap()
    {
        return eventMap;
    }

    public void setEventMap(Map<Function, Long> eventMap)
    {
        this.eventMap = eventMap;
    }

    public List<Line> getLineList()
    {
        return lineList;
    }

    public void setLineList(List<Line> lineList)
    {
        this.lineList = lineList;
    }

    /**
     * Encontrar o ID do evento na modelagem
     *
     * @param line
     * @return
     */
    public long getEventId(Line line)
    {
        Line lFound = getEventLine( line );

        if( lFound == null )
        {
            return 0;
        }

        lFound.setInterfaceNick( line.getInterfaceNick() );

        long id = 0;
        for( Map.Entry<Function,Long> entry : eventMap.entrySet() )
        {
            Function f = entry.getKey();

            if( !lFound.getModuleName().equals( f.getModuleName() ) )
            {
                continue ;
            }
            else if( !lFound.getInterfaceNick().equals( f.getInterfaceNick() ) )
            {
                continue ;
            }
            else if( lFound.getMethodName().equals( f.getFunctionName() ) )
            {
                id = entry.getValue();
                break;
            }
        }

        return id;
    }

    /**
     * Encontrar a linha relacionada ao evento
     *
     * @param line
     * @return
     */
    public Line getEventLine(Line line)
    {
        if( line.getEventSet().isEmpty() )
        {
            return null;
        }

        long id = line.getEventSet().iterator().next();

        Line lFound = null;
        //encontrar o evento no repositorio
        for( Line lr : getLineList() )
        {
            if( lr.getModuleName().equals( line.getModuleName() ) )
            {
                if( lr.getId() == id )
                {
                    lFound = lr;
                    break ;
                }
            }
        }

        return lFound;
    }


    private static Map<Function,Long> getMap()
    {
        Map<Function,Long> map = new HashMap<Function, Long>();

        Function f1 = new Function();
        f1.setModuleName( "ActiveMessageC" );
        f1.setInterfaceName( "SplitControl" );
        f1.setInterfaceNick( "AMControl" );
        f1.setFunctionName( "stopDone" );

        map.put( f1 , (long) 1 );

        Function f2 = new Function();
        f2.setModuleName( "ActiveMessageC" );
        f2.setInterfaceName( "SplitControl" );
        f2.setInterfaceNick( "AMControl" );
        f2.setFunctionName( "startDone" );

        map.put( f2 , (long) 2 );

        return map;
    }

    public static void main(String args[]) throws Exception
    {
        List<Line> lineList = RepositoryControl.getInstance().list();
        EventId.getInstance().setLineList( lineList );
        EventId.getInstance().setEventMap( getMap() );

        Line line = new Line();
        line.setModuleName( "ActiveMessageC" );
        line.setInterfaceName( "SplitControl" );
        line.setInterfaceNick( "AMControl" );
        line.setMethodName( "startDone" );
        line.getEventSet().add( (long) 4 );

        Line l = EventId.getInstance().getEventLine( line );
        System.out.println( "encontrou: " + l.getMethodName() );

        long id = EventId.getInstance().getEventId( line );
        System.out.println( "encontrou: " + id );
    }
}
