package org.frc2851.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.TimedRobot;
import org.frc2851.robot.framework.command.CommandScheduler;
import org.frc2851.robot.subsystems.*;
import org.frc2851.robot.util.Logger;
import org.frc2851.robot.util.UDPHandler;

public final class Robot extends TimedRobot
{
    private double mLastGameDataSend = 0;
    private boolean mFirstGameDataSend = true;

    public static void main(String... args)
    {
        RobotBase.startRobot(Robot::new);
    }

    @Override
    public void robotInit()
    {
        Constants.udpHandler.addReceiver(new UDPHandler.MessageReceiver("IP:", (message) -> Constants.driverStationIP = message));

        CommandScheduler.getInstance().addSubsystem(Drivetrain.getInstance());
        CommandScheduler.getInstance().addSubsystem(Intake.getInstance());
        CommandScheduler.getInstance().addSubsystem(Indexer.getInstance());
        CommandScheduler.getInstance().addSubsystem(Shooter.getInstance());
        CommandScheduler.getInstance().addSubsystem(Climber.getInstance());
        //CommandScheduler.getInstance().addSubsystem(Disker.getInstance());
    }

    @Override
    public void robotPeriodic()
    {
        String gameData;
        gameData = DriverStation.getInstance().getGameSpecificMessage();
        if (gameData.length() > 0 && System.currentTimeMillis() - mLastGameDataSend >= 1000)
        {
            if (mFirstGameDataSend)
            {
                Logger.println(Logger.LogLevel.DEBUG, "COLOR: " + gameData);
                mFirstGameDataSend = false;
            }
            if (!Constants.driverStationIP.equals(""))
                Constants.udpHandler.sendTo("COLOR:" + gameData.charAt(0), Constants.driverStationIP, Constants.sendPort);
            mLastGameDataSend = System.currentTimeMillis();
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