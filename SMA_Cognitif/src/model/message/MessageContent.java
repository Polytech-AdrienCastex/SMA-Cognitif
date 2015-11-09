package model.message;

import model.message.information.Information;

/**
 *
 * @author p1002239
 */
public class MessageContent
{
    public MessageContent(Action action, Performatif performatif, Information information)
    {
        this.performatif = performatif;
        this.information = information;
        this.action = action;
    }
    
    private final Action action;
    private final Performatif performatif;
    private final Information information;
    
    public Action getAction()
    {
        return action;
    }
    public Performatif getPerformatif()
    {
        return performatif;
    }
    public <T extends Information> T getInformation()
    {
        return (T)information;
    }
}
