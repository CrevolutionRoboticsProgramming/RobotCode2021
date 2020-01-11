package org.frc2851.robot.util;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class TalonSRXFactory
{
    public static TalonSRX makeTalonSRX(int port)
    {
        TalonSRX returnTalon = new TalonSRX(port);
        returnTalon.configFactoryDefault();
        return returnTalon;
    }

    public static VictorSPX makeVictorSPX(int port)
    {
        VictorSPX returnVictor = new VictorSPX(port);
        returnVictor.configFactoryDefault();
        return returnVictor;
    }

    public static void configurePIDF(TalonSRX talon, int slot, PID pid)
    {
        configurePIDF(talon, slot, pid.getP(), pid.getI(), pid.getD(), pid.getF());
    }

    public static void configurePIDF(TalonSRX talon, int slot, double p, double i, double d, double f)
    {
        talon.config_kP(slot, p);
        talon.config_kI(slot, i);
        talon.config_kD(slot, d);
        talon.config_kF(slot, f);
    }
}
