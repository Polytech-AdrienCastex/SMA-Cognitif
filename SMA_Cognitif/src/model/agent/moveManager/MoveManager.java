package model.agent.moveManager;

import model.environment.Case;

import java.util.LinkedList;

/**
 * Created by benoitvuillemin on 09/11/2015.
 */
public class MoveManager implements IMoveManager {
    Case destination;
    LinkedList<Case> history;

    protected int compareCase(Case c1, Case c2)
    {
        int index_c1 = history.indexOf(c1);
        int index_c2 = history.indexOf(c2);
        index_c1 = index_c1 == -1 ? Integer.MAX_VALUE : index_c1;
        index_c2 = index_c2 == -1 ? Integer.MAX_VALUE : index_c2;
        return Integer.compare(index_c1, index_c2);
    }

    public Case getNextMove(Case currentCase)
    {
        return null;
    }

    public void confirmMove(Case c)
    {

    }
}
