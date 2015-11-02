package environment;

import general.Vector2D;

/**
 *
 * @author p1002239
 */
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
        
        for(int x = 0; x < size.x; x++)
        for(int y = 0; y < size.y; y++)
            this.cases[x][y] = new Case(new Vector2D(x, y));
    }
    
    private final Vector2D size;
    private final Case[][] cases;
    
    public Case getCase(Vector2D location)
    {
        return getCase(location.x, location.y);
    }
    public Case getCase(int x, int y)
    {
        return cases[x][y];
    }
    
    public Vector2D getSize()
    {
        return size;
    }
}
