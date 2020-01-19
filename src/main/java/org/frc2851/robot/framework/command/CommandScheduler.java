package org.frc2851.robot.framework.command;

import edu.wpi.first.wpilibj.DriverStation;
import org.frc2851.robot.framework.Component;
import org.frc2851.robot.framework.Subsystem;
import org.frc2851.robot.util.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.function.BooleanSupplier;

public final class CommandScheduler
{
    private static CommandScheduler mInstance;

    private Vector<Subsystem<?>> mSubsystems;
    private HashMap<BooleanSupplier, Command> mCommands;
    private Vector<Command> mScheduledCommands;

    private Vector<Command> mOldExecutedCommands = new Vector<>();
    private Vector<Command> mNewExecutedCommands = new Vector<>();

    private CommandScheduler()
    {
    }

    public void addSubsystem(Subsystem<?>... subsystems)
    {
        mSubsystems.addAll(List.of(subsystems));
    }

    public void add(BooleanSupplier trigger, Command command)
    {
        mCommands.put(trigger, command);
    }

    public void run()
    {
        // If the trigger was tripped and no other command has the same requirements, schedule the command
        for (HashMap.Entry<BooleanSupplier, Command> pair : mCommands.entrySet())
        {
            if (pair.getKey().getAsBoolean())
            {
                boolean componentNotBeingUsed = true;
                for (Command command : mScheduledCommands)
                {
                    for (Component usedComponent : command.getRequirements())
                    {
                        for (Component requiredComponent : pair.getValue().getRequirements())
                        {
                            if (usedComponent == requiredComponent)
                            {
                                componentNotBeingUsed = false;
                                break;
                            }
                        }
                    }
                }
                if (componentNotBeingUsed)
                    mScheduledCommands.add(pair.getValue());
            }
        }

        if (!DriverStation.getInstance().isDisabled())
        {
            for (Subsystem<?> subsystem : mSubsystems)
            {
                subsystem.periodic();
            }

            for (Command command : mScheduledCommands)
            {
                if (command.getState() == Command.State.NOT_STARTED)
                {
                    command.initialize();
                    command.execute();

                    if (!mOldExecutedCommands.contains(command))
                    {
                        Vector<String> componentNames = new Vector<>();
                        for (Component component : command.getRequirements())
                            componentNames.add(component.getName());

                        String message = "\"" + command.getName().toUpperCase() + "\" was executed";

                        if (command instanceof InstantCommand)
                            message += " (instantly)";
                        else if (command instanceof RunCommand)
                            message += " (ongoing)";

                        Logger.println(Logger.LogLevel.DEBUG, componentNames.toString(), message);
                    }
                    mNewExecutedCommands.add(command);
                } else if (command.isFinished())
                {
                    command.end();
                    mScheduledCommands.remove(command);
                }
            }
        } else
        {
            for (Command command : mScheduledCommands)
            {
                command.end();
                mScheduledCommands.remove(command);
            }
        }

        mOldExecutedCommands = (Vector<Command>) mNewExecutedCommands.clone();
        mNewExecutedCommands.clear();
    }

    public CommandScheduler getInstance()
    {
        return mInstance;
    }
}
