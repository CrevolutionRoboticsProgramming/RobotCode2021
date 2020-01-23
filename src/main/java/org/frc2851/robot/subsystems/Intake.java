package org.frc2851.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.frc2851.robot.Constants;
import org.frc2851.robot.framework.Component;
import org.frc2851.robot.framework.Subsystem;
import org.frc2851.robot.framework.command.CommandScheduler;
import org.frc2851.robot.framework.command.InstantCommand;
import org.frc2851.robot.framework.command.RunCommand;
import org.frc2851.robot.util.MotorControllerFactory;

import static org.frc2851.robot.Constants.intakeRollBar;


public class Intake extends Subsystem {
    private static Intake mInstance = new Intake();

    private Intake() {
        addComponents(new RollBar());
    }

    public static Intake getInstance() {
        return mInstance;
    }

    public static class RollBar extends Component {
        private CANSparkMax mMotor1;

        public RollBar() {
            super(Intake.class);
            mMotor1 = MotorControllerFactory.makeSparkMax(intakeRollBar);
            setDefaultCommand(new RunCommand(this::in, "in", this));
        }

        public void in() {
            double in = Constants.driverController.get(Constants.intakeRollBarTrigger);
            double out = Constants.driverController.get(Constants.outtakeRollBarTrigger);

            mMotor1.set(in > 0 ? Math.min(in, 1) : Math.max(in, -1));
            mMotor1.set(out > 0 ? Math.min(out, -1) : Math.max(out, 1));
        }
    }
}