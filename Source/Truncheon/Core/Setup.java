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

import java.io.Console;
import java.io.File;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;

public class Setup
{
    Console console = System.console();

    public final void setupLogic()throws Exception
    {
        System.gc();
        new Truncheon.API.BuildInfo().versionViewer();
        console.readLine("Welcome to Truncheon! This program needs an initial configuration before using it. To begin the setup, press the Enter/Return key. Else press the Ctrl + C keys.\nWe recommend the Computer Administrator to setup Truncheon. Please contact your Administrator if you are a user and you're seeing this message.");
        showPrerequisites();
        createDirs();
        initializeDatabase();
        createAdminUser();
        System.exit(1);
    }

    private final void showPrerequisites()throws Exception
    {
        //read the license File
        System.out.println("Do you accept the Product License? [Y/N]");
        if(console.readLine().toLowerCase().equalsIgnoreCase("y"))
            //read the Readme File
            //Read the changelog file
            System.out.println("Accepted agreement placeholder");
        else
            System.exit(2);
        return;
    }

    private final void createDirs()
    {
        new Truncheon.API.BuildInfo().versionViewer();
        System.out.println("Checking for previous installation and existing directories...");
        String[] directoryList = {"./System", "./Users", "./System/Public", "./System/Private", "./System/Public/Truncheon", "./System/Public/Truncheon/Logs", "./System/Private/Truncheon"};

        for(int i = 0; i < directoryList.length; i++)
        {
            File makeDir =  new File(directoryList[i]);

            if(makeDir.exists() == false)
                makeDir.mkdirs();
            else
                System.out.println("Folder " + directoryList[i]+ " Exists.");
        }
        return;
    }

    /*private final void createFiles()
    {
        //create the M1, M2 and M3 files.
    }*/

    private final void initializeDatabase()
    {
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
            conn.close();
            System.out.println("Master User Database File has been initialized successfully!");
            System.gc();
        }
        catch (Exception E)
        {
            new Truncheon.API.ErrorHandler().handleException(E);
        }
        return;
    }

    private final void createAdminUser()throws Exception
    {
        try
        {
            new Truncheon.API.Dragon.AddUser().Setup();
        }
        catch(Exception E)
        {
            new Truncheon.API.ErrorHandler().handleException(E);
        }
    }
}