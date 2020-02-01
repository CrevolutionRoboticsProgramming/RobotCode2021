package org.frc2851.robot.io.button;

import org.frc2851.robot.io.Controller;

public class RawButton extends Button
{
    public RawButton(Controller controller, ButtonID id, Button... comboButtons)
    {
        super(controller, id, comboButtons);
    }

    @Override
    public boolean get()
    {
        return super.get();
    }
}
