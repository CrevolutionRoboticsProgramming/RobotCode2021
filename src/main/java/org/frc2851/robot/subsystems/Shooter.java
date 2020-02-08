package org.frc2851.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import org.frc2851.robot.Constants;
import org.frc2851.robot.framework.Component;
import org.frc2851.robot.framework.Subsystem;
import org.frc2851.robot.framework.command.CommandScheduler;
import org.frc2851.robot.framework.command.InstantCommand;
import org.frc2851.robot.util.MotorControllerFactory;

public class Shooter extends Subsystem
{
    private static Shooter mInstance = new Shooter();
    private Turret mTurret;
    private Angler mAngler;
    private Launcher mLauncher;

    private Shooter()
    {
        mTurret = new Turret();
        mAngler = new Angler();
        mLauncher = new Launcher();
        addComponents(mTurret, mAngler, mLauncher);
    }

    public static Shooter getInstance()
    {
        return mInstance;
    }

    private static class Turret extends Component
    {
        private TalonSRX mMotor;
        private DigitalInput mLimitSwitch;

        public Turret()
        {
            super(Shooter.class);

            mMotor = MotorControllerFactory.makeTalonSRX(Constants.shooterTurretPort);
            mLimitSwitch = new DigitalInput(Constants.shooterTurretLimitSwitchPort);

            CommandScheduler.getInstance().addTrigger(mLimitSwitch::get,
                    new InstantCommand(() -> mMotor.setSelectedSensorPosition(0), "zero encoder", this));

            setDefaultCommand(new InstantCommand(() -> mMotor.set(ControlMode.PercentOutput, Constants.shooterTurretRotateAxis.get()),
                    "rotate", this));
        }
    }

    private static class Angler extends Component
    {
        private TalonSRX mMotor;
        private DigitalInput mLimitSwitch;

        public Angler()
        {
            super(Shooter.class);

            mMotor = MotorControllerFactory.makeTalonSRX(Constants.shooterAnglerPort);
            mLimitSwitch = new DigitalInput(Constants.shooterAnglerLimitSwitchPort);

            CommandScheduler.getInstance().addTrigger(mLimitSwitch::get,
                    new InstantCommand(() -> mMotor.setSelectedSensorPosition(0), "zero encoder", this));

            setDefaultCommand(new InstantCommand(() -> mMotor.set(ControlMode.PercentOutput, Constants.shooterAnglerAxis.get()),
                    "rotate", this));
        }
    }

    private static class Launcher extends Component
    {
        private TalonSRX mMotor;

        public Launcher()
        {
            super(Shooter.class);

            mMotor = MotorControllerFactory.makeTalonSRX(Constants.shooterLauncherPort);

            CommandScheduler.getInstance().addTrigger(Constants.shooterLauncherShootButton::get,
                    new InstantCommand(this::launch, "launch", this));

            setDefaultCommand(new InstantCommand(this::stop, "stop", this));
        }

        public void launch()
        {
            mMotor.set(ControlMode.PercentOutput, 1.0);
        }

        public void stop()
        {
            mMotor.set(ControlMode.PercentOutput, 0.0);
        }
    }
}
