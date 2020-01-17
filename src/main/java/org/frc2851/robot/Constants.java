package org.frc2851.robot;

import org.frc2851.robot.io.Controller;
import org.frc2851.robot.io.axis.Axis;
import org.frc2851.robot.util.UDPHandler;

public final class Constants
{
    public static final Controller driverController = new Controller(0);
    public static final Controller operatorController = new Controller(1);

    public static final UDPHandler udpHandler = new UDPHandler(1184);
    public static String driverStationIP = "";
    public static final int sendPort = 1182;

    // DriveTrain
    public static final int driveTrainLeftMasterPort = 1;
    public static final int driveTrainLeftFollowerAPort = 2;
    public static final int driveTrainLeftFollowerBPort = 3;
    public static final int driveTrainRightMasterPort = 4;
    public static final int driveTrainRightFollowerAPort = 5;
    public static final int driveTrainRightFollowerBPort = 6;

    public static final Axis driveTrainThrottleAxis = new Axis(Axis.AxisID.LEFT_Y, (input) -> -input); // Up on the controller is read as negative BrokeBack
    public static final Axis driveTrainTurnAxis = new Axis(Axis.AxisID.RIGHT_X);
}
