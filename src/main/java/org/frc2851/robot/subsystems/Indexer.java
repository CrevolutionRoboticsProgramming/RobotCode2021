package org.frc2851.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import org.frc2851.robot.Constants;
import org.frc2851.robot.framework.Component;
import org.frc2851.robot.framework.Subsystem;
import org.frc2851.robot.framework.command.CommandScheduler;
import org.frc2851.robot.framework.command.InstantCommand;
import org.frc2851.robot.framework.command.Trigger;
import org.frc2851.robot.util.MotorControllerFactory;

public class Indexer extends Subsystem
{
    private static Indexer mInstance = new Indexer();

    private Indexer()
    {
        addComponents(new Snail(), new Elevator());
    }

    public static Indexer getInstance()
    {
        return mInstance;
    }

    public static class Snail extends Component
    {
        private VictorSPX mMotor;

        public Snail()
        {
            super(Indexer.class);

            mMotor = MotorControllerFactory.makeVictorSPX(Constants.indexerSnailMotorPort);

            CommandScheduler.getInstance().addTrigger(
                    new Trigger(new Trigger.Raw(), () -> Constants.shooterLauncherShootTrigger.get() || Constants.intakeIntakeTrigger.get()),
                    new InstantCommand(() -> mMotor.set(ControlMode.PercentOutput, 1.0), "run", this));
            setDefaultCommand(new InstantCommand(() -> mMotor.set(ControlMode.PercentOutput, 0.0), "stop", this));
        }
    }

    public static class Elevator extends Component
    {
        private VictorSPX mMotor;

        public Elevator()
        {
            super(Indexer.class);

            mMotor = MotorControllerFactory.makeVictorSPX(Constants.indexerElevatorMotorPort);

            CommandScheduler.getInstance().addTrigger(
                    new Trigger(new Trigger.Raw(), () -> Constants.shooterLauncherShootTrigger.get() || Constants.intakeIntakeTrigger.get()),
                    new InstantCommand(() -> mMotor.set(ControlMode.PercentOutput, 1.0), "run", this));
            setDefaultCommand(new InstantCommand(() -> mMotor.set(ControlMode.PercentOutput, 0.0), "stop", this));
        }
    }
}
