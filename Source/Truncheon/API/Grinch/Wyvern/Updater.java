package Truncheon.API.Grinch.Wyvern;

import Truncheon.API.IOStreams;

public class Updater
{
    /**
    *
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    public final void updaterLogic() throws Exception
    {
        try
        {
            if(! new Truncheon.API.Minotaur.PolicyEnforce().checkPolicyQuiet("update"))
                IOStreams.printError("STANDARD USERS CANNOT UPDATE THE PROGRAM!\nPlease contact your administrator for more information.");
            else
            {
                Truncheon.API.BuildInfo.aboutProgram();
                IOStreams.println("----------------------\n[ Wyvern Updater 1.2 ]\n----------------------\n");
                IOStreams.printAttention("The program will download and install updates from the official repository.");
                IOStreams.printWarning("DO NOT TURN OFF THE SYSTEM, CHANGE NETWORK STATES OR CLOSE THIS PROGRAM.\nBY DOING THIS, YOU MIGHT RISK THE LOSS OF DATA OR PROGRAM INSTABILITY.\n\nSince this program takes minimal resources and time to install the update, please wait for\nthe update to complete and then perform other operations.\n\n[ ATTENTION ] : It is advised that the program should be restarted after applying the updates.\nThis will help in applying and reflecting the changes implemented in the new update.");
                IOStreams.println("\n-----------------------\n");

                if (download())
                {
                    IOStreams.printInfo("Downloaded Update Package Successfully!");
                    IOStreams.printInfo("Installing Update From: ./Update.zip");
                    
                    if (install())
                        IOStreams.printInfo("Update installed successfully.\n\nIT IS RECOMMENDED TO RESTART THE PROGRAM TO APPLY THE UPDATES AND REFLECT THE NEW CHANGES.");
                    else
                        IOStreams.printError("Failed to install update.\nPossible Causes:\n- The downloaded update file was partially downloaded or was corrupt.\n- The download was interrupted by a network change.\n- The update location has been moved to a new URL.\n\nPossible Solutions:\n- Retry the update.\n- Restart the network/routers and check the network connection stability.\n- Contact the Administrator for more information.\n\n");
                }
                else
                    IOStreams.printError("Failed to download update.\nPossible Causes:\n- Limited/restricted network access, or firewall rules which prevented downloading the file.\n- The download session was interrupted by a network change.\n- The update location has been moved to a new URL.Possible Solutions:\n- Retry the update.\n- Restart the network/routers and check the network connection stability.\n- Contact the Administrator for more information.\n-------------------------\n");
            }
        }
        catch(Exception E)
        {
            //Handle any exceptions thrown during runtime
            new Truncheon.API.ExceptionHandler().handleException(E);
        }
        System.gc();
    }

    /**
    *
    * @return
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final boolean download() throws Exception
    {
        return new Truncheon.API.Grinch.Download().downloadUpdate();
    }

    /**
    *
    * @return
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final boolean install() throws Exception
    {
        return new Truncheon.API.Grinch.Wyvern.Installer().installPackage();
    }
}
