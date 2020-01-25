package org.frc2851.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import org.frc2851.robot.Constants;
import org.frc2851.robot.framework.Component;
import org.frc2851.robot.framework.Subsystem;
import org.frc2851.robot.framework.command.CommandScheduler;
import org.frc2851.robot.framework.command.InstantCommand;
import org.frc2851.robot.framework.command.RunCommand;
import org.frc2851.robot.util.MotorControllerFactory;

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
        private VictorSPX mCLimberMaster;

        public ClimberComponent()
        {
            super(Climber.class);

            VictorSPX mClimberMaster = MotorControllerFactory.makeVictorSPX(Constants.climberMaster);

            setDefaultCommand(new RunCommand(() -> mClimberMaster.set(ControlMode.PercentOutput, 0), "stop", this));
            CommandScheduler.getInstance().addTrigger(() -> Constants.climberExtendButton.get(),
                    new InstantCommand(() -> mClimberMaster.set(ControlMode.PercentOutput, 1.0), "extend", this));
            CommandScheduler.getInstance().addTrigger(() -> Constants.climberRetractButton.get(),
                    new InstantCommand(() -> mClimberMaster.set(ControlMode.PercentOutput, -1.0), "retract", this));
        }
    }
}
