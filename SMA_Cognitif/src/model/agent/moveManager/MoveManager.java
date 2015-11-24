package model.agent.moveManager;

import model.environment.Case;

import java.util.LinkedList;

public class MoveManager implements IMoveManager
{
    private final LinkedList<Case> history;
    private final int maxHistory;

    public MoveManager(int maxHistory)
    {
        this.maxHistory = maxHistory;
        this.history = new LinkedList<>();
    }

    /**
     * Defines if a Case is "greater" than an other.
     * A Case is greater if it's older (in the history) than the other Case
     *
     * @param c1 First Case
     * @param c2 Second Case
     * @return the difference between the two cases
     */
    @Override
    public int compare(Case c1, Case c2)
    {
        int index_c1 = history.indexOf(c1);
        int index_c2 = history.indexOf(c2);
        index_c1 = index_c1 == -1 ? Integer.MAX_VALUE : index_c1;
        index_c2 = index_c2 == -1 ? Integer.MAX_VALUE : index_c2;
        return Integer.compare(index_c1, index_c2);
    }

    /**
     * Add a Case into the history
     *
     * @param c Case to add
     */
    @Override
    public void confirmMove(Case c)
    {
        history.add(c);
        while(history.size() > maxHistory)
            history.removeFirst();
    }
}
