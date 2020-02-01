package org.frc2851.robot.io.button;

import org.frc2851.robot.io.Controller;

public class OnPressButton extends Button
{
    private boolean mLastRawState = false;

    public OnPressButton(Controller controller, ButtonID id, Button... comboButtons)
    {
        super(controller, id, comboButtons);
    }

    @Override
    public boolean get()
    {
        boolean raw = super.get();
        boolean returnValue = raw && !mLastRawState;
        mLastRawState = raw;
        return returnValue;
    }
}
