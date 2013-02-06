package br.cin.ufpe.nesc2cpn.translator.node;

import br.cin.ufpe.nesc2cpn.repository.Line;

/**
 *
 * @author avld
 */
public class ForNode extends ComposedNode {
    private Line assign;
    private Line condition;
    private Line plus;
    private int interationNumber;

    public ForNode()
    {

    }

    public Line getAssign() {
        return assign;
    }

    public void setAssign(Line assign) {
        this.assign = assign;
    }

    public Line getCondition() {
        return condition;
    }

    public void setCondition(Line condition) {
        this.condition = condition;
    }

    public int getInterationNumber() {
        return interationNumber;
    }

    public void setInterationNumber(int interationNumber) {
        this.interationNumber = interationNumber;
    }

    public Line getPlus() {
        return plus;
    }

    public void setPlus(Line plus) {
        this.plus = plus;
    }
    
}
