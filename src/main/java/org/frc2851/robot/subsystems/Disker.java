package org.frc2851.robot.subsystems;

import com.revrobotics.CANSparkMax;
import org.frc2851.robot.Constants;
import org.frc2851.robot.framework.Component;
import org.frc2851.robot.framework.Subsystem;
import org.frc2851.robot.framework.command.RunCommand;
import org.frc2851.robot.util.MotorControllerFactory;

public class Disker extends Subsystem {

    private static float rotationSpeed = 0.5f;

    private Disker() {
        addComponents(new DiskerComponent());
    }






    public class DiskerComponent extends Component {

        private CANSparkMax rotatorMotator;

        public DiskerComponent() {
            super(Disker.class);

            rotatorMotator = MotorControllerFactory.makeSparkMax(Constants.diskerRotatorPort);

            setDefaultCommand(new RunCommand(this::rotateDisker, "rotate disker", this));
        }



        public void rotateDisker() {
            boolean clockwise = Constants.diskerRotateClockwiseButton.get();
            boolean counter = Constants.diskerRotateCounterButton.get();

            if(clockwise && counter) return;

            //Legit no idea if negative is clockwise or positive is clockwise
            if(clockwise)
                rotatorMotator.set(rotationSpeed);
            else if (counter)
                rotatorMotator.set(-rotationSpeed);

        }
    }

}




