package org.frc2851.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import org.frc2851.robot.Constants;
import org.frc2851.robot.framework.Component;
import org.frc2851.robot.framework.Subsystem;
import org.frc2851.robot.framework.command.CommandScheduler;
import org.frc2851.robot.framework.command.InstantCommand;
import org.frc2851.robot.framework.trigger.OnPressTrigger;
import org.frc2851.robot.framework.trigger.RawTrigger;
import org.frc2851.robot.util.Logger;
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
        private DutyCycleEncoder mEncoder;
        //private DigitalInput mLimitSwitch;

        public Turret()
        {
            super(Shooter.class);

            mMotor = MotorControllerFactory.makeTalonSRX(Constants.Turret.motorPort);
            mMotor.setNeutralMode(NeutralMode.Brake);

            mEncoder = new DutyCycleEncoder(Constants.Turret.absoluteEncoderDIOPort);

            //mLimitSwitch = new DigitalInput(Constants.shooterTurretLimitSwitchPort);
/*
            CommandScheduler.getInstance().addTrigger(
                    new OnPressTrigger(mLimitSwitch::get),
                    new InstantCommand(() -> mMotor.setSelectedSensorPosition(0), "zero encoder", this));
*/
            // When we receive the target offset from the RPi, schedule a new command that rotates us to the target with a P controller
            Constants.udpHandler.addReceiver(new UDPHandler.MessageReceiver("X OFFSET:", (message) ->
            {
                if (Constants.Shooter.enableVisionTrackingTrigger.get())
                {
                    CommandScheduler.getInstance().schedule(new InstantCommand(() ->
                    {
                        mMotor.set(ControlMode.PercentOutput, Double.parseDouble(message) * Constants.Turret.kP);
                        System.out.println(mEncoder.getDistance());
                    }, "rotate to vision target", this));
                }
            }));

            setDefaultCommand(new InstantCommand(() -> mMotor.set(ControlMode.PercentOutput, deadband(Constants.Turret.directDriveAxis.get())),
                    "direct drive", this));
        }

        private double deadband(double value)
        {
            return Math.abs(value) > Constants.Shooter.deadband ? value : 0;
        }
    }

    private static class Angler extends Component
    {
        private TalonSRX mMotor;
        private DutyCycleEncoder mEncoder;
        //private DigitalInput mLimitSwitch;

        public Angler()
        {
            super(Shooter.class);

            mMotor = MotorControllerFactory.makeTalonSRX(Constants.Angler.motorPort);
            mMotor.setNeutralMode(NeutralMode.Brake);

            mEncoder = new DutyCycleEncoder(Constants.Angler.absoluteEncoderDIOPort);

            //mLimitSwitch = new DigitalInput(Constants.shooterAnglerLimitSwitchPort);
/*
            CommandScheduler.getInstance().addTrigger(
                    new OnPressTrigger(mLimitSwitch::get),
                    new InstantCommand(() -> mMotor.setSelectedSensorPosition(0), "zero encoder", this));
*/
            // When we receive the target offset from the RPi, schedule a new command that angles us to the target with a P controller
            Constants.udpHandler.addReceiver(
                    new UDPHandler.MessageReceiver("Y OFFSET:", (message) ->
                    {
                        if (Constants.Shooter.enableVisionTrackingTrigger.get())
                        {
                            CommandScheduler.getInstance().schedule(new InstantCommand(() ->
                                    mMotor.set(ControlMode.PercentOutput, Double.parseDouble(message) * Constants.Angler.kP), "angle to vision target", this));
                        }
                    }));

            setDefaultCommand(new InstantCommand(() -> mMotor.set(ControlMode.PercentOutput, deadband(Constants.Angler.directDriveAxis.get())),
                    "direct drive", this));
        }

        private double deadband(double value)
        {
            return Math.abs(value) > Constants.Shooter.deadband ? value : 0;
        }
    }

    private static class Launcher extends Component
    {
        private TalonFX mLeaderMotor, mFollowerMotor;

        private long mBeginShootingTime;

        public Launcher()
        {
            super(Shooter.class);

            mLeaderMotor = MotorControllerFactory.makeTalonFX(Constants.Launcher.leaderMotorPort);
            mLeaderMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);

            mFollowerMotor = MotorControllerFactory.makeTalonFX(Constants.Launcher.followerMotorPort);
            mFollowerMotor.follow(mLeaderMotor);
            mFollowerMotor.setInverted(true);

            CommandScheduler.getInstance().addTrigger(new OnPressTrigger(() -> Constants.Launcher.directDriveAxis.get() != 0.0),
                    new InstantCommand(() -> mBeginShootingTime = System.currentTimeMillis(), "set start shooting time", this));

            mLeaderMotor.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, 0);

            CommandScheduler.getInstance().addTrigger(new RawTrigger(() -> Constants.Launcher.directDriveAxis.get() != 0.0),
                    new InstantCommand(() ->
                    {
                        launch(Constants.Launcher.directDriveAxis.get() * Math.min((System.currentTimeMillis() - mBeginShootingTime) / Constants.Launcher.spinUpTime, 1.0));
                        Logger.println(Logger.LogLevel.DEBUG, "launcher output", String.valueOf(mLeaderMotor.getMotorOutputPercent()));
                    }, "run", this));
            setDefaultCommand(new InstantCommand(this::stop, "stop", this));
        }

        public void launch(double output)
        {
            mLeaderMotor.set(ControlMode.PercentOutput, output);
        }

        public void stop()
        {
            mLeaderMotor.set(ControlMode.PercentOutput, 0.0);
        }
    }
}
