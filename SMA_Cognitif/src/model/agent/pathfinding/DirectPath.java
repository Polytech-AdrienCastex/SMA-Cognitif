package model.agent.pathfinding;

import model.environment.AgentSystem;
import model.environment.Case;
import model.general.Vector2D;

/**
 *
 * @author Adrien
 */
public class DirectPath implements PathFinding
{
    @Override
    public Case getNextCase(AgentSystem as, Case currentCase, Case destinationCase)
    {
        Vector2D v = destinationCase.getLocation().sub(currentCase.getLocation()).sign();

        if(v.x != 0)
            v = new Vector2D(v.x, 0);
        else
            v = new Vector2D(0, v.y);

        v = v.add(currentCase.getLocation());

        return as.getGrid().getCase(v);
    }
    
}
