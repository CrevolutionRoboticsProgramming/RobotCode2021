package org.frc2851.robot;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.*;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import org.frc2851.robot.subsystems.Drivetrain;
import org.frc2851.robot.subsystems.ExampleSubsystem;
import org.frc2851.robot.util.CommandFactory;
import org.frc2851.robot.util.Logger;

import java.util.ArrayList;

public final class Robot extends TimedRobot
{
    private ArrayList<Command> mOldExecutedCommands = new ArrayList<>();
    private ArrayList<Command> mNewExecutedCommands = new ArrayList<>();
    private Drivetrain mDrivetarain = new Drivetrain();
    private ExampleSubsystem mExampleSubsystem = new ExampleSubsystem();

    public static void main(String... args)
    {
        RobotBase.startRobot(Robot::new);
    }

    @Override
    public void robotInit()
    {
        super.robotInit();

        // For every new command that was executed, print out something saying it was executed
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

        mExampleSubsystem = new ExampleSubsystem();
        new Trigger(() -> Constants.driverController.get(Constants.exampleSubsystemRunButton))
                .whenActive(CommandFactory.makeRunCommand(mExampleSubsystem::go, "go", mExampleSubsystem.getName(), mExampleSubsystem));
        new Trigger(() -> !Constants.driverController.get(Constants.exampleSubsystemRunButton))
                .whenActive(CommandFactory.makeRunCommand(mExampleSubsystem::stop, "stop", mExampleSubsystem.getName(), mExampleSubsystem));
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