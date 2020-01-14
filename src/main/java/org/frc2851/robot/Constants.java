package org.frc2851.robot;

import org.frc2851.robot.io.Controller;
import org.frc2851.robot.io.axis.Axis;
import org.frc2851.robot.io.button.Button;

public final class Constants
{
    public static final Controller driverController = new Controller(0);
    public static final Controller operatorController = new Controller(1);

    public static final int exampleSubsystemPort = 0;

    public static final Button exampleSubsystemRunButton = new Button(Button.ButtonID.A, Button.ButtonBehaviorType.RAW);
    public static final Axis y = new Axis(Axis.AxisID.LEFT_Y);
    public static final Axis x = new Axis(Axis.AxisID.RIGHT_X);
}
