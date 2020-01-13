package org.frc2851.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class Drivetrain extends SubsystemBase {
    TalonSRX talon1 = new TalonSRX(0);
    TalonSRX talon2 = new TalonSRX(1);
    TalonSRX talon3 = new TalonSRX(2);
    TalonSRX talon4 = new TalonSRX(3);

    Joystick stick = new Joystick(0);

    public Drivetrain()
    {
        super();
        talon1.set(ControlMode.PercentOutput, stick.getRawAxis(1) * .5);
        talon2.set(ControlMode.PercentOutput, stick.getRawAxis(1) * .5);
        talon3.set(ControlMode.PercentOutput, stick.getRawAxis(5) * .5);
        talon4.set(ControlMode.PercentOutput, stick.getRawAxis(5) * .5);
    }
}