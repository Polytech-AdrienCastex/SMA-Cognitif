package message;

import agent.Agent;

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
}
