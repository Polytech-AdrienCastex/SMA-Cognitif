package model.environment;

import model.general.Vector2D;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Grid
{
    public Grid(int size)
    {
        this(size, size);
    }
    public Grid(int sizeX, int sizeY)
    {
        this(new Vector2D(sizeX, sizeY));
    }
    public Grid(Vector2D size)
    {
        this.size = size;
        this.cases = new Case[size.x][size.y];
        
        getAllLocations(size)
                .forEach(v -> this.cases[v.x][v.y] = new Case(v));
    }
    
    private final Vector2D size;
    private final Case[][] cases;
    
    protected static Stream<Vector2D> getAllLocations(Vector2D size)
    {
        return IntStream.range(0, size.x * size.y)
                .mapToObj(i -> new Vector2D(i % size.x, i / size.x));
    }
    public Stream<Case> getAllCases()
    {
        return getAllLocations(size)
                .map(v -> this.cases[v.x][v.y]);
    }
    
    public Case getCase(Vector2D location)
    {
        if(!location.isInBound(Vector2D.ZERO, size))
            return null;
        
        return cases[location.x][location.y];
    }
    
    public Vector2D getSize()
    {
        return size;
    }
    
    public void clear()
    {
        getAllCases().forEach(Case::clear);
    }

    public Collection<Case> getCases()
    {
        return getAllCases()
                .collect(Collectors.toList());
    }
    public List<Vector2D> getFreeLocations()
    {
        return getAllCases()
                .filter(Case::isEmpty)
                .map(Case::getLocation)
                .collect(Collectors.toList());
    }
}
