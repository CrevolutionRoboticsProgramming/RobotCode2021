package org.frc2851.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.frc2851.robot.Constants;
import org.frc2851.robot.util.CommandFactory;
import org.frc2851.robot.util.TalonSRXFactory;

public class Drivetrain extends SubsystemBase
{
    private TalonSRX mLeftMaster, mLeftFollowerA, mLeftFollowerB,
            mRightMaster, mRightFollowerA, mRightFollowerB;

    public Drivetrain()
    {
        super();

        mLeftMaster = TalonSRXFactory.makeTalonSRX(Constants.driveTrainLeftMasterPort);
        mLeftFollowerA = TalonSRXFactory.makeTalonSRX(Constants.driveTrainLeftFollowerAPort);
        mLeftFollowerB = TalonSRXFactory.makeTalonSRX(Constants.driveTrainLeftFollowerBPort);
        mRightMaster = TalonSRXFactory.makeTalonSRX(Constants.driveTrainRightMasterPort);
        mRightFollowerA = TalonSRXFactory.makeTalonSRX(Constants.driveTrainRightFollowerAPort);
        mRightFollowerB = TalonSRXFactory.makeTalonSRX(Constants.driveTrainRightFollowerBPort);

        mRightMaster.setInverted(true);
        mRightFollowerA.setInverted(true);
        mRightFollowerB.setInverted(true);

        mLeftFollowerA.follow(mLeftMaster);
        mLeftFollowerB.follow(mLeftMaster);
        mRightFollowerA.follow(mRightMaster);
        mRightFollowerB.follow(mRightMaster);

        setDefaultCommand(CommandFactory.makeRunCommand(this::arcadeDrive, "arcade drive", getName(), this));
    }

    public void arcadeDrive()
    {
        double throttle = Constants.driverController.get(Constants.driveTrainThrottleAxis);
        double turn = Constants.driverController.get(Constants.driveTrainTurnAxis);

        double leftOut = throttle + turn;
        double rightOut = throttle - turn;

        // The ternary operator expressions keep the output within -1.0 and 1.0 even though the Talons do this for us
        mLeftMaster.set(ControlMode.PercentOutput, leftOut > 0 ? Math.min(leftOut, 1) : Math.max(leftOut, -1));
        mRightMaster.set(ControlMode.PercentOutput, rightOut > 0 ? Math.min(rightOut, 1) : Math.max(rightOut, -1));
    }
}
