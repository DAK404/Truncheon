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
import Truncheon.API.ExceptionHandler;

/**
 * Loader class to load the Kernel up
 * Conforms to the Nion Program Structure
 * 
 * @author: DAK404 (https://github.com/DAK404)
 * @version: 
 * @since: 
 */
public class Loader
{

    private Console console = System.console();

    private static List<String> filePath = new  ArrayList<String>();

    public Loader()
    {
        try
        {
            System.out.println("Nion: Truncheon");
            System.out.println("Booting Program. Please wait...");
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
            break;

            case "repair":
            new Loader().repairMode();
            System.exit(211);

            // case "safemode":

            // break;

            case "debug_ex_ha":
            new Loader().debugExceptionHandler();

            default:
            System.exit(3);
        }

        System.gc();
        BuildInfo.viewBuildInfo();
        new Loader().loaderLogic();
    }

    private void loaderLogic()throws Exception
    {
        try
        {
            switch(abraxisLogic())
            {
                case 4:
                    new Setup().setupLogic();
                    break;
            }

            System.out.println("DEFAULT");
            while(! console.readLine("X> ").equalsIgnoreCase("exit"))
            {
                BuildInfo.viewBuildInfo();
                debug();
            }
        }
        catch(Exception e)
        {

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
        System.out.println("REPAIR MODE: WORK IN PROGRESS");
        console.readLine();
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

    private byte abraxisLogic()throws Exception
    {
        byte abraxisResult = 55;
        /*
        Return value table:

        RETURN VALUE	MEANING
            0           File integrity OK
            1           File integrity FAILED
            2           Manifest File Missing
            3           Kernel File Population Failed
            4           Program Setup Required

        */

        //Check if the manifest File is found
        if(manifestFileExists())
        {
            IOStreams.printInfo("Manifest file found.");

            //Begin the population of the files in the installed directory of Truncheon
            if(populateFiles(new File("./")))
            {
                IOStreams.printInfo("Kernel Files Population Complete.");

                //Begin checking the file hashes to enforce integrity
                if(checkFileHash())
                {
                    IOStreams.printInfo("File Hash Check Complete.");

                    //Set the return value result to 0, denoting that the File Integrity is okay
                    abraxisResult = 0;

                    //Check if the program requires the first time setup
                    if(checkDirectoryStructure())
                        IOStreams.printInfo("Setup Not Required. Booting Program...");

                    else
                    {
                        IOStreams.printAttention("Setup Incomplete.");

                        //Set the return value to be 4, denoting that the program requires setup
                        abraxisResult = 4;
                    }
                }

                else
                {
                    IOStreams.printError("File Integrity Checks failed!");

                    //Set the return value to be 1, denoting that the File Integrity has failed
                    abraxisResult = 1;
                }
            }

            else
            {
                IOStreams.printError("Kernel Files Population failed!");

                //Set the return value to be 3, denoting that the Kernel File Population has failed
                abraxisResult = 3;
            }
        }

        else
        {
            IOStreams.printError("Manifest file not found! Aborting...");

            //Set the return value to be 2, denoting that the Manifest File is missing
            abraxisResult = 2;
        }

        return abraxisResult;
    }

    private boolean manifestFileExists()
    {
        //Check if the manifest file exists
        return new File("./.Manifest/Truncheon/Manifest.m1").exists();
    }

    private boolean populateFiles(File checkDir)
    {
        boolean filePopulationStatus = false;
        try
        {
            File[] fileList = checkDir.listFiles();

            for (File f: fileList)
            {
                //Don't bother checking directories that can change over time
                //The System, Users, .Manifest, SQLite Driver, JRE, Logs and the program runner are excluded
                if(fileIgnoreList(f.getName()))
                    continue;

                if (f.isDirectory())
                    populateFiles(f);

                if (f.isFile())
                {
                    String a = f.getPath();
                    filePath.add(a);
                }
            }
            filePopulationStatus = true;
        }
        catch(Exception e)
        {
            //Write the exception to File
        }
        return filePopulationStatus;
    }

    private boolean checkFileHash()throws Exception
    {
        boolean kernelIntegrity = true;

        Properties props = new Properties();
        FileInputStream manifestEntries = new FileInputStream("./.Manifest/Truncheon/Manifest.m1");
        props.loadFromXML(manifestEntries);
        manifestEntries.close();

        // DEBUG CODE //
        //props.list(System.out);
        // DEBUG CODE //

        for(String fileName: filePath)
        {
            // if(fileIgnoreList(fileName))
            //     continue;


            String fileHash = new Truncheon.API.Minotaur.Cryptography().fileToMD5(fileName);

            try
            {
                String manifestHash = (System.getProperty("os.name").contains("Windows")?props.get(fileName):props.get(fileName.replaceAll(File.separator, "\\\\"))).toString();

                if(!manifestHash.equals(fileHash))
                {
                    kernelIntegrity = false;
                    IOStreams.printError("Integrity Failure: " + fileName);
                    IOStreams.printError("File Hash        : " + fileHash);
                    System.out.println();
                }
            }
            catch(NullPointerException unknownFileFound)
            {
                IOStreams.printAttention("Unrecognized File Found : " + fileName);
                IOStreams.printAttention("Unrecognized File Hash  : " + fileHash);
                System.out.println();
            }
        }

        return kernelIntegrity;
    }


    private boolean checkDirectoryStructure()
    {
        //logic to check if the program has been setup or it needs to be setup
        return new File("./System/Truncheon").exists() && new File("./Users/Truncheon").exists();
    }

    private boolean fileIgnoreList(String fileName)
    {
        boolean status = false;
        String[] ignoreList = {".Manifest", "System", "Users", "org", "JRE", "BootShell.cmd", "Logs"};
        for(String files : ignoreList)
        {
            if(fileName.equalsIgnoreCase(files))
            {
                status = true;
                break;
            }
        }
        return status;
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
    private void debugExceptionHandler()
    {
        try
        {
            throw new Exception();
        }
        catch(Exception e)
        {
            new ExceptionHandler().handleException(e);
        }

    }

    private void debug()
    {
        int mb = 1024 * 1024;
        // get Runtime instance
        Runtime instance = Runtime.getRuntime();
        System.out.println("\n*********************************************");
        System.out.println("        ---   DEBUG INFORMATION   ---        ");
        System.out.println("*********************************************");
        System.out.println("\n   - Heap utilization statistics [MB] -  \n");
        System.out.println("      [*]  Process ID   : "+ProcessHandle.current().pid());
         // available memory
        System.out.println("      [*]  Total Memory : " + instance.totalMemory() + " Bytes");
        // free memory
        System.out.println("      [*]  Free Memory  : " + instance.freeMemory() + " Bytes");
        // used memory
        System.out.println("      [*]  Used Memory  : " + (instance.totalMemory() - instance.freeMemory()) + " Bytes");
        // Maximum available memory
        System.out.println("      [*]  Max Memory   : " + instance.maxMemory() + " Bytes");
        System.out.println("\n*********************************************\n\n");
    }
    /*
    ----------------------------------------------------------------------------------
    END OF DEBUG LOGIC
    ----------------------------------------------------------------------------------
    */
}
