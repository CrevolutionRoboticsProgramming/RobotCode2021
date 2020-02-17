package org.frc2851.robot.subsystems;

import com.revrobotics.AlternateEncoderType;
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

public class Drivetrain extends Subsystem
{
    private static Drivetrain mInstance = new Drivetrain();

    private Drivetrain()
    {
        addComponents(new Drivebase(), new GearShifter());
    }

    public static Drivetrain getInstance()
    {
        return mInstance;
    }

    public static class Drivebase extends Component
    {
        private CANSparkMax mLeftMaster, mLeftFollowerA, mLeftFollowerB,
                mRightMaster, mRightFollowerA, mRightFollowerB;
        private CANEncoder mLeftEncoder, mRightEncoder;

        public Drivebase()
        {
            super(Drivetrain.class);

            mLeftMaster = MotorControllerFactory.makeSparkMax(Constants.drivetrainLeftMasterPort);
            mLeftFollowerA = MotorControllerFactory.makeSparkMax(Constants.drivetrainLeftFollowerAPort);
            mLeftFollowerB = MotorControllerFactory.makeSparkMax(Constants.drivetrainLeftFollowerBPort);
            mRightMaster = MotorControllerFactory.makeSparkMax(Constants.drivetrainRightMasterPort);
            mRightFollowerA = MotorControllerFactory.makeSparkMax(Constants.drivetrainRightFollowerAPort);
            mRightFollowerB = MotorControllerFactory.makeSparkMax(Constants.drivetrainRightFollowerBPort);

            mLeftMaster.setInverted(true);
            mLeftFollowerA.setInverted(true);
            mLeftFollowerB.setInverted(true);

            mLeftFollowerA.follow(mLeftMaster);
            mLeftFollowerB.follow(mLeftMaster);
            mRightFollowerA.follow(mRightMaster);
            mRightFollowerB.follow(mRightMaster);

            mLeftEncoder = mLeftMaster.getEncoder();
            mRightEncoder = mRightMaster.getEncoder();

            setDefaultCommand(new RunCommand(this::arcadeDrive, "arcade drive", this));
        }

        public void arcadeDrive()
        {
            double throttle = Constants.drivetrainThrottleAxis.get();
            double turn = Constants.drivetrainTurnAxis.get();

            double leftOut = throttle + turn;
            double rightOut = throttle - turn;

            // The ternary operator expressions keep the output within -1.0 and 1.0 even though the Talons do this for us
            mLeftMaster.set(leftOut > 0 ? Math.min(leftOut, 1) : Math.max(leftOut, -1));
            mRightMaster.set(rightOut > 0 ? Math.min(rightOut, 1) : Math.max(rightOut, -1));
        }
    }

    public static class GearShifter extends Component
    {
        private DoubleSolenoid mShifterSolenoid;

        public GearShifter()
        {
            super(Drivetrain.class);

            mShifterSolenoid = new DoubleSolenoid(Constants.drivetrainShifterSolenoidForward, Constants.drivetrainShifterSolenoidReverse);

            CommandScheduler.getInstance().addTrigger(
                    Constants.drivetrainShiftGearTrigger.negate(),
                    new InstantCommand(() ->
                    {
                        mShifterSolenoid.set(DoubleSolenoid.Value.kForward);
                        Constants.udpHandler.sendTo("GEAR-HIGH", Constants.driverStationIP, Constants.sendPort);
                    }, "high gear", this));
            CommandScheduler.getInstance().addTrigger(
                    Constants.drivetrainShiftGearTrigger,
                    new InstantCommand(() ->
                    {
                        mShifterSolenoid.set(DoubleSolenoid.Value.kReverse);
                        Constants.udpHandler.sendTo("GEAR-LOW", Constants.driverStationIP, Constants.sendPort);
                    }, "low gear", this));
        }
    }
}
