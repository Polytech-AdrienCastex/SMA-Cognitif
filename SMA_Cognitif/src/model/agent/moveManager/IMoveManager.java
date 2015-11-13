package model.agent.moveManager;

import model.environment.Case;

import java.util.Comparator;

/**
 * Created by benoitvuillemin on 09/11/2015.
 */
public interface IMoveManager extends Comparator<Case> {
    public void confirmMove(Case c);
}
