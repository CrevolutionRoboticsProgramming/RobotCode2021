package org.frc2851.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.frc2851.robot.Constants;
import org.frc2851.robot.framework.Component;
import org.frc2851.robot.framework.Subsystem;
import org.frc2851.robot.framework.command.InstantCommand;
import org.frc2851.robot.framework.command.RunCommand;
import org.frc2851.robot.util.MotorControllerFactory;

public class Drivetrain extends Subsystem
{
    private static Drivetrain mInstance = new Drivetrain();
    private Drivebase mDrivebase = new Drivebase();
    private GearShifter mGearShifter = new GearShifter();

    private Drivetrain()
    {
        super();

        addComponent(mDrivebase);
        addComponent(mGearShifter);

        mDrivebase.leftMaster = MotorControllerFactory.makeSparkMax(Constants.drivetrainLeftMasterPort);
        mDrivebase.leftFollowerA = MotorControllerFactory.makeSparkMax(Constants.drivetrainLeftFollowerAPort);
        mDrivebase.leftFollowerB = MotorControllerFactory.makeSparkMax(Constants.drivetrainLeftFollowerBPort);
        mDrivebase.rightMaster = MotorControllerFactory.makeSparkMax(Constants.drivetrainRightMasterPort);
        mDrivebase.rightFollowerA = MotorControllerFactory.makeSparkMax(Constants.drivetrainRightFollowerAPort);
        mDrivebase.rightFollowerB = MotorControllerFactory.makeSparkMax(Constants.drivetrainRightFollowerBPort);

        mDrivebase.rightMaster.setInverted(true);
        mDrivebase.rightFollowerA.setInverted(true);
        mDrivebase.rightFollowerB.setInverted(true);

        mDrivebase.leftFollowerA.follow(mDrivebase.leftMaster);
        mDrivebase.leftFollowerB.follow(mDrivebase.leftMaster);
        mDrivebase.rightFollowerA.follow(mDrivebase.rightMaster);
        mDrivebase.rightFollowerB.follow(mDrivebase.rightMaster);

        mDrivebase.leftEncoder = mDrivebase.leftMaster.getEncoder();
        mDrivebase.rightEncoder = mDrivebase.rightMaster.getEncoder();

        mGearShifter.shifterSolenoid = new DoubleSolenoid(Constants.drivetrainShifterSolenoidForward, Constants.drivetrainShifterSolenoidReverse);

        mDrivebase.setDefaultCommand(new RunCommand(this::arcadeDrive, "arcade drive", mDrivebase));
    }

    public void arcadeDrive()
    {
        double throttle = Constants.driverController.get(Constants.drivetrainThrottleAxis);
        double turn = Constants.driverController.get(Constants.drivetrainTurnAxis);

        double leftOut = throttle + turn;
        double rightOut = throttle - turn;

        // The ternary operator expressions keep the output within -1.0 and 1.0 even though the Talons do this for us
        mDrivebase.leftMaster.set(leftOut > 0 ? Math.min(leftOut, 1) : Math.max(leftOut, -1));
        mDrivebase.rightMaster.set(rightOut > 0 ? Math.min(rightOut, 1) : Math.max(rightOut, -1));
    }

    public InstantCommand getSetHighGearCommand()
    {
        return new InstantCommand(() -> mGearShifter.shifterSolenoid.set(DoubleSolenoid.Value.kForward), "high gear", mGearShifter);
    }

    public InstantCommand getSetLowGearCommand()
    {
        return new InstantCommand(() -> mGearShifter.shifterSolenoid.set(DoubleSolenoid.Value.kReverse), "low gear", mGearShifter);
    }

    public static Drivetrain getInstance()
    {
        return mInstance;
    }

    static class Drivebase extends Component
    {
        public CANSparkMax leftMaster, leftFollowerA, leftFollowerB,
                rightMaster, rightFollowerA, rightFollowerB;
        public CANEncoder leftEncoder, rightEncoder;
    }

    static class GearShifter extends Component
    {
        public DoubleSolenoid shifterSolenoid;
    }
}
