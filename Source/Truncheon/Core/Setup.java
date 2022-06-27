package Truncheon.Core;

//import the required Java IO classes
import java.io.Console;
import java.io.File;

//Import the required Java SQL classes
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.print.attribute.standard.MediaSize.ISO;

import Truncheon.API.IOStreams;

public class Setup
{
    private String [][] displayProgressStrings = {
        {
            "Accept License and Display Readme: ", "PENDING"
        },
        {
            "Initialize Libraries: ", "PENDING"
        },
        {
            "Initialize Database System: ", "PENDING"
        },
        {
            "Initialize Default Policies: ", "PENDING"
        },
        {
            "Create Administrator account: ", "PENDING"
        },
        {
            "Check for System Updates: ", "PENDING"
        },
    };
    
    public void setupLogic()throws Exception
    {
        displaySetupProgress();
    }
    
    private void displayPrerequisiteInformation()
    {
        
    }
    
    private void initializeDirectories()
    {
        
    }
    
    private void initializeDatabase()
    {
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
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        IOStreams.printAttention("Master User Database Initialization: " + (initializeDatabaseStatus?"Complete":"Failed") + "!");
    }
    
    private void initializeAdministratorAccount()
    {
        
    }
    
    private void initializeDefaultPolicies()
    {
        
    }
    
    private void displaySetupProgress()
    {
        IOStreams.printInfo("[ -- Program Setup Checklist -- ]");
        for(int i = 0; i < displayProgressStrings.length; i++)
        for(int j = 1; j < 2; j += 2)
        IOStreams.println(" * " + displayProgressStrings[i][j-1] + displayProgressStrings[i][j]);
        IOStreams.printInfo("[ ----------------------------- ]");
    }
}
