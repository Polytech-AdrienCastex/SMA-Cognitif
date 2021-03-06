package view;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Label;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Stream;
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
        this.removed = new LinkedList<>();
        
        mb.addObserver(this);
        
        pane = this;
    }
    
    private final Map<Agent, JComponent> cmps;
    private final Collection<BigInteger> removed;
    private JPanel pane;
    private Container frame = null;
    
    protected void refresh()
    {
        if(frame == null)
        {
            frame = this;
            while(frame.getParent() != null)
                frame = frame.getParent();
        }
        
        frame.setVisible(true);
    }
    
    protected JComponent getComponent(Agent agent)
    {
        if(!cmps.containsKey(agent))
        {
            JComponent c = new JPanel();
            c.setBackground(AgentManager.getColorFromAgent(agent));
            c.setSize(100, 100);
            cmps.put(agent, c);
            pane.add(c);
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
                if(removed.remove(notification.msg.getID()))
                    break;
                
                Label label = new Label();
                label.setBackground(AgentManager.getColorFromAgent(notification.msg.getFrom()));
                label.setText(notification.msg.getContent().getAction().name());
                label.setName(notification.msg.getID().toString());

                getComponent(notification.msg.getTo())
                        .add(label);

                this.refresh();
                break;

            case Remove:
                JComponent c = getComponent(notification.msg.getTo());
                String uid = notification.msg.getID().toString();

                Label cl = Stream.of(c.getComponents())
                        .filter(l -> l instanceof Label)
                        .map(Label.class::cast)
                        .filter(l -> uid.equals(l.getName()))
                        .findFirst()
                        .orElse(null);

                if(cl != null)
                    c.remove(cl);
                else
                    removed.add(notification.msg.getID());

                this.refresh();
                break;
        }

        this.repaint();
    }
}
