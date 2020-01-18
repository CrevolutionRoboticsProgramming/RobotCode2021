package org.frc2851.robot;

import badlog.lib.BadLog;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.*;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import org.frc2851.robot.subsystems.Drivetrain;
import org.frc2851.robot.util.CommandFactory;
import org.frc2851.robot.util.Logger;
import org.frc2851.robot.util.UDPHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;

public final class Robot extends TimedRobot
{
    private ArrayList<Command> mOldExecutedCommands = new ArrayList<>();
    private ArrayList<Command> mNewExecutedCommands = new ArrayList<>();

    private double mLastGameDataSend = DriverStation.getInstance().getMatchTime();
    private boolean mFirstGameDataSend = true;

    private BadLog mBadLog;

    public static void main(String... args)
    {
        RobotBase.startRobot(Robot::new);
    }

    @Override
    public void robotInit()
    {
        initializeBadLog();

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

        Drivetrain drivetrain = new Drivetrain();
        new Trigger(() -> !Constants.driverController.get(Constants.drivetrainShiftGearButton))
                .whenActive(CommandFactory.makeRunCommand(drivetrain::setHighGear, "high gear", drivetrain.getName(), drivetrain));
        new Trigger(() -> Constants.driverController.get(Constants.drivetrainShiftGearButton))
                .whenActive(CommandFactory.makeRunCommand(drivetrain::setLowGear, "low gear", drivetrain.getName(), drivetrain));

        // Subsystem initializations

        BadLog.createValue("Match Number", String.valueOf(DriverStation.getInstance().getMatchNumber()));
        BadLog.createTopic("Match Time", "s", () -> DriverStation.getInstance().getMatchTime());
        mBadLog.finishInitialization();
    }

    @Override
    public void robotPeriodic()
    {
        mOldExecutedCommands = (ArrayList<Command>) mNewExecutedCommands.clone();
        mNewExecutedCommands.clear();

        String gameData;
        gameData = DriverStation.getInstance().getGameSpecificMessage();
        if (gameData.length() > 0 && mLastGameDataSend - DriverStation.getInstance().getMatchTime() >= 1)
        {
            if (mFirstGameDataSend)
            {
                Logger.println(Logger.LogLevel.DEBUG, "", "COLOR: " + gameData);
                mFirstGameDataSend = false;
            }
            if (!Constants.driverStationIP.equals(""))
                Constants.udpHandler.sendTo("COLOR:" + gameData.charAt(0), Constants.driverStationIP, Constants.sendPort);
            mLastGameDataSend = DriverStation.getInstance().getMatchTime();
        }

        CommandScheduler.getInstance().run();
    }

    private void initializeBadLog()
    {
        // TODO: Can the Rio keep an accurate time?
        LocalDateTime localDateTime = new Timestamp(System.currentTimeMillis()).toLocalDateTime();
        String date = localDateTime.toLocalDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
                .replace("/", "-");
        String time = localDateTime.toLocalTime().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM))
                .replace(":", "-").replace(" AM", "").replace(" PM", "");

        // Makes a new log in the "logs" directory in the directory where the application was executed
        File badLogRootDir = new File(Paths.get(System.getProperty("user.dir") + File.separator + "logs" + File.separator + "badlog").toString());
        File badLogFile = new File(badLogRootDir.getAbsolutePath() + File.separator + date, time + ".bag");

        badLogFile.mkdirs();

        try
        {
            // While there are still X or more files in the logs...
            while (Files.walk(badLogRootDir.toPath())
                .filter(Files::isRegularFile)
                .count() >= 20)
            {
                // Delete the oldest file
                Files.walk(badLogRootDir.toPath())
                        .filter(Files::isRegularFile)
                        .min((path1, path2) ->
                        {
                            try
                            {
                                return Files.getLastModifiedTime(path1).compareTo(Files.getLastModifiedTime(path2));
                            } catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                            return 0;
                        }).get().toFile().delete();
            }

            // Delete any empty folders
            Files.walk(badLogRootDir.toPath())
                    .filter(Files::isDirectory)
                    .forEach((path) ->
                    {
                        if (path.toFile().list().length == 0)
                            path.toFile().delete();
                    });
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        try
        {
            badLogFile.createNewFile();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        mBadLog = BadLog.init(badLogFile.getPath());
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