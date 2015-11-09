package controller;

import model.agent.Agent;
import model.agent.AgentFactory;
import model.environment.AgentSystem;
import model.environment.Grid;
import model.general.Vector2D;
import view.MainFrame;

/**
 *
 * @author p1002239
 */
public class EntryPoint
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        // Cours : liris.cnrs.fr/~saknine/SMA
        // Mot de passe : akines2015
        
        AgentSystem as = AgentSystem.create()
                .setGrid(new Grid(5))
                .addAgents(g -> AgentFactory.createAgents(g, new Integer[]
                {
                    0, 0,
                    0, 1,
                    0, 2,
                    0, 3,
                    1, 0,
                    1, 1,
                    1, 2,
                    2, 2
                }))
                .build()
                .dispatchAgents()
                .startAgents();
        
        new MainFrame(as.getGrid()).setVisible(true);
    }
    
}
