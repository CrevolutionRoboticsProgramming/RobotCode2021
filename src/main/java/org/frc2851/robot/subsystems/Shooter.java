package org.frc2851.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import org.frc2851.robot.Constants;
import org.frc2851.robot.framework.Component;
import org.frc2851.robot.framework.Subsystem;
import org.frc2851.robot.framework.command.CommandScheduler;
import org.frc2851.robot.framework.command.InstantCommand;
import org.frc2851.robot.framework.command.RunCommand;
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

            setDefaultCommand(new InstantCommand(this::rotate, "rotating", this));
        }
        public void rotate(){
            double rotate = Constants.shooterTurretRotateAxis.get();

            mMotor.set(ControlMode.PercentOutput, rotate > 0 ? Math.min(rotate, 1) : Math.max(rotate, -1));
        }
    }

    private static class Angler extends Component
    {
        private TalonSRX mMotor;

        public Angler()
        {
            super(Shooter.class);

            mMotor = MotorControllerFactory.makeTalonSRX(Constants.shooterAnglerPort);

            setDefaultCommand(new InstantCommand(this::rotate, "rotating", this));


        }
        public void rotate(){
            double rotate = Constants.shooterAnglerAxis.get();

            mMotor.set(ControlMode.PercentOutput, rotate > 0 ? Math.min(rotate, 1) : Math.max(rotate, -1));
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
