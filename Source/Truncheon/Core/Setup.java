package Truncheon.Core;

//import the required Java IO classes
import java.io.Console;
import java.io.File;

//Import the required Java SQL classes
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import Truncheon.API.IOStreams;

public class Setup
{
    public void setupLogic()throws Exception
    {

    }

    private void displayPrerequisiteInformation()
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

    }
}
