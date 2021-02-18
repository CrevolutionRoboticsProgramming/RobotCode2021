package org.frc2851.robot.subsystems;

import com.revrobotics.*;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.frc2851.robot.Constants;
import org.frc2851.robot.framework.Component;
import org.frc2851.robot.framework.Subsystem;
import org.frc2851.robot.framework.command.Command;
import org.frc2851.robot.framework.command.CommandScheduler;
import org.frc2851.robot.framework.command.RunCommand;
import org.frc2851.robot.trajectory.Trajectory;
import org.frc2851.robot.util.Logger;
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
        private CANSparkMax mLeftLeader, mLeftFollowerA, mLeftFollowerB,
                mRightLeader, mRightFollowerA, mRightFollowerB;
        private CANEncoder mLeftEncoder, mRightEncoder;
        private CANPIDController mLeftPIDController, mRightPIDController;
        //private PigeonIMU mPigeon;

        public Drivebase()
        {
            super(Drivetrain.class);

            mLeftLeader = MotorControllerFactory.makeSparkMax(Constants.Drivebase.leftLeaderPort);
            mLeftFollowerA = MotorControllerFactory.makeSparkMax(Constants.Drivebase.leftFollowerAPort);
            mLeftFollowerB = MotorControllerFactory.makeSparkMax(Constants.Drivebase.leftFollowerBPort);
            mRightLeader = MotorControllerFactory.makeSparkMax(Constants.Drivebase.rightLeaderPort);
            mRightFollowerA = MotorControllerFactory.makeSparkMax(Constants.Drivebase.rightFollowerAPort);
            mRightFollowerB = MotorControllerFactory.makeSparkMax(Constants.Drivebase.rightFollowerBPort);

            // TODO: get Talon that the Pigeon will connect to
            //mPigeon = new PigeonIMU()

            mLeftLeader.setInverted(true);
            mLeftFollowerA.setInverted(true);
            mLeftFollowerB.setInverted(true);

            mLeftFollowerA.follow(mLeftLeader);
            mLeftFollowerB.follow(mLeftLeader);
            mRightFollowerA.follow(mRightLeader);
            mRightFollowerB.follow(mRightLeader);

            mLeftEncoder = mLeftLeader.getAlternateEncoder(AlternateEncoderType.kQuadrature, 2048);
            mRightEncoder = mRightLeader.getAlternateEncoder(AlternateEncoderType.kQuadrature, 2048);

            mRightEncoder.setInverted(true);
            mLeftEncoder.setPosition(0);
            mRightEncoder.setPosition(0);

            mLeftPIDController = mLeftLeader.getPIDController();
            mRightPIDController = mRightLeader.getPIDController();
            mLeftPIDController.setFeedbackDevice(mLeftEncoder);
            mRightPIDController.setFeedbackDevice(mRightEncoder);

            setDefaultCommand(new RunCommand(this::arcadeDrive, "arcade drive", this));
        }

        public void arcadeDrive()
        {
            double throttle = deadband(Constants.Drivebase.throttleAxis.get());
            double turn = deadband(Constants.Drivebase.turnAxis.get());

            double leftOut = throttle + turn;
            double rightOut = throttle - turn;

            // The ternary operator expressions keep the output within -1.0 and 1.0 even though the Talons do this for us
            mLeftLeader.set(leftOut > 0 ? Math.min(leftOut, 1) : Math.max(leftOut, -1));
            mRightLeader.set(rightOut > 0 ? Math.min(rightOut, 1) : Math.max(rightOut, -1));
        }

        private double deadband(double value)
        {
            return Math.abs(value) > Constants.Drivebase.deadband ? value : 0;
        }

        private void resetEncoders()
        {
            mLeftEncoder.setPosition(0);
            mRightEncoder.setPosition(0);
        }

        private void resetHeading()
        {
            //mPigeon.setYaw(0);
        }

        private Command followTrajectory(Trajectory leftTrajectory, Trajectory rightTrajectory)
        {
            return new Command("follow trajectory", false, this)
            {
                private Thread mThread;
                private long mBegin;
                private boolean mStopFlag;

                private int getTimeElapsed()
                {
                    return (int) (System.currentTimeMillis() - mBegin);
                }

                /**
                 * Bounds the provided angle between 0 and 360
                 * @param angle The angle to bound
                 * @return The bounded angle
                 */
                private double getPositiveBoundedAngle(double angle)
                {
                    // The angles are cast to integers so the result of the division will not have a fractional
                    // component, meaning that it will represent the number of full rotations completed in that direction
                    if (angle > 0)
                        return angle - (((int) angle / 360) * 360);
                    else
                        return 360 + (angle + (((int) angle / -360) * 360));
                }

                @Override
                public void initialize()
                {
                    super.initialize();

                    mBegin = System.currentTimeMillis();
                    mStopFlag = false;

                    resetEncoders();
                    //mPigeon.setYaw(Math.toDegrees(leftTrajectory.getPoints().get(0).getHeading()));

                    mLeftPIDController.setP(Constants.Drivebase.kP);
                    mLeftPIDController.setI(Constants.Drivebase.kI);
                    mLeftPIDController.setD(Constants.Drivebase.kD);

                    mRightPIDController.setP(Constants.Drivebase.kP);
                    mRightPIDController.setI(Constants.Drivebase.kI);
                    mRightPIDController.setD(Constants.Drivebase.kD);

                    mThread = new Thread(this::run);
                    mThread.start();
                }

                private void run()
                {
                    while (!isFinished() && !mStopFlag)
                    {
                        int time = getTimeElapsed();

                        if (time >= leftTrajectory.getPoints().size() * leftTrajectory.getPoints().get(0).getDt()
                                || time >= rightTrajectory.getPoints().size() * rightTrajectory.getPoints().get(0).getDt())
                            return;

                        double[] ypr = new double[3];
                        //mPigeon.getYawPitchRoll(ypr);

                        // These adjusted values bound the angle within 0 and 360
                        double adjustedMeasuredHeading = getPositiveBoundedAngle(ypr[0]);
                        double adjustedTargetHeading = getPositiveBoundedAngle(Math.toDegrees(leftTrajectory.getPoints().get(time).getHeading()));

                        // The normal heading error works when the difference between angles is within 180 degrees;
                        // otherwise, the error appears to be larger than it is (the difference between -179 and 179 is
                        // actually just 2). The adjusted heading error accounts for this by shifting the problem to
                        // 360 and -360 instead of 180 and -180 so both cases are covered
                        double headingError = ypr[0] - Math.toDegrees(leftTrajectory.getPoints().get(time).getHeading());
                        double adjustedHeadingError = adjustedMeasuredHeading - adjustedTargetHeading;

                        double absoluteSmallest = Math.min(Math.abs(headingError), Math.abs(adjustedHeadingError));
                        double smallestHeadingError = absoluteSmallest == headingError || absoluteSmallest == -headingError ? headingError : adjustedHeadingError;

                        // Multiplying the heading error by an empirically-determined magic number corrects deviations
                        // from the target heading
                        double velocityDifferential = Constants.Drivebase.turnkP * smallestHeadingError;

                        mLeftPIDController.setReference((leftTrajectory.getPoints().get(time).getVelocity() - velocityDifferential) / Constants.Drivebase.inchesPerRotation, ControlType.kVelocity);
                        mRightPIDController.setReference((rightTrajectory.getPoints().get(time).getVelocity() + velocityDifferential) / Constants.Drivebase.inchesPerRotation, ControlType.kVelocity);
                    }
                }

                @Override
                public boolean isFinished()
                {
                    return getTimeElapsed() >= leftTrajectory.getPoints().size() * leftTrajectory.getPoints().get(0).getDt()
                            || getTimeElapsed() >= rightTrajectory.getPoints().size() * rightTrajectory.getPoints().get(0).getDt();
                }

                @Override
                public void end()
                {
                    super.end();

                    double leftError = (leftTrajectory.getPoints().get(leftTrajectory.getPoints().size() - 1).getPosition() - (mLeftEncoder.getPosition() * Constants.Drivebase.inchesPerRotation));
                    double rightError = (rightTrajectory.getPoints().get(rightTrajectory.getPoints().size() - 1).getPosition() - (mRightEncoder.getPosition() * Constants.Drivebase.inchesPerRotation));

                    Logger.println(Logger.LogLevel.DEBUG, "Finished following trajectory with a left error of " + leftError + " inches and a right error of " + rightError + " inches");

                    mLeftLeader.set(0.0);
                    mRightLeader.set(0.0);

                    resetEncoders();
                    resetHeading();

                    mStopFlag = true;
                }
            };
        }
    }

    public static class GearShifter extends Component
    {
        private DoubleSolenoid mShifterSolenoid;

        public GearShifter()
        {
            super(Drivetrain.class);

            mShifterSolenoid = new DoubleSolenoid(Constants.GearShifter.forwardChannel, Constants.GearShifter.reverseChannel);

            CommandScheduler.getInstance().addTrigger(
                    Constants.GearShifter.shiftGearTrigger.negate(),
                    new Command("high gear", false, this)
                    {
                        private long mLastMessageSend = 0;

                        @Override
                        public void initialize()
                        {
                            mShifterSolenoid.set(DoubleSolenoid.Value.kForward);

                            if (System.currentTimeMillis() - mLastMessageSend >= 1000)
                            {
                                Constants.udpHandler.sendTo("GEAR-HIGH", Constants.driverStationIP, Constants.sendPort);
                                mLastMessageSend = System.currentTimeMillis();
                            }
                        }

                        @Override
                        public boolean isFinished()
                        {
                            return true;
                        }
                    });
            CommandScheduler.getInstance().addTrigger(
                    Constants.GearShifter.shiftGearTrigger,
                    new Command("low gear", false, this)
                    {
                        private long mLastMessageSend = 0;

                        @Override
                        public void initialize()
                        {
                            mShifterSolenoid.set(DoubleSolenoid.Value.kReverse);

                            if (System.currentTimeMillis() - mLastMessageSend >= 1000)
                            {
                                Constants.udpHandler.sendTo("GEAR-LOW", Constants.driverStationIP, Constants.sendPort);
                                mLastMessageSend = System.currentTimeMillis();
                            }
                        }

                        @Override
                        public boolean isFinished()
                        {
                            return true;
                        }
                    });
        }
    }
}
