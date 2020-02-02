package org.frc2851.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
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

        public Turret()
        {
            super(Shooter.class);

            mMotor = MotorControllerFactory.makeTalonSRX(Constants.shooterTurretPort);

            mLeftMaster.set(leftOut > 0 ? Math.min(leftOut, 1) : Math.max(leftOut, -1));
            mRightMaster.set(rightOut > 0 ? Math.min(rightOut, 1) : Math.max(rightOut, -1));

            setDefaultCommand(new InstantCommand(this::stop, "stop", this));
        }

        public void rotateClockwise()
        {
            mMotor.set(ControlMode.PercentOutput, 1.0);
        }

        public void rotateCounterClockwise()
        {
            mMotor.set(ControlMode.PercentOutput, -1.0);
        }

        public void stop()
        {
            mMotor.set(ControlMode.PercentOutput, 0.0);
        }
    }

    private static class Angler extends Component
    {
        private TalonSRX mMotor;

        public Angler()
        {
            super(Shooter.class);

            mMotor = MotorControllerFactory.makeTalonSRX(Constants.shooterAnglerPort);

            CommandScheduler.getInstance().addTrigger(Constants.shooterAnglerRaiseButton::get,
                    new InstantCommand(this::raise, "raise", this));
            CommandScheduler.getInstance().addTrigger(Constants.shooterAnglerLowerButton::get,
                    new InstantCommand(this::lower, "lower", this));

            setDefaultCommand(new InstantCommand(this::stop, "stop", this));
        }

        public void raise()
        {
            mMotor.set(ControlMode.PercentOutput, 1.0);
        }

        public void lower()
        {
            mMotor.set(ControlMode.PercentOutput, -1.0);
        }

        public void stop()
        {
            mMotor.set(ControlMode.PercentOutput, 0.0);
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
