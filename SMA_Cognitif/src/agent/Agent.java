package agent;

import environment.Case;

/**
 *
 * @author p1002239
 */
public class Agent
{
    public Agent()
    {
        this.currentCase = null;
    }
    
    private Case currentCase;
    
    public Case getCurrentCase()
    {
        return currentCase;
    }
    public void setCurrentCase(Case currentCase)
    {
        this.currentCase = currentCase;
    }
}
