package org.frc2851.robot.util;

public class Logger
{
    private static int longestLabel = 0;

    public enum LogLevel
    {
        DEBUG(""), WARNING("WARNING : "), ERROR("ERROR : ");

        private String mLabel;

        LogLevel(String label)
        {
            mLabel = label;
        }

        @Override
        public String toString()
        {
            return mLabel;
        }
    }

    public static void println(LogLevel level, String label, String message)
    {
        if (label.length() > longestLabel)
            longestLabel = label.length();

        if (label.length() < longestLabel)
        {
            for (int i = 0; i <= longestLabel - label.length() + 1; ++i)
                label = " ".concat(label);
        }

        System.out.println(level.toString() + label + ": " + message);
    }

    public static void println(LogLevel level, String message)
    {
        println(level, "", message);
    }
}
