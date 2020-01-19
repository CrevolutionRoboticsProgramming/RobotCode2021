package org.frc2851.robot.subsystems;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.frc2851.robot.framework.Component;
import org.frc2851.robot.framework.Subsystem;

public class Drivetrain extends Subsystem<Drivetrain>
{
    Component mDrivebase = new Component()
    {
        public CANSparkMax leftMaster, leftFollowerA, leftFollowerB,
                rightMaster, rightFollowerA, rightFollowerB;

        @Override
        public String getName()
        {
            return "Drivebase";
        }
    };

    Component mGearShifter = new Component()
    {
        public DoubleSolenoid shifterSolenoid;

        @Override
        public String getName()
        {
            return "Gear Shifter";
        }
    };

    private Drivetrain()
    {
        super();

        addComponent(mDrivebase);
        addComponent(mGearShifter);
    }

    @Override
    protected Drivetrain getDefaultInstance()
    {
        return this;
    }
}
