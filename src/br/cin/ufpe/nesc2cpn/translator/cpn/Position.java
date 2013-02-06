package br.cin.ufpe.nesc2cpn.translator.cpn;

/**
 *
 * @author avld
 */
public class Position {
    public static final int LEFT = 0;
    public static final int RIGHT = 1;

    private int x;
    private int y;
    private int diretion;
    private boolean canModify;

    public Position()
    {
        this.canModify = true;
    }

    public Position( int x , int y )
    {
        this.x = x;
        this.y = y;
        this.canModify = true;
    }

    public Position( int x , int y , int diretion )
    {
        this.x = x;
        this.y = y;
        this.diretion = diretion;
        this.canModify = true;
    }

    public boolean isCanModify() {
        return canModify;
    }

    public void setCanModify(boolean canModify) {
        this.canModify = canModify;
    }

    public int getDiretion() {
        return diretion;
    }

    public void setDiretion(int diretion) {
        this.diretion = diretion;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
}
