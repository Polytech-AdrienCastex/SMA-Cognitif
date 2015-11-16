package view;

import java.awt.GridLayout;
import java.awt.Label;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import model.agent.Agent;
import model.message.MailBox.Notification;

public class MailBox extends JPanel implements Observer
{
    public MailBox(model.message.MailBox mb)
    {
        super(new GridLayout(3, 30, 2, 2));
        
        this.cmps = new HashMap<>();
        
        mb.addObserver(this);
        
        pane = this;
    }
    
    private JPanel pane;
    
    private final Map<Agent, JComponent> cmps;
    
    protected JComponent getComponent(Agent agent)
    {
        if(!cmps.containsKey(agent))
        {
            System.out.println("XXX");
            JButton c = new JButton("CCC");
            this.setBackground(AgentManager.getColorFromAgent(agent));
            c.setSize(100, 100);
            cmps.put(agent, c);
            pane.add(c);
            this.repaint();
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
