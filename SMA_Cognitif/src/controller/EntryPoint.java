package controller;

import static java.lang.Thread.sleep;
import model.agent.Agent;
import model.agent.AgentFactory;
import model.agent.pathfinding.AStar;
import model.environment.AgentSystem;
import model.environment.Grid;
import view.MainFrame;

public class EntryPoint
{
    public static void main(String[] args) throws InterruptedException
    {
        AgentSystem as = AgentSystem.create()
                .setGrid(new Grid(7))
                .addAgents(g -> AgentFactory.createAgents(g, new Integer[]
                {
                    0, 0,
                    0, 1,
                    //0, 2,
                    //0, 3,
                    0, 4,
                    //0, 5,
                    //0, 6,
                    
                    //1, 0,
                    1, 1,
                    //1, 2,
                    //1, 3,
                    //1, 4,
                    1, 5,
                    //1, 6,
                    
                    //2, 0,
                    2, 1,
                    //2, 2,
                    //2, 3,
                    //2, 4,
                    //2, 5,
                    2, 6,
                    
                    //3, 0,
                    //3, 1,
                    //3, 2,
                    //3, 3,
                    //3, 4,
                    //3, 5,
                    //3, 6,
                    
                    //4, 0,
                    //4, 1,
                    //4, 2,
                    //4, 3,
                    //4, 4,
                    4, 5,
                    //4, 6,
                    
                    //5, 0,
                    5, 1,
                    //5, 2,
                    //5, 3,
                    5, 4,
                    5, 5,
                    //5, 6,
                    
                    //6, 0,
                    6, 1,
                    //6, 2,
                    //6, 3,
                    //6, 4,
                    //6, 5,
                    6, 6,
                }, new AStar(), 0.0))
                .build()
                .dispatchAgents();
        
        new MainFrame(as).setVisible(true);
    }
}
