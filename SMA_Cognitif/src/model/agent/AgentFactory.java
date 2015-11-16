package model.agent;

import model.agent.pathfinding.AStar;
import model.agent.pathfinding.PathFinding;
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
    
    public static Collection<Agent.Builder> createAgents(Grid grid, Vector2D[] locations)
    {
        PathFinding path = new AStar();
        return Stream.of(locations)
                .map(grid::getCase)
                .map(c ->
                {
                    return Agent.create()
                            .setDestination(c);
                })
                .peek(a -> a.setPathFinder(path))
                .collect(Collectors.toList());
    }
    public static Collection<Agent.Builder> createAgents(Grid grid, Integer[] locations)
    {
        PathFinding path = new AStar();
        return IntStream.range(0, (int)(locations.length / 2))
                .map(i -> i*2)
                .mapToObj(i -> new Vector2D(locations[i], locations[i + 1]))
                .map(grid::getCase)
                .map(c ->
                {
                    return Agent.create()
                            .setDestination(c);
                })
                .peek(a -> a.setPathFinder(path))
                .collect(Collectors.toList());
    }
}
