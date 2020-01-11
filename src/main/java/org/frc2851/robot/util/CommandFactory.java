package org.frc2851.robot.util;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;

public class CommandFactory
{
    public static InstantCommand makeInstantCommand(Runnable toRun, String name, String subsystem, Subsystem... requirements)
    {
        InstantCommand returnCommand = new InstantCommand(toRun, requirements);
        returnCommand.setName(name);
        returnCommand.setSubsystem(subsystem);
        return returnCommand;
    }

    public static RunCommand makeRunCommand(Runnable toRun, String name, String subsystem, Subsystem... requirements)
    {
        RunCommand returnCommand = new RunCommand(toRun, requirements);
        returnCommand.setName(name);
        returnCommand.setSubsystem(subsystem);
        return returnCommand;
    }
}
