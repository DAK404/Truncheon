package Truncheon.Core;

//import the required Java IO classes
import java.io.Console;
import java.io.File;

//Import the required Java SQL classes
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import Truncheon.API.BuildInfo;
import Truncheon.API.IOStreams;

public class Setup
{
    Console console = System.console();

    private String prereqInfoStatus, initDB, initDirs, initPolicies = "PENDING";
    
    public void setupLogic()throws Exception
    {
        displayPrerequisiteInformation();
        initializeDirectories();
        initializeDatabase();
        initializeDefaultPolicies();
    }
    
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

        prereqInfoStatus = "COMPLETE";
    }
    
    private void initializeDirectories()
    {
        displaySetupProgress();
        String [] directoryNames = {"./System/Truncheon/Public/Logs", "./System/Truncheon/Private/Backups", "./Users/Truncheon"};
        for (String dirs: directoryNames)
            new File(dirs).mkdirs();
        initDirs = "COMPLETE";
    }
    
    private void initializeDatabase()
    {
        displaySetupProgress();
        boolean initializeDatabaseStatus = false;
        try
        {
            String databasePath = "jdbc:sqlite:./System/Truncheon/Private/MUD.dbx";
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
            e.printStackTrace();
        }
        initDB = (initializeDatabaseStatus?"COMPLETE":"FAILED");
    }
    
    private void initializeAdministratorAccount()
    {
        
    }
    
    private void initializeDefaultPolicies()
    {
        displaySetupProgress();
        new Truncheon.API.Minotaur.PolicyEdit();
        initPolicies = "Complete";
    }
    
    private void displaySetupProgress()
    {
        BuildInfo.viewBuildInfo();
        IOStreams.printInfo("[ -- Program Setup Checklist -- ]");
        IOStreams.println("[*] Show Program Prerequisites  : " + prereqInfoStatus);
        IOStreams.println("[*] Initialize Directories      : " + initDirs);
        IOStreams.println("[*] Initialize Database System  : " + initDB);
        IOStreams.println("[*] Initialize Program Policies : " + initPolicies);
        IOStreams.printInfo("[ ----------------------------- ]");
    }
}
