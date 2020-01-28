package org.frc2851.robot.io;

import java.util.List;
import java.util.Vector;

public class Button
{
    private Controller mController;
    private ButtonID mID;
    private ButtonStateRetriever mButtonStateRetriever;
    private Vector<Button> mComboButtons;

    public Button(Controller controller, ButtonID id, ButtonStateRetriever buttonStateRetriever, Button... comboButtons)
    {
        mController = controller;
        mID = id;
        mButtonStateRetriever = buttonStateRetriever;
        mComboButtons = new Vector<>(List.of(comboButtons));
    }

    public boolean get()
    {
        boolean allComboButtonsPressed = true;
        for (Button comboButton : mComboButtons)
        {
            if (!comboButton.get())
                allComboButtonsPressed = false;
        }

        boolean raw = mController.get(mID) && allComboButtonsPressed;

        return mButtonStateRetriever.get(raw);
    }

    public ButtonType getButtonType()
    {
        return mID.getButtonType();
    }

    public ButtonStateRetriever getButtonStateRetriever()
    {
        return mButtonStateRetriever;
    }

    public final int getID()
    {
        return mID.getID();
    }

    public enum ButtonType
    {
        NORMAL, TRIGGER, POV
    }

    public enum ButtonID
    {
        A(1, ButtonType.NORMAL), B(2, ButtonType.NORMAL), X(3, ButtonType.NORMAL), Y(4, ButtonType.NORMAL),
        LEFT_BUMPER(5, ButtonType.NORMAL), RIGHT_BUMPER(6, ButtonType.NORMAL), SELECT(7, ButtonType.NORMAL),
        START(8, ButtonType.NORMAL), LEFT_STICK(9, ButtonType.NORMAL), RIGHT_STICK(10, ButtonType.NORMAL),

        LEFT_TRIGGER(2, ButtonType.TRIGGER), RIGHT_TRIGGER(3, ButtonType.TRIGGER),

        POV_CENTER(-1, ButtonType.POV), POV_UP(0, ButtonType.POV), POV_UP_RIGHT(45, ButtonType.POV), POV_RIGHT(90, ButtonType.POV),
        POV_DOWN_RIGHT(135, ButtonType.POV), POV_DOWN(180, ButtonType.POV), POV_DOWN_LEFT(225, ButtonType.POV),
        POV_LEFT(270, ButtonType.POV), POV_UP_LEFT(-45, ButtonType.POV),

        GUITAR_GREEN(1, ButtonType.NORMAL), GUITAR_RED(2, ButtonType.NORMAL),
        GUITAR_BLUE(3, ButtonType.NORMAL), GUITAR_YELLOW(4, ButtonType.NORMAL),
        GUITAR_ORANGE(5, ButtonType.NORMAL), GUITAR_BACK(7, ButtonType.NORMAL),
        GUITAR_START(8, ButtonType.NORMAL),

        GUITAR_STRUM_NEUTRAL(-1, ButtonType.POV), GUITAR_STRUM_UP(0, ButtonType.POV),
        GUITAR_STRUM_DOWN(180, ButtonType.POV),

        DRUM_GREEN(1, ButtonType.NORMAL), DRUM_RED(2, ButtonType.NORMAL),
        DRUM_BLUE(3, ButtonType.NORMAL), DRUM_YELLOW(4, ButtonType.NORMAL),
        DRUM_PEDAL(5, ButtonType.NORMAL),
        DRUM_COMBO(10, ButtonType.NORMAL);

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

    public static abstract class ButtonStateRetriever
    {
        public abstract boolean get(boolean raw);
    }

    public static class RawButton extends ButtonStateRetriever
    {
        @Override
        public boolean get(boolean raw)
        {
            return raw;
        }
    }

    public static class OnPressButton extends ButtonStateRetriever
    {
        private boolean mLastRawState = false;

        @Override
        public boolean get(boolean raw)
        {
            boolean returnValue = raw && !mLastRawState;
            mLastRawState = raw;
            return returnValue;
        }
    }

    public static class OnReleaseButton extends ButtonStateRetriever
    {
        private boolean mLastRawState = false;

        @Override
        public boolean get(boolean raw)
        {
            boolean returnValue = !raw && mLastRawState;
            mLastRawState = raw;
            return returnValue;
        }
    }

    public static class ToggleButton extends ButtonStateRetriever
    {
        private boolean mLastRawState = false;
        private boolean mToggleState = false;

        @Override
        public boolean get(boolean raw)
        {
            if (raw && !mLastRawState)
                mToggleState = !mToggleState;
            mLastRawState = raw;
            return mToggleState;
        }
    }

    public static class EveryOtherPressButton extends ButtonStateRetriever
    {
        private boolean mLastRawState = false;
        private boolean mOtherPress = false;

        @Override
        public boolean get(boolean raw)
        {
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

    public static class EveryOtherPressInverseButton extends ButtonStateRetriever
    {
        private boolean mLastRawState = false;
        private boolean mOtherPress = false;

        @Override
        public boolean get(boolean raw)
        {
            boolean returnValue;

            if (raw && !mLastRawState)
            {
                returnValue = !mOtherPress;
                mOtherPress = !mOtherPress;
            } else
                returnValue = false;

            mLastRawState = raw;

            return returnValue;
        }
    }
}
