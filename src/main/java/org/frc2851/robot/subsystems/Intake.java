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

import static org.frc2851.robot.Constants.*;


public class Intake extends Subsystem {
    private static Intake mInstance = new Intake();

    private Intake() {
        addComponents(new RollBar(), new Actuate());
    }

    public static Intake getInstance() {
        return mInstance;
    }

    public static class RollBar extends Component {
        private CANSparkMax mMotor1;

        public RollBar() {
            super(Intake.class);
            mMotor1 = MotorControllerFactory.makeSparkMax(intakeRollBar);
            setDefaultCommand(new RunCommand(this::rollBar, "rollBar", this));
        }

        public void rollBar() {
            if((Constants.intakeRollBarTrigger).get()){
                mMotor1.set(.5);}
           else if((Constants.outtakeRollBarTrigger).get()){
                mMotor1.set(-.5);
            }
           else{
               mMotor1.set(0);
            }
            }
        }
    public static class Actuate extends Component{
        private DoubleSolenoid mActuate;

        public Actuate() {
            super(Intake.class);
            mActuate = new DoubleSolenoid(Constants.intakeActuateSolenoidIn, Constants.intakeActuateSolenoidOut);
            CommandScheduler.getInstance().addTrigger(() -> !Constants.actuateIntakeBumper.get(),
                    new InstantCommand(() -> mActuate.set(DoubleSolenoid.Value.kForward), "out", this));
            CommandScheduler.getInstance().addTrigger(() -> Constants.actuateIntakeBumper.get(),
                    new InstantCommand(() -> mActuate.set(DoubleSolenoid.Value.kReverse), "in", this));
        }
    }

    }
