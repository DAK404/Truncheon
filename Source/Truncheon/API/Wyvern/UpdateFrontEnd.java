package Truncheon.API.Wyvern;

/**
*
*/
public final class UpdateFrontEnd
{
    /**
    *
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    public final void updateLogic() throws Exception
    {
        try
        {
            if(! new Truncheon.API.Minotaur.PolicyEnforcement().checkPolicy("update"))
            return;

            new Truncheon.API.BuildInfo().about();
            System.out.println("------------------------\n[ Wyvern Updater 1.1.3 ]\n------------------------\n");
            System.out.println("[ ATTENTION ] : The program will download and install updates from the official repository.\n[  WARNING  ] : DO NOT TURN OFF THE SYSTEM, CHANGE NETWORK STATES OR CLOSE THIS PROGRAM.\nBY DOING THIS, YOU MIGHT RISK THE LOSS OF DATA OR PROGRAM INSTABILITY.\n\nSince this program takes minimal resources and time to install the update, please wait for\nthe update to complete and then perform other operations.\n\n[ ATTENTION ] : It is advised that the program should be restarted after applying the updates.\nThis will help in applying and reflecting the changes implemented in the new update.");
            System.out.println("\n-------------------------\n");

            if (download())
            {
                System.out.println("[   INFO!   ] : Update downloaded successfully.");
                System.out.println("[   INFO!   ] : Update file location: ./Update.zip\n");
                System.out.println("[   INFO!   ] : Installing update from ./Update.zip");

                if (install())
                System.out.println("[   INFO!   ] : Update installed successfully.\n\n[ ATTENTION ] : IT IS RECOMMENDED TO RESTART THE PROGRAM TO APPLY THE UPDATES AND REFLECT THE NEW CHANGES.\n");

                else
                {
                    System.out.println("[   ERROR   ] : Failed to install update.\n");
                    System.out.println("Possible Causes:\n- The downloaded update file was partially downloaded or was corrupt.\n- The download was interrupted by a network change.\n- The update location has been moved to a new URL.\n\n");
                    System.out.println("Possible Solutions:\n- Retry the update.\n- Restart the network/routers and check the network connection stability.\n- Contact the Administrator for more information.\n\n");
                }
            }
            else
            {
                System.out.println("[   ERROR   ] : Failed to download update. Error.\n");
                System.out.println("Possible Causes:\n- Limited/restricted network access, or firewall rules which prevented downloading the file.\n- The download session was interrupted by a network change.\n- The update location has been moved to a new URL.\n");
                System.out.println("Possible Solutions:\n- Retry the update.\n- Restart the network/routers and check the network connection stability.\n- Contact the Administrator for more information.\n-------------------------\n");
            }
            System.gc();
            System.out.println("Press ENTER to continue..");
            System.in.read();
        }
        catch(Exception E)
        {
            //Handle any exceptions thrown during runtime
            new Truncheon.API.ErrorHandler().handleException(E);
        }
    }

    /**
    *
    * @return
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final boolean download() throws Exception
    {
        try
        {
            return new Truncheon.API.Wyvern.Download().downloadUpdate();
        }
        catch (Exception E)
        {
            return false;
        }
    }

    /**
    *
    * @return
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final boolean install() throws Exception {
        try
        {
            return new Truncheon.API.Wyvern.UpdateInstaller().install();
        }
        catch (Exception E)
        {
            return false;
        }
    }
}