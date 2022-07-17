package Truncheon.API.Minotaur;

//Import the required Java IO classes
import java.io.Console;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;

//Import the required Java Util classes
import java.util.Properties;

import java.lang.Math;

//Import the Truncheon classes
import Truncheon.API.BuildInfo;
import Truncheon.API.IOStreams;

/**
*
*/
public class PolicyEdit
{
    public final String [] resetValues = {"auth", "update", "download", "script", "filemgmt", "read", "edit", "usermgmt"};

    private final String policyFileName = "./System/Truncheon/Private/Policy.burn";

    private Console console = System.console();
    Properties props = null;

    public PolicyEdit()
    {
        try
        {
            if(!new File(policyFileName).exists())
                resetPolicyFile();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
    *
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    public final void policyEditorLogic()throws Exception
    {
        if(! authenticationLogic())
            IOStreams.printError("Authentication Failure. Exiting...");
        else
            policyEditor();
    }

    /**
    *
    * @return
    */
    private final boolean authenticationLogic()
    {
        boolean challengeStatus = false;
        try
        {
            BuildInfo.viewBuildInfo();
            System.out.println("[ ATTENTION ] : This module requires the user to authenticate to continue. Please enter the user credentials.");

            String username = new Truncheon.API.Minotaur.Cryptography().stringToSHA3_256(console.readLine("Username: "));
            String password = new Truncheon.API.Minotaur.Cryptography().stringToSHA3_256(String.valueOf(console.readPassword("Password: ")));
            String securityKey = new Truncheon.API.Minotaur.Cryptography().stringToSHA3_256(String.valueOf(console.readPassword("Security Key: ")));

            challengeStatus = (new Truncheon.API.Dragon.LoginAuth(username).authenticationLogic(password, securityKey)?(new Truncheon.API.Dragon.LoginAuth(username).checkPrivilegeLogic()?true:false):false);
        }
        catch(Exception E)
        {
            //Handle any exceptions thrown during runtime
        }
        return challengeStatus;
    }

    /**
    *
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final void policyEditor()throws Exception
    {
        while(true)
        {
            props = new Properties();
            if(! new File(policyFileName).exists())
            resetPolicyFile();
            displaySettings();

            // TODO: Accept a String and convert to a String Array and then parse the arguments for every option


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

    /**
    *
    */
    private final void displaySettings()throws Exception
    {
        BuildInfo.viewBuildInfo();
        System.out.println("         Minotaur Policy Editor 1.3         ");
        System.out.println("--------------------------------------------");
        System.out.println("      - Current Policy Configuration -      ");
        System.out.println("--------------------------------------------");
        System.out.println("\nPolicy File  : " + policyFileName);
        System.out.println("Policy Format: XML\n");
        FileInputStream configStream = new FileInputStream(policyFileName);
        props.loadFromXML(configStream);
        configStream.close();
        props.list(System.out);
        System.out.println("\n--------------------------------------------\n");
        System.gc();
    }

    /**
    *
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
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

    /**
    * Saves a policy to the file, with the key and value structure
    *
    * The policies are stored in an XML structured file
    * @param policyName
    * @param policyValue
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    public final void savePolicy(String policyName, String policyValue)throws Exception
    {
        props.setProperty(policyName, policyValue);
        FileOutputStream output = new FileOutputStream(policyFileName);
        props.storeToXML(output, "TruncheonSettings");
        output.close();
        System.out.println("Policy " + policyName + " has been saved successfully.");
        System.gc();
    }

    /**
    * Resets all the policies to its default values.
    *
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final void resetPolicyFile()throws Exception
    {
        new File(policyFileName).delete();

        props = new Properties();

        for(int i = 0; i < resetValues.length; ++i)
            savePolicy(resetValues[i], "on");

        savePolicy("sysname", "SYSTEM" + ((int)(Math.random() * (999999 - 100000 + 1)) + 100000));
    }
}
