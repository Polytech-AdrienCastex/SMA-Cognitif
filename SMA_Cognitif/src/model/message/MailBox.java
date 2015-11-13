package model.message;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;
import model.agent.Agent;

public class MailBox extends Observable
{
    public MailBox()
    {
        this.mails = new HashMap<>();
    }
    
    private final Map<Agent, LinkedList<Message>> mails;
    
    public static class Notification
    {
        public enum Action
        {
            Remove,
            Add
        }
        
        public Notification(Message msg, Action action)
        {
            this.msg = msg;
            this.action = action;
        }
        
        public final Message msg;
        public final Action action;
    }
    
    public boolean hasPendingMessage(Agent agent)
    {
        return !getList(agent).isEmpty();
    }
    
    public Message getPendingMessage(Agent agent)
    {
        Message msg = getList(agent).poll();
        
        setChanged();
        notifyObservers(new Notification(msg, Notification.Action.Remove));
        
        return msg;
    }
    
    public boolean putPendingMessage(Agent from, Agent to, MessageContent messageContent)
    {
        if(to == null)
            return false;
        
        Message msg = new Message(from, to, messageContent);
        if(getList(to).add(msg))
        {
            setChanged();
            notifyObservers(new Notification(msg, Notification.Action.Add));
            return true;
        }
        else
            return false;
    }
    
    protected synchronized LinkedList<Message> getList(Agent agent)
    {
        if(!mails.containsKey(agent))
            mails.put(agent, new LinkedList<>());
        return mails.get(agent);
    }
}
