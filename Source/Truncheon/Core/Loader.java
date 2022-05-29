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


    private static List<String> filePath = new  ArrayList<String>();

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
            break;

            case "repair":
            new Loader().repairMode();
            System.exit(211);
            
            default:
            System.exit(3);
        }
        
        new Loader().loaderLogic();
    }

    private void loaderLogic()throws Exception
    {
        BuildInfo.viewBuildInfo();

        //Invoke Abraxis logic to verify the kernel integrity
        abraxisLogic();
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

        if(manifestFileExists())
        {
            IOStreams.printInfo("Manifest file found.");
            if(populateFiles(new File("./")))
            {
                IOStreams.printInfo("Kernel Files Population Complete.");
                if(checkFileHash())
                {
                    IOStreams.printInfo("File Hash Check Complete.");

                    abraxisResult = 0;

                    if(checkDirectoryStructure())
                        IOStreams.printInfo("Setup Not Required. Booting Program...");
                    else
                    {
                        IOStreams.printAttention("Setup Incomplete.");
                        abraxisResult = 4;
                    }
                }
                else
                {
                    IOStreams.printError("File Integrity Checks failed!");
                    abraxisResult = 1;
                }
            }
            else
            {
                IOStreams.printError("Kernel Files Population failed!");
                abraxisResult = 3;
            }
        }
        else
        {
            IOStreams.printError("Manifest file not found! Aborting...");
            abraxisResult = 2;
        }

        return abraxisResult;
    }

    private boolean manifestFileExists()
    {
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
                //Dont bother checking directories that can change over time
                //The System, Users, .Manifest, SQLite Driver, JRE and the program runner are excluded
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
                    IOStreams.printError("Expected Hash    : " + manifestHash);
                    System.out.println();
                }
            }
            catch(NullPointerException unknownFileFound)
            {
                IOStreams.printAttention("Unrecognized File Found : " + fileName);
                IOStreams.printAttention("Unrecognized File Hash  : " + fileHash);
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
        String[] ignoreList = {".Manifest", "System", "Users", "org", "JRE", "BootShell.cmd"};
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
