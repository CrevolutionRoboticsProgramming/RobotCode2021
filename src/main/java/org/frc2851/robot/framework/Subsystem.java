package org.frc2851.robot.framework;

import java.util.Vector;

public abstract class Subsystem<T extends Subsystem<?>>
{
    private Vector<Component> mComponents;
    private T mInstance = null;

    public Subsystem()
    {
        mComponents = new Vector<>();
    }

    public void periodic()
    {
    }

    public void addComponent(Component component)
    {
        mComponents.add(component);
    }

    public String getName()
    {
        return getClass().getSimpleName();
    }

    protected abstract T getDefaultInstance();

    public T getInstance()
    {
        if (mInstance == null)
            mInstance = getDefaultInstance();
        return mInstance;
    }
}
