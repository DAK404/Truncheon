/*

----- PROGRAM DOCUMENTATION -----

THIS PROGRAM IS UNDER DEVELOPMENT
AND SHOULD NOT BE CONSIDERED
RELEASE READY. FEATURES MAY BE
BROKEN OR INCOMPLETE. COMPILE AND
TEST AT YOUR OWN RISK.

---------------------------------

     --- Program Details ---

     Author  : DAK404
     Date    : 17-June-2021
     Version : 0.1.14

     -----------------------

*/


package Truncheon.Core;

//Import the required Java IO classes
import java.io.Console;
import java.io.File;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;

//Import the required Java Util classes
import java.util.Properties;

//Import the required Java SQL classes
import java.sql.Connection;
import java.sql.PreparedStatement;
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
    private boolean _legal = false;
    private boolean _dirs = false;
    private boolean _dbInit = false;
    private boolean _policyInit = false;
    private boolean _adminAccCreate = false;
    private boolean _mosaicSetup = false;

    //Initialize the Console class for IO via the STDIN
    Console console = System.console();

    //Initialize the Properties class to create the policy file and set the default policy values
    Properties props = null;

    /**
     * Logic which will help in setting up the program before first use.
     * 
     * Directories and files are created if they are not found 
     * or can be imported from a pre existsing installation from Mosaic.
     * @throws Exception : Handle general exceptions during thrown during runtime.
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
        System.out.println("\n===============\n\nSetup Complete! Would you like to check for a system update?\n[ ATTENTION ] : You will require an internet connection to check and download the updates.\n");
        if(console.readLine("> ").equalsIgnoreCase("y"))
            new Truncheon.API.Wyvern.UpdateFrontEnd().updateLogic();

        //Restart the program after the setup is complete to apply changes.
        System.exit(1);
    }

    /**
     * Display the details of the setup program.
     * 
     * The user will then know the steps of the setup program.
     * @throws Exception : Handle general exceptions during thrown during runtime.
     */
    private final void showSetupDetails()throws Exception
    {
        //clear the screen and display the build information.
        new Truncheon.API.BuildInfo().versionViewer();

        //Print the setup procedure and details.
        System.out.println("Nion: Truncheon");
        System.out.println("\nWelcome to Truncheon!");
        System.out.println("The Truncheon Shell needs to be initialized before it can be used.\nThe program cannot be used until the setup is complete.");
        System.out.println("[ ATTENTION ] : DO NOT TURN OFF THE DEVICE, TERMINATE THE APPLICATION, DISCONNECT ANY DEVICES, DRIVES OR NETWORK ADAPTERS!\n");
        System.out.println("--------------------------------------");
        System.out.println("          - SETUP PROCEDURE -         ");
        System.out.println("--------------------------------------");
        System.out.println("A. Legal and Important Information");
        System.out.println("\t1. EULA [ END USER LICENSE AGREEMENT ]");
        System.out.println("\t2. Readme");
        System.out.println("\t3. What's New!");
        System.out.println("\t4. Contributors");
        System.out.println("B. Check for Mosaic's Files");
        System.out.println("\t1. Copy Database Files");
        System.out.println("\t2. Copy User Files");
        System.out.println("C. Create Truncheon Dependencies");
        System.out.println("\t1. Create Truncheon Directories");
        System.out.println("\t2. Create Multi User Database");
        System.out.println("\t3. Initialize Administrator Account");
        System.out.println("D. Initialize the System Name");
        System.out.println("E. Initialize the Policy Files");
        System.out.println("--------------------------------------");
        System.out.println("\n\nPress Enter to continue, or press CTRL + C keys to exit the program.");

        //Provide a choice to the user to begin the setup or exit.
        console.readLine("Available Options [ Press ENTER key | Press CTRL + C keys ]");
    }

    /**
     * Show the legal documents, readme file, the changelog file and the credits file
     * 
     * @throws Exception : Handle general exceptions during thrown during runtime.
     */
    private final void showPrerequisites()throws Exception
    {
        //Read the License file
        new Truncheon.API.Wraith.ReadFile().showHelp("License.eula");
        //The user must accept the terms of the license to proceed.
        System.out.println("\nDo you accept the Product License? [Y/N]");
        if(console.readLine().equalsIgnoreCase("y"))
        {
            new Truncheon.API.Wraith.ReadFile().showHelp("Readme.txt");
            new Truncheon.API.Wraith.ReadFile().showHelp("Credits.txt");
        }
        //Exit if the user does not accept the terms of the license.
        else
            System.exit(2);

        //Set the status of the prerequisites section as true, ie complete.
        _legal = true;
    }

    /**
     * Checks if Mosaic is installed in the same directory already.
     * 
     * @return boolean : returns true if found, else false.
     * @throws Exception
     */
    private final boolean checkMosaic()throws Exception
    {
        //Display the setup program status.
        displayStatus();

        //Check if the Mosaic database already exists.
        if(new File("./System/Private/Fractal.db").exists())
        {
            //If true, a choice is provided to the user to either import the files or to havea  fresh start.
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
            //Copy over the database and rename it from Fractal.db to mud.db
            syncHelper(new File("./System/Private/Fractal.db"), new File("./System/Private/Truncheon/mud.db"));

            //Copy over the Mosaic user directories to the Truncheon user directories
            syncHelper(new File("./Users/Mosaic"), new File("./Users/Truncheon"));
                
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
     * The helper program which helps in synchronizing Mosaic files with Truncheon
     * 
     * @param src : The source directory/file to be copied
     * @param dest : The destination directory to the copied to
     * @throws Exception
     */
    private final void syncHelper(File src, File dest ) throws Exception 
    {
        try
        {
            //Checks if the source is a directory
            if( src.isDirectory() )
            {
                //If true, create the directory(ies)
                dest.mkdirs();

                //Copy over the child directories and files to the destination, recursively
                for( File sourceChild : src.listFiles() ) 
                {
                    File destChild = new File( dest, sourceChild.getName() );

                    //Recurse through the entire directory
                    syncHelper( sourceChild, destChild );
                }
            } 
            //If the source is a file
            else
            {
                //Copy the file using a byte stream
                InputStream in = new FileInputStream(src);
                OutputStream out = new FileOutputStream(dest);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0)
                    out.write(buf, 0, len);
                in.close();
                out.close();
            }
            System.gc();
        }
        catch(Exception E)
        {
            E.printStackTrace();
        }
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
            _dirs = true;
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
            PreparedStatement pstmt = conn.prepareStatement(sql);

            //Execute the statement, passing the data to the SQL system to execute it
            pstmt.execute(sql);

            //Close the connections after using the database
            pstmt.close();
            conn.close();
            System.gc();

            //Notify the user that the database has been initialized successfully, which can accommodate user data now.
            System.out.println("Master User Database File has been initialized successfully!");
            
            //Set the database creation and table initialization as successful.
            _dbInit = true;
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
     * @throws Exception
     */
    private final void createAdminUser()throws Exception
    {
        try
        {
            //Display the setup program status
            displayStatus();
            new Truncheon.API.Dragon.AddUser().setupAdminUser();

            //set the administrator account creation status as true
            _adminAccCreate = true;
        }
        catch(Exception E)
        {
            new Truncheon.API.ErrorHandler().handleException(E);
        }
    }

    /**
     * Initializes the policies, by setting the Policy Names.
     * 
     * Used to set the System name too.
     * Also sets the policy with the default value as 'on'
     * @throws Exception
     */
    private void initializePolicy()throws Exception
    {
        try
        {
            //Initialize a string to hold the system name.
            String sysName;

            //Store the string by asking the user to enter the system name
            sysName = console.readLine("Enter the name of this system\n\nThis will be used to identify your system in the prompt. The default name is SYSTEM.\n\n> ");

            //In case of invalid inputs, the system shall revert back to the default system name 'SYSTEM'
            if(sysName == null || sysName.equals("") || sysName.startsWith(" "))
                sysName = "SYSTEM";
            
            //Remove all the spaces in the string
            if(sysName.contains(" "))
                sysName = sysName.replaceAll(" ", "");

            //Creates a new Properties file.
            props = new Properties();

            //Sets the system name under the variable sysName
            initPolicyHelper("sysname", sysName);

            //Saves the essential variables to the BURN file, with the default value as 'on'
            String [] resetValues = { "update", "download", "script", "filemanager", "read", "write", "usermgmt"};
            for(int i = 0; i < resetValues.length; ++i)
                initPolicyHelper(resetValues[i], "on");
        }
        catch (Exception E)
        {
            E.printStackTrace();
        }
    }

    /**
     * Helps in saving the policy to the BURN file.
     * 
     * This helps in iterative implementation of saving a policy and its value.
     * @param policyName : The Name of the policy which is stored to the file
     * @param policyValue : The value of the policy stored to the file.
     * @throws Exception
     */
    private void initPolicyHelper(String policyName, String policyValue)throws Exception
    {
        try
        {
            //Stores the property variable and value
            props.setProperty(policyName, policyValue);

            //Open the file output stream to the BURN file
            FileOutputStream output = new FileOutputStream("./System/Private/Truncheon/Policy.burn");

            //Store them all under the description TruncheonSettings
            props.storeToXML(output, "TruncheonSettings");

            //Close the output stream
            output.close();
            System.gc();

            //set the policy initialization as true
            _policyInit = true;
        }
        catch(Exception E)
        {
            E.printStackTrace();
        }
    }

    /**
     * Display the status of the Setup.
     * 
     * Generally signifies which section of the program has finished setting up.
     * @throws Exception
     */
    private void displayStatus()throws Exception
    {
        //Clear the screen and display the Truncheon Build Information
        new Truncheon.API.BuildInfo().versionViewer();

        //Display the setup status and information.
        System.out.println("SETUP CHECKLIST");
        System.out.println("===============\n");
        if(_legal)
            System.out.println("1. Legal and Important Information    : COMPLETE");
        if(_dirs)
            System.out.println("2. Initialize Truncheon Dependencies  : COMPLETE");
        if(_dbInit)
            System.out.println("3. Initialize Database Files          : COMPLETE");
        if(_policyInit)
            System.out.println("4. Initialize Policies and BURN Files : COMPLETE");
        if(_adminAccCreate)
            System.out.println("5. Administrator account creation     : COMPLETE");

        //Notify the user that Mosaic files have already been found
        if(_mosaicSetup)
            System.out.println("!Mosaic Installation Detected!");
    }
}