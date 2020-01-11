package org.frc2851.robot.io;

import edu.wpi.first.wpilibj.Joystick;
import org.frc2851.robot.io.axis.Axis;
import org.frc2851.robot.io.button.Button;

public class Controller
{
    private Joystick mJoystick;
    private double mAxisThreshold = 0.15;
    private double mDeadband = 0.02;

    public Controller(int channel)
    {
        mJoystick = new Joystick(channel);
    }

    public boolean get(Button button)
    {
        switch (button.getButtonType())
        {
            case NORMAL:
                return button.get(mJoystick.getRawButton(button.getID()));
            case TRIGGER:
                return Math.abs(mJoystick.getRawAxis(button.getID())) > mAxisThreshold;
            case POV:
                return mJoystick.getPOV() == button.getID();
        }

        return button.get(mJoystick.getRawButton(button.getID()));
    }

    public double get(Axis axis)
    {
        return deadband(axis.get(mJoystick.getRawAxis(axis.getID())));
    }

    private double deadband(double input)
    {
        return Math.abs(input) > mDeadband ? input : 0;
    }
}
