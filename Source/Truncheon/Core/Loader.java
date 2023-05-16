/*
*                             |                                          |
*                             ||                                         ||
*   |||||||| |||||||  |||  || |||  |||  |||||| |||  || |||||||| |||||||  |||  |||
*      |||         || |||  || |||| ||| |||     |||  ||                || |||| |||
*      |||    ||||||  |||  || |||||||| |||     |||||||  |||||||  ||   || ||||||||
*      |||    ||  ||  |||  || ||| |||| |||     |||  ||  |||      ||   || ||| ||||
*      |||    ||   ||  |||||  |||  |||  |||||| |||  ||  |||||||   |||||  |||  |||
*                                   ||         |||                             ||
*                                    |                                          |
*/

/*
* ---------------!DISCLAIMER!--------------- *
*                                            *
*   <INSERT THE DISCLAIMER HERE>             *
*                                            *
*   THIS CODE FALLS UNDER THE LGPL LICENSE.  *
*    YOU MUST INCLUDE THIS DISCLAIMER WHEN   *
*        DISTRIBUTING THE SOURCE CODE.       *
*   (SEE LICENSE FILE FOR MORE INFORMATION)  *
*                                            *
* ------------------------------------------ *
*/

package Truncheon.Core;

//Import the required Java IO classes
import java.io.File;
import java.io.Console;
import java.io.FileInputStream;

//Import the required Java Util classes
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;

//Import the required Truncheon classes
import Truncheon.API.IOStreams;
import Truncheon.API.BuildInfo;
import Truncheon.API.ExceptionHandler;

