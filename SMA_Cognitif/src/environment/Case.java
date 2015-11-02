package environment;

import agent.Agent;
import general.Vector2D;

/**
 *
 * @author p1002239
 */
public class Case
{
    public Case(Vector2D location)
    {
        this.location = location;
        this.currentAgent = null;
    }
    
    private Agent currentAgent;
    private final Vector2D location;
    
    public Vector2D getLocation()
    {
        return location;
    }
    
    public boolean isEmpty()
    {
        return currentAgent == null;
    }
    public synchronized boolean setAgent(Agent agent)
    {
        if(!isEmpty())
            return false;
        
        Case oldCase = agent.getCurrentCase();
        this.currentAgent = agent;
        agent.setCurrentCase(this);
        oldCase.currentAgent = null;
        
        return true;
    }
}
