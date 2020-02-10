package org.frc2851.robot.framework.command;

import java.util.function.BooleanSupplier;

public class Trigger
{
    private Type mType;
    private BooleanSupplier mCondition;

    public Trigger(Type type, BooleanSupplier condition)
    {
        mType = type;
        mCondition = condition;
    }

    public boolean get()
    {
        return mType.get(mCondition.getAsBoolean());
    }

    public Trigger negate()
    {
        return new Trigger(mType, mCondition)
        {
            @Override
            public boolean get()
            {
                return !super.get();
            }
        };
    }

    public interface Type
    {
        boolean get(boolean raw);
    }

    public static class Raw implements Type
    {
        public boolean get(boolean raw)
        {
            return raw;
        }
    }

    public static class OnPress implements Type
    {
        private boolean mLastRawState = false;

        public boolean get(boolean raw)
        {
            boolean returnValue = raw && !mLastRawState;
            mLastRawState = raw;
            return returnValue;
        }
    }

    public static class OnRelease implements Type
    {
        private boolean mLastRawState = false;

        public boolean get(boolean raw)
        {
            boolean returnValue = !raw && mLastRawState;
            mLastRawState = raw;
            return returnValue;
        }
    }

    public static class Toggle implements Type
    {
        private OnPress mOnPress = new OnPress();
        private boolean mToggleState = false;

        public boolean get(boolean raw)
        {
            if (mOnPress.get(raw))
                mToggleState = !mToggleState;

            return mToggleState;
        }
    }

    public static class EveryOtherPress implements Type
    {
        private OnPress mOnPress = new OnPress();
        private boolean mIsOtherPress = false;

        public boolean get(boolean raw)
        {
            if (mOnPress.get(raw))
                mIsOtherPress = !mIsOtherPress;

            return mOnPress.get(raw) && mIsOtherPress;
        }
    }

    public static class EveryOtherPressInverse implements Type
    {
        private OnPress mOnPress = new OnPress();
        private boolean mIsOtherPress = true;

        public boolean get(boolean raw)
        {
            if (mOnPress.get(raw))
                mIsOtherPress = !mIsOtherPress;

            return mOnPress.get(raw) && mIsOtherPress;
        }
    }
}
