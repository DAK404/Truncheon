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


package Truncheon.Core;

//Import the required Java IO classes
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
    //Initialize the system name to be displayed on the shell.
    private String _sysName;

    /**
    * Constructor to initialize the system name to be used by the shell
    *
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    public Boot()throws Exception
    {
        _sysName = new Truncheon.API.Minotaur.PolicyEnforcement().retrievePolicyValue("sysname");
    }

    /**
    * Logic to accept the boot parameters and boot the program in the desired mode.
    *
    * @param Args : Accept Program Arguments and boot modes
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    public static void main(String[] Args)throws Exception
    {
        try
        {
            /*
            * To-Do
            * - Accept Other bootmodes and their implementations
            * - Streamline code to accomodate all modes
            */

            //Accept the arguments specified in the Launcher program
            switch(Args[0])
            {
                /*
                * Normal Mode :
                * Boots directly into the program.
                * ErrorHandler tries to handle exceptions.
                */
                case "normal":
                new Truncheon.Core.Boot().bootLogic();
                break;

                /*
                * Default Condition exits the program with error
                * code of 101, signifiying a wrong boot mode specification
                */
                default:
                System.exit(0x7F);
            }

        }
        catch(Exception E)
        {
            //Handle any exceptions thrown during runtime
            new Truncheon.API.ErrorHandler().handleException(E);
        }
    }

    /**
    * logic to access basic functions of the program.
    *
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final void bootLogic()throws Exception
    {
        //Clear the screen and display the program information
        new Truncheon.API.BuildInfo().versionViewer();

        //Boot Checklist: Writes the status of files checked during the boot process
        System.out.println("BOOT CHECKLIST");
        System.out.println("==============\n");

        //Checks include the verification of the existence of the "./System" and "./Users/Truncheon" directories
        if(new File("./System").exists() & new File("./Users/Truncheon").exists())
        {
            //Confirms the existence of the base directories, moving on to check if the "./System" folder contains Truncheon Files
            System.out.println("* Base Directories check       : COMPLETE");
            if(new File("./System/Public/Truncheon").exists() & new File("./System/Private/Truncheon").exists())
            {
                //Display the pass status to the user
                System.out.println("* Truncheon Directories check  : COMPLETE");

                //Begin the main logic of the boot program
                Console console=System.console();
                System.out.println("\n==============\n");

                /*
                * NOTE:
                * This logic uses a simple implementation
                * since the functionalities defined are basic.
                * None of the modules require arguments to work.
                */

                //Run an infinite loop to process the inputs
                while(true)
                //The logic to process the inputs specified by the guest user.
                switch(console.readLine("~Guest@" + _sysName + "> ").toLowerCase())
                {
                    //Opens the MainMenu program, and gives the user a chance to authenticate.
                    case "login":
                    new Truncheon.Core.MainMenu().mainMenuLogic();
                    break;

                    //Print the details of the program, ie version number, iteration, etc.
                    case "about":
                    new Truncheon.API.BuildInfo().about();
                    break;

                    //Open the helpfile for the boot program.
                    case "?":
                    case "help":
                    new Truncheon.API.Wraith.ReadFile().showHelp("HelpDocuments/Boot.manual");
                    break;

                    //Clears the screen
                    case "clear":
                    new Truncheon.API.BuildInfo().versionViewer();
                    break;

                    //Exits the program, sends exit code 0 to ProgramLauncher.
                    case "exit":
                    System.exit(0);

                    //Restarts the program, sends exit code 1 to ProgramLauncher.
                    case "restart":
                    System.exit(0xC0);

                    //Do nothing if the input is blank
                    case "":
                    break;

                    //Print 'invalid input' if the command is not recognized.
                    default:
                    System.out.println("Invalid input.");
                    break;
                }
            }
        }

        //If the checks fail, the program defaults back to the setup program.
        new Truncheon.Core.Setup().setupLogic();
    }
}
