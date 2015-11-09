package view;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JFrame;
import model.environment.Grid;

/**
 *
 * @author Adrien
 */
public class MainFrame extends JFrame
{
    public MainFrame(Grid grid)
    {
        this.setLayout(new GridLayout(grid.getSize().x, grid.getSize().y, 5, 5));
        
        this.setPreferredSize(new Dimension(400, 400));
        
        grid.getCases()
                .stream()
                .sorted((c1, c2) -> c1.getLocation().y == c2.getLocation().y ? Integer.compare(c1.getLocation().x, c2.getLocation().x) : Integer.compare(c1.getLocation().y, c2.getLocation().y))
                .map(Case::new)
                .forEachOrdered(this::add);
        
        this.pack();
    }
}
