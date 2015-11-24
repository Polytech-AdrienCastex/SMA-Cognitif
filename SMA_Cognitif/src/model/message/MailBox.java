package model.message;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.ConcurrentLinkedQueue;
import model.agent.Agent;

public class MailBox extends Observable
{
    public MailBox()
    {
        this.mails = new HashMap<>();
    }
    
    private final Map<Agent, ConcurrentLinkedQueue<Message>> mails;
    
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
    
    protected boolean spamProtection(Agent from, Agent to, Message msg)
    {
        ConcurrentLinkedQueue<Message> listMsgs = getList(to);
        
        if(listMsgs.contains(msg))
            return false;
        
        listMsgs.stream()
                .filter(m -> m.getFrom().equals(from))
                .peek(m ->
                {
                    setChanged();
                    notifyObservers(new Notification(m, Notification.Action.Remove));
                })
                .forEach(listMsgs::remove);
        
        return true;
    }
    
    public boolean putPendingMessage(Agent from, Agent to, MessageContent messageContent)
    {
        if(to == null)
            return false;
        
        Message msg = new Message(from, to, messageContent);
        
        if(!spamProtection(from, to, msg))
            return false;
        
        if(getList(to).add(msg))
        {
            setChanged();
            notifyObservers(new Notification(msg, Notification.Action.Add));
            return true;
        }
        else
            return false;
    }
    
    protected synchronized ConcurrentLinkedQueue<Message> getList(Agent agent)
    {
        if(!mails.containsKey(agent))
            mails.put(agent, new ConcurrentLinkedQueue<>());
        return mails.get(agent);
    }
}
