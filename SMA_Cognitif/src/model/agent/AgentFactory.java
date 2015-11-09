package model.agent;

import model.environment.Grid;
import model.general.Vector2D;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 *
 * @author Adrien
 */
public class AgentFactory
{
    private AgentFactory()
    { }
    
    public static Collection<Agent> createAgents(Grid grid, Vector2D[] locations)
    {
        return Stream.of(locations)
                .map(grid::getCase)
                .map(Agent::new)
                .collect(Collectors.toList());
    }
    public static Collection<Agent> createAgents(Grid grid, Integer[] locations)
    {
        return IntStream.range(0, (int)(locations.length / 2))
                .map(i -> i*2)
                .mapToObj(i -> new Vector2D(locations[i], locations[i + 1]))
                .map(grid::getCase)
                .map(Agent::new)
                .collect(Collectors.toList());
    }
}
