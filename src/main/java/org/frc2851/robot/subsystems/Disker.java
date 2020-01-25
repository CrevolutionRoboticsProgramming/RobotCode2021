package org.frc2851.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.SensorUtil;
import org.frc2851.robot.Constants;
import org.frc2851.robot.framework.Component;
import org.frc2851.robot.framework.Subsystem;
import org.frc2851.robot.framework.command.RunCommand;
import org.frc2851.robot.util.MotorControllerFactory;

import java.nio.ByteBuffer;

public class Disker extends Subsystem {

    private static float rotationSpeed = 0.5f;
    private static Disker disker = new Disker();

    private Disker() {
        addComponents(new DiskerMotorComponent(), new DiskerColorSensorComponent());
    }

    public static Disker getInstance() {
        return disker;
    }




    public class DiskerMotorComponent extends Component {

        private CANSparkMax rotatorMotator;
        private CANEncoder rotatorEncoder;

        public DiskerMotorComponent() {
            super(Disker.class);

            rotatorMotator = MotorControllerFactory.makeSparkMax(Constants.diskerRotatorPort);

            rotatorEncoder = rotatorMotator.getEncoder();

            setDefaultCommand(new RunCommand(this::rotateDisker, "rotate disker", this));
        }



        public void rotateDisker() {
            boolean clockwise = Constants.diskerRotateClockwiseButton.get();
            boolean counter = Constants.diskerRotateCounterButton.get();

            if(clockwise && counter) {
                rotatorMotator.set(0);
                return;
            }

            //Legit no idea if negative is clockwise or positive is clockwise
            if(clockwise)
                rotatorMotator.set(rotationSpeed);
            else if (counter)
                rotatorMotator.set(-rotationSpeed);
            else
                rotatorMotator.set(0);
        }
    }




    public class DiskerColorSensorComponent extends Component {

        private ColorSensor sensor;

        public DiskerColorSensorComponent() {
            super(Disker.class);

            this.sensor = new ColorSensor();

            setDefaultCommand(new RunCommand(this::checkColor, "disker color checker", this));
        }



        public void checkColor() {
            System.out.println("I2C Port: " + I2C.Port.kOnboard);
            //System.out.println("Color sensor rgb reading:  (" + sensor.red() + ", " + sensor.green() + ", " + sensor.blue() + ")");
        }

    }




    public class ColorSensor {
//gnom

        private I2C.Port port;
        private ByteBuffer buffer;
        private I2C sensor;

        public ColorSensor() {
            port = I2C.Port.kOnboard;
            buffer = ByteBuffer.allocate(1);
            sensor = new I2C(port, 0x39);

            sensor.write(0x00, 192);
        }



        public int red(){
            sensor.read(0x16, 1, buffer);
            return buffer.get(0);
        }
        public int green(){
            sensor.read(0x18, 1, buffer);
            return buffer.get(0);
        }
        public int blue(){
            sensor.read(0x1A, 1, buffer);
            return buffer.get(0);
        }

    }

}




