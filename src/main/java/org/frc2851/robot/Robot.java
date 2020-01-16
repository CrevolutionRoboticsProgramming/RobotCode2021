package org.frc2851.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.*;
import org.frc2851.robot.subsystems.Drivetrain;
import org.frc2851.robot.util.Logger;
import org.frc2851.robot.util.UDPHandler;

import java.util.ArrayList;

public final class Robot extends TimedRobot
{
    private ArrayList<Command> mOldExecutedCommands = new ArrayList<>();
    private ArrayList<Command> mNewExecutedCommands = new ArrayList<>();

    private Drivetrain mDrivetrain;

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
                    message += " (instantly)";
                else if (command instanceof RunCommand)
                    message += " (ongoing)";

                Logger.println(Logger.LogLevel.DEBUG, subsystemNames.toString(), message);
            }
            mNewExecutedCommands.add(command);
        });

        Constants.udpHandler.addReceiver(new UDPHandler.MessageReceiver("IP:", (message) -> Constants.driverStationIP = message));

        mDrivetrain = new Drivetrain();
    }

    @Override
    public void robotPeriodic()
    {
        mOldExecutedCommands = (ArrayList<Command>) mNewExecutedCommands.clone();
        mNewExecutedCommands.clear();

        String gameData;
        gameData = DriverStation.getInstance().getGameSpecificMessage();
        if (gameData.length() > 0)
        {
            Constants.udpHandler.sendTo("COLOR:" + gameData.charAt(0), Constants.driverStationIP, Constants.sendPort);
        }

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