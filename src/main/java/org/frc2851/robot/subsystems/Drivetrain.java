package org.frc2851.robot.subsystems;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.frc2851.robot.Constants;
import org.frc2851.robot.util.CommandFactory;
import org.frc2851.robot.util.MotorControllerFactory;

public class Drivetrain extends SubsystemBase
{
    private CANSparkMax mLeftMaster, mLeftFollowerA, mLeftFollowerB,
            mRightMaster, mRightFollowerA, mRightFollowerB;
    private DoubleSolenoid mShifterSolenoid;

    public Drivetrain()
    {
        super();

        mLeftMaster = MotorControllerFactory.makeSparkMax(Constants.drivetrainLeftMasterPort);
        mLeftFollowerA = MotorControllerFactory.makeSparkMax(Constants.drivetrainLeftFollowerAPort);
        mLeftFollowerB = MotorControllerFactory.makeSparkMax(Constants.drivetrainLeftFollowerBPort);
        mRightMaster = MotorControllerFactory.makeSparkMax(Constants.drivetrainRightMasterPort);
        mRightFollowerA = MotorControllerFactory.makeSparkMax(Constants.drivetrainRightFollowerAPort);
        mRightFollowerB = MotorControllerFactory.makeSparkMax(Constants.drivetrainRightFollowerBPort);

        mRightMaster.setInverted(true);
        mRightFollowerA.setInverted(true);
        mRightFollowerB.setInverted(true);

        mLeftFollowerA.follow(mLeftMaster);
        mLeftFollowerB.follow(mLeftMaster);
        mRightFollowerA.follow(mRightMaster);
        mRightFollowerB.follow(mRightMaster);

        mShifterSolenoid = new DoubleSolenoid(Constants.drivetrainShifterSolenoidForward, Constants.drivetrainShifterSolenoidReverse);

        setDefaultCommand(CommandFactory.makeRunCommand(this::arcadeDrive, "arcade drive", getName(), this));
    }

    public void arcadeDrive()
    {
        double throttle = Constants.driverController.get(Constants.drivetrainThrottleAxis);
        double turn = Constants.driverController.get(Constants.drivetrainTurnAxis);

        double leftOut = throttle + turn;
        double rightOut = throttle - turn;

        // The ternary operator expressions keep the output within -1.0 and 1.0 even though the Talons do this for us
        mLeftMaster.set(leftOut > 0 ? Math.min(leftOut, 1) : Math.max(leftOut, -1));
        mRightMaster.set(rightOut > 0 ? Math.min(rightOut, 1) : Math.max(rightOut, -1));
    }

    public void setHighGear()
    {
        mShifterSolenoid.set(DoubleSolenoid.Value.kForward);
    }

    public void setLowGear()
    {
        mShifterSolenoid.set(DoubleSolenoid.Value.kReverse);
    }
}
