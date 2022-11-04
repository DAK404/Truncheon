package Truncheon.Core;

//import the required Java IO classes
import java.io.Console;
import java.io.File;

//Import the required Java SQL classes
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

//Import the required Truncheon classes
import Truncheon.API.BuildInfo;
import Truncheon.API.IOStreams;

/**
 * Program to make Truncheon ready for normal use
 * 
 * @author: DAK404 (https://github.com/DAK404)
 * @version: 7.3.6
 * @since: 2018
 */
public class Setup
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
        console.readLine("Press ENTER to continue, or press CTRL + C to quit.");

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
        IOStreams.printInfo("[ -- Program Setup Checklist -- ]");
        IOStreams.println("[*] Show Program Prerequisites   : " + prereqInfoStatus);
        IOStreams.println("[*] Initialize Directories       : " + initDirs);
        IOStreams.println("[*] Initialize Database System   : " + initDB);
        IOStreams.println("[*] Initialize Program Policies  : " + initPolicies);
        IOStreams.println("[*] Create Administrator Account : " + initAdminAccount);
        IOStreams.printInfo("[ ----------------------------- ]");
    }
}
