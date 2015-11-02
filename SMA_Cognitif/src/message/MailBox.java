package message;

import agent.Agent;
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
        return !mails.get(agent).isEmpty();
    }
    
    public Message getPendingMessage(Agent agent)
    {
        return mails.get(agent).poll();
    }
    
    public void putPendingMessage(Agent from, Agent to, MessageContent messageContent)
    {
        mails.get(from).add(new Message(from, to, messageContent));
    }
    
    protected LinkedList<Message> getList(Agent agent)
    {
        if(!mails.containsKey(agent))
            mails.put(agent, new LinkedList<>());
        return mails.get(agent);
    }
}
