package org.frc2851.robot;

import org.frc2851.robot.io.Controller;
import org.frc2851.robot.io.Axis;
import org.frc2851.robot.io.Button;
import org.frc2851.robot.util.UDPHandler;

public final class Constants
{
    public static final Controller driverController = new Controller(0);
    public static final Controller operatorController = new Controller(1);

    public static final UDPHandler udpHandler = new UDPHandler(1184);
    public static String driverStationIP = "";
    public static final int sendPort = 1182;

    // Drivetrain
    public static final int drivetrainLeftMasterPort = 1;
    public static final int drivetrainLeftFollowerAPort = 2;
    public static final int drivetrainLeftFollowerBPort = 3;
    public static final int drivetrainRightMasterPort = 4;
    public static final int drivetrainRightFollowerAPort = 5;
    public static final int drivetrainRightFollowerBPort = 6;

    public static final int drivetrainShifterSolenoidForward = 0;
    public static final int drivetrainShifterSolenoidReverse = 1;

    public static final Axis drivetrainThrottleAxis = new Axis(driverController, Axis.AxisID.LEFT_Y, (input) -> -input); // Up on the controller is read as negative BrokeBack
    public static final Axis drivetrainTurnAxis = new Axis(driverController, Axis.AxisID.RIGHT_X);
    public static final Button drivetrainShiftGearButton = new Button(driverController, Button.ButtonID.LEFT_BUMPER, new Button.ToggleButton());

    // Intake
    public static final int intakeMotorPort = 7;

    public static final int intakeExtendSolenoidForward = 2;
    public static final int intakeExtendSolenoidReverse = 3;

    public static final Button intakeIntakeButton = new Button(driverController, Button.ButtonID.RIGHT_TRIGGER, new Button.RawButton());
    public static final Button intakeOuttakeButton = new Button(driverController, Button.ButtonID.LEFT_TRIGGER, new Button.RawButton());
    public static final Button intakeExtendButton = new Button(driverController, Button.ButtonID.RIGHT_BUMPER, new Button.ToggleButton());

    // Shooter

    //    Turret
    public static final int shooterTurretPort = 13;

    public static final Button shooterTurretRotateClockwiseButton = new Button(operatorController, Button.ButtonID.POV_RIGHT, new Button.RawButton());
    public static final Button shooterTurretRotateCounterClockwiseButton = new Button(operatorController, Button.ButtonID.POV_LEFT, new Button.RawButton());

    //    Angler
    public static final int shooterAnglerPort = 14;

    public static final Button shooterAnglerRaiseButton = new Button(operatorController, Button.ButtonID.POV_UP, new Button.RawButton());
    public static final Button shooterAnglerLowerButton = new Button(operatorController, Button.ButtonID.POV_DOWN, new Button.RawButton());

    //    Launcher
    public static final int shooterLauncherPort = 15;

    public static final Button shooterLauncherShootButton = new Button(operatorController, Button.ButtonID.RIGHT_TRIGGER, new Button.RawButton());

    // Climber
    public static final int climberMaster = 7;

    public static final Button climberExtendButton = new Button(driverController, Button.ButtonID.X, new Button.RawButton());
    public static final Button climberRetractButton = new Button(driverController, Button.ButtonID.Y, new Button.RawButton());
}