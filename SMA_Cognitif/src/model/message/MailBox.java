package model.message;

import model.agent.Agent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author p1002239
 */
public class MailBox
{
    public MailBox()
    {
        this.mails = new HashMap<>();
    }
    
    private final Map<Agent, LinkedList<Message>> mails;
    
    public boolean hasPendingMessage(Agent agent)
    {
        return !getList(agent).isEmpty();
    }
    
    public Message getPendingMessage(Agent agent)
    {
        return getList(agent).poll();
    }
    
    public boolean putPendingMessage(Agent from, Agent to, MessageContent messageContent)
    {
        if(to == null)
            return false;
        
        getList(to).add(new Message(from, to, messageContent));
        return true;
    }
    
    protected synchronized LinkedList<Message> getList(Agent agent)
    {
        if(!mails.containsKey(agent))
            mails.put(agent, new LinkedList<>());
        return mails.get(agent);
    }
}
