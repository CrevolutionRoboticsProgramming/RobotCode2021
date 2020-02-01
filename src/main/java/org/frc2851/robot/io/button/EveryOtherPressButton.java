package org.frc2851.robot.io.button;

import org.frc2851.robot.io.Controller;

public class EveryOtherPressButton extends Button
{
    private boolean mLastRawState = false;
    private boolean mOtherPress = false;

    public EveryOtherPressButton(Controller controller, ButtonID id, Button... comboButtons)
    {
        super(controller, id, comboButtons);
    }

    @Override
    public boolean get()
    {
        boolean raw = super.get();
        boolean returnValue;

        if (raw && !mLastRawState)
        {
            returnValue = mOtherPress;
            mOtherPress = !mOtherPress;
        } else
            returnValue = false;

        mLastRawState = raw;

        return returnValue;
    }
}
