package Truncheon.API.Minotaur;

import java.io.Console;
import java.io.FileInputStream;

import java.util.Properties;

public class PolicyEnforcement
{
    public final boolean checkPolicy(String Policy)throws Exception
	{
		Console console = System.console();
		try
		{
            boolean stat = false;
			System.gc();
			Properties prop = new Properties();
			String propsFileName="./System/Private/Truncheon/Policy.burn";
			FileInputStream configStream = new FileInputStream(propsFileName);
			prop.loadFromXML(configStream);
			if(prop.getProperty(Policy).equalsIgnoreCase("off"))
            {
				System.out.println("The Administrator has disabled this feature. Contact the Administrator for more information.\nPress ENTER to continue..");
                console.readLine();
            }
			else if(prop.getProperty(Policy).equalsIgnoreCase("on"))
				stat=true;
			
			configStream.close();
            System.gc();

			return stat;
		}
		catch(Exception E)
		{
			System.out.println("[ ATTENTION ] : This policy or module is either not configured or has an issue with it. Contact your administrator for more info.");
            console.readLine();
			return false;
		}
	}   
}
