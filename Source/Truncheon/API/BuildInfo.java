package Truncheon.API;

public final class BuildInfo
{
    public final String _version = "1.0.0";
    public final String _kernel = "Synergy";
    public final String _buildID = _version + "_11.08.2021_1944_TRNCHN";
    public final String _buildDate = "11-August-2021";

    public final void versionViewer()
    {
        clearScreen();
        System.out.println("///////////////////////\n");
        System.out.println("    Nion: Truncheon    ");
        System.out.println("     Version " + _version +"     \n");
        System.out.println("///////////////////////");
        System.out.println(" - RELEASE CANDIDATE - \n");
    }

    public final void clearScreen()
    {
        try
        {
            /*
                Clear Screen Notes:
                
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
            return;
        }
        catch(Exception E)
        {
            new Truncheon.API.ErrorHandler().handleException(E);
        }
    }

    public final void about()throws Exception
    {
        clearScreen();
        System.out.println("Nion: Truncheon\n_______________\n");

        System.out.println("Iteration   : 8");
        System.out.println("Version     : " + _version);
        System.out.println("Kernel      : " + _kernel);
        System.out.println("Date        : " + _buildDate);
        System.out.println("Build ID    : " + _buildID);
        System.out.println("OS          : " + System.getProperty("os.name"));
        System.out.println("\nDescription :\nTruncheon is the successor of Mosaic,\nbut now focusing on simplifying the\nprogram to give back the power to the\ndevelopers and end users.\n\n");
        System.gc();
        return;
    }
}