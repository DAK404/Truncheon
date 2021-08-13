package Truncheon.API.Minotaur;

import java.io.Console;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;

import java.util.Properties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PolicyEditor 
{
    private final String fileName = "./System/Private/Truncheon/Policy.burn";

    Console console = System.console();
    Properties props = null;

    public PolicyEditor()
    {

    }   
    
    public final void policyEditorLogic()throws Exception
    {
        if(authenticationLogic() == false)
        {
            System.out.println("Authentication failed. Returning to main menu.");
                Thread.sleep(5000);
                return;
        }
        policyEditor();
        return;
    }

    private final boolean authenticationLogic()
    {
        try
        {
            new Truncheon.API.BuildInfo().versionViewer();
            System.out.println("[ ATTENTION ] : This module requires the user to authenticate to continue. Please enter the user credentials.");

            String username = new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(console.readLine("Username: "));
            String password=new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(String.valueOf(console.readPassword("Password: ")));
            String securityKey=new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(String.valueOf(console.readPassword("Security Key: ")));

            if(new Truncheon.API.Dragon.LoginAPI(username, password, securityKey).status() == true)
            {
                return checkAdminStatus(username);
            }
        }
        catch(Exception E)
        {
            new Truncheon.API.ErrorHandler().handleException(E);
        }
        return false;
    }

    private final boolean checkAdminStatus(String u)throws Exception
    {
        String url = "jdbc:sqlite:./System/Private/Truncheon/mud.db";
        Connection conn = DriverManager.getConnection(url);

        PreparedStatement pstmt = conn.prepareStatement("SELECT Administrator FROM FCAD WHERE Username = ?");
        pstmt.setString(1, u);
        ResultSet rs = pstmt.executeQuery();
        String temp = rs.getString("Administrator");

        rs.close();
        pstmt.close();
        conn.close();

        System.gc();

        if(temp.equalsIgnoreCase("Yes"))
            return true;
        return false;
    }

    private final void policyEditor()throws Exception
    {
        

        while(true)
        {
            props = new Properties();
            if(new File(fileName).exists() == false)
            resetPolicyFile();
            displaySettings();
            switch(console.readLine("[ MODIFY | RESET | HELP | EXIT ]\n\nPolicyEditor)> ").toLowerCase())
            {
                case "modify":
                    editPolicy();
                    break;
                
                case "reset":
                    resetPolicyFile();
                    break;

                case "exit":
                    return;

                case "":
                    break;

                default:
                    System.out.println("Invalid command. Please try again.");
                    break;
            }
        }
    }

    private final void displaySettings()throws Exception
    {
        new Truncheon.API.BuildInfo().versionViewer();
        System.out.println("         Minotaur Policy Editor 1.3         ");
        System.out.println("--------------------------------------------");
        System.out.println("      - Current Policy Configuration -      ");
        System.out.println("--------------------------------------------");
        System.out.println("\nPolicy File  : " + fileName);
        System.out.println("Policy Format: XML\n");
        FileInputStream configStream = new FileInputStream(fileName);
        props.loadFromXML(configStream);
        configStream.close();
        props.list(System.out);
        System.out.println("\n--------------------------------------------\n");
        System.gc();
        return;
    }

    private final void editPolicy()throws Exception
    {
        displaySettings();
        do
        {
            String pName = console.readLine("Enter the name of the policy : ").toLowerCase();
            String pValue = console.readLine("Enter the value for the policy : ");
            System.out.println("Saving Policy...");
            savePolicy(pName, pValue);
            System.gc();
        }
        while(console.readLine("Do you want to modify another policy? [ Y | N ] > ").equalsIgnoreCase("y"));
    }

    private final void savePolicy(String policyName, String policyValue)throws Exception
    {
        props.setProperty(policyName, policyValue);
        FileOutputStream output = new FileOutputStream(fileName);
        props.storeToXML(output, "TruncheonSettings");
        output.close();
        System.out.println("Policy " + policyName + " has been saved successfully.");
        System.gc();
        return;
    }

    private final void resetPolicyFile()throws Exception
    {
        System.out.println(new File("./System/Private/Truncheon/Policy.BURN").delete());
        Thread.sleep(3000);
        savePolicy("sysname", "SYSTEM");
        String [] resetValues = { "update", "download", "script", "filemanager", "read", "write", "usermgmt"};
        for(int i = 0; i < resetValues.length; ++i)
            savePolicy(resetValues[i], "on");
        return;
    }
}
