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
        addComponents(new DiskerComponent());
    }

    public static Disker getInstance()
    {
        return disker;
    }



    public class DiskerComponent extends Component
    {
        private TalonSRX mRotatorMotator;
        private ColorSensor sensor;
        private Color target;
        private RotationMode mMode = RotationMode.CONTROL;

        private double mRotationSpeed = 0.25; //Fastest allowed = 82%
        private double mColorFinderSpeed = 0.10; //Take it back now yall

        public DiskerComponent()
        {
            super(Disker.class);

            mRotatorMotator = MotorControllerFactory.makeTalonSRX(Constants.diskerRotatorPort);
            mRotatorMotator.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);

            sensor = new ColorSensor();
            target = sensor.mRed; //Set to look for red by default

            setDefaultCommand(new RunCommand(this::rotateDisker, "rotate disker", this));
        }



        public void setTargetColor(Color color) {
            this.target = color;
        }



        public void rotateDisker()
        {
            if(mMode == RotationMode.CONTROL) {
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

                //Switch to thrice or find rotation mode on press of the disker rotate thrice/find button
                if (Constants.diskerRotateThriceButton.get())
                {
                    mMode = RotationMode.THRICE;
                    mRotatorMotator.setSelectedSensorPosition(0);
                    mRotatorMotator.set(ControlMode.PercentOutput, mRotationSpeed);
                }
                else if (Constants.diskerRotateFindButton.get())
                {
                    mMode = RotationMode.FIND;
                    mRotatorMotator.setSelectedSensorPosition(0);
                    mRotatorMotator.set(ControlMode.PercentOutput, mColorFinderSpeed);
                }
            }
            else if (mMode == RotationMode.THRICE)
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
            else if (mMode == RotationMode.FIND)
            {
                boolean finished;
                if (Robot.isReal())
                    finished = (sensor.isMatched() && sensor.getMatch().equals(target));
                else
                    finished = true;

                if (finished)
                {
                    mRotatorMotator.set(ControlMode.PercentOutput, 0);
                    mMode = RotationMode.CONTROL;
                }
            }
        }
    }



    public class ColorSensor
    {
        private I2C.Port mPort;
        private ColorSensorV3 mSensor;
        private ColorMatch mMatch = new ColorMatch();
        public final Color mRed = new Color(255, 0, 0);
        public final Color mGreen = new Color(0, 255, 0);
        public final Color mBlue = new Color(0, 255, 255);
        public final Color mYellow = new Color(255, 255, 0);

        public ColorSensor()
        {
            mPort = I2C.Port.kOnboard;
            mSensor = new ColorSensorV3(mPort);
            mMatch.addColorMatch(mRed);
            mMatch.addColorMatch(mGreen);
            mMatch.addColorMatch(mBlue);
            mMatch.addColorMatch(mYellow);
        }



        public boolean isMatched()
        {
            return mMatch.matchClosestColor(mSensor.getColor()).confidence > 0.75;
        }

        public Color getMatch()
        {
            return mMatch.matchClosestColor(mSensor.getColor()).color;
        }

    }



    public enum RotationMode {
        CONTROL,
        THRICE,
        FIND;
    }

}