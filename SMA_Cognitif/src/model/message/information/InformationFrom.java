package model.message.information;

import model.general.Vector2D;

public class InformationFrom implements Information
{
    public InformationFrom(Vector2D location, Vector2D locationEmitter)
    {
        this.location = location;
        this.locationEmitter = locationEmitter;
    }
    
    private final Vector2D location;
    private final Vector2D locationEmitter;
    
    public Vector2D getLocation()
    {
        return location;
    }
    
    public Vector2D getEmitterLocation()
    {
        return locationEmitter;
    }
}
