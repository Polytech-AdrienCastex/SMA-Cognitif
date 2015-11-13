package model.message;

import model.agent.Agent;

/**
 *
 * @author p1002239
 */
public class Message
{
    protected Message(Agent from, Agent to, MessageContent content)
    {
        this.from = from;
        this.to = to;
        this.content = content;
    }
    
    private final Agent from;
    private final Agent to;
    private final MessageContent content;
    
    public Agent getFrom()
    {
        return from;
    }
    
    public Agent getTo()
    {
        return to;
    }
    
    public MessageContent getContent()
    {
        return content;
    }
}
