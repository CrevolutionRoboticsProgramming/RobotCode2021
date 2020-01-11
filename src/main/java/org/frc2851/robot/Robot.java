package org.frc2851.robot;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.*;
import org.frc2851.robot.util.Logger;

import java.util.ArrayList;

public final class Robot extends TimedRobot
{
    private ArrayList<Command> mOldExecutedCommands = new ArrayList<>();
    private ArrayList<Command> mNewExecutedCommands = new ArrayList<>();

    public static void main(String... args)
    {
        RobotBase.startRobot(Robot::new);
    }

    @Override
    public void robotInit()
    {
        super.robotInit();

        CommandScheduler.getInstance().onCommandExecute(command ->
        {
            if (!mOldExecutedCommands.contains(command))
            {
                ArrayList<String> subsystemNames = new ArrayList<>();
                for (Subsystem subsystem : command.getRequirements())
                    subsystemNames.add(subsystem.getClass().getSimpleName());

                String message = "\"" + command.getName().toUpperCase() + "\" was executed";

                if (command instanceof InstantCommand)
                    message = message.concat(" (instantly)");
                else if (command instanceof RunCommand)
                    message = message.concat(" (ongoing)");

                Logger.println(Logger.LogLevel.DEBUG, subsystemNames.toString(), message);
            }
            mNewExecutedCommands.add(command);
        });
    }

    @Override
    public void robotPeriodic()
    {
        mOldExecutedCommands = (ArrayList<Command>) mNewExecutedCommands.clone();
        mNewExecutedCommands.clear();

        CommandScheduler.getInstance().run();
    }

    @Override
    public void disabledInit()
    {
    }

    @Override
    public void disabledPeriodic()
    {
    }

    @Override
    public void autonomousInit()
    {
    }

    @Override
    public void autonomousPeriodic()
    {
    }

    @Override
    public void teleopInit()
    {
    }

    @Override
    public void teleopPeriodic()
    {
    }

    @Override
    public void testPeriodic()
    {
    }
}