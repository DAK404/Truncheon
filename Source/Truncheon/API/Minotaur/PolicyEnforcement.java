/*
*    ███    ██ ██  ██████  ███    ██        ████████ ██████  ██    ██ ███    ██  ██████ ██   ██ ███████  ██████  ███    ██
*    ████   ██ ██ ██    ██ ████   ██ ██        ██    ██   ██ ██    ██ ████   ██ ██      ██   ██ ██      ██    ██ ████   ██
*    ██ ██  ██ ██ ██    ██ ██ ██  ██           ██    ██████  ██    ██ ██ ██  ██ ██      ███████ █████   ██    ██ ██ ██  ██
*    ██  ██ ██ ██ ██    ██ ██  ██ ██ ██        ██    ██   ██ ██    ██ ██  ██ ██ ██      ██   ██ ██      ██    ██ ██  ██ ██
*    ██   ████ ██  ██████  ██   ████           ██    ██   ██  ██████  ██   ████  ██████ ██   ██ ███████  ██████  ██   ████
*/

/*
* ---------------!DISCLAIMER!--------------- *
*                                            *
*         THIS CODE IS RELEASE READY         *
*                                            *
*  THIS CODE HAS BEEN CHECKED, REVIEWED AND  *
*   TESTED. THIS CODE HAS NO KNOWN ISSUES.   *
*    PLEASE REPORT OR OPEN A NEW ISSUE ON    *
*     GITHUB IF YOU FIND ANY PROBLEMS OR     *
*              ERRORS IN THE CODE.           *
*                                            *
*   THIS CODE FALLS UNDER THE LGPL LICENSE.  *
*    YOU MUST INCLUDE THIS DISCLAIMER WHEN   *
*        DISTRIBUTING THE SOURCE CODE.       *
*   (SEE LICENSE FILE FOR MORE INFORMATION)  *
*                                            *
* ------------------------------------------ *
*/

package Truncheon.API.Minotaur;

//Import the required Java IO classes
import java.io.Console;
import java.io.FileInputStream;

//Import the required Java Util classes
import java.util.Properties;

/**
* Program to enforce the policies set by the administrators
*
* @version 0.4.2
* @since 0.2.7
* @author DAK404
*/
public class PolicyEnforcement
{
    /**
    * Check the policy value against the policy name specified.
    *
    * @param Policy : The policy that must be checked from the .BURN file
    * @return boolean : The value of the policy set in the file
    * @throws Exception : Handle exceptions thrown during program runtime
    */
    public final boolean checkPolicy(String Policy)throws Exception
    {
        //Initialize the policy value as false by default
        boolean stat = false;

        Console console = System.console();
        try
        {
            //Feeds the retrievePolicyValue() with the policy to be checked
            switch(retrievePolicyValue(Policy).toLowerCase())
            {
                //If the policy value returned is false, print an error statement about the policy value
                case "off":
                System.out.println("[ ATTENTION ] : This module access is denied due to the policy configuration.\nPlease contact the system administrator for more information.");
                console.readLine();
                break;

                //Set the stat value as true if the policy value returned is true
                case "on":
                stat = true;
                break;

                //When a policy is not found or cannot be loaded, display an error message about the misconfigured policy
                case "error":
                System.out.println("[ WARNING ] : Module Policy is not configured. Please contact the system administrator to initialize the policy.");
                console.readLine();
                break;

                //Handle any other inputs returned after checking the policy
                default:
                System.out.println("[ ERROR ] : POLICY CONFIGURATION ERROR!.");
                console.readLine();
                break;
            }
        }
        catch(Exception E)
        {
            E.printStackTrace();
        }
        return stat;
    }

    /**
    * Retrieves the policy value in a string format, to the program requesting the value
    *
    * @param policyParameter : The policy that must be checked against the policy file
    * @return String : The value of the value retrieved from the file
    * @throws Exception : Handle exceptions thrown during program runtime
    */
    public final String retrievePolicyValue(String policyParameter)throws Exception
    {
        //Initialize the policy value as an empty string
        String policyValue = "";
        try
        {
            //Open the properties streams
            Properties prop = new Properties();
            String propsFileName="./System/Private/Truncheon/Policy.burn";

            //Load the file stream containing the program properties
            FileInputStream configStream = new FileInputStream(propsFileName);

            //Load the properties from an XML formatted file
            prop.loadFromXML(configStream);

            //Get the property value specified in the file
            policyValue = prop.getProperty(policyParameter);

            //Close the streams
            configStream.close();
        }
        catch(Exception E)
        {
            //Set the string value to "error" if the given property is not found, unreadable or is misconfigured
            policyValue = "Error";
        }

        //return the policy value in the string format
        return policyValue;
    }
}
