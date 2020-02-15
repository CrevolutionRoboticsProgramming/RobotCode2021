package org.frc2851.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.frc2851.robot.Constants;
import org.frc2851.robot.framework.Component;
import org.frc2851.robot.framework.Subsystem;
import org.frc2851.robot.framework.command.CommandScheduler;
import org.frc2851.robot.framework.command.InstantCommand;

public class Climber extends Subsystem
{
    private static Climber mInstance = new Climber();

    private Climber()
    {
        addComponents(new ClimberComponent());
    }

    public static Climber getInstance()
    {
        return mInstance;
    }

    public static class ClimberComponent extends Component
    {
        private DoubleSolenoid mSolenoid;

        public ClimberComponent()
        {
            super(Climber.class);

            mSolenoid = new DoubleSolenoid(Constants.climberSolenoidForward, Constants.climberSolenoidReverse);

            retract();

            CommandScheduler.getInstance().addTrigger(
                    Constants.climberExtendTrigger,
                    new InstantCommand(this::extend, "extend", this));
            CommandScheduler.getInstance().addTrigger(
                    Constants.climberRetractTrigger,
                    new InstantCommand(this::retract, "retract", this));
        }

        public void extend()
        {
            mSolenoid.set(DoubleSolenoid.Value.kForward);
        }

        public void retract()
        {
            mSolenoid.set(DoubleSolenoid.Value.kReverse);
        }
    }
}
