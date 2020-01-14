package org.frc2851.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.frc2851.robot.Constants;
import org.frc2851.robot.util.CommandFactory;


public class Drivetrain extends SubsystemBase {
    TalonSRX talonleft1 = new TalonSRX(0);
    TalonSRX talonleft2 = new TalonSRX(1);
    TalonSRX talonright1 = new TalonSRX(2);
    TalonSRX talonright2 = new TalonSRX(3);



    public Drivetrain() {
        super();

        setDefaultCommand(CommandFactory.makeRunCommand(this::drive, "drive", getName(), this));
    }
    public void drive(){

        talonright1.set(ControlMode.PercentOutput, .5 * Constants.driverController.get(Constants.y) - Constants.driverController.get(Constants.x));
        talonright2.set(ControlMode.PercentOutput, .5 * Constants.driverController.get(Constants.y) - Constants.driverController.get(Constants.x));
        talonleft1.set(ControlMode.PercentOutput, .5 * Constants.driverController.get(Constants.y) + Constants.driverController.get(Constants.x));
        talonleft2.set(ControlMode.PercentOutput, .5 * Constants.driverController.get(Constants.y) + Constants.driverController.get(Constants.x));
    }
}