package Truncheon.API.Minotaur;

import java.io.Console;
import java.io.FileInputStream;

import java.util.Properties;

public class PolicyEnforcement
{
    public final boolean checkPolicy(String Policy)throws Exception
    {
        boolean stat = false;
        Console console = System.console();
        try
        {
            switch(retrivePolicyValue(Policy).toLowerCase())
            {
                case "off":
                    System.out.println("[ ATTENTION ] : This module access is denied due to the policy configuration.n\nPlease contact the system administrator for more information.");
                    console.readLine();
                    break;
                
                case "on":
                    stat = true;
                    break;

                case "error":
                    System.out.println("[ WARNING ] : Module Policy is not configured. Please contact the system admnistrator to initialize the policy.");
                    console.readLine();
                    break;

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
    
    public final String retrivePolicyValue(String policyParameter)throws Exception
    {
        String policyValue = "";
        try
        {           
            Properties prop = new Properties();
            String propsFileName="./System/Private/Truncheon/Policy.burn";
            FileInputStream configStream = new FileInputStream(propsFileName);
            prop.loadFromXML(configStream);
            policyValue = prop.getProperty(policyParameter);
            configStream.close();
        }
        catch(Exception E)
        {
            policyValue = "Error";
        }
        return policyValue;
    }
}
