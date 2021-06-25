/*

----- PROGRAM DOCUMENTATION -----

THIS PROGRAM IS UNDER DEVELOPMENT
AND SHOULD NOT BE CONSIDERED
RELEASE READY. FEATURES MAY BE
BROKEN OR INCOMPLETE. COMPILE AND
TEST AT YOUR OWN RISK.

---------------------------------

     --- Program Details ---     

     Author  : DAK404
     Date    : 17-June-2021
     Version : 0.1.14

     -----------------------

*/


package Truncheon.Core;

//import the Java's IO classes
import java.io.Console;
import java.io.File;

/**
 * Program to start working with Truncheon.
 * 
 * It provides a basic functionality to a guest user to access important information and an option to login or quit.
 *  
 * @version 0.1.12
 * @since 0.0.1
 * @author DAK404
 */
public final class Boot
{

    /**
    * Sole constructor. (For invocation by subclass constructors, typically implicit.)
    */
    public Boot()
    {

    }


    /**
     * Logic to accept the boot parameters and boot the program in the desired mode.
     * 
     * @param Args : Accept Program Arguments and boot modes
     * @throws Exception : Handle general exceptions during thrown during runtime.
     */
    public static void main(String[] Args)throws Exception
    {
        try
        {
            switch(Args[0])
            {
                case "fastdbg":
                        new Truncheon.Core.DebugMainMenu().debugProgram();
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

    /**
     * logic to access basic functions of the program.
     * 
     * @throws Exception : Handle general exceptions during thrown during runtime.
     */
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

                        case "restart":
                            System.exit(1);

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
