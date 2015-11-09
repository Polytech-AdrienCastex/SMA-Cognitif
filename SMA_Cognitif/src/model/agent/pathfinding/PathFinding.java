package model.agent.pathfinding;

import model.environment.AgentSystem;
import model.environment.Case;

/**
 *
 * @author Adrien
 */
public interface PathFinding
{
    public Case getNextCase(AgentSystem as, Case currentCase, Case destinationCase);
}
