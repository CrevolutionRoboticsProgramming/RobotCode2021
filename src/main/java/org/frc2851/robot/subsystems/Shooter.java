package org.frc2851.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import org.frc2851.robot.Constants;
import org.frc2851.robot.framework.Component;
import org.frc2851.robot.framework.Subsystem;
import org.frc2851.robot.framework.command.CommandScheduler;
import org.frc2851.robot.framework.command.InstantCommand;
import org.frc2851.robot.framework.command.Trigger;
import org.frc2851.robot.util.MotorControllerFactory;
import org.frc2851.robot.util.UDPHandler;

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
            mMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);

            mLimitSwitch = new DigitalInput(Constants.shooterTurretLimitSwitchPort);

            CommandScheduler.getInstance().addTrigger(
                    new Trigger(new Trigger.OnPress(), mLimitSwitch::get),
                    new InstantCommand(() -> mMotor.setSelectedSensorPosition(0), "zero encoder", this));

            // When we receive the target offset from the RPi, schedule a new command that rotates us to the target with a P controller
            Constants.udpHandler.addReceiver(new UDPHandler.MessageReceiver("X OFFSET:", (message) ->
            {
                if (Constants.shooterEnableVisionTracking.get())
                {
                    CommandScheduler.getInstance().schedule(new InstantCommand(() ->
                            mMotor.set(ControlMode.PercentOutput, Double.parseDouble(message) * Constants.shooterTurretKP), "rotate to vision target", this));
                }
            }));

            setDefaultCommand(new InstantCommand(() -> mMotor.set(ControlMode.PercentOutput, deadband(Constants.shooterTurretRotateAxis.get())),
                    "rotate", this));
        }

        private double deadband(double value)
        {
            return Math.abs(value) > Constants.shooterDeadband ? value : 0;
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
            mMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);

            mLimitSwitch = new DigitalInput(Constants.shooterAnglerLimitSwitchPort);

            CommandScheduler.getInstance().addTrigger(
                    new Trigger(new Trigger.OnPress(), mLimitSwitch::get),
                    new InstantCommand(() -> mMotor.setSelectedSensorPosition(0), "zero encoder", this));

            // When we receive the target offset from the RPi, schedule a new command that angles us to the target with a P controller
            Constants.udpHandler.addReceiver(
                    new UDPHandler.MessageReceiver("Y OFFSET:", (message) ->
                    {
                        if (Constants.shooterEnableVisionTracking.get())
                        {
                            CommandScheduler.getInstance().schedule(new InstantCommand(() ->
                                    mMotor.set(ControlMode.PercentOutput, Double.parseDouble(message) * Constants.shooterAnglerKP), "angle to vision target", this));
                        }
                    }));

            setDefaultCommand(new InstantCommand(() -> mMotor.set(ControlMode.PercentOutput, deadband(Constants.shooterAnglerAxis.get())),
                    "rotate", this));
        }

        private double deadband(double value)
        {
            return Math.abs(value) > Constants.shooterDeadband ? value : 0;
        }
    }

    private static class Launcher extends Component
    {
        private TalonSRX mMotor;

        public Launcher()
        {
            super(Shooter.class);

            mMotor = MotorControllerFactory.makeTalonSRX(Constants.shooterLauncherPort);
            mMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);

            CommandScheduler.getInstance().addTrigger(
                    Constants.shooterLauncherShootTrigger,
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
