package org.frc2851.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;
import org.frc2851.robot.Constants;
import org.frc2851.robot.Robot;
import org.frc2851.robot.framework.Component;
import org.frc2851.robot.framework.Subsystem;
import org.frc2851.robot.framework.command.RunCommand;
import org.frc2851.robot.util.MotorControllerFactory;

public class Disker extends Subsystem
{
    private static Disker disker = new Disker();

    private Disker()
    {
        addComponents(new DiskerMotorComponent(), new DiskerColorSensorComponent());
    }

    public static Disker getInstance()
    {
        return disker;
    }

    public class DiskerMotorComponent extends Component
    {
        private TalonSRX mRotatorMotator;
        private RotationMode mMode = RotationMode.CONTROL;

        private double mRotationSpeed = 0.25; //Fastest allowed = 82%

        public DiskerMotorComponent()
        {
            super(Disker.class);

            mRotatorMotator = MotorControllerFactory.makeTalonSRX(Constants.diskerRotatorPort);
            mRotatorMotator.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);

            setDefaultCommand(new RunCommand(this::rotateDisker, "rotate disker", this));
        }

        public void rotateDisker()
        {
            if (mMode == RotationMode.CONTROL)
            {
                boolean clockwise = Constants.diskerRotateClockwiseButton.get();
                boolean counter = Constants.diskerRotateCounterButton.get();

                //Legit no idea if negative is clockwise or positive is clockwise
                if (clockwise && counter)
                    mRotatorMotator.set(ControlMode.PercentOutput, 0);
                else if (clockwise)
                    mRotatorMotator.set(ControlMode.PercentOutput, mRotationSpeed);
                else if (counter)
                    mRotatorMotator.set(ControlMode.PercentOutput, -mRotationSpeed);
                else
                    mRotatorMotator.set(ControlMode.PercentOutput, 0);

                if (Constants.diskerRotateThriceButton.get())
                {
                    mMode = RotationMode.THRICE;
                    mRotatorMotator.setSelectedSensorPosition(0);
                    mRotatorMotator.set(ControlMode.PercentOutput, mRotationSpeed);
                }
            } else
            {
                boolean finished;
                if (Robot.isReal())
                    finished = mRotatorMotator.getSelectedSensorPosition() >= 65536;
                else
                    finished = mRotatorMotator.getSelectedSensorPosition() >= 65536 * 3;

                if (finished)
                {
                    mRotatorMotator.set(ControlMode.PercentOutput, 0);
                    mMode = RotationMode.CONTROL;
                }
            }
        }
    }

    public class DiskerColorSensorComponent extends Component
    {
        private ColorSensor mColorSensor;

        public DiskerColorSensorComponent()
        {
            super(Disker.class);

            mColorSensor = new ColorSensor();

            setDefaultCommand(new RunCommand(this::checkColor, "disker color checker", this));
        }

        public void checkColor()
        {
            //System.out.println("I2C Port: " + I2C.Port.kOnboard);
            //System.out.println("Color sensor rgb reading:  (" + sensor.red() + ", " + sensor.green() + ", " + sensor.blue() + ")");
        }
    }

    public class ColorSensor
    {
        private I2C.Port mPort;
        private ColorSensorV3 mSensor;
        private ColorMatch mMatch = new ColorMatch();
        private Color mRed = new Color(255, 0, 0);
        private Color mGreen = new Color(0, 255, 0);
        private Color mBlue = new Color(0, 255, 255);
        private Color mYellow = new Color(255, 255, 0);

        public ColorSensor()
        {
            mPort = I2C.Port.kOnboard;
            mSensor = new ColorSensorV3(mPort);
            mMatch.addColorMatch(mRed);
            mMatch.addColorMatch(mGreen);
            mMatch.addColorMatch(mBlue);
            mMatch.addColorMatch(mYellow);

            //match.matchClosestColor(sensor.getColor()).confidence;
        }

        public boolean isMatched(Color color)
        {
            return mMatch.matchClosestColor(color).confidence > 0.75;
        }
    }

    public enum RotationMode
    {
        CONTROL,
        THRICE
    }
}