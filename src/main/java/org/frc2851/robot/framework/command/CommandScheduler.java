package org.frc2851.robot.framework.command;

import edu.wpi.first.wpilibj.DriverStation;
import org.frc2851.robot.framework.Component;
import org.frc2851.robot.framework.Subsystem;
import org.frc2851.robot.util.Logger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.function.BooleanSupplier;

public final class CommandScheduler
{
    private static CommandScheduler mInstance = new CommandScheduler();

    private Vector<Subsystem> mSubsystems;
    private HashMap<BooleanSupplier, Command> mCommands;
    private Vector<Command> mScheduledCommands;

    private Vector<Command> mOldExecutedCommands;
    private Vector<Command> mNewExecutedCommands;

    private CommandScheduler()
    {
        mSubsystems = new Vector<>();
        mCommands = new HashMap<>();
        mScheduledCommands = new Vector<>();
        mOldExecutedCommands = new Vector<>();
        mNewExecutedCommands = new Vector<>();
    }

    public static CommandScheduler getInstance()
    {
        return mInstance;
    }

    public void addSubsystem(Subsystem... subsystems)
    {
        mSubsystems.addAll(List.of(subsystems));
    }

    public void addTrigger(BooleanSupplier trigger, Command command)
    {
        if (trigger != null && command != null)
        {
            System.out.println("Registered command " + command.getName());
            mCommands.put(trigger, command);
        }
    }

    public void schedule(Command command)
    {
        mScheduledCommands.add(command);
    }

    public void run()
    {
        if (DriverStation.getInstance().isDisabled())
        {
            for (Command command : mScheduledCommands)
                command.end();
            mScheduledCommands.clear();
            return;
        }

        // If the trigger was tripped and no other command has the same requirements, schedule the command
        for (HashMap.Entry<BooleanSupplier, Command> pair : mCommands.entrySet())
        {
            if (pair.getKey().getAsBoolean())
            {
                boolean componentNotBeingUsed = true;

                Iterator<Command> scheduledCommandsIterator = mScheduledCommands.iterator();
                while (scheduledCommandsIterator.hasNext())
                {
                    Command scheduledCommand = scheduledCommandsIterator.next();
                    if (pair.getValue() == scheduledCommand)
                        continue;

                    for (Component requiredComponent : pair.getValue().getRequirements())
                    {
                        for (Component usedComponent : scheduledCommand.getRequirements())
                        {
                            if (requiredComponent == usedComponent)
                            {
                                if (scheduledCommand.isInterruptible())
                                {
                                    scheduledCommand.end();
                                    scheduledCommandsIterator.remove();
                                } else
                                {
                                    componentNotBeingUsed = false;
                                    break;
                                }
                            }
                        }
                    }
                }
                if (componentNotBeingUsed)
                    schedule(pair.getValue());
            }
        }

        for (Subsystem subsystem : mSubsystems)
        {
            for (Component component : subsystem.getComponents())
            {
                component.periodic();

                boolean componentIsNotBeingUsed = true;
                for (Command command : mScheduledCommands)
                {
                    for (Component componentInUse : command.getRequirements())
                    {
                        if (componentInUse == component)
                        {
                            componentIsNotBeingUsed = false;
                            break;
                        }
                    }
                }

                if (componentIsNotBeingUsed && component.getDefaultCommand() != null)
                    schedule(component.getDefaultCommand());
            }
        }

        Iterator<Command> scheduledCommandsIterator = mScheduledCommands.iterator();
        while (scheduledCommandsIterator.hasNext())
        {
            Command command = scheduledCommandsIterator.next();

            if (command.getState() == Command.State.NOT_STARTED)
                command.initialize();

            command.execute();

            if (!mOldExecutedCommands.contains(command))
            {
                Vector<String> componentNames = new Vector<>();
                for (Component component : command.getRequirements())
                    componentNames.add(component.getName());

                String message = command.getName().toUpperCase() + " was executed";

                if (command instanceof InstantCommand)
                    message += " (instantly)";
                else if (command instanceof RunCommand)
                    message += " (ongoing)";

                Logger.println(Logger.LogLevel.DEBUG, componentNames.toString(), message);
            }
            mNewExecutedCommands.add(command);

            if (command.isFinished())
            {
                command.end();
                scheduledCommandsIterator.remove();
            }
        }

        mOldExecutedCommands = (Vector<Command>) mNewExecutedCommands.clone();
        mNewExecutedCommands.clear();
    }
}
