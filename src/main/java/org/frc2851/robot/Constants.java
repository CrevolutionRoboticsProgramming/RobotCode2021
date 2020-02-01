package org.frc2851.robot;

import edu.wpi.first.wpilibj.I2C;
import org.frc2851.robot.io.Controller;
import org.frc2851.robot.io.Axis;
import org.frc2851.robot.io.Button;
import org.frc2851.robot.util.UDPHandler;

public final class Constants
{
    private static final Controller driverController = new Controller(0);
    private static final Controller operatorController = new Controller(1);

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
    public static final Button drivetrainShiftGearButton = new Button(driverController, Button.ButtonID.LEFT_BUMPER, Button.ButtonBehaviorType.TOGGLE);

    // Intake
    public static final int intakeMotorPort = 7;
    public static final int intakeExtenderSolenoidForward = 2;
    public static final int intakeExtenderSolenoidReverse = 3;

    public static final Button intakeIntakeButton = new Button(driverController, Button.ButtonID.RIGHT_TRIGGER, Button.ButtonBehaviorType.RAW);
    public static final Button intakeOuttakeButton = new Button(driverController, Button.ButtonID.LEFT_TRIGGER, Button.ButtonBehaviorType.RAW);
    public static final Button intakeToggleExtendButton = new Button(driverController, Button.ButtonID.RIGHT_BUMPER, Button.ButtonBehaviorType.TOGGLE);

    // Conveyor
    public static final int conveyorSnailMotorPort = 8;
    public static final int conveyorElevatorMotorPort = 9;

    // Climber
    public static final int climberMasterPort = 10;
    public static final int climberFollowerPort = 11;

    public static final Button climberExtendButton = new Button(driverController, Button.ButtonID.X, Button.ButtonBehaviorType.RAW);
    public static final Button climberRetractButton = new Button(driverController, Button.ButtonID.Y, Button.ButtonBehaviorType.RAW);

    // Disker
    public static final int diskerMotorPort = 12;
    public static final I2C.Port diskerI2CPort = I2C.Port.kOnboard;

    public static final Button diskerForwardButton = new Button(driverController, Button.ButtonID.B, Button.ButtonBehaviorType.RAW);
    public static final Button diskerReverseButton = new Button(driverController, Button.ButtonID.A, Button.ButtonBehaviorType.RAW);

    // Shooter

    //    Turret
    public static final int shooterTurretPort = 13;

    public static final Button shooterTurretRotateClockwiseButton = new Button(operatorController, Button.ButtonID.POV_RIGHT, Button.ButtonBehaviorType.RAW);
    public static final Button shooterTurretRotateCounterClockwiseButton = new Button(operatorController, Button.ButtonID.POV_LEFT, Button.ButtonBehaviorType.RAW);

    //    Angler
    public static final int shooterAnglerPort = 14;

    public static final Button shooterAnglerRaiseButton = new Button(operatorController, Button.ButtonID.POV_UP, Button.ButtonBehaviorType.RAW);
    public static final Button shooterAnglerLowerButton = new Button(operatorController, Button.ButtonID.POV_DOWN, Button.ButtonBehaviorType.RAW);

    //    Launcher
    public static final int shooterLauncherLeftPort = 15;
    public static final int shooterLauncherRightPort = 16;

    public static final Button shooterLauncherShootButton = new Button(operatorController, Button.ButtonID.RIGHT_TRIGGER, Button.ButtonBehaviorType.RAW);
}
