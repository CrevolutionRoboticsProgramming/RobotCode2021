package org.frc2851.robot;

import org.frc2851.robot.framework.trigger.RawTrigger;
import org.frc2851.robot.framework.trigger.ToggleTrigger;
import org.frc2851.robot.io.Axis;
import org.frc2851.robot.io.Button;
import org.frc2851.robot.io.Controller;
import org.frc2851.robot.util.UDPHandler;

public final class Constants
{
    public static final Controller driverController = new Controller(0);
    public static final Controller operatorController = new Controller(1);

    public static final UDPHandler udpHandler = new UDPHandler(1183);
    public static String driverStationIP = "";
    public static final int sendPort = 1182;

    public static final class Drivebase
    {
        public static final int leftLeaderPort = 1;
        public static final int leftFollowerAPort = 2;
        public static final int leftFollowerBPort = 3;
        public static final int rightLeaderPort = 4;
        public static final int rightFollowerAPort = 5;
        public static final int rightFollowerBPort = 6;

        public static final double deadband = 0.15;
        public static final double inchesPerRotation = 1.0; // TODO: tune
        public static final double kP = 0.0; // TODO: tune
        public static final double kI = 0.0; // TODO: tune
        public static final double kD = 0.0; // TODO: tune
        public static final double turnkP = 0.0; // TODO: tune

        public static final Axis throttleAxis = new Axis(driverController, Axis.AxisID.LEFT_Y, (input) -> -input); // Up on the controller is read as negative BrokeBack
        public static final Axis turnAxis = new Axis(driverController, Axis.AxisID.RIGHT_X);
    }

    public static final class GearShifter
    {
        public static final int forwardChannel = 0;
        public static final int reverseChannel = 7;

        public static final ToggleTrigger shiftGearTrigger = new ToggleTrigger(new Button(driverController, Button.ButtonID.LEFT_BUMPER)::get);
    }

    public static final class Extender
    {
        public static final int forwardChannel = 1;
        public static final int reverseChannel = 6;
        public static final ToggleTrigger extendTrigger = new ToggleTrigger(new Button(driverController, Button.ButtonID.RIGHT_BUMPER)::get);
    }

    public static final class RollBar
    {
        public static final int motorPort = 7;
        public static final RawTrigger intakeTrigger = new RawTrigger(new Button(driverController, Button.ButtonID.RIGHT_TRIGGER)::get);
        public static final RawTrigger outtakeTrigger = new RawTrigger(new Button(driverController, Button.ButtonID.LEFT_TRIGGER)::get);
    }

    public static final class Indexer
    {
        public static final RawTrigger feedShooterTrigger = new RawTrigger(new Button(operatorController, Button.ButtonID.RIGHT_BUMPER)::get);
    }

    public static final class Snail
    {
        public static final int motorPort = 8;
    }

    public static final class Elevator
    {
        public static final int motorPort = 9;
    }

    public static final class Shooter
    {
        public static final double deadband = 0.2;
        public static final Button enableVisionTrackingTrigger = new Button(operatorController, Button.ButtonID.LEFT_TRIGGER);
    }

    public static final class Turret
    {
        public static final int motorPort = 11;
        public static final int absoluteEncoderDIOPort = 0;
        public static final int limitSwitchPort = 2;

        public static final double kP = 0.0; // TODO: tune

        public static final Axis directDriveAxis = new Axis(operatorController, Axis.AxisID.RIGHT_X);
    }

    public static final class Angler
    {
        public static final int motorPort = 12;
        public static final int absoluteEncoderDIOPort = 1;
        public static final int limitSwitchPort = 3;

        public static final double kP = 0.0; // TODO: tune

        public static final Axis directDriveAxis = new Axis(operatorController, Axis.AxisID.LEFT_Y);
    }

    public static final class Launcher
    {
        public static final int leaderMotorPort = 13;
        public static final int followerMotorPort = 14;

        public static final Axis directDriveAxis = new Axis(operatorController, Axis.AxisID.RIGHT_Y);//TRIGGER, (input) -> -input);
        public static final RawTrigger shooterLauncher25PercentShooterTrigger = new RawTrigger(new Button(operatorController, Button.ButtonID.A)::get);
        public static final RawTrigger shooterLauncher50PercentShooterTrigger = new RawTrigger(new Button(operatorController, Button.ButtonID.B)::get);
        public static final RawTrigger shooterLauncher75PercentShooterTrigger = new RawTrigger(new Button(operatorController, Button.ButtonID.X)::get);
        public static final RawTrigger shooterLauncher100PercentShooterTrigger = new RawTrigger(new Button(operatorController, Button.ButtonID.Y)::get);

        // Measured in milliseconds
        public static final double spinUpTime = 2000;
    }
}