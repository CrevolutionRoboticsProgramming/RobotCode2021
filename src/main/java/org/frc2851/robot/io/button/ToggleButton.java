package org.frc2851.robot.io.button;

import org.frc2851.robot.io.Controller;

public class ToggleButton extends Button
{
    private boolean mLastRawState = false;
    private boolean mToggleState = false;

    public ToggleButton(Controller controller, ButtonID id, Button... comboButtons)
    {
        super(controller, id, comboButtons);
    }

    @Override
    public boolean get()
    {
        boolean raw = super.get();
        if (raw && !mLastRawState)
            mToggleState = !mToggleState;
        mLastRawState = raw;
        return mToggleState;
    }
}
