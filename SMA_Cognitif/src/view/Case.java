package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Label;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;
import model.agent.Agent;

public class Case extends JPanel implements Observer
{
    public Case(model.environment.Case c)
    {
        this.setBackground(new Color(230, 230, 230));
        
        this.text = new Label();
        this.add(text);
        
        this.c = c;
        c.addObserver(this);
        
        this.setPreferredSize(new Dimension(50, 50));
        text.setPreferredSize(new Dimension(50, 50));
        updateText();
    }
    
    protected final model.environment.Case c;
    
    private final Label text;
    
    protected void updateText()
    {
        Agent a = c.getAgent();
        text.setForeground(AgentManager.getColorFromAgent(a));
        text.setText(a == null ? "" : a.getName());
    }

    @Override
    public void update(Observable o, Object arg)
    {
        updateText();
        this.repaint();
    }
}
