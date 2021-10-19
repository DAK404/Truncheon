/*
*    ███    ██ ██  ██████  ███    ██        ████████ ██████  ██    ██ ███    ██  ██████ ██   ██ ███████  ██████  ███    ██
*    ████   ██ ██ ██    ██ ████   ██ ██        ██    ██   ██ ██    ██ ████   ██ ██      ██   ██ ██      ██    ██ ████   ██
*    ██ ██  ██ ██ ██    ██ ██ ██  ██           ██    ██████  ██    ██ ██ ██  ██ ██      ███████ █████   ██    ██ ██ ██  ██
*    ██  ██ ██ ██ ██    ██ ██  ██ ██ ██        ██    ██   ██ ██    ██ ██  ██ ██ ██      ██   ██ ██      ██    ██ ██  ██ ██
*    ██   ████ ██  ██████  ██   ████           ██    ██   ██  ██████  ██   ████  ██████ ██   ██ ███████  ██████  ██   ████
*/

/*
* ---------------!DISCLAIMER!--------------- *
*                                            *
*         THIS CODE IS RELEASE READY         *
*                                            *
*  THIS CODE HAS BEEN CHECKED, REVIEWED AND  *
*   TESTED. THIS CODE HAS NO KNOWN ISSUES.   *
*    PLEASE REPORT OR OPEN A NEW ISSUE ON    *
*     GITHUB IF YOU FIND ANY PROBLEMS OR     *
*              ERRORS IN THE CODE.           *
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
import java.io.Console;
import java.io.File;

//Import the required Java SQL classes
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;

/**
* Program to set Truncheon up for the initial use.
*
* Sets up the required directories, files and the database.
* @version 0.8.67
* @since 0.7.5
* @author DAK404
*/
public class Setup
{
    //The boolean data required to indicate if the setup for a section has been completed or not.
    private String _legal = "INCOMPLETE";
    private String _dirs = "INCOMPLETE";
    private String _dbInit = "INCOMPLETE";
    private String _policyInit = "INCOMPLETE";
    private String _adminAccCreate = "INCOMPLETE";
    private boolean _mosaicSetup = false;

    //Initialize the Console class for IO via the STDIN
    private Console console = System.console();

    /**
    * Logic which will help in setting up the program before first use.
    *
    * Directories and files are created if they are not found
    * or can be imported from a pre existsing installation from Mosaic.
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    public final void setupLogic()throws Exception
    {
        System.gc();

        //Show the steps that the setup program will take.
        showSetupDetails();

        //Show the legal documents and the readme files for the program use.
        showPrerequisites();

        createDirs();

        /*
        * Check and import the pre-existing databases and contents
        * from Mosaic if it exists
        */
        if(! checkMosaic())
        {
            /*
            * If it does not exist, The program will then
            * create a new database and an Administrator account.
            */
            initializeDatabase();
            createAdminUser();
        }

        //Create a policy file, will not import the Mosaic policy due to the differences.
        initializePolicy();

        //Display the overall status of the setup progress, confirming that the setup process has, indeed, been completed.
        displayStatus();

        //Request the user for a Truncheon update. This will check and apply an update if required.
        System.out.println("\nSetup Complete! Would you like to check for a system update?\n[ ATTENTION ] : You will require an internet connection to check and download the updates.\n\nAvailable Options : [ Y | N ]");
        if(console.readLine("> ").equalsIgnoreCase("y"))
        new Truncheon.API.Wyvern.UpdateFrontEnd().updateLogic();

