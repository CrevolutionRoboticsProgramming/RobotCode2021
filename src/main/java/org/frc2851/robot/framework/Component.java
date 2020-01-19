package org.frc2851.robot.framework;

import org.frc2851.robot.framework.command.Command;

public abstract class Component
{
    private Command mDefaultCommand;

    public void periodic()
    {
    }

    public void setDefaultCommand(Command command)
    {
        mDefaultCommand = command;
    }

    public Command getDefaultCommand()
    {
        return mDefaultCommand;
    }

    public String getName()
    {
        return getClass().getSimpleName();
    }
}
