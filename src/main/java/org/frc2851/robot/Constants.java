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
    public static final Button drivetrainShiftGearButton = new Button(driverController, Button.ButtonID.LEFT_BUMPER, Button.ButtonBehaviorType.TOGGLE);

    //Intake
    public static final int intakeRollBar = 7;

    public static final int intakeActuateSolenoidIn = 2;
    public static final int intakeActuateSolenoidOut = 3;

    public static final Button intakeRollBarTrigger = new Button(driverController, Button.ButtonID.RIGHT_TRIGGER, Button.ButtonBehaviorType.RAW);
    public static final Button outtakeRollBarTrigger = new Button(driverController, Button.ButtonID.LEFT_TRIGGER, Button.ButtonBehaviorType.RAW);
    public static final Button actuateIntakeBumper = new Button(driverController, Button.ButtonID.RIGHT_BUMPER, Button.ButtonBehaviorType.TOGGLE);
}
