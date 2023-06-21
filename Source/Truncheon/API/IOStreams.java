package Truncheon.API;

import java.io.Console;

public class IOStreams
{
    static String[] _textColorForeground = {"[30", "[31", "[32", "[33", "[34", "[35", "[36", "[37", "[39"};
    static String[] _textColorBackground = {"40", "41", "42", "43", "44", "45", "46", "47", "49"};
    
    public static void printInfo(String message)
    {
        println(2, 8, "[ INFORMATION ] " + message);
    }

    public static void printError(String message)
    {
        println(1, 8, "[    ERROR    ] " + message);
    }

    public static void printWarning(String message)
    {
        println(3, 8, "[   WARNING   ] " + message);
    }

    public static void printAttention(String message)
    {
        println(5, 8, "[  ATTENTION  ] " + message);
    }

    public static void println(String message)
    {
        System.out.println(message);
    }

    /**
     *  <pre>
        +~~~~~~~+~~~~~~+~~~~~~+~~~~~~~~~~~+
        | Index |  fg  |  bg  |  color    |
        +~~~~~~~+~~~~~~+~~~~~~+~~~~~~~~~~~+
        |   0   |  30  |  40  |  black    |
        |   1   |  31  |  41  |  red      |
        |   2   |  32  |  42  |  green    |
        |   3   |  33  |  43  |  yellow   |
        |   4   |  34  |  44  |  blue     |
        |   5   |  35  |  45  |  magenta  |
        |   6   |  36  |  46  |  cyan     |
        |   7   |  37  |  47  |  white    |
        |   8   |  39  |  49  |  default  |
        +~~~~~~~+~~~~~~+~~~~~~+~~~~~~~~~~~+
        </pre>
     * 
     * @param foregroundIndex: Index value for the foreground color
     * @param backgroundIndex: Index value for the background color
     * @param message: The intended message that needs to be printed on the screen
     */
    public static void println(int foregroundIndex, int backgroundIndex, String message)
    {
        try
        {
            System.out.println((char)27 + _textColorForeground[foregroundIndex] + ";" + _textColorBackground[backgroundIndex] + "m" + message + (char)27 + "[0m");
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            System.out.println(e + "Invalid Syntax.");
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    public static void confirmReturnToContinue()
    {
        Console console = System.console();
        console.readLine("Press RETURN to Continue...");
    }
}
