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

/**
 *
 */
class ProgramLauncher
{
    /**
     *
     * @param Args
     * @throws Exception : Handle exceptions thrown during program runtime.
     */
    public static void main(String[] Args)throws Exception
    {
        try
        {
            if(Args.length < 2)
            {
                System.out.println("Cannot start the program launcher!\n\nSyntax: java ProgramLauncher <kernel_name> <boot_mode>\n");
                System.exit(-1);
            }

            while(true)
            {
                //Clean up before execution
                System.gc();


                /*
                * --- LAUNCHER EXIT TABLE ---
                *
                * The exit table denotes the exit code that ProgramLaunccher can accept.
                * Specific exit code is reserved for a specific meaning. This means that
                * the program has to follow the exit code to attain desired output.
                *
                * --- LAUNCHER EXIT TABLE ---
                *
                * NOTES:
                *
                * OBSERVE THAT THE PROCESS CREATION STATEMENTS ARE INSIDE THE WHILE LOOP.
                * THIS IS IMPLEMENTED SO THAT WHEN THE PROGRAM EXITS AND ALTERS PARAMETERS,
                * THE SAME IS REFLECTED WHEN THE NEW PROCESS IS CREATED. THIS WOULD HELP
                * IN IMPLEMENTING FUTURE FUNCTIONALITIES.
                *
                * ---------------------------
                */

                //Spawn a ProcessBuilder called session_monitor to track the process's exit code
                ProcessBuilder sessionMonitor=new ProcessBuilder("java", Args[0]+".Core.Boot", Args[1]);

                //Spawn a process with  called process_monitor to start the new process
                Process processMonitor= sessionMonitor.inheritIO().start();

                //Wait for the process to complete its execution before it passes on to shunt logic
                processMonitor.waitFor();

                //Monitor the exit code of the process to perform certain actions based on the process exit code.
                switch(processMonitor.exitValue())
                {
                    case 0:
                    //Normal exit mode
                    System.exit(0);

                    case 1:
                    System.err.println("[ ERROR ] : Cannot load program.\nPlease check the files' integrity and try again.\nPress ENTER to Continue");
                    System.in.read();
                    System.exit(-1);

                    case 0xC0:
                    //Restart mode
                    System.out.println("[ ATTENTION ] : Program Restarting...");
                    break;

                    case 0xC1:
                    //Error exit mode
                    System.err.println("[ WARNING ] : Program exited with an error.\nPlease check the Log Files for more information.");
                    Thread.sleep(2500);
                    System.exit(702);

                    case 0xC2:
                    //Error restart mode
                    System.err.println("[ WARNING ] : Program Restarting due to an Error...");
                    Thread.sleep(2500);
                    break;

                    case 0xC3:
                    System.out.println("[ ATTENTION ] : Program now booting into safe mode...");
                    Args[1]="safemode";
                    break;

                    case 0x7F:
                    //Invalid boot mode
                    System.err.println("Invalid Boot Mode Detected! Exiting Program...");
                    System.exit(0x7F);

                    default:
                    //Generic error output
                    System.exit(400);
                }

                //Clean up resources after execution
                System.gc();
            }
        }
        catch(Exception E)
        {
            System.out.println("\n\n");
            System.out.println("[ SYSTEM FAILURE ] : THE FOLLOWING ERROR WAS DETECTED DURING PROGRAM EXECUTION.");
            System.out.println("\nERROR : "+E);
            System.out.println("\nHERE IS THE FOLLOWING DATA (STACK TRACE) WHICH HAS CAUSED THE FAILURE.");
            System.out.println("\n*****************************");
            System.out.println("!    PROGRAM STACK TRACE    !");
            System.out.println("*****************************\n");
            E.printStackTrace();
            System.out.println("\n*****************************");
            System.out.println("!      STACK TRACE END      !");
            System.out.println("*****************************\n");
            System.out.println("\nPress ENTER to continue..");
            System.in.read();
        }
    }
}