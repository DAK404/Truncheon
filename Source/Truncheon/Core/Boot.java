package Truncheon.Core;

import java.io.Console;

public final class Boot
{
    public static void main(String[] Args)throws Exception
    {
        try
        {
            Console console=System.console();
            new Truncheon.API.BuildInfo().versionViewer();
            switch(Args[0])
            {
                case "fastdbg":
                        System.out.println("Truncheon has booted in "+Args[0]+" mode\n");
                        console.readLine("Default Debugging Session active.\nPress enter to continue.\n");
                        new Truncheon.Core.MainMenu().mainMenu();
                        break;

                case "normal":
                        System.out.println("Cannot boot in normal mode yet. Changing the boot mode to fastdbg.");
                        System.in.read();
                        System.exit(150001);

                default:
                        System.exit(101);
            }
            
        }
        catch(Exception E)
        {
            new Truncheon.API.ErrorHandler().handleException(E);
        }
    }
}
