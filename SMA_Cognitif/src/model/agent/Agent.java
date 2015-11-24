package model.agent;

import model.agent.moveManager.IMoveManager;
import model.agent.moveManager.MoveManager;
import model.agent.pathfinding.PathFinding;
import model.environment.AgentSystem;
import model.environment.Case;
import model.general.Vector2D;
import model.message.Action;
import model.message.Message;
import model.message.MessageContent;
import model.message.Performatif;
import model.message.information.InformationFrom;

import java.util.Random;
import java.util.stream.Stream;

public class Agent extends Thread
{
    public static Builder create()
    {
        return new Builder();
    }
    public static class Builder
    {
        public Builder()
        { }

        private int minSleepTime = 50;
        private int maxSleepTime = 150;
        private Double instability = 0.0;
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
        
        public Builder setMinSleepTime(int minSleepTime)
        {
            this.minSleepTime = minSleepTime;
            return this;
        }
        public Builder setMaxSleepTime(int maxSleepTime)
        {
            this.maxSleepTime = maxSleepTime;
            return this;
        }
        public Builder setInstability(Double instability)
        {
            this.instability = instability;
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
            if(moveManager == null)
                this.moveManager = new MoveManager(DEFAULT_HISTORY_SIZE);
            if(name == null)
                this.name = destination.getLocation().x + ":" + destination.getLocation().y;
            if(instability < 0)
                instability *= -1;
            if(minSleepTime < 0)
                minSleepTime *= -1;
            if(maxSleepTime < 0)
                maxSleepTime *= -1;
            if(maxSleepTime < minSleepTime)
                maxSleepTime += minSleepTime;

            return new Agent(
                    name,
                    pathFinder,
                    destination,
                    currentCase,
                    moveManager,
                    instability,
                    minSleepTime,
                    maxSleepTime);
        }
    }

    protected Agent(
            String name,
            PathFinding pathFinder,
            Case destination,
            Case currentCase,
            IMoveManager moveManager,
            Double instability,
            int minSleepTime,
            int maxSleepTime)
    {
        super(name);
        
        this.pathFinder = pathFinder;
        this.destination = destination;
        this.currentCase = currentCase;
        this.moveManager = moveManager;
        this.instability = instability / 100.0;
        this.minSleepTime = minSleepTime;
        this.maxSleepTime = maxSleepTime;
    }

    private final int minSleepTime;
    private final int maxSleepTime;
    private final Double instability;
    private final PathFinding pathFinder;
    private final Case destination;
    private final IMoveManager moveManager;
    private static final Random rnd = new Random();
    private AgentSystem as;
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

    public void setAgentSystem(AgentSystem as) {
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
    
    public boolean isPersonnalySatisfied()
    {
        return destination.equals(currentCase);
    }


    @Override
    public void run()
    {
        int sleepTime = maxSleepTime - minSleepTime;
        
        sleep(new Random().nextInt(1000) + 500);
        
        while(!isInterrupted())
        {
            if(!this.isPersonnalySatisfied())
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
                                    new InformationFrom(futureCase.getLocation(), currentCase.getLocation())));
                }
                else
                {
                    futureCase.setAgent(this);
                    this.moveManager.confirmMove(futureCase);
                }
            }
            else if(instability != 0 && !as.getAgents().stream().allMatch(Agent::isPersonnalySatisfied) && rnd.nextDouble() < instability)
            {
                Case c = getCloseLocations()
                        .filter(Case::isEmpty)
                        .sorted((c1, c2) -> Integer.compare(rnd.nextInt(), rnd.nextInt()))
                        .findFirst()
                        .orElse(null);
                
                if(c != null && c.setAgent(this))
                    sleep((sleepTime != 0 ? new Random().nextInt(sleepTime) : 0) + minSleepTime);
            }
            
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

                            if(currentCase.getLocation().equals(f.getLocation()) && msg.getFrom().getCurrentCase().getLocation().equals(f.getEmitterLocation()))
                            { // Move
                                Case c = null;
                                if(!destination.equals(currentCase))
                                {
                                    Case futureCase = pathFinder.getNextCase(as, currentCase, destination);
                                    c = getCloseLocations()
                                            .filter(Case::isEmpty)
                                            .filter(futureCase::equals)
                                            .findFirst()
                                            .orElse(null);
                                }
                                
                                if(c == null)
                                    c = getCloseLocations()
                                            .filter(Case::isEmpty)
                                            .sorted(moveManager::compare)
                                            .findFirst()
                                            .orElse(null);
                                if(c != null)
                                {
                                    c.setAgent(this);
                                    this.moveManager.confirmMove(c);
            
                sleep((sleepTime != 0 ? new Random().nextInt(sleepTime) : 0) + minSleepTime);
                                }
                                else
                                {
                                    /*c = */getCloseLocations()
                                            .filter(cc -> !cc.equals(msg.getFrom().currentCase))
                                            .forEach(cc ->
                                            {
                                                as.getMailBox().putPendingMessage(
                                                        this,
                                                        cc.getAgent(),
                                                        new MessageContent(
                                                                Action.Move,
                                                                Performatif.Request,
                                                                new InformationFrom(cc.getLocation(), currentCase.getLocation())));
                                            });
                                    
                                            as.getMailBox().putPendingMessage(msg.getFrom(), msg.getTo(), msg.getContent());
                                            /*
                                            .sorted((c1, c2) -> Integer.compare(rnd.nextInt(), rnd.nextInt()))
                                            .findFirst()
                                            .get();
                                    as.getMailBox().putPendingMessage(
                                            this,
                                            c.getAgent(),
                                            new MessageContent(
                                                    Action.Move,
                                                    Performatif.Request,
                                                    new InformationFrom(c.getLocation(), currentCase.getLocation())));*/
                                }
                            }
                            break;
                    }
                }
            }
            
            sleep((sleepTime != 0 ? new Random().nextInt(sleepTime) : 0) + minSleepTime);
        }
    }
}
