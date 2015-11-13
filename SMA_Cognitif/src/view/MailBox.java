package view;

import java.awt.GridLayout;
import java.awt.Label;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JComponent;
import javax.swing.JPanel;
import model.agent.Agent;
import model.message.MailBox.Notification;

public class MailBox extends JPanel implements Observer
{
    public MailBox(model.message.MailBox mb)
    {
        this.mb = mb;
        this.cmps = new HashMap<>();
        
        this.setLayout(new GridLayout(3, 30));
    }
    
    private final model.message.MailBox mb;
    private final Map<Agent, JComponent> cmps;
    
    protected JComponent getComponent(Agent agent)
    {
        if(!cmps.containsKey(agent))
        {
            JComponent c = new JPanel();
            cmps.put(agent, c);
        }
        
        return cmps.get(agent);
    }

    @Override
    public void update(Observable o, Object arg)
    {
        if(!(arg instanceof Notification))
            return;
        
        Notification notification = (Notification)arg;
        
        switch(notification.action)
        {
            case Add:
                JPanel panel = new JPanel();
                Label label = new Label("");
                label.setBackground(AgentManager.getColorFromAgent(notification.msg.getFrom()));
                label.setText(notification.msg.getContent().getAction().name());
                panel.add(label);
                
                getComponent(notification.msg.getTo())
                        .add(panel);
                break;
                
            case Remove:
                break;
        }
    }
}
