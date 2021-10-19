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

package Truncheon.API;

//Import the required Java IO classes
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.Console;

/**
 * Program to handle the exceptions and errors in the program
 * 
 * @version 0.7.42
 * @since 0.5.3
 * @author DAK404
 */
public final class ErrorHandler
{
    /**
     * Logic to handle the exception caught by a program
     * 
     * @param E The exception caught
     */
    public final void handleException(Exception E)
    {
        try
        {
            System.out.println("\n\n");
            //Store the exception in a string format
            String Err = E.toString();

            //Print the exception details
            System.err.println("--------------------------------");
            System.err.println("[ SYSTEM FAILURE - FATAL ERROR ]");
            System.err.println("--------------------------------");
            System.err.println("\nAn error was encountered during program execution and cannot continue.");

            System.err.println("\nError Details: " + Err);

            System.err.println("\nThe following stack trace may indicate the cause of failure.");

            //Print the stack trace, which caused the exception
            System.err.println("\n*****************************");
			System.err.println("!    PROGRAM STACK TRACE    !");
			System.err.println("*****************************\n");
			E.printStackTrace();
			System.err.println("\n*****************************");
			System.err.println("!       STACK TRACE END     !");
			System.err.println("*****************************\n");

            //Print the stack trace to a log file for debugging and error tracing
            System.out.println("\nThe above data will be written to a log file which can, optionally,\nbe uploaded to help the developers to solve any potential issues.");

            StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			E.printStackTrace(pw);

            String temporary = """
            --- ERROR REPORT ---
            Program Stack Trace\n"""
            + sw.toString() + """
            End Of Stack Trace
            """;

            new Truncheon.API.Wraith.WriteFile().logToFile(temporary, "Logs/ErrorLog");
            Console console=System.console();

            //Request for any user comments which can provide context for every exceptions caught
            System.err.println("\n[ OPTIONAL ] : Any relevant information to add to file?");

            //save the comments to the log file causing the crashes and issues
            new Truncheon.API.Wraith.WriteFile().logToFile("User Comments: " + console.readLine("User Comments: ") + "\n", "Logs/ErrorLog");

            new Truncheon.API.Wraith.WriteFile().logToFile("--- ERROR REPORT ---\n\n", "Logs/ErrorLog");

            sw.close();
            pw.close();
            System.gc();

            System.err.println("\nError information has been written to log file successfully.");
            System.err.println("Program cannot be recovered to previous state. Unsaved data will be lost.\nPress enter to restart program.");
            System.in.read();

            //Restart the program to start afresh
            System.exit(1);
        }
        catch(Exception Ex)
        {
            //Catch any exceptions while handling an error
            Ex.printStackTrace();
            System.exit(0);
        }
    }
}