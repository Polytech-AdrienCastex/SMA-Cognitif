package model.agent;

import java.util.Random;
import java.util.stream.Stream;
import model.agent.pathfinding.AStar;
import model.agent.pathfinding.PathFinding;
import model.environment.AgentSystem;
import model.environment.Case;
import model.general.Vector2D;
import model.message.Action;
import model.message.Message;
import model.message.MessageContent;
import model.message.Performatif;
import model.message.information.InformationFrom;

/**
 *
 * @author p1002239
 */
public class Agent extends Thread
{
    public Agent(String name, Case destination)
    {
        this.destination = destination;
        this.currentCase = null;

        this.setName(name);

        this.pathFinder = new AStar();
    }
    public Agent(Case destination)
    {
        this(destination.getLocation().x + ":" + destination.getLocation().y, destination);
    }

    private final PathFinding pathFinder;
    private final Case destination;
    private Case currentCase;
    private boolean interrupted;
    
    public Case getCurrentCase()
    {
        return currentCase;
    }
    public void setCurrentCase(Case currentCase)
    {
        this.currentCase = currentCase;
    }

    private AgentSystem as;
    public void setAgentSystem(AgentSystem as)
    {
        this.as = as;
    }


    protected Stream<Case> getCloseLocations()
    {
        Vector2D location = getCurrentCase().getLocation();
        return Stream.of(new Vector2D[]
                {
                    location.add(Vector2D.UP),
                    location.add(Vector2D.DOWN),
                    location.add(Vector2D.LEFT),
                    location.add(Vector2D.RIGHT)
                })
                .map(as.getGrid()::getCase)
                .filter(cc -> cc != null);
    }

    @Override
    public void interrupt()
    {
        super.interrupt();
        this.interrupted = true;
    }

    @Override
    public synchronized void start()
    {
        this.interrupted = false;
        super.start();
    }

    @Override
    public boolean isInterrupted()
    {
        return interrupted || super.isInterrupted();
    }
    
    protected void sleep(int time)
    {
        try
        {
            Thread.sleep(time);
        }
        catch (InterruptedException ex)
        { }
    }

    @Override
    public void run()
    {
        while(!isInterrupted())
        {
            if(as.getMailBox().hasPendingMessage(this))
            {
                Message msg = as.getMailBox().getPendingMessage(this);
                
                if(msg != null)
                {
                    MessageContent mc = msg.getContent();

                    switch(mc.getAction())
                    {
                        case Move:
                            InformationFrom f = mc.getInformation();

                            if(currentCase.getLocation().equals(f.getLocation()))
                            { // Move
                                Random rnd = new Random();
                                Case c = getCloseLocations()
                                        .filter(Case::isEmpty)
                                        .sorted((c1, c2) -> Integer.compare(rnd.nextInt(), rnd.nextInt()))
                                        .findFirst()
                                        .orElse(null);
                                if(c != null)
                                    c.setAgent(this);
                                else
                                {
                                    c = getCloseLocations()
                                            .filter(cc -> !cc.equals(msg.getFrom().currentCase))
                                            .sorted((c1, c2) -> Integer.compare(rnd.nextInt(), rnd.nextInt()))
                                            .findFirst()
                                            .get();
                                    as.getMailBox().putPendingMessage(this, c.getAgent(), new MessageContent(Action.Move, Performatif.Request, new InformationFrom(c.getLocation())));
                                }
                            }
                            break;
                    }
                }
            }
            else if(!destination.equals(currentCase))
            {
                Case futureCase = pathFinder.getNextCase(as, currentCase, destination);
                Agent futureCaseAgent = futureCase.getAgent();
                if(futureCaseAgent != null)
                {
                    as.getMailBox().putPendingMessage(this, futureCaseAgent, new MessageContent(Action.Move, Performatif.Request, new InformationFrom(futureCase.getLocation())));
                }
                else
                {
                    futureCase.setAgent(this);
                }
            }

            sleep(new Random().nextInt(1000));
        }
                System.out.println("!!!!! CLOSE");
    }
}
