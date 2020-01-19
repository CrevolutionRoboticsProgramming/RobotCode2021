package org.frc2851.robot.framework;

import org.frc2851.robot.framework.command.CommandScheduler;

import java.util.List;
import java.util.Vector;

public abstract class Subsystem
{
    private Vector<Component> mComponents;

    public Subsystem(Component... components)
    {
        mComponents = new Vector<>(List.of(components));
        CommandScheduler.getInstance().addSubsystem(this);
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
