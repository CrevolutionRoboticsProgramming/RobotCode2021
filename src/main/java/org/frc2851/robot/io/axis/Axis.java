package org.frc2851.robot.io.axis;

public class Axis
{
    private AxisID mID;
    private AxisShaper mShaper;

    public Axis(AxisID id, AxisShaper shaper)
    {
        mID = id;
        mShaper = shaper;
    }

    public Axis(AxisID id)
    {
        this(id, (input) -> input);
    }

    public double get(double raw)
    {
        return mShaper.shape(raw);
    }

    public int getID()
    {
        return mID.getID();
    }

    public enum AxisID
    {
        LEFT_X(0), LEFT_Y(1), LEFT_TRIGGER(2), RIGHT_TRIGGER(3), RIGHT_X(4), RIGHT_Y(5);

        private int mID;

        AxisID(int id)
        {
            mID = id;
        }

        public int getID()
        {
            return mID;
        }
    }

    public interface AxisShaper
    {
        double shape(double input);
    }
}
