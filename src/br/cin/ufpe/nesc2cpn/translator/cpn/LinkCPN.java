package br.cin.ufpe.nesc2cpn.translator.cpn;

import br.cin.ufpe.nesc2cpn.cpnModule.Arc;
import br.cin.ufpe.nesc2cpn.cpnModule.Place;
import br.cin.ufpe.nesc2cpn.cpnModule.Trans;

/**
 *
 * @author avld
 */
public class LinkCPN extends SimpleCPN
{
    
    public LinkCPN( Place place , Trans trans )
    {
        Arc arc =  initArc( place , trans );
        arc.setOrientation( Arc.TransitionToPlace );
    }

}
