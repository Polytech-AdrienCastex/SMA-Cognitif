package model.general;

/**
 *
 * @author p1002239
 */
public class Vector2D
{
    public Vector2D(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    
    public final int x;
    public final int y;
    
    public static final Vector2D ZERO = new Vector2D(0, 0);
    public static final Vector2D UP = new Vector2D(0, -1);
    public static final Vector2D DOWN = new Vector2D(0, 1);
    public static final Vector2D LEFT = new Vector2D(-1, 0);
    public static final Vector2D RIGHT = new Vector2D(1, 0);
    public static final Vector2D NEG = new Vector2D(-1, -1);
    
    public Vector2D add(Vector2D value)
    {
        return new Vector2D(x + value.x, y + value.y);
    }
    public Vector2D mul(Vector2D value)
    {
        return new Vector2D(x * value.x, y * value.y);
    }
    public Vector2D sub(Vector2D value)
    {
        return new Vector2D(x - value.x, y - value.y);
    }
    public Vector2D div(Vector2D value)
    {
        return new Vector2D(x / value.x, y / value.y);
    }
    
    public Vector2D sign()
    {
        return new Vector2D((int)Math.signum(x), (int)Math.signum(y));
    }
    public Vector2D negative()
    {
        return this.mul(NEG);
    }
    
    public boolean isInBound(int xmin, int xmax, int ymin, int ymax)
    {
        return xmin <= x && x < xmax && ymin <= y && y < ymax;
    }
    public boolean isInBound(Vector2D min, Vector2D max)
    {
        return isInBound(min.x, max.x, min.y, max.y);
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof Vector2D)
        {
            Vector2D v = (Vector2D)obj;
            return v.x == x && v.y == y;
        }
        
        return false;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 47 * hash + this.x;
        hash = 47 * hash + this.y;
        return hash;
    }

    @Override
    public String toString()
    {
        return "(" + x + ", " + y + ")";
    }
}
