package org.frc2851.robot.framework.command;

import org.frc2851.robot.framework.Component;

public class RunCommand extends Command
{
    private Runnable mToRun;

    public RunCommand(Runnable toRun, String name, Component... requirements)
    {
        super(name, requirements);
        mToRun = toRun;
    }

    @Override
    public void execute()
    {
        super.execute();
        mToRun.run();
    }
}
