package org.frc2851.robot.io.button;

public class Button
{
    private boolean mLastRawState = false;
    private boolean mToggleState = false;
    private boolean mOtherPress = false;
    private ButtonBehaviorType mButtonBehaviorType;
    private ButtonID mID;

    public Button(ButtonID id, ButtonBehaviorType buttonBehaviorType)
    {
        mID = id;
        mButtonBehaviorType = buttonBehaviorType;
    }

    public boolean get(boolean raw)
    {
        if (mButtonBehaviorType == null)
            return false;
        else
        {
            boolean returnValue;
            switch (mButtonBehaviorType)
            {
                case RAW:
                    returnValue = raw;
                    break;
                case ON_PRESS:
                    returnValue = raw && !mLastRawState;
                    break;
                case ON_RELEASE:
                    returnValue = !raw && mLastRawState;
                    break;
                case TOGGLE:
                    // If the button was just pressed...
                    if (raw && !mLastRawState)
                        mToggleState = !mToggleState;
                    returnValue = mToggleState;
                    break;
                case EVERY_OTHER_PRESS:
                    if (raw && !mLastRawState)
                    {
                        returnValue = mOtherPress;
                        mOtherPress = !mOtherPress;
                    } else
                        returnValue = false;
                    break;
                case EVERY_OTHER_PRESS_INVERSE:
                    if (raw && !mLastRawState)
                    {
                        returnValue = !mOtherPress;
                        mOtherPress = !mOtherPress;
                    } else
                        returnValue = false;
                    break;
                default:
                    returnValue = false;
            }
            mLastRawState = raw;
            return returnValue;
        }
    }

    public ButtonType getButtonType()
    {
        return mID.getButtonType();
    }

    public ButtonBehaviorType getButtonReturnType()
    {
        return mButtonBehaviorType;
    }

    public final int getID()
    {
        return mID.getID();
    }

    public enum ButtonType
    {
        NORMAL, TRIGGER, POV
    }

    public enum ButtonBehaviorType
    {
        RAW, ON_PRESS, ON_RELEASE, TOGGLE, EVERY_OTHER_PRESS, EVERY_OTHER_PRESS_INVERSE
    }

    public enum ButtonID
    {
        A(1, ButtonType.NORMAL), B(2, ButtonType.NORMAL), X(3, ButtonType.NORMAL), Y(4, ButtonType.NORMAL),
        LEFT_BUMPER(5, ButtonType.NORMAL), RIGHT_BUMPER(6, ButtonType.NORMAL), SELECT(7, ButtonType.NORMAL),
        START(8, ButtonType.NORMAL), LEFT_STICK(9, ButtonType.NORMAL), RIGHT_STICK(10, ButtonType.NORMAL),

        LEFT_TRIGGER(2, ButtonType.TRIGGER), RIGHT_TRIGGER(3, ButtonType.TRIGGER),

        POV_CENTER(-1, ButtonType.POV), POV_UP(0, ButtonType.POV), POV_UP_RIGHT(45, ButtonType.POV), POV_RIGHT(90, ButtonType.POV),
        POV_DOWN_RIGHT(135, ButtonType.POV), POV_DOWN(180, ButtonType.POV), POV_DOWN_LEFT(225, ButtonType.POV),
        POV_LEFT(270, ButtonType.POV), POV_UP_LEFT(-45, ButtonType.POV);;

        private int mID;
        private ButtonType mButtonType;

        ButtonID(int id, ButtonType buttonType)
        {
            mID = id;
            mButtonType = buttonType;
        }

        public int getID()
        {
            return mID;
        }

        public ButtonType getButtonType()
        {
            return mButtonType;
        }
    }
}
