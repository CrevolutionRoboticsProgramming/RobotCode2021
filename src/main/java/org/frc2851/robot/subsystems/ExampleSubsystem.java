package org.frc2851.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.frc2851.robot.Constants;
import org.frc2851.robot.util.CommandFactory;
import org.frc2851.robot.util.Logger;
import org.frc2851.robot.util.TalonSRXFactory;

public class ExampleSubsystem extends SubsystemBase
{
    private TalonSRX Talon;

    public ExampleSubsystem()
    {
        super();

        Talon = TalonSRXFactory.makeTalonSRX(Constants.exampleSubsystemPort);

        setDefaultCommand(CommandFactory.makeRunCommand(this::doThing, "Play Xbox", this.getName(), this));
    }

    public void doThing()
    {
        Logger.println(Logger.LogLevel.DEBUG, "[" + getName() + "]", "Did the thing/things/thingy/thinger/something");
    }

    public void go()
    {
        Talon.set(ControlMode.PercentOutput, 1);
    }

    public void stop()
    {
        Talon.set(ControlMode.PercentOutput, 0);
    }
}
