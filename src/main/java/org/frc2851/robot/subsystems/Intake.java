package org.frc2851.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.frc2851.robot.Constants;
import org.frc2851.robot.framework.Component;
import org.frc2851.robot.framework.Subsystem;
import org.frc2851.robot.framework.command.CommandScheduler;
import org.frc2851.robot.framework.command.InstantCommand;
import org.frc2851.robot.framework.command.RunCommand;

public class Intake extends Subsystem
{
    private static Intake mInstance = new Intake();
    
    private Intake()
    {
        super(new Motor(), new Extender());
    }
    
    public static Intake getInstance()
    {
        return mInstance;
    }

    private static class Motor extends Component
    {
        private VictorSPX mMotor;

        public Motor()
        {
            mMotor = new VictorSPX(Constants.intakeMotorPort);

            CommandScheduler.getInstance().addTrigger(() -> Constants.driverController.get(Constants.intakeIntakeButton),
                    new RunCommand(this::intake, "intake", this));
            CommandScheduler.getInstance().addTrigger(() -> Constants.driverController.get(Constants.intakeOuttakeButton),
                    new RunCommand(this::outtake, "outtake", this));

            setDefaultCommand(new RunCommand(this::stop, "stop", this));
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
            mMotor.set(ControlMode.PercentOutput, -1.0);
        }
    }

    private static class Extender extends Component
    {
        private DoubleSolenoid mExtenderSolenoid;

        public Extender()
        {
            mExtenderSolenoid = new DoubleSolenoid(Constants.intakeExtenderSolenoidForward, Constants.intakeExtenderSolenoidReverse);

            CommandScheduler.getInstance().addTrigger(() -> !Constants.driverController.get(Constants.intakeToggleExtendButton),
                    new InstantCommand(this::retract, "retract", this));
            CommandScheduler.getInstance().addTrigger(() -> Constants.driverController.get(Constants.intakeToggleExtendButton),
                    new InstantCommand(this::extend, "extend", this));
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
}
