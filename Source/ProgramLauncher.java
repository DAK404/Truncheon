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
				*/

				//Spawn a ProcessBuilder called session_monitor to track the process's exit code
				ProcessBuilder session_monitor=new ProcessBuilder("java", Args[0]+".Core.Boot", Args[1]);
				//Spawn a process with  called process_monitor to start the new process
				Process process_monitor= session_monitor.inheritIO().start();
				//Wait for the process to complete its execution before it passes on to shunt logic
				process_monitor.waitFor();
				

                switch(process_monitor.exitValue())
				{
					case 0:
							//Normal exit mode
							System.exit(0);
							
					case 1:
							//Restart mode
							break;
					
					case 2:
							//Error exit mode
							System.exit(2);
							
					case 3:
							//Error restart mode
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
        }
    }
}