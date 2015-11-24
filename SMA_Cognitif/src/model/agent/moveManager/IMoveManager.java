package model.agent.moveManager;

import model.environment.Case;

import java.util.Comparator;

public interface IMoveManager extends Comparator<Case>
{
    public void confirmMove(Case c);
}
