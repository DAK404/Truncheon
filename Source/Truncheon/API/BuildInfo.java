package Truncheon.API;

public class BuildInfo
{
    public static String _Branding = """
  _____ ____  _   _ _   _  ____ _   _ _____ ___  _   _ 
 |_   _|  _ \\| | | | \\ | |/ ___| | | | ____/ _ \\| \\ | |
   | | | |_) | | | |  \\| | |   | |_| |  _|| | | |  \\| |
   | | |  _ <| |_| | |\\  | |___|  _  | |__| |_| | |\\  |
   |_| |_| \\_\\\\___/|_| \\_|\\____|_| |_|_____\\___/|_| \\_|
                                                                                                                             
    """;

    public static String _Version = "1.1.0 \"KATANA\"";
    public static String _BuildDate = "14-May-2022";
    public static String _BuildID = "NION_14_MAY_22_1312";
    public static String _BuildType = "Development";

    public static void viewBuildInfo()
    {
        try
        {
            System.setProperty("file.encoding", "IBM437");

            clearScreen();
            System.out.println(_Branding);
            System.out.println(" Build Version : " + _Version);
            System.out.println(" Build Date    : " + _BuildDate);
            System.out.println(" Build ID      : " + _BuildID);
            System.out.println(" Build Type    : " + _BuildType);
            System.out.println("--------------------------------------------\n");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void clearScreen()
    {
        try
        {
            /*
            * Clear Screen Notes:

            * The program is reliant on clearing the screen based on the OS being run
            * Clear screen has been tested on Windows and Linux platforms only
            * Clear screen should have the IO Flush right after clearing the screen

            */

            if(System.getProperty("os.name").contains("Windows"))

            //Spawns a new process within cmd to clear the screen
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

            else
            //invokes bash to clear the screen
            new ProcessBuilder("/bin/bash", "-c" ,"reset").inheritIO().start().waitFor();

            System.out.flush();
        }
        catch(Exception e)
        {
            System.err.println("\n\nERROR WHILE CLEARING SCREEN");
            System.err.println("ERROR: " + e + "\n\n");
        }
    }    
}
