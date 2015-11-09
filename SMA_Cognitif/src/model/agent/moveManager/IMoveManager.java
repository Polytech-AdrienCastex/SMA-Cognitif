package model.agent.moveManager;

import model.environment.Case;

/**
 * Created by benoitvuillemin on 09/11/2015.
 */
public interface IMoveManager {
    public Case getNextMove(Case currentCase);
    public void confirmMove(Case c);
}