//Import the required Java SQL classes
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

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

    public static void main(String[] args)throws Exception
    {
        
        switch(args[0].toLowerCase())
        {
            case "normal":
            break;

            case "debug_ex_ha":
            new Loader().debugExceptionHandler();

            case "iostreams":
            new Loader().printTest();
            break;

            default:
            System.exit(3);
        }
        System.gc();
        new Loader().loaderLogic();
    }

    private void loaderLogic()throws Exception
    {
        try
        {
            switch(abraxisLogic())
            {
                case 0:
                break;

                case 1:
                IOStreams.printError("File Integrity Check Failure. Cannot Boot Program.");
                break;

                case 2:
                IOStreams.printError("Kernel File Integrity Violation! Aborting Program Boot.");
                System.exit(4);
                break;

                case 3:
                IOStreams.printError("Manifest File Missing! Aborting startup...");
                System.exit(4);
                break;

                case 4:
                IOStreams.printError("Failed to populate the files. Cannot Boot Program.");
                System.exit(4);
                break;

                case 5:
                new Setup().setupLogic();
                break;
            }

            System.out.println();

            BuildInfo.viewBuildInfo();
            //Start a limited shell here.
            String tempInput = "";
            do
            {
                tempInput = console.readLine("~lounge> ");

                switch(tempInput.toLowerCase())
                {
                    case "login":
                    new Truncheon.Core.NionKernel().startNionKernel();
                    System.gc();
                    break;

                    case "clear":
                    BuildInfo.viewBuildInfo();
                    break;

                    case "mem":
                    debug();
                    break;

                    case "restart":
                    System.exit(211);
                    break;
                }
            }
            while(! tempInput.equalsIgnoreCase("exit"));
        }
        catch(Exception e)
        {

        }
    }

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

        ----------------------------------------------------
        | RETURN VALUE |    	MEANING                    |
        ----------------------------------------------------
        |       0      |  File integrity OK                |
        |       1      |  Kernel integrity FAILED          |
        |       2      |  File checking FAILED             |
        |       3      |  Kernel File Population Failed    |
        |       4      |  Manifest File Corrupt or Missing |
        |       5      |  Program Setup Required           |
        ----------------------------------------------------
        */

        //check if Manifest File exists
        if(manifestFileExists())
        {
            IOStreams.printInfo("Manifest file found. Populating files and directories...");

            //begin the population of files in directory

            //Begin the population of the files in the installed directory of Truncheon
            if(populateFiles(new File("./")))
            {
                IOStreams.printInfo("Files and Directories populated! Running Integrity Checks...");
                //check core files first

                IOStreams.printInfo("Checking Core Files...");
                if(checkCoreFiles())
                {
                    IOStreams.printInfo("Checking Kernel Integrity...");
                    if(checkFileHash())
                    {
                        abraxisResult = 0;

                        IOStreams.printInfo("Checking Program Setup status...");

                        if(checkDirectoryStructure())
                        IOStreams.printInfo("Setup Completed! Booting Program...");
                        else
                        {
                            IOStreams.printAttention("Setup Incomplete.");

                            //Set the return value to be 4, denoting that the program requires setup
                            abraxisResult = 5;
                        }
                    }
                    else
                    {
                        IOStreams.printError("Kernel File Checking Failed! Aborting.");

                        abraxisResult = 1;
                    }
                }
                else
                {
                    IOStreams.printError("Kernel Integrity Failed! Aborting.");

                    abraxisResult = 2;
                }
            }
            else
            {
                IOStreams.printError("Kernel File Population Failed! Aborting.");

                abraxisResult = 3;
            }
        }
        else
        {
            IOStreams.printError("Manifest File Error! Aborting.");

            abraxisResult = 4;
        }

        System.gc();
        return abraxisResult;
    }


    private boolean manifestFileExists()
    {
        //Check if the manifest file exists
        return new File("./.Manifest/Truncheon/KernelFilesHashes.m1").exists();
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

    //read the files that are supposed to be in a Manifest .m2 file and check if the contents are the same as the directory structure before file hash checks
    private boolean checkCoreFiles()throws Exception
    {
        int fileCount = 0;
        boolean returnValue = true;

        try
        {
            Properties props = new Properties();
            FileInputStream manifestEntries = new FileInputStream("./.Manifest/Truncheon/KernelFiles.m2");
            props.loadFromXML(manifestEntries);
            manifestEntries.close();

            // DEBUG CODE //
            //props.list(System.out);
            // DEBUG CODE //

            for(String fp : filePath)
            {
                if(fp.endsWith(".class"))
                {
                    if((System.getProperty("os.name").contains("Windows")?fp:convertSlashFormat(fp)) == (props.get(fp)))
                    {
                        System.out.println(props.get(fp).getClass());
                        System.out.println(fp);
                        returnValue = false;
                        break;
                    }
                    fileCount++;
                }
            }

            if(props.size() > fileCount)
            returnValue = false;
        }
        catch(Exception e)
        {

        }

        return returnValue;
    }

    private boolean checkFileHash()throws Exception
    {
        boolean kernelIntegrity = true;

        try
        {
            Properties props = new Properties();
            FileInputStream manifestEntries = new FileInputStream("./.Manifest/Truncheon/KernelFilesHashes.m1");
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
                    String manifestHash = (System.getProperty("os.name").contains("Windows")?props.get(fileName):props.get(convertSlashFormat(fileName))).toString();

                    if(!manifestHash.equals(fileHash))
                    {
                        kernelIntegrity = false;
                        IOStreams.printError("Integrity Failure: " + fileHash + "\t" + fileName);
                        //IOStreams.printError("File Hash        : " + fileHash);
                        System.out.println();
                    }
                }
                catch(NullPointerException unknownFileFound)
                {
                    IOStreams.printAttention("Unrecognized File Found : " + fileHash + "\t" + fileName);
                    System.out.println();
                }
            }
        }
        catch(Exception e)
        {
            IOStreams.printError("Manifest File Population Failure.");
            kernelIntegrity = false;
        }
        filePath.clear();
        System.gc();
        return kernelIntegrity;
    }

    private String convertSlashFormat(String path)
    {
        return path.replaceAll(File.separator, "\\\\");
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
        System.gc();
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

    private void printTest()throws Exception
    {
        IOStreams.printInfo("Test");
        IOStreams.printAttention("Test");
        IOStreams.printError("Test");
        IOStreams.printWarning("Test");

        IOStreams.println(0, 7, "Hello World!");
        IOStreams.println(3, 8, "Hello World!");
        IOStreams.println(8, 1, "Hello World!");
    }

    private void debug()
    {
        //int mb = 1024 * 1024;
        // get Runtime instance
        Runtime instance = Runtime.getRuntime();
        System.out.println("\n*********************************************");
        System.out.println("        ---   DEBUG INFORMATION   ---        ");
        System.out.println("*********************************************");
        System.out.println("\n   - Heap utilization statistics [Bytes] -  \n");
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

    //logic to handle the compilation of non-linked/under development modules

    private void compileDeez()throws Exception
    {
        new Truncheon.API.Dragon.AccountDelete("test");
        new Truncheon.API.Dragon.AccountModify();
        new Truncheon.API.Wraith.WraithRead();
        new Truncheon.API.Wraith.WraithEdit();
        new Truncheon.Core.NionKernel();
        new Truncheon.API.Anvil();
        new Truncheon.API.Grinch.FileManagement("");
    }

    /*
    ----------------------------------------------------------------------------------
    END OF DEBUG LOGIC
    ----------------------------------------------------------------------------------
    */
}

/**
* Program to make Truncheon ready for normal use
*
* @author: DAK404 (https://github.com/DAK404)
* @version: 7.3.6
* @since: 2018
*/
class Setup
{
    /**
    * Instantiate the Console class to accept input operations through the console
    */
    Console console = System.console();

    /**
    * Initialize a set of strings to show various statuses of the setup stages
    */
    private String prereqInfoStatus = "PENDING";
    private String initDB = "PENDING";
    private String initDirs = "PENDING";
    private String initPolicies = "PENDING";
    private String initAdminAccount = "PENDING";

    /**
    * The main logic of the setup program
    *
    * @throws Exception
    */
    public void setupLogic()throws Exception
    {
        displayPrerequisiteInformation();
        initializeDatabase();
        initializeDirectories();
        initializeDefaultPolicies();
        initializeAdministratorAccount();

        displaySetupProgress();
        IOStreams.printAttention("Setup Complete!\nYou may now use Truncheon Shell!\nThe program needs to reboot to apply the changes.\n\nDo you want to check for new updates? [ Y | N ]");
        console.readLine();
        System.exit(101);
    }

    /**
    * Shows a set of prerequisite information to the user before the setup starts.
    */
    private void displayPrerequisiteInformation()
    {
        displaySetupProgress();
        String displaySetupMessage = """
        Welcome to Truncheon!

        This program needs to be setup before it can be used normally.
        If you are a System Administrator, please continue the setup. If not, please contact the System Administrator for more information.

        The setup cannot be interrupted, and if done so, the program will need to be reset and setup to make the shell usable by the end user.
        The setup is a one time process and will need to be done only once.

        Press ENTER to continue, or press the CTRL + C keys to exit.
        """;

        IOStreams.println(displaySetupMessage);
        console.readLine("Setup> ");

        displaySetupProgress();
        IOStreams.printAttention("THE PROGRAM LICENSE SHALL BE DISPLAYED TO YOU. IF YOU ACCEPT IT, PRESS Y. ELSE, PRESS N.\nBY PROCEEDING TO SETTING UP AND USE THE PROGRAM, YOU HEREBY AGREE THAT YOU ACCEPT THE PROGRAM LICENSE CLAUSES.\nIF YOU DO NOT WANT TO AGREE TO THE LICENSE CLAUSES IN THE FUTURE, PLEASE UNINSTALL THE PROGRAM IMMEDIATELY.");
        console.readLine("Press RETURN to continue, or press CTRL + C to quit.");

        //Read the EULA file
        IOStreams.printAttention("Do you agree to the clauses specified in the license?");
        if(console.readLine("Accept EULA?> ").equalsIgnoreCase("N"))
        {
            System.exit(0);
        }
        else
        {
            //display the readme, changelog and the other files
        }
        prereqInfoStatus = "COMPLETE";
    }

    /**
    * Initialize a set of directories required by Truncheon
    */
    private void initializeDirectories()
    {
        displaySetupProgress();
        String [] directoryNames = {"./System/Truncheon/Public/Logs", "./Users/Truncheon"};
        for (String dirs: directoryNames)
        new File(dirs).mkdirs();
        initDirs = "COMPLETE";
    }

    /**
    * Initialize the database file and its table for user credential operations
    */
    private void initializeDatabase()
    {
        displaySetupProgress();
        boolean initializeDatabaseStatus = false;
        try
        {
            new File("./System/Truncheon/Private/Backups").mkdirs();
            String databasePath = "jdbc:sqlite:./System/Truncheon/Private/Mud.dbx";
            IOStreams.printInfo("Checking for existing Master User Database...");

            if(new File(databasePath).exists())
            IOStreams.printError("Master User Database already exists! Aborting...");
            else
            {
                String createMUDTable = "CREATE TABLE IF NOT EXISTS MUD (" +
                "Username TEXT," +
                "Name TEXT NOT NULL," +
                "Password TEXT NOT NULL," +
                "SecurityKey TEXT NOT NULL," +
                "PIN TEXT NOT NULL," +
                "Privileges TEXT NOT NULL," +
                "PRIMARY KEY(Username));";

                Class.forName("org.sqlite.JDBC");
                Connection dbConnection = DriverManager.getConnection(databasePath);
                Statement statement = dbConnection.createStatement();

                statement.execute(createMUDTable);

                statement.close();
                dbConnection.close();

                System.gc();

                initializeDatabaseStatus = true;
            }
        }
        catch(Exception e)
        {
            new Truncheon.API.ExceptionHandler().handleException(e);
        }
        initDB = (initializeDatabaseStatus?"COMPLETE":"FAILED");
    }

    /**
    * Initialize the first administrator account
    */
    private void initializeAdministratorAccount()throws Exception
    {
        new Truncheon.API.Dragon.AccountCreate().createDefaultAdministratorAccount();
        initAdminAccount = "COMPLETE";
    }

    /**
    * Initialize all the default policies along with a system name appended by a set of random numbers
    */
    private void initializeDefaultPolicies()
    {
        displaySetupProgress();
        new Truncheon.API.Minotaur.PolicyEdit();
        initPolicies = "COMPLETE";
    }

    /**
    * Display the progress of the setup program to the user
    */
    private void displaySetupProgress()
    {
        BuildInfo.viewBuildInfo();
        IOStreams.println("[ -- Program Setup Checklist -- ]");
        IOStreams.println("[*] Show Program Prerequisites   : " + prereqInfoStatus);
        IOStreams.println("[*] Initialize Directories       : " + initDirs);
        IOStreams.println("[*] Initialize Database System   : " + initDB);
        IOStreams.println("[*] Initialize Program Policies  : " + initPolicies);
        IOStreams.println("[*] Create Administrator Account : " + initAdminAccount);
        IOStreams.println("[ ----------------------------- ]");
    }
}
