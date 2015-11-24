package view;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import model.agent.Agent;

public class AgentManager
{
    private AgentManager()
    { }
    
    private static final Map<Agent, Color> agents = new HashMap<>();
    
    private static final Color[] colors = new Color[]
    {
        Color.BLACK,
        Color.BLUE,
        Color.CYAN,
        Color.GRAY,
        Color.GREEN,
        Color.MAGENTA,
        Color.ORANGE,
        Color.PINK,
        Color.RED,
        Color.YELLOW,
        Color.LIGHT_GRAY,
        Color.WHITE
    };
    private static int colorIndex = 0;
    
    public static Color getColorFromAgent(Agent agent)
    {
        if(!agents.containsKey(agent))
            agents.put(agent, colors[(colorIndex++) % colors.length]);
        
        return agents.get(agent);
    }
}
