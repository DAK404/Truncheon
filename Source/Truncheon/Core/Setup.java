package Truncheon.Core;

import java.io.Console;
import java.io.File;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;

public class Setup
{
    Console console = System.console();
    public void setupLogic()
    {
        new Truncheon.API.BuildInfo().versionViewer();
        console.readLine("Welcome to Truncheon! This program needs an initial configuration before using it. To begin the setup, press the Enter/Return key. Else press the Ctrl + C keys.\nWe recommend the Computer Administrator to setup Truncheon. Please contact your Administrator if you are a user and you're seeing this message.");
        createDirs();
        initializeDatabase();
    }

    private void showPrerequisites()
    {

    }

    private void createDirs()
    {
        System.out.println("Checking for previous installation and existing directories...");
        String[] directoryList = {"./System", "./User", "./System/Public", "./System/Private", "./System/Public/Truncheon", "./System/Public/Truncheon/Logs", "./System/Private/Truncheon"};
        
        for(int i = 0; i < directoryList.length; i++)
        {
            File makeDir =  new File(directoryList[i]);

            if(makeDir.exists() == false)
                makeDir.mkdirs();
            else
                continue;            
        }
        return;
    }

    private void createFiles()
    {
        //create the M1, M2 and M3 files.
    }

    private void initializeDatabase()
    {
        //TODO : IMPORT THE FSAD TABLE AND IMPORT THE CURRENT DB IF EXISTS

        try 
        {           
            //Mud.db = Master User Database file
            String url = "jdbc:sqlite:./System/Private/Truncheon/mud.db";

            //create a connection to the database
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();

            System.out.println("Master User Database File created successfully!");

            String sql = "CREATE TABLE IF NOT EXISTS FCAD (\n" +
                "    UserID integer PRIMARY KEY,\n" +
				"    Name text NOT NULL,\n" +
                "    Username text UNIQUE,\n" +
                "    Password text NOT NULL,\n" +
                "    SecurityKey text NOT NULL,\n" +
				"    PIN text NOT NULL,\n" +
				"    Administrator text NOT NULL);";
				
            stmt.execute(sql);
            System.out.println("Master User Database File has been initialized successfully!");
        } 
        catch (Exception E) 
        {
            new Truncheon.API.ErrorHandler().handleException(E);
        } 
        return;
    }

    private void createAdminUser()
    {



    }

    private void cleanup()
    {
        //remove unwanted files out of the directory
    }
}