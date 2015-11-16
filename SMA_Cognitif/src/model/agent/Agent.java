package model.agent;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;

import model.agent.moveManager.IMoveManager;
import model.agent.moveManager.MoveManager;
import model.agent.pathfinding.AStar;
import model.agent.pathfinding.PathFinding;
import model.environment.AgentSystem;
import model.environment.Case;
import model.environment.Grid;
import model.general.Vector2D;
import model.message.*;
import model.message.information.InformationFrom;

/**
 *
 * @author p1002239
 */
public class Agent extends Thread
{
    public static class Builder
    {
        public Builder()
        { }

        private PathFinding pathFinder = null;
        private Case destination = null;
        private Case currentCase = null;
        private IMoveManager moveManager = null;
        private static final int DEFAULT_HISTORY_SIZE = 10;
        private String name = null;

        public Builder setPathFinder(PathFinding pathFinder)
        {
            this.pathFinder = pathFinder;
            return this;
        }

        public Builder setDestination(Case destination)
        {
            this.destination = destination;
            return this;
        }

        public Builder setCurrentCase(Case currentCase)
        {
            this.currentCase = currentCase;
            return this;
        }

        public Builder setMoveManager(IMoveManager moveManager)
        {
            this.moveManager = moveManager;
            return this;
        }

        public Builder setName(String name)
        {
            this.name = name;
            return this;
        }

        public Agent build()
        {
            if(pathFinder == null)
                throw new IllegalStateException("PathFinder is not specified. Use setPathFinder(...).");
            if(destination == null)
                throw new IllegalStateException("Destination is not specified. Use setDestination(...).");
            if(currentCase == null)
                throw new IllegalStateException("CurrentCase is not specified. Use setCurrentCase(...).");
            if(moveManager == null)
                this.moveManager = new MoveManager(DEFAULT_HISTORY_SIZE);
            if(name == null)
                this.name = destination.getLocation().x + ":" + destination.getLocation().y;

            return new Agent(name, pathFinder, destination, currentCase, moveManager);
        }
    }

    protected Agent(String name, PathFinding pathFinder, Case destination, Case currentCase, IMoveManager moveManager) {
        super(name);
        this.pathFinder = pathFinder;
        this.destination = destination;
        this.currentCase = currentCase;
        this.moveManager = moveManager;
    }

    private final PathFinding pathFinder;
    private final Case destination;
    private AgentSystem as;
    private Case currentCase;
    private boolean interrupted;
    private IMoveManager moveManager;

    public Case getCurrentCase()
    {
        return currentCase;
    }

    public void setCurrentCase(Case currentCase)
    {
        this.currentCase = currentCase;
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
                                        .sorted(moveManager::compare)
                                        .findFirst()
                                        .orElse(null);
                                if(c != null)
                                {
                                    c.setAgent(this);
                                    this.moveManager.confirmMove(c);
                                }
                                else
                                {
                                    c = getCloseLocations()
                                            .filter(cc -> !cc.equals(msg.getFrom().currentCase))
                                            .sorted((c1, c2) -> Integer.compare(rnd.nextInt(), rnd.nextInt()))
                                            .findFirst()
                                            .get();
                                    as.getMailBox().putPendingMessage(
                                            this,
                                            c.getAgent(),
                                            new MessageContent(
                                                    Action.Move,
                                                    Performatif.Request,
                                                    new InformationFrom(c.getLocation())));
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
                    as.getMailBox().putPendingMessage(
                            this,
                            futureCaseAgent,
                            new MessageContent(
                                    Action.Move,
                                    Performatif.Request,
                                    new InformationFrom(futureCase.getLocation())));
                }
                else
                {
                    futureCase.setAgent(this);
                    this.moveManager.confirmMove(futureCase);
                }
            }

            sleep(new Random().nextInt(1000));
        }
    }
}
