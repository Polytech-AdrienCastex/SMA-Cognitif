package model.agent.pathfinding;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import model.environment.AgentSystem;
import model.environment.Case;
import model.general.Vector2D;

/**
 *
 * @author Adrien
 */
public class AStar implements PathFinding
{
    @Override
    public Case getNextCase(AgentSystem as, Case currentCase, Case destinationCase)
    {
        Vector2D size = as.getGrid().getSize();
        Double values[][] = new Double[size.x][size.y];
        
        
        Set<Case> closedSet = new HashSet<>();
        Set<Case> openSet = new HashSet<>();
        openSet.add(currentCase);
        Map<Case, Case> cameFrom = new HashMap<>();
        
        Map<Case, Double> gScore = new HashMap<>();
        Map<Case, Double> fScore = new HashMap<>();
        as.getGrid()
                .getAllCases()
                .peek(c -> fScore.put(c, Double.POSITIVE_INFINITY))
                .forEach(c -> gScore.put(c, Double.POSITIVE_INFINITY));
        
        gScore.put(currentCase, 0.0);
        fScore.put(currentCase, gScore.get(currentCase) + h_cost(currentCase, destinationCase));
        
        while(!openSet.isEmpty())
        {
            Case current = openSet.stream()
                    .sorted((c1, c2) -> Double.compare(fScore.get(c1), fScore.get(c2)))
                    .findFirst()
                    .get();
            
            if(current == destinationCase)
                return getFirstNext(cameFrom, currentCase, destinationCase);
            
            openSet.remove(current);
            closedSet.add(current);
            
            for(Case n : getNeighbors(current, as))
            {
                if(closedSet.contains(n))
                    continue;
                
                double tgScore = gScore.get(current) + dist(current, n);
                if(!openSet.contains(n))
                    openSet.add(n);
                else if(tgScore >= gScore.get(n))
                    continue;
                
                cameFrom.put(n, current);
                gScore.put(n, tgScore);
                fScore.put(n, gScore.get(n) + h_cost(n, destinationCase));
            }
        }
        
        return null;
    }
    
    protected static Case[] getNeighbors(Case c, AgentSystem as)
    {
        Vector2D location = c.getLocation();
        return Stream.of(new Vector2D[]
                {
                    location.add(Vector2D.UP),
                    location.add(Vector2D.DOWN),
                    location.add(Vector2D.LEFT),
                    location.add(Vector2D.RIGHT)
                })
                .map(as.getGrid()::getCase)
                .filter(cc -> cc != null)
                .toArray(Case[]::new);
    }
    
    protected static Case getFirstNext(Map<Case, Case> cameFrom, Case currentCase, Case destinationCase)
    {
        Case current = destinationCase;
        while(cameFrom.get(current) != currentCase)
        {
            current = cameFrom.get(current);
        }
        return current;
    }
    
    protected static double h_cost(Case from, Case to)
    {
        return dist(from, to);
    }
    protected static double dist(Case from, Case to)
    {
        Vector2D f = from.getLocation();
        Vector2D t = to.getLocation();
        
        double x = f.x - t.x;
        double y = f.y - t.y;
        
        return Math.sqrt(x*x + y*y) * (to.isEmpty() ? 1 : 5);
    }
}
