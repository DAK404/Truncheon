// ================================================= //
// ! DO NOT DELETE ! DO NOT DELETE ! DO NOT DELETE !
// ================================================= //

/*
*           THIS CODE HAS NOT BEEN TESTED.
*       DO NOT INCLUDE THIS CODE IN RELEASES
*      COMPILE WITH DEBUG BUILDS AND CHECK FOR
*                  ISSUES AND BUGS.
*/

// ================================================= //
// ! DO NOT DELETE ! DO NOT DELETE ! DO NOT DELETE !
// ================================================= //

class ProgramLauncher
{
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
                ProcessBuilder session_monitor=new ProcessBuilder("java", Args[0]+".Core.Boot", Args[1]);

                //Spawn a process with  called process_monitor to start the new process
                Process process_monitor= session_monitor.inheritIO().start();

                //Wait for the process to complete its execution before it passes on to shunt logic
                process_monitor.waitFor();

                //Monitor the exit code of the process to perform certain actions based on the process exit code.
                switch(process_monitor.exitValue())
                {
                    case 0:
                            //Normal exit mode
                            System.exit(0);

                    case 1:
                            //Restart mode
                            System.out.println("Program is restarting... Please wait.");
                            Thread.sleep(2500);
                            break;

                    case 2:
                            //Error exit mode
                            System.out.println("Program is exiting on an error.");
                            Thread.sleep(2500);
                            System.exit(2);

                    case 3:
                            //Error restart mode
                            System.out.println("Program is restarting due to an error... Please wait.");
                            Thread.sleep(2500);
                            break;

                    case 4:
                            Args[1]="safemode";
                            break;

                    case 101:
                            //Invalid boot mode
                            System.exit(101);

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
            System.out.println("\nPress Enter To Continue.");
            System.in.read();
        }
    }
}