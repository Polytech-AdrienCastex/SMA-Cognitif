package model.message.information;

import model.general.Vector2D;

/**
 *
 * @author p1002239
 */
public class InformationFrom implements Information
{
    public InformationFrom(Vector2D location)
    {
        this.location = location;
    }
    
    private final Vector2D location;
    
    public Vector2D getLocation()
    {
        return location;
    }
}
