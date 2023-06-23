package Truncheon.API;

public class BuildInfo
{
    public final static String _Branding = """
                           |                                           |      
                           ||                                          ||     
|||||||| |||||||  |||  ||| |||  |||  |||||| |||  ||| |||||||| |||||||  |||  |||
   |||         || |||  ||| |||| ||| |||     |||  |||                || |||| |||
   |||    ||||||  |||  ||| |||||||| |||     ||||||||  |||||||  ||   || ||||||||
   |||    ||  ||  |||  ||| ||| |||| |||     |||  |||  |||      ||   || ||| ||||
   |||    ||   ||  ||||||  |||  |||  |||||| |||  |||  |||||||   |||||  |||  |||
                                 ||         |||                              ||
                                  |                                           |""";

    public final static String _KernelName = "Truncheon";
    public final static String _Version = "1.1.0";
    public final static String _VersionCodeName = "Katana";
    public final static String _BuildDate = "20-MAY-2023";
    public final static String _BuildID = "20230520-1647_NION";
    public final static String _BuildType = "DEBUG_&_TEST_0";

    public static void viewBuildInfo()
    {
        try
        {
            clearScreen();
            IOStreams.println(_Branding);
            IOStreams.println("Nion: " + _KernelName);
            IOStreams.println("Version: " + _Version + " [" + _BuildID + "]\n");
            IOStreams.println("[ CAUTION ] - " + _BuildType + " Build! Expect Errors and Crashes!\n");
            IOStreams.println("-----------------------------------------------------------------------------\n");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        System.gc();
    }

    public static void aboutProgram()
    {
        try
        {
            clearScreen();
            IOStreams.println(_Branding);
            IOStreams.println("Nion: Truncheon");
            IOStreams.println("\nAn easy to use, cross-platform shell written in Java\nfor the ease of use and convenience for all users!");
            IOStreams.println("\nBuild Version    : " + _Version);
            IOStreams.println("Version Codename : " + _VersionCodeName);
            IOStreams.println("Build Date       : " + _BuildDate);
            IOStreams.println("Build Identifier : " + _BuildID);
            IOStreams.println("Build Type/Branch: " + _BuildType);
            IOStreams.println("Kernel Name      : " + _KernelName);
            IOStreams.println("---------------------------------------------\n");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        System.gc();
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
