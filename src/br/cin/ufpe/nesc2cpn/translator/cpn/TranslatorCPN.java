package br.cin.ufpe.nesc2cpn.translator.cpn;

import br.cin.ufpe.nesc2cpn.cpnModule.IDControl;
import br.cin.ufpe.nesc2cpn.cpnModule.Arc;
import br.cin.ufpe.nesc2cpn.cpnModule.Place;
import br.cin.ufpe.nesc2cpn.cpnModule.Trans;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author avld
 */
public class TranslatorCPN
{
    public static final int DISTANCE = 100;

    public IDControl idControl;

    private List<Place> placeList;
    private List<Trans> transList;
    private List<Arc> arcList;

    private int minX = 0;
    private int minY = 0;

    private int maxX = 0;
    private int maxY = 0;

    private int startX = 0;
    private int startY = 0;

    private int endX = 0;
    private int endY = 0;

    public TranslatorCPN()
    {
        idControl = IDControl.getInstance();

        placeList = new ArrayList<Place>();
        transList = new ArrayList<Trans>();
        arcList = new ArrayList<Arc>();
    }


    // ------------------------- //
    // ------------------------- //
    // ------------------------- //

    public List<Arc> getArcList() {
        return arcList;
    }

    public void setArcList(List<Arc> arcList) {
        this.arcList = arcList;
    }

    public List<Place> getPlaceList() {
        return placeList;
    }

    public void setPlaceList(List<Place> placeList) {
        this.placeList = placeList;
    }

    public List<Trans> getTransList() {
        return transList;
    }

    public void setTransList(List<Trans> transList) {
        this.transList = transList;
    }

    // ------------------------- //
    // ------------------------- //
    // ------------------------- //

    public Place getPlaceFirst() {
        return placeList.get( 0 );
    }

    public Trans getTransLast() {
        return transList.get( transList.size() - 1 );
    }

    // ------------------------- //
    // ------------------------- //
    // ------------------------- //

    public int getEndX() {
        return endX;
    }

    public void setEndX(int endX) {
        this.endX = endX;
    }

    public int getEndY() {
        return endY;
    }

    public void setEndY(int endY) {
        this.endY = endY;
    }

    public int getMaxX() {
        return maxX;
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public int getMinX() {
        return minX;
    }

    public void setMinX(int minX) {
        this.minX = minX;
    }

    public int getMinY() {
        return minY;
    }

    public void setMinY(int minY) {
        this.minY = minY;
    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

}
