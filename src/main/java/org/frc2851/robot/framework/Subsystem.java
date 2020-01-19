package org.frc2851.robot.framework;

import org.frc2851.robot.framework.command.CommandScheduler;

import java.util.Vector;

public abstract class Subsystem
{
    private Vector<Component> mComponents;

    public Subsystem()
    {
        mComponents = new Vector<>();
        CommandScheduler.getInstance().addSubsystem(this);
    }

    public void addComponent(Component component)
    {
        mComponents.add(component);
    }

    public Vector<Component> getComponents()
    {
        return mComponents;
    }

    public String getName()
    {
        return getClass().getSimpleName();
    }
}
