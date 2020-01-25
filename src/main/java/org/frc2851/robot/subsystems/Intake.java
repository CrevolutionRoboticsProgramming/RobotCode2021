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
import org.frc2851.robot.util.MotorControllerFactory;

public class Intake extends Subsystem
{
    private static Intake mInstance = new Intake();

    private Intake()
    {
        addComponents(new RollBar(), new Extender());
    }

    public static Intake getInstance()
    {
        return mInstance;
    }

    public static class RollBar extends Component
    {
        private VictorSPX mMotor;

        public RollBar()
        {
            super(Intake.class);

            mMotor = MotorControllerFactory.makeVictorSPX(Constants.intakeMotorPort);

            CommandScheduler.getInstance().addTrigger(Constants.intakeIntakeButton::get,
                    new InstantCommand(this::intake, "intake", this));
            CommandScheduler.getInstance().addTrigger(Constants.intakeOuttakeButton::get,
                    new InstantCommand(this::outtake, "outtake", this));
            setDefaultCommand(new RunCommand(this::stop, "stop", this));
        }

        private void intake()
        {
            mMotor.set(ControlMode.PercentOutput, 1.0);
        }

        private void outtake()
        {
            mMotor.set(ControlMode.PercentOutput, -1.0);
        }

        private void stop()
        {
            mMotor.set(ControlMode.PercentOutput, 0.0);
        }
    }

    public static class Extender extends Component
    {
        private DoubleSolenoid mExtenderSolenoid;

        public Extender()
        {
            super(Intake.class);

            mExtenderSolenoid = new DoubleSolenoid(Constants.intakeExtendSolenoidForward, Constants.intakeExtendSolenoidReverse);

            CommandScheduler.getInstance().addTrigger(() -> !Constants.intakeExtendButton.get(),
                    new InstantCommand(() -> mExtenderSolenoid.set(DoubleSolenoid.Value.kForward), "extend", this));
            CommandScheduler.getInstance().addTrigger(() -> Constants.intakeExtendButton.get(),
                    new InstantCommand(() -> mExtenderSolenoid.set(DoubleSolenoid.Value.kReverse), "retract", this));
        }
    }
}
