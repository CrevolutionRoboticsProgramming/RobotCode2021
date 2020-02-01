package org.frc2851.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.util.Color;
import org.frc2851.robot.Constants;
import org.frc2851.robot.framework.Component;
import org.frc2851.robot.framework.Subsystem;
import org.frc2851.robot.framework.command.CommandScheduler;
import org.frc2851.robot.framework.command.InstantCommand;
import org.frc2851.robot.util.MotorControllerFactory;

public class Disker extends Subsystem
{
    private static Disker mInstance = new Disker();
    private static DiskerComponent mDiskerComponent;

    private Disker()
    {
        mDiskerComponent = new DiskerComponent();
        addComponents(mDiskerComponent);
    }

    public static Disker getInstance()
    {
        return mInstance;
    }

    private static class DiskerComponent extends Component
    {
        private VictorSPX mMotor;
        private ColorSensorV3 mColorSensor;
        private ColorMatch mColorMatcher;

        private final Color mBlueTarget = ColorMatch.makeColor(0, 255, 255);
        private final Color mGreenTarget = ColorMatch.makeColor(0, 255, 0);
        private final Color mRedTarget = ColorMatch.makeColor(255, 0, 0);
        private final Color mYellowTarget = ColorMatch.makeColor(255, 255, 0);

        public DiskerComponent()
        {
            super(Disker.class);

            mMotor = MotorControllerFactory.makeVictorSPX(Constants.diskerMotorPort);
            mColorSensor = new ColorSensorV3(Constants.diskerI2CPort);
            mColorMatcher = new ColorMatch();

            mColorMatcher.addColorMatch(mBlueTarget);
            mColorMatcher.addColorMatch(mGreenTarget);
            mColorMatcher.addColorMatch(mRedTarget);
            mColorMatcher.addColorMatch(mYellowTarget);

            CommandScheduler.getInstance().addTrigger(() -> mColorMatcher.matchClosestColor(mColorSensor.getColor()).confidence > 0.8,
                    new InstantCommand(() -> {}, "spin to color", this));
            CommandScheduler.getInstance().addTrigger(Constants.diskerForwardButton::get,
                    new InstantCommand(this::forward, "forward", this));
            CommandScheduler.getInstance().addTrigger(Constants.diskerReverseButton::get,
                    new InstantCommand(this::reverse, "reverse", this));

            setDefaultCommand(new InstantCommand(this::stop, "stop", this));
        }

        public void forward()
        {
            mMotor.set(ControlMode.PercentOutput, 1.0);
        }

        public void reverse()
        {
            mMotor.set(ControlMode.PercentOutput, -1.0);
        }

        public void stop()
        {
            mMotor.set(ControlMode.PercentOutput, 0.0);
        }
    }
}
