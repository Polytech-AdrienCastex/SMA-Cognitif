package model.environment;

import model.agent.Agent;
import model.general.Vector2D;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import model.message.MailBox;

/**
 *
 * @author Adrien
 */
public class AgentSystem
{
    public AgentSystem(MailBox mailBox, Grid grid, Collection<Agent> agents)
    {
        this.mailBox = mailBox;
        this.grid = grid;
        this.agents = agents;
        
        agents.forEach(a -> a.setAgentSystem(this));
    }
    
    private final MailBox mailBox;
    private final Grid grid;
    private final Collection<Agent> agents;
    
    public static Builder create()
    {
        return new Builder();
    }

    public Grid getGrid()
    {
        return grid;
    }
    public MailBox getMailBox()
    {
        return mailBox;
    }
    public Collection<Agent> getAgents()
    {
        return agents;
    }
    
    public static class Builder
    {
        public Builder()
        { }
        
        private MailBox mailBox = null;
        private Grid grid = null;
        private final Collection<Agent.Builder> agents = new LinkedList<>();
        
        public Builder setMailBox(MailBox mailBox)
        {
            this.mailBox = mailBox;
            return this;
        }
        public Builder setGrid(Grid grid)
        {
            this.grid = grid;
            return this;
        }
        
        public Builder addAgent(Function<Grid, Agent.Builder> agentSupplier)
        {
            checkGrid();
            
            agents.add(agentSupplier.apply(grid));
            return this;
        }
        public Builder addAgent(Agent.Builder agent)
        {
            agents.add(agent);
            return this;
        }
        public Builder addAgents(Function<Grid, Collection<Agent.Builder>> agentsSupplier)
        {
            checkGrid();
            
            addAgents(agentsSupplier.apply(grid));
            return this;
        }
        public Builder addAgents(Collection<Agent.Builder> agents)
        {
            this.agents.addAll(agents);
            return this;
        }
        public Builder addAgents(Agent.Builder[] agents)
        {
            addAgents(Arrays.asList(agents));
            return this;
        }

        
        protected void checkGrid()
        {
            if(grid == null)
                throw new IllegalStateException("Grid is not specified. Use setGrid(...).");
        }
        
        public AgentSystem build()
        {
            checkGrid();
            
            if(mailBox == null)
                mailBox = new MailBox();

            return new AgentSystem(mailBox, grid, agents.stream()
                    .map(Agent.Builder::build)
                    .collect(Collectors.toList()));
        }
    }
    
    
    private static Random rnd = null;
    public static Vector2D getRandomLocation(List<Vector2D> freeLocations)
    {
        if(rnd == null)
            rnd = new Random();
        return freeLocations.get(rnd.nextInt(freeLocations.size()));
    }
    
    
    public AgentSystem dispatchAgents()
    {
        dispatchAgents(AgentSystem::getRandomLocation);
        return this;
    }

    public AgentSystem dispatchAgents(Function<List<Vector2D>, Vector2D> locationSupplier)
    {
        dispatchAgents(locationSupplier, grid, agents);
        return this;
    }

    public static void dispatchAgents(Function<List<Vector2D>, Vector2D> locationSupplier, Grid grid, Collection<Agent> agents)
    {
        grid.clear();

        for(Agent a : agents)
            grid.getCase(locationSupplier.apply(grid.getFreeLocations())).setAgent(a);
    }
    
    public AgentSystem startAgents()
    {
        agents.stream().forEach(Agent::start);
        return this;
    }
    public AgentSystem stopAgents()
    {
        agents.stream().forEach(Agent::interrupt);
        return this;
    }
}
