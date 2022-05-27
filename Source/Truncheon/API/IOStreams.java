package Truncheon.API;

public class IOStreams
{
    public static void printInfo(String message)
    {
        System.out.println((char)27 + "[32m[ INFORMATION ] " + message + (char)27 + "[0m");
        message = null;
    }

    public static void printError(String message)
    {
        System.err.println((char)27 + "[31m[    ERROR    ] " + message + (char)27 + "[0m");
        message = null;
    }

    public static void printWarning(String message)
    {
        System.err.println((char)27 + "[33m[   WARNING   ] " + message + (char)27 + "[0m");
        message = null;
    }

    public static void printAttention(String message)
    {
        System.out.println((char)27 + "[36m[  ATTENTION  ] " + message + (char)27 + "[0m");
        message = null;
    }

    public static void println(String message)
    {
        System.out.println(message);
        message = null;
    }    
}
