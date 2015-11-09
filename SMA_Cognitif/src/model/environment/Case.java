package model.environment;

import java.util.Observable;
import model.agent.Agent;
import model.general.Vector2D;

/**
 *
 * @author p1002239
 */
public class Case extends Observable
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
        synchronized(this)
        {
            return currentAgent == null;
        }
    }
    public synchronized boolean setAgent(Agent agent)
    {
        if(!isEmpty())
            return false;

        synchronized(this)
        {
            Case oldCase = agent.getCurrentCase();
            
            this.currentAgent = agent;
            agent.setCurrentCase(this);

            if(oldCase != null)
            {
                synchronized(oldCase)
                {
                    oldCase.currentAgent = null;
                    oldCase.notifyChanges();
                }
            }
        }

        notifyChanges();

        return true;
    }
    public Agent getAgent()
    {
        return currentAgent;
    }
    
    protected void notifyChanges()
    {
        setChanged();
        notifyObservers();
    }

    public void clear()
    {
        if(!isEmpty())
        {
            this.currentAgent.setCurrentCase(null);
            this.currentAgent = null;
        }
    }
}
