package Truncheon.API;

public final class BuildInfo
{
    public final void versionViewer()
    {
        clearScreen();
        System.out.println("///////////////////////\n");
        System.out.println("    Nion: Truncheon    ");
        System.out.println("     Version 0.2.2     \n");
        System.out.println("///////////////////////\n");
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
        System.out.println("Version     : 0.2.2");
        System.out.println("Kernel      : Synergy");
        System.out.println("Date        : 05-August-2021");
        System.out.println("Build ID    : 0.2.2_05.8.2021_1201_TRNCHN");
        System.out.println("OS          : " + System.getProperty("os.name"));
        System.out.println("\nDescription :\nTruncheon is the successor of Mosaic,\nbut now focusing on simplifying the\nprogram to give back the power to the\ndevelopers and end users.\n\n");
        System.gc();
        return;
    }
}