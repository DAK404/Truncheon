package Truncheon.Core;

//Import the required Java IO classes
import java.io.File;
import java.io.Console;
import java.io.FileInputStream;
import java.io.FileOutputStream;

//Import the required Java NIO classes
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

//Import the required Java Util classes
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

//Import the required Java Net package for URL parsing
import java.net.URL;

import Truncheon.API.IOStreams;
import Truncheon.API.BuildInfo;

public class Loader 
{
    Console console = System.console();

    public Loader()
    {
        try
        {

        }
        catch(Exception e)
        {
            System.err.println("FATAL ERROR: " + e);
        }
    }

    public static void main(String[] args)throws Exception
    {
        switch(args[0].toLowerCase())
        {
            case "normal":
            new Loader().PLACEHOLDER_ENTRY_POINT();
            break;
            
            case "debug":
                Console console = System.console();
                String debugParam = console.readLine(">");
                switch(debugParam.toLowerCase())
                {
                    case "io":
                        new Loader()._debugPrintStreams();
                        break;

                    case "buildinfo":
                        System.out.println("Testing Build Info...");
                        BuildInfo.viewBuildInfo();
                        System.out.println("Press RETURN to clear screen...");
                        System.in.read();
                        BuildInfo.clearScreen();
                        break;
                    
                    default:
                        System.out.println("INVALID DEBUG ARGUMENT. QUITTING...");
                        break;
                }
            break;
        }        
    }

    /*
    ----------------------------------------------------------------------------------
    SECTION NAME : REPAIR MODE LOGIC
    OBJECTIVE    : Provide a shell access to repair the system to a working state.
    This mode is restricted to a small set of commands which can be used to find
    the root cause of the kernel integrity failures which prevent booting system
    to the normal mode.
    AUTHOR       : Deepak Anil Kumar (@DAK404)
    ----------------------------------------------------------------------------------
    */
    
    private void repairMode()
    {

    }

    private void repairCmdProcessor()
    {

    }

    private void repairOnlineMode()
    {

    }

    private void downloadBuildFromCloud()
    {

    }

    private void restoreFromBackup()
    {

    }

    private void removeExistingBinaries()
    {

    }

    /*
    ----------------------------------------------------------------------------------
    END OF REPAIR MODE LOGIC
    ----------------------------------------------------------------------------------
    */

    /*
    ----------------------------------------------------------------------------------
    SECTION NAME : ABRAXIS LOGIC
    OBJECTIVE    : Performs an integrity check on the kernel files to prevent any
    unauthorized changes made. This will enforce only signed binaries to be loaded,
    notify any unrecognized files and prevent loading of an unsigned binary file.
    AUTHOR       : Deepak Anil Kumar (@DAK404)
    ----------------------------------------------------------------------------------
    */

    private void abraxisLogic()
    {

    }

    private void populateFiles()
    {

    }

    private void checkDirectoryStructure()
    {

    }

    private void checkFileHash()
    {

    }

    private void fileIgnoreList()
    {

    }

    /*
    ----------------------------------------------------------------------------------
    END OF ABRAXIS LOGIC
    ----------------------------------------------------------------------------------
    */

    /*
    ----------------------------------------------------------------------------------
    SECTION NAME : DEBUG LOGIC
    OBJECTIVE    : These are methods which are used to debug the system during the
    development of the program. These methods shall be removed once the code shall
    mature after testing the intended features.
    Also, do note that the methods shall keep changing over time, therefore, please
    do not build any APIs or code on top of these methods. The instructions for the
    same can be seen in the Documentation, which shall be released once the code is
    completed.
    AUTHOR       : Deepak Anil Kumar (@DAK404)
    ----------------------------------------------------------------------------------
    */

    private void _debugPrintStreams()
    {
        IOStreams.println("Hello World!");
        IOStreams.printInfo("THIS IS AN INFORMATION STRING");
        IOStreams.printWarning("THIS IS A WARNING STRING");
        IOStreams.printAttention("THIS IS AN ATTENTION STRING");
        IOStreams.printError("THIS IS AN ERROR STRING");
    }

    private void PLACEHOLDER_ENTRY_POINT()
    {
        BuildInfo.viewBuildInfo();

        IOStreams.println("DEFAULT OUTPUT");
        IOStreams.printInfo("DEFAULT INFO");
        IOStreams.printWarning("DEFAULT WARNING");
        IOStreams.printAttention("DEFAULT ATTENTION");
        IOStreams.printError("DEFAULT ERROR");

        System.out.println("End Of Demo.");
    }

    /*
    ----------------------------------------------------------------------------------
    END OF DEBUG LOGIC
    ----------------------------------------------------------------------------------
    */
}
