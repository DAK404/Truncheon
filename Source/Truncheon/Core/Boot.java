package Truncheon.Core;

import java.io.Console;
import java.io.File;

public final class Boot
{
    public static void main(String[] Args)throws Exception
    {
        try
        {
            switch(Args[0])
            {
                case "fastdbg":
                        new Truncheon.Core.DebugMainMenu().mainMenuLogic();
                        break;

                case "normal":
                        new Truncheon.Core.Boot().bootLogic();
                        break;

                default: 
                        System.exit(101);
            }
            
        }
        catch(Exception E)
        {
            new Truncheon.API.ErrorHandler().handleException(E);
        }
    }

    private final void bootLogic()throws Exception
    {
        new Truncheon.API.BuildInfo().versionViewer();
        System.out.println("Verifying Installation...");
        if(new File("./System").exists() == true & new File("./Users").exists() == true)
            if(new File("./System/Public/Truncheon").exists() == true & new File("./System/Private/Truncheon").exists() == true)
            {
                Console console=System.console();
                while(true)
                {
                    switch(console.readLine("guest@system> ").toLowerCase())
                    {
                        case "login":
                            new Truncheon.Core.MainMenu().mainMenuLogic();
                            break;

                        case "about":
                            new Truncheon.API.BuildInfo().about();
                            break;

                        case "help":
                            System.out.println("Available commands:\n\nlogin\nabout");
                            break;

                        case "clear":
                            new Truncheon.API.BuildInfo().clearScreen();
                            break;

                        case "exit":
                            System.exit(0);

                        case "":
                            break;

                        default:
                            System.out.println("Invalid input.");
                            break;
                    }
                }
            }
        new Truncheon.Core.Setup().setupLogic();
    }
}