        //Restart the program after the setup is complete to apply changes.
        System.exit(1);
    }

    /**
    * Display the details of the setup program.
    *
    * The user will then know the steps of the setup program.
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final void showSetupDetails()throws Exception
    {
        String setupStrings = """
        Nion: Truncheon v""" + new Truncheon.API.BuildInfo()._version + """
        \nWelcome to Truncheon!
        \nThe shell needs to be initialized before it can be used.
        \nIf you are not the system administrator, please press the CTRL + C keys to quit.
        \nThe setup program is meant for administrators to initialize the program environment.\n\n""";

        displaySetupInformation(setupStrings);
        
        setupStrings = """
        The following section shall describe the setup procedure\nalong with the changes made to the system.
        --------------------------------------
                  - SETUP PROCEDURE -         
        --------------------------------------
         A. LEGAL AND IMPORTANT INFORMATION
        \t1. EULA [END USER LICENSE AGREEMENT]
        \t2. Readme
        \t3. What's New!
         B. CHECK FOR MOSAIC FILES 
        \t1. Import the database
        \t2. Copy Data from\n\t   Mosaic to Truncheon
        \t3. END SETUP
         C. Create Truncheon Dependencies
        \t1. Create Truncheon Directories
        \t2. Create Multi User Database
        \t3. Create Administrator Account
         D. INITIALIZE THE SYSTEM NAME
         E. INITIALIZE PROGRAM POLICIES
         F. CHECK FOR UPDATES
         G. END SETUP
        \n--------------------------------------
        If you wish not to continue with the setup, please press the CTRL + C keys to exit.
        To Continue with the setup, press the ENTER key.
        """;

        displaySetupInformation(setupStrings);
    }

    /**
     * 
     */
    private final void displaySetupInformation(String info)throws Exception
    {
        new Truncheon.API.BuildInfo().versionViewer();
        System.out.println(info);
        console.readLine("Available Options [ Press ENTER key | Press CTRL + C keys ]\n> ");
    }

    /**
    * Show the legal documents, readme file, the changelog file and the credits file
    *
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final void showPrerequisites()throws Exception
    {
        //Read the License file
        new Truncheon.API.Wraith.ReadFile().showHelp("License.eula");

        //Display the status of the setup
        displayStatus();

        //The user must accept the terms of the license to proceed.
        System.out.println("\nPress 'Y' if you accept the license, or press 'N' if you want to quit.\nDo you accept the Product License? [Y/N]");
        if(console.readLine("> ").equalsIgnoreCase("y"))
        {
            new Truncheon.API.Wraith.ReadFile().showHelp("Readme.txt");
            new Truncheon.API.Wraith.ReadFile().showHelp("Credits.txt");
        }
        //Exit if the user does not accept the terms of the license.
        else
        System.exit(2);

        //Set the status of the prerequisites section as true, ie complete.
        _legal = "COMPLETE";
    }

    /**
    * Checks if Mosaic is installed in the same directory already.
    *
    * @return boolean : returns true if found, else false.
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final boolean checkMosaic()throws Exception
    {
        //Display the setup program status.
        displayStatus();

        //Check if the Mosaic database already exists.
        if(new File("./System/Private/Fractal.db").exists())
        {
            //If true, a choice is provided to the user to either import the files or to have a fresh start.
            System.out.println("[ ATTENTION ] : Mosaic Files have been found. Do you want to copy over existing data?");

            //Start syncing the files with Mosaic files if the user permits to do so.
            if(console.readLine("[ Y | N ]\n> ").equalsIgnoreCase("Y"))
            _mosaicSetup = mosaicSyncLogic();
        }
        //else return false to signify that the user will need a fresh start
        return _mosaicSetup;
    }

    /**
    * Program to import the pre-existing Mosaic Database and User directories to Truncheon.
    *
    * It skips the entire setup process and directly jumps to creating the policy files
    * @return boolean : Returns the status of importing Mosaic information if found.
    */
    private final boolean mosaicSyncLogic()
    {
        try
        {
            Truncheon.API.Grinch.FileManager sync = new Truncheon.API.Grinch.FileManager();
            
            //Copy over the database and rename it from Fractal.db to mud.db
            sync.copyMoveHelper(new File("./System/Private/Fractal.db"), new File("./System/Private/Truncheon/mud.db"));

            //Copy over the Mosaic user directories to the Truncheon user directories
            sync.copyMoveHelper(new File("./Users/Mosaic"), new File("./Users/Truncheon"));

            System.gc();

            //return true to checkMosaic() that the synchronization was successful.
            return true;
        }
        catch (Exception E)
        {
            new Truncheon.API.ErrorHandler().handleException(E);
        }
        //Else, return false to checkMosaic() that the synchronization failed
        return false;
    }

    /**
    * Helps in creating the directories essential for Truncheon to work.
    */
    private final void createDirs()
    {
        try
        {
            //Display the setup program status
            displayStatus();

            //Clear the screen and view the Build Information
            new Truncheon.API.BuildInfo().versionViewer();

            //Initialize the Truncheon directories' names
            String[] directoryList = {"./Users/Truncheon", "./System/Public/Truncheon/Logs", "./System/Private/Truncheon"};

            //Traverse the array, whilst creating the directories
            for(int i = 0; i < directoryList.length; i++)
            {
                //Initialize a file object to create the directory
                File makeDir =  new File(directoryList[i]);

                //Create the directory if it does not already exist
                if(! makeDir.exists())
                makeDir.mkdirs();
                //else notify the user that the directory already exists, and wont attempt to create the directory.
                else
                System.out.println("Folder " + directoryList[i]+ " Exists.");
            }

            System.gc();

            //Set the directory creation status, signifying that the directories have been created
            _dirs = "COMPLETE";
        }
        catch(Exception E)
        {
            E.printStackTrace();
        }
    }

    /**
    * Helps in Initializing the Multi User Database to store the credentials
    */
    private final void initializeDatabase()
    {
        try
        {
            //Display the setup program status
            displayStatus();

            //check if the Master User Database exists
            if(new File("./System/Private/Truncheon/mud.db").exists())
            {
                //Do not continue if the database file exists
                System.out.println("[ ERROR ] : Master User Database already exists! Aborting...");
                _dbInit = "ERROR";
                return;
            }

            //Set the URL to save the database to
            String url = "jdbc:sqlite:./System/Private/Truncheon/mud.db";

            //create a connection to the database
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(url);

            //Notify the user that the database file has been created successfully
            System.out.println("Master User Database File created successfully!");

            //Create the table using the SQL statements and store it in a String.
            String sql = "CREATE TABLE IF NOT EXISTS FCAD (\n" +
            "    UserID integer PRIMARY KEY,\n" +
            "    Name text NOT NULL,\n" +
            "    Username text UNIQUE,\n" +
            "    Password text NOT NULL,\n" +
            "    SecurityKey text NOT NULL,\n" +
            "    PIN text NOT NULL,\n" +
            "    Administrator text NOT NULL);";

            //Pass the sql statement String to the PreparedStatement
            Statement stmt = conn.createStatement();

            //Execute the statement, passing the data to the SQL system to execute it
            stmt.execute(sql);

            //Close the connections after using the database
            stmt.close();
            conn.close();
            System.gc();

            //Notify the user that the database has been initialized successfully, which can accommodate user data now.
            System.out.println("Master User Database File has been initialized successfully!");
            
            console.readLine("Press Enter to Continue.");

            //Set the database creation and table initialization as successful.
            _dbInit = "COMPLETE";
        }
        catch (Exception E)
        {
            new Truncheon.API.ErrorHandler().handleException(E);
        }
    }

    /**
    * Helps in creating the Administrator account.
    *
    * This is the default account which cannot be deleted or demoted.
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final void createAdminUser()throws Exception
    {
        try
        {
            //Display the setup program status
            displayStatus();

            System.out.println("[ ATTENTION ] : AN ADMINISTRATOR ACCOUNT NEEDS TO BE SETUP IN ORDER TO HAVE A DEFAULT ACCOUNT IN THE SYSTEM DATABASE.");
            System.out.println("The following steps will require information which will be used to create an Administrator account.");
            console.readLine("Press ENTER to continue.");

            new Truncheon.API.Dragon.AddUser().setupAdminUser();

            //set the administrator account creation status as true
            _adminAccCreate = "COMPLETE";
        }
        catch(Exception E)
        {
            //Handle any exceptions thrown during runtime
            new Truncheon.API.ErrorHandler().handleException(E);
        }
    }

    /**
    * Initializes the policies, by setting the Policy Names.
    *
    * Used to set the System name too.
    * Also sets the policy with the default value as 'on'
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private void initializePolicy()throws Exception
    {
        try
        {
            //Display the setup program status
            displayStatus();

            //Initialize a string to hold the system name.
            String sysName;

            //Store the string by asking the user to enter the system name
            sysName = console.readLine("\nEnter the name of this system\n\nThis will be used to identify your system in the prompt.\nThe default name is SYSTEM.\n\n> ");

            //In case of invalid inputs, the system shall revert back to the default system name 'SYSTEM'
            if(sysName == null || sysName.equals("") || sysName.startsWith(" "))
            sysName = "SYSTEM";

            //Remove all the spaces in the string
            if(sysName.contains(" "))
            sysName = sysName.replaceAll(" ", "");

            Truncheon.API.Minotaur.PolicyEditor pInit = new Truncheon.API.Minotaur.PolicyEditor();
            pInit.resetPolicyFile();
            pInit.savePolicy("sysname", sysName);

            _policyInit = "COMPLETE";
        }
        catch (Exception E)
        {
            E.printStackTrace();
        }
    }

    /**
    * Display the status of the Setup.
    *
    * Generally signifies which section of the program has finished setting up.
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private void displayStatus()throws Exception
    {
        //Clear the screen and display the Truncheon Build Information
        new Truncheon.API.BuildInfo().versionViewer();

        //Display the setup status and information.
        System.out.println("SETUP CHECKLIST");
        System.out.println("===============\n");
        System.out.println("1. Legal and Important Information    : " + _legal);
        System.out.println("2. Initialize Truncheon Dependencies  : " + _dirs);
        System.out.println("3. Initialize Database Files          : " + _dbInit);
        System.out.println("4. Administrator account creation     : " + _adminAccCreate);
        System.out.println("5. Initialize Policies and BURN Files : " + _policyInit);
        System.out.println("\n===============");

        //Notify the user that Mosaic files have already been found
        if(_mosaicSetup)
        System.out.println("!Mosaic Installation Detected!");
    }
}