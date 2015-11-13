package view;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Label;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import javafx.scene.layout.VBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import model.agent.Agent;
import model.message.MailBox.Notification;

public class MailBox extends JPanel implements Observer
{
    public MailBox(model.message.MailBox mb)
    {
        this.cmps = new HashMap<>();
        
        mb.addObserver(this);
        
        this.setLayout(new GridLayout(3, 30));
    }
    
    private final Map<Agent, JComponent> cmps;
    
    protected JComponent getComponent(Agent agent)
    {
        if(!cmps.containsKey(agent))
        {
            JComponent c = new JPanel();
            c.setBackground(AgentManager.getColorFromAgent(agent));
            cmps.put(agent, c);
            this.add(c);
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
                Label label = new Label();
                label.setBackground(AgentManager.getColorFromAgent(notification.msg.getFrom()));
                label.setText(notification.msg.getContent().getAction().name());
                
                JComponent c = getComponent(notification.msg.getTo());
                c.add(label);
                break;
                
            case Remove:
                break;
        }
        
        this.repaint();
    }
}
