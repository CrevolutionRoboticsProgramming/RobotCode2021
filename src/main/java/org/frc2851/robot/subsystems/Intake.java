package org.frc2851.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.frc2851.robot.Constants;
import org.frc2851.robot.util.CommandFactory;

public class Intake extends SubsystemBase
{
    private VictorSPX mMotor;
    private DoubleSolenoid mExtenderSolenoid;

    public Intake()
    {
        super();

        mMotor = new VictorSPX(Constants.intakeMotorPort);
        mExtenderSolenoid = new DoubleSolenoid(Constants.intakeExtenderSolenoidForward, Constants.intakeExtenderSolenoidReverse);

        setDefaultCommand(CommandFactory.makeRunCommand(this::stop, "stop", getName(), this));
    }

    public void stop()
    {
        mMotor.set(ControlMode.PercentOutput, 0.0);
    }

    public void intake()
    {
        mMotor.set(ControlMode.PercentOutput, 1.0);
    }

    public void outtake()
    {
        mMotor.set(ControlMode.PercentOutput, 0.0);
    }

    public void extend()
    {
        mExtenderSolenoid.set(DoubleSolenoid.Value.kForward);
    }

    public void retract()
    {
        mExtenderSolenoid.set(DoubleSolenoid.Value.kReverse);
    }
}
