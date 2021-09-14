package Truncheon.API;

/**
 * Program to display the information of the current build
 * 
 * @version 0.3.1
 * @since 0.0.1
 * @author DAK404
 */
public final class BuildInfo
{
    /**The String Constant which holds the version number of the program*/
    public final String _version = "1.0.0";

    /**The String Constant which holds the name of the kernel used*/
    public final String _kernel = "Synergy";

    /**The String Constant which holds the Build ID of the program*/
    public final String _buildID = _version + "_11.08.2021_1944_TRNCHN";

    /**The String Constant which holds the date compiled of the build*/
    public final String _buildDate = "11-August-2021";

    /**
     * Displays the basic program information on the screen
     */
    public final void versionViewer()
    {
        clearScreen();
        System.out.println("///////////////////////\n");
        System.out.println("    Nion: Truncheon    ");
        System.out.println("     Version " + _version +"     \n");
        System.out.println("///////////////////////");
        System.out.println(" - RELEASE CANDIDATE - \n");
        //debugMemoryInformation();
    }

    /**
     * Clears the screen, dependent on the OS
     */
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
        }
        catch(Exception E)
        {
            //Handle any exceptions thrown during runtime
            new Truncheon.API.ErrorHandler().handleException(E);
        }
    }

    /**
     * Displays detailed information about the program.
     * 
     * useful for extra information 
     * @throws Exception
     */
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
    }

    /**
     * The Information about the memeory used when this API is called
     */
    /*
    private void debugMemoryInformation()
    {
        // get Runtime instance
        Runtime instance = Runtime.getRuntime();
        System.out.println("*****************************************");
        System.out.println("      ---   DEBUG INFORMATION   ---      ");
        System.out.println("*****************************************");
        System.out.println("\n  - Heap utilization statistics -  \n ");
        System.out.println(" [*]  Process ID   : " + ProcessHandle.current().pid());
         // available memory
        System.out.println(" [*]  Total Memory : " + instance.totalMemory()  + " Bytes");
        // free memory
        System.out.println(" [*]  Free Memory  : " + instance.freeMemory()  + " Bytes");
        // used memory
        System.out.println(" [*]  Used Memory  : " + (instance.totalMemory() - instance.freeMemory())  + " Bytes"); 
        // Maximum available memory
        System.out.println(" [*]  Max Memory   : " + instance.maxMemory()  + " Bytes");
        System.out.println("\n*****************************************\n\n");
    } */
}