package br.cin.ufpe.nesc2cpn.translator.cpn;

import br.cin.ufpe.nesc2cpn.cpnModule.Arc;
import br.cin.ufpe.nesc2cpn.cpnModule.CPNItem;
import br.cin.ufpe.nesc2cpn.cpnModule.Place;
import br.cin.ufpe.nesc2cpn.cpnModule.Trans;
import br.cin.ufpe.nesc2cpn.repository.Line;
import br.cin.ufpe.nesc2cpn.translator.EventId;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author avld
 */
public class SimpleCPN extends TranslatorCPN
{
    private List<String> variableUsed;
    
    public SimpleCPN()
    {
        variableUsed = new ArrayList<String>();
    }

    protected Place initPlace( int x , int y )
    {
        setStartX( x );
        setMinX( x );

        setStartY( y );
        setMinY( y );

        Place place = new Place();
        place.setId( idControl.getItemNextId() );
        place.setText( "place" + idControl.getPlaceNextId() );
        place.setPosattrX( x );
        place.setPosattrY( y );
        place.setInitmark( new CPNItem() );
        place.setType( new CPNItem("INT") );

        super.getPlaceList().add( place );

        return place;
    }

    protected Trans initTrans( int x , int y )
    {
        setEndX( x );
        setMaxX( x );

        setEndY( y );
        setMaxY( y );

        Trans trans = new Trans();
        trans.setId( idControl.getItemNextId() );
        trans.setText( "trans" + idControl.getTransNextId() );
        trans.setPosattrX( x );
        trans.setPosattrY( y );
        trans.setTime( null );
        trans.setCode( null );

        super.getTransList().add( trans );

        return trans;
    }

    protected Arc initArc( Place place , Trans trans )
    {
        Arc arc = new Arc();
        arc.setId( idControl.getItemNextId() );
        arc.setText( "arc" + idControl.getArcNextId() );
        arc.setOrientation( Arc.PlaceToTransition );
        arc.setTransendIdref( trans.getId() );
        arc.setPlaceendIdref( place.getId() );

        arc.setAnnot( new CPNItem("i") );
        arc.getAnnot().setPosattrX( 5 + ( place.getPosattrX() + trans.getPosattrX() ) / 2 );
        arc.getAnnot().setPosattrY( ( place.getPosattrY() + trans.getPosattrY() ) / 2 );

        if( place.getPosattrX() != trans.getPosattrX()
                && place.getPosattrY() != trans.getPosattrY() )
        {
            CPNItem bendpoint = new CPNItem();
            bendpoint.setPosattrX( trans.getPosattrX() );
            bendpoint.setPosattrY( place.getPosattrY() );

            arc.getBendpointList().add( bendpoint );
        }

        super.getArcList().add( arc );

        return arc;
    }

    public Arc createLink( Place place )
    {
        Trans trans = getTransLast();
        
        Arc arc =  initArc( place , trans );
        arc.setOrientation( Arc.TransitionToPlace );

        return arc;
    }

    // ------------------------ //
    // ------------------------ //
    // ------------------------ //

    public List<String> getVariableUsed() {
        return variableUsed;
    }

    public void addVariableUsed(String name){
        variableUsed.add( name );
    }

    // ------------------------ //
    // ------------------------ //
    // ------------------------ //

    public CPNItem getCodeAddEnergy( Line line )
    {
        CPNItem item = new CPNItem();
        item.setText("output( resp );\n"
                   + "action(\n" + getCode( line ) + "\n);");

        return item;
    }

    private String format(Double value)
    {
        String valueStr = value.toString();
        return valueStr.replace( '-' , '~' );
    }

    public CPNItem getCodeAddEnergy( List<Line> lineList )
    {
        StringBuilder builder = new StringBuilder();
        builder.append("output( resp );\n");
        builder.append("action(\n");

        Iterator<Line> it = lineList.iterator();
        while( it.hasNext() )
        {
            Line line = it.next();

            builder.append( getCode( line ) );

            if( it.hasNext() )
            {
                builder.append(";\n");
                builder.append("(* ---------------- *)\n");
            }
            
        }

        builder.append("\n);");

        CPNItem item = new CPNItem( builder.toString() );
        return item;
    }

    public String getCode(Line line)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("  addEnergy( ").append( format( line.getEnergyMean() ) )
               .append(" , ").append( format( line.getEnergyVariance() ) ).append(" );\n");
//        builder.append("  addPower( ").append( format( line.getPowerMean() ) )
//               .append(" , ").append( format( line.getPowerVariance() ) ).append(" );\n");
        builder.append("  addTime( ").append( format( line.getTimeMean() ) )
               .append(" , ").append( format( line.getTimeVariance() ) ).append(" )");
        
        if( line.isRadioStart() )
        {
            builder.append(";\n");
            builder.append( "  radioOn := true" );
        }

        if( line.isRadioStop() )
        {
            builder.append(";\n");
            builder.append( "  radioOn := false" );
        }

        if( line.isRadioOn() )
        {
            builder.append(";\n");
            builder.append( "  addRadioTime( (!timeInst) )" );
        }

        //TODO: quando for calcular o led
//        if( line.isLed0On() )
//        {
//            builder.append(";\n");
//            builder.append( "  addLed0Energy( (!timeInst) )" );
//        }
//
//        if( line.isLed1On() )
//        {
//            builder.append(";\n");
//            builder.append( "  addLed1Energy( (!timeInst) )" );
//        }
//
//        if( line.isLed2On() )
//        {
//            builder.append(";\n");
//            builder.append( "  addLed2Energy( (!timeInst) )" );
//        }

        if( !line.getEventSet().isEmpty() )
        {
            long id = EventId.getInstance().getEventId( line );
            
            if( id > 0 )
            {
                builder.append(";\n");
                builder.append( "  schedulerList := (ins (!schedulerList) " );
                builder.append( id );
                builder.append( " )" );
            }
        }

        builder.append(";\n");
        builder.append("  calcPower()");

        return builder.toString();
    }

    public CPNItem getCodeAddTime( Line line )
    {
        CPNItem item = new CPNItem();
        item.setText("@+addTime( "+ format( line.getTimeMean() ) +
                             " , "+ format( line.getTimeVariance() ) +" )");

        return item;
    }

    public CPNItem getCodeAddTime( List<Line> lineList )
    {
        StringBuilder builder = new StringBuilder();
        builder.append("@+");

        Iterator<Line> it = lineList.iterator();
        while( it.hasNext() )
        {
            Line line = it.next();

            builder.append("addTime( ").append( format( line.getTimeMean() ) )
                   .append(" , ").append( format( line.getTimeVariance() ) ).append(" )\n");
            
            if( it.hasNext() )
            {
                builder.append(" +");
            }
        }

        CPNItem item = new CPNItem( builder.toString() );

        return item;
    }
}
