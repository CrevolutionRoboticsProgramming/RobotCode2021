package org.frc2851.robot.framework.command;

import org.frc2851.robot.framework.Component;

import java.util.List;
import java.util.Vector;

public class Command
{
    private String mName;
    private State mState = State.NOT_STARTED;
    private Vector<Component> mRequirements;
    
    public Command(String name, Component... requirements)
    {
        mName = name;
        mRequirements = new Vector<>(List.of(requirements));
    }
    
    public void initialize()
    {
    }
    
    public void execute()
    {
        setState(State.EXECUTING);
    }
    
    public void end()
    {
        setState(State.NOT_STARTED);
    }
    
    public boolean isFinished()
    {
        return false;
    }

    public final String getName()
    {
        return mName;
    }
    
    public final Vector<Component> getRequirements()
    {
        return mRequirements;
    }

    private void setState(State state)
    {
        mState = state;
    }

    public State getState()
    {
        return mState;
    }

    public enum State
    {
        NOT_STARTED, EXECUTING
    }
}
