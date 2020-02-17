package org.frc2851.robot;

import org.frc2851.robot.framework.command.Trigger;
import org.frc2851.robot.io.Axis;
import org.frc2851.robot.io.Button;
import org.frc2851.robot.io.Controller;
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
    public static final int drivetrainShifterSolenoidReverse = 7;

    public static final double drivetrainDeadband = 0.10;

    public static final Axis drivetrainThrottleAxis = new Axis(driverController, Axis.AxisID.LEFT_Y, (input) -> -input); // Up on the controller is read as negative BrokeBack
    public static final Axis drivetrainTurnAxis = new Axis(driverController, Axis.AxisID.RIGHT_X);
    public static final Trigger drivetrainShiftGearTrigger = new Trigger(new Trigger.Toggle(), new Button(driverController, Button.ButtonID.LEFT_BUMPER)::get);

    // Intake
    public static final int intakeMotorPort = 7;

    public static final int intakeExtendSolenoidForward = 1;
    public static final int intakeExtendSolenoidReverse = 6;

    public static final Trigger intakeIntakeTrigger = new Trigger(new Trigger.Raw(), new Button(driverController, Button.ButtonID.RIGHT_TRIGGER)::get);
    public static final Trigger intakeOuttakeTrigger = new Trigger(new Trigger.Raw(), new Button(driverController, Button.ButtonID.LEFT_TRIGGER)::get);
    public static final Trigger intakeExtendTrigger = new Trigger(new Trigger.Toggle(), new Button(driverController, Button.ButtonID.RIGHT_BUMPER)::get);

    // Indexer
    public static final int indexerSnailMotorPort = 8;
    public static final int indexerElevatorMotorPort = 10;

    /* Shooter */
    public static final double shooterDeadband = 0.10;

    public static final Button shooterEnableVisionTracking = new Button(operatorController, Button.ButtonID.LEFT_TRIGGER);

    //    Turret
    public static final int shooterTurretPort = 11;
    public static final int shooterTurretLimitSwitchPort = 0;

    public static final double shooterTurretKP = 0.0; // TODO: tune

    public static final Axis shooterTurretRotateAxis = new Axis(operatorController, Axis.AxisID.RIGHT_X);

    //    Angler
    public static final int shooterAnglerPort = 12;
    public static final int shooterAnglerLimitSwitchPort = 1;

    public static final double shooterAnglerKP = 0.0; // TODO: tune

    public static final Axis shooterAnglerAxis = new Axis(operatorController, Axis.AxisID.LEFT_Y);

    //    Launcher
    public static final int shooterLauncherPort = 13;

    public static final Trigger shooterLauncherShootTrigger = new Trigger(new Trigger.Raw(), new Button(operatorController, Button.ButtonID.RIGHT_TRIGGER)::get);

    // Disker
    public static final int diskerRotatorPort = 9;

    public static final Trigger diskerRotateCounterTrigger = new Trigger(new Trigger.Raw(), new Button(operatorController, Button.ButtonID.A)::get);
    public static final Trigger diskerRotateClockwiseTrigger = new Trigger(new Trigger.Raw(), new Button(operatorController, Button.ButtonID.B)::get);
    public static final Trigger diskerRotateThriceTrigger = new Trigger(new Trigger.OnPress(), new Button(operatorController, Button.ButtonID.X)::get);
    public static final Trigger diskerRotateFindTrigger = new Trigger(new Trigger.OnPress(), new Button(operatorController, Button.ButtonID.Y)::get);

    // Climber
    public static final int climberSolenoidForward = 2;
    public static final int climberSolenoidReverse = 5;

    public static final Trigger climberExtendTrigger = new Trigger(new Trigger.OnPress(), new Button(driverController, Button.ButtonID.X)::get);
    public static final Trigger climberRetractTrigger = new Trigger(new Trigger.OnPress(), new Button(driverController, Button.ButtonID.Y)::get);
}