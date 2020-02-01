package org.frc2851.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;
import org.frc2851.robot.Constants;
import org.frc2851.robot.framework.Component;
import org.frc2851.robot.framework.Subsystem;
import org.frc2851.robot.framework.command.RunCommand;
import org.frc2851.robot.util.MotorControllerFactory;

import java.nio.ByteBuffer;

public class Disker extends Subsystem {

    private static float rotationSpeed = 0.25f; //Fastest allowed = 82%
    private static Disker disker = new Disker();

    private Disker() {
        addComponents(new DiskerMotorComponent(), new DiskerColorSensorComponent());
    }

    public static Disker getInstance() {
        return disker;
    }




    public class DiskerMotorComponent extends Component {

        private TalonSRX rotatorMotator;
        private RotationMode mode = RotationMode.CONTROL;
        private static final int is_simulation = 1; //Set to 1200 or 2400 if running a snowbot sim, 1 if not

        public DiskerMotorComponent() {
            super(Disker.class);

            rotatorMotator = MotorControllerFactory.makeTalonSRX(Constants.diskerRotatorPort);
            rotatorMotator.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);

            setDefaultCommand(new RunCommand(this::rotateDisker, "rotate disker", this));
        }



        public void rotateDisker() {
            if(mode == RotationMode.CONTROL) {
                boolean clockwise = Constants.diskerRotateClockwiseButton.get();
                boolean counter = Constants.diskerRotateCounterButton.get();

                //Legit no idea if negative is clockwise or positive is clockwise
                if(clockwise && counter)
                    rotatorMotator.set(ControlMode.PercentOutput, 0);
                else if (clockwise)
                    rotatorMotator.set(ControlMode.PercentOutput, rotationSpeed);
                else if (counter)
                    rotatorMotator.set(ControlMode.PercentOutput, -rotationSpeed);
                else
                    rotatorMotator.set(ControlMode.PercentOutput, 0);

                if(Constants.diskerRotateThriceButton.get()) {
                    System.out.println("Started rotation of the thrice");
                    mode = RotationMode.THRICE;
                    rotatorMotator.setSelectedSensorPosition(0);
                    rotatorMotator.set(ControlMode.PercentOutput, rotationSpeed);
                }
            } else {
                System.out.println("Sensor pos; " + rotatorMotator.getSelectedSensorPosition() / is_simulation);
                if (rotatorMotator.getSelectedSensorPosition() >= 65536 * 3 * is_simulation) {
                    System.out.println("Ended rotation of the thrice");
                    rotatorMotator.set(ControlMode.PercentOutput, 0);
                    mode = RotationMode.CONTROL;
                }
            }
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
            //System.out.println("I2C Port: " + I2C.Port.kOnboard);
            //System.out.println("Color sensor rgb reading:  (" + sensor.red() + ", " + sensor.green() + ", " + sensor.blue() + ")");
        }

    }




    public class ColorSensor {

        private I2C.Port port;
        private ColorSensorV3 sensor;
        private ColorMatch match = new ColorMatch();
        private Color r = new Color(255, 0, 0);
        private Color g = new Color(0, 255, 0);
        private Color b = new Color(0, 255, 255);
        private Color y = new Color(255, 255, 0);

        public ColorSensor() {
            port = I2C.Port.kOnboard;
            sensor = new ColorSensorV3(port);
            match.addColorMatch(r);
            match.addColorMatch(g);
            match.addColorMatch(b);
            match.addColorMatch(y);

            //match.matchClosestColor(sensor.getColor()).confidence;
        }

        public boolean isMatched(Color color) {
            return match.matchClosestColor(color).confidence > 75;
        }

    }


    public enum RotationMode {
        CONTROL,
        THRICE;
    }

}