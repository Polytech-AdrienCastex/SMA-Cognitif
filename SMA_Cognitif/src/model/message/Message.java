package model.message;

import java.math.BigInteger;
import model.agent.Agent;

public class Message
{
    protected Message(Agent from, Agent to, MessageContent content)
    {
        this.from = from;
        this.to = to;
        this.content = content;
        
        this.uid = getUID();
    }
    
    private final Agent from;
    private final Agent to;
    private final MessageContent content;
    private final BigInteger uid;
    
    private static BigInteger suid = BigInteger.ZERO;
    protected static BigInteger getUID()
    {
        suid = suid.add(BigInteger.ONE);
        return suid;
    }
    
    public BigInteger getID()
    {
        return uid;
    }
    
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

    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof Message))
            return false;
        
        Message msg = (Message)obj;
        return msg.from.equals(this.from)
                && msg.to.equals(this.to)
                && msg.content.equals(this.content);
    }
}
