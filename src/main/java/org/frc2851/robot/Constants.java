package org.frc2851.robot;

import org.frc2851.robot.io.Controller;
import org.frc2851.robot.io.axis.Axis;
import org.frc2851.robot.io.button.*;
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
    public static final ToggleButton drivetrainShiftGearButton = new ToggleButton(driverController, Button.ButtonID.LEFT_BUMPER);

    // Intake
    public static final int intakeMotorPort = 7;

    public static final int intakeExtendSolenoidForward = 2;
    public static final int intakeExtendSolenoidReverse = 3;

    public static final RawButton intakeIntakeButton = new RawButton(driverController, Button.ButtonID.RIGHT_TRIGGER);
    public static final RawButton intakeOuttakeButton = new RawButton(driverController, Button.ButtonID.LEFT_TRIGGER);
    public static final OnPressButton intakeExtendButton = new OnPressButton(driverController, Button.ButtonID.RIGHT_BUMPER);

    // Indexer
    public static final int indexerSnailMotorPort = 8;
    public static final int indexerElevatorMotorPort = 10;

    /* Shooter */

    //    Turret
    public static final int shooterTurretPort = 11;
    public static final int shooterTurretLimitSwitchPort = 0;

    public static final Axis shooterTurretRotateAxis = new Axis(operatorController, Axis.AxisID.RIGHT_X);

    //    Angler
    public static final int shooterAnglerPort = 12;
    public static final int shooterAnglerLimitSwitchPort = 1;

    public static final Axis shooterAnglerAxis = new Axis(operatorController, Axis.AxisID.LEFT_Y);

    //    Launcher
    public static final int shooterLauncherPort = 13;

    public static final RawButton shooterLauncherShootButton = new RawButton(operatorController, Button.ButtonID.RIGHT_TRIGGER);

    // Disker
    public static final int diskerRotatorPort = 9;

    public static final RawButton diskerRotateCounterButton = new RawButton(operatorController, Button.ButtonID.A);
    public static final RawButton diskerRotateClockwiseButton = new RawButton(operatorController, Button.ButtonID.B);
    public static final OnPressButton diskerRotateThriceButton = new OnPressButton(operatorController, Button.ButtonID.X);
    public static final OnPressButton diskerRotateFindButton = new OnPressButton(operatorController, Button.ButtonID.Y);

    // Climber
    public static final int climberMaster = 14;

    public static final RawButton climberExtendButton = new RawButton(driverController, Button.ButtonID.X);
    public static final RawButton climberRetractButton = new RawButton(driverController, Button.ButtonID.Y);
}