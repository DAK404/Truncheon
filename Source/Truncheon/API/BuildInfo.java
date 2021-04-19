package Truncheon.API;

public final class BuildInfo
{
    public void versionViewer()
    {
        clearScreen();
        System.out.println("Truncheon Kernel 0.1.4_X");
        System.out.println("Build Date: 19-Apr-2021");
        System.out.println("Notes: Prototype Build\n\n");
    }

    public void clearScreen()
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
                new ProcessBuilder("/bin/bash", "-c" ,"clear").inheritIO().start().waitFor();
            
            System.out.flush();
            return;
        }
        catch(Exception E)
        {
            //Pass the errors to Error Handler.
        }
    }

    public void debuggingAPI()
    {
        System.out.println("Program Debug API\nPowered by Truncheon Debugger 2.0");
        System.out.println("Checking versionViewer API");
        versionViewer();
        System.out.println("Checking clearScreen API");
        clearScreen();
    }
}