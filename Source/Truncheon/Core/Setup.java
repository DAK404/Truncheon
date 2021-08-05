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
import java.io.OutputStream;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;

import java.util.Properties;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;

public class Setup
{
    private boolean _legal = false;
    private boolean _dirs = false;
    private boolean _dbInit = false;
    private boolean _policyInit = false;
    private boolean _adminAccCreate = false;

    Console console = System.console();
    Properties props = null;

    public final void setupLogic()throws Exception
    {
        System.gc();
        new Truncheon.API.BuildInfo().versionViewer();
        console.readLine("Welcome to Truncheon! This program needs an initial configuration before using it. To begin the setup, press the Enter/Return key. Else press the Ctrl + C keys.\nWe recommend the Computer Administrator to setup Truncheon. Please contact your Administrator if you are a user and you're seeing this message.");
        showPrerequisites();
        if(checkMosaic()==false)
        {
            createDirs();
            initializeDatabase();
            createAdminUser();
        }
        initializePolicy();

        displayStatus();
        System.out.println("\n===============\n\nSetup Complete! Would you like to check for a system update?\n[ ATTENTION ] : You will require an internet connection to check and download the updates.\n");
        if(console.readLine("> ").equalsIgnoreCase("y"))
            new Truncheon.API.Wyvern.UpdateFrontEnd().updateLogic();
        System.exit(1);
    }

    private final void showPrerequisites()throws Exception
    {
        new Truncheon.API.Wraith.ReadFile().showHelp("License.eula");
        System.out.println("\nDo you accept the Product License? [Y/N]");
        if(console.readLine().toLowerCase().equalsIgnoreCase("y"))
            //read changelog file
            new Truncheon.API.Wraith.ReadFile().showHelp("changelog.txt");
        else
            System.exit(2);

        _legal = true;
        return;
    }

    private final boolean checkMosaic()throws Exception
    {
        if(new File("./System/Private/Fractal.db").exists() == true)
        {
            System.out.println("[ ATTENTION ] : Mosaic Files have been found. Do you want to copy over existing data?");
            if(console.readLine("[ Y | N ]\n> ").equalsIgnoreCase("Y"))
                return mosaicSyncLogic();
        }
        return false;
    }

    private final boolean mosaicSyncLogic()
    {
        try 
        {
            new File("./System/Private/Truncheon").mkdir();

            syncHelper(new File("./System/Private/Fractal.db"), new File("./System/Private/Truncheon/mud.db"));
            syncHelper(new File("./Users/Mosaic"), new File("./Users/Truncheon"));
                
            System.gc();
            return true;
        }
        catch (Exception E)
        {
            new Truncheon.API.ErrorHandler().handleException(E);
        }
        return false;
    }

    private final void syncHelper(File src, File dest ) throws Exception 
    {
        try
        {
            if( src.isDirectory() )
            {
                dest.mkdirs();
                for( File sourceChild : src.listFiles() ) 
                {
                    File destChild = new File( dest, sourceChild.getName() );
                    syncHelper( sourceChild, destChild );
                }
            } 
            else
            {
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

    private final void createDirs()
    {
        try
        {
            displayStatus();
            new Truncheon.API.BuildInfo().versionViewer();
            System.out.println("Checking for previous installation and existing directories...");
            String[] directoryList = {"./System", "./Users/Truncheon", "./System/Public", "./System/Private", "./System/Public/Truncheon", "./System/Public/Truncheon/Logs", "./System/Private/Truncheon"};

            for(int i = 0; i < directoryList.length; i++)
            {
                File makeDir =  new File(directoryList[i]);

                if(makeDir.exists() == false)
                    makeDir.mkdirs();
                else
                    System.out.println("Folder " + directoryList[i]+ " Exists.");
            }

            _dirs = true;
            return;
        }
        catch(Exception E)
        {
            E.printStackTrace();
        }
    }

    /*private final void createFiles()
    {
        //create the M1, M2 and M3 files.
    }*/

    private final void initializeDatabase()
    {
        try
        {
            displayStatus();
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

            _dbInit = true;
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
            displayStatus();
            new Truncheon.API.Dragon.AddUser().Setup();

            _adminAccCreate = true;
        }
        catch(Exception E)
        {
            new Truncheon.API.ErrorHandler().handleException(E);
        }
    }

    private void initializePolicy()throws Exception
    {
        try
        {
            props = new Properties();
            String [] resetValues = { "update", "download", "script", "filemanager", "read", "write", "usermgmt"};
            for(int i = 0; i < resetValues.length; ++i)
                initPolicyHelper(resetValues[i], "on");
        }
        catch (Exception E)
        {
            E.printStackTrace();
        }
    }

    private void initPolicyHelper(String policyName, String policyValue)throws Exception
    {
        try
        {
            props.setProperty(policyName, policyValue);
            FileOutputStream output = new FileOutputStream("./System/Private/Truncheon/Policy.burn");
            props.storeToXML(output, "TruncheonSettings");
            output.close();
            System.gc();
            _policyInit = true;
            return;
        }
        catch(Exception E)
        {
            E.printStackTrace();
        }
    }

    private void displayStatus()throws Exception
    {
        try
        {
            new Truncheon.API.BuildInfo().versionViewer();
            System.out.println("SETUP CHECKLIST");
            System.out.println("===============\n");
            if(_legal == true)
                System.out.println("1. Legal and Important Information    : COMPLETE");
            if(_dirs == true)
                System.out.println("2. Initialize Truncheon Dependencies  : COMPLETE");
            if(_dbInit == true)
                System.out.println("3. Initialize Database Files          : COMPLETE");
            if(_policyInit == true)
                System.out.println("4. Initialize Policies and BURN Files : COMPLETE");
            if(_adminAccCreate == true)
                System.out.println("5. Administrator account creation     : COMPLETE");
        }
        catch (Exception E)
        {
            E.printStackTrace();
        }
    }
}