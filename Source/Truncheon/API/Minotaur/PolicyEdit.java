package Truncheon.API.Minotaur;

//Import the required Java IO classes
import java.io.Console;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;

//Import the required Java Util classes
import java.util.Properties;

//Import the Truncheon classes
import Truncheon.API.BuildInfo;
import Truncheon.API.IOStreams;

/**
*
*/
public class PolicyEdit
{
    boolean _userIsAdmin = false;

    public final String [] resetValues = {"auth", "update", "download", "script", "filemgmt", "read", "edit", "usermgmt", "policy"};

    private final String policyFileName = "./System/Truncheon/Private/Policy.burn";

    private String suggestedInputs = "";

    private Console console = System.console();
    private Properties props = null;

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
        if(!authenticationLogic())
            IOStreams.printError("Authentication Failure. Exiting...");
        else
            if(new Truncheon.API.Minotaur.PolicyEnforce().checkPolicy("policy") || _userIsAdmin)
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

            challengeStatus = (new Truncheon.API.Dragon.LoginAuth(username).authenticationLogic(new Truncheon.API.Minotaur.Cryptography().stringToSHA3_256(String.valueOf(console.readPassword("Password: "))), new Truncheon.API.Minotaur.Cryptography().stringToSHA3_256(String.valueOf(console.readPassword("Security Key: ")))));

            _userIsAdmin = new Truncheon.API.Dragon.LoginAuth(username).checkPrivilegeLogic();
        }
        catch(Exception E)
        {
            //Handle any exceptions thrown during runtime
        }
        return challengeStatus & _userIsAdmin;
    }

    /**
    *
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final void policyEditor()throws Exception
    {
        suggestedInputs = "[ MODIFY " + (_userIsAdmin?"| RESET ":"") + "| UPDATE | HELP | EXIT ]";
        props = new Properties();
        if(! new File(policyFileName).exists())
            resetPolicyFile();
        viewPolicyInfo();
        
        String input;
        
        do
        {
            input = console.readLine("PolicyEditor)> ");

            String[] policyCommandArray = Truncheon.API.Anvil.splitStringToArray(input);

            switch(policyCommandArray[0].toLowerCase())
            {
                case "modify":
                if(policyCommandArray.length < 2)
                    System.out.println("Goober");
                else
                    savePolicy(policyCommandArray[1], policyCommandArray[2]);
                break;

                case "reset":
                if(_userIsAdmin)
                {
                    IOStreams.printAttention("Resetting Policy File...");
                    resetPolicyFile();
                }
                break;

                case "update":
                    viewPolicyInfo();
                break;

                case "exit":
                case "":
                break;

                default:
                System.out.println("Invalid command. Please try again.");
                break;
            }
            System.gc();
        }
        while(! input.equalsIgnoreCase("exit"));
    }

    private void viewPolicyInfo()throws Exception
    {
        displaySettings();
        IOStreams.println(suggestedInputs + "\n");
        IOStreams.println("Add or Modify Policy Syntax:\nmodify <policy_name> <policy_value>");
        System.out.println();
    }

    /**
    *
    */
    private final void displaySettings()throws Exception
    {
        BuildInfo.viewBuildInfo();
        System.out.println("         Minotaur Policy Editor 2.0         ");
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
    * Saves a policy to the file, with the key and value structure
    *
    * The policies are stored in an XML structured file
    * @param policyName
    * @param policyValue
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final void savePolicy(String policyName, String policyValue)throws Exception
    {
        props.setProperty(policyName, policyValue);
        FileOutputStream output = new FileOutputStream(policyFileName);
        props.storeToXML(output, "TruncheonSettings");
        output.close();
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
        savePolicy("module", "off");
    }
}
