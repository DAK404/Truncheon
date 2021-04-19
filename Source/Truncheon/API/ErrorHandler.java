package Truncheon.API;

import java.io.StringWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.Console;
import java.io.File;

public final class ErrorHandler
{
    public final void handleException(Exception E)
    {
        try
        {
            System.out.println("\n\n");
            String Err = E.toString();

            System.err.println("--------------------------------");
            System.err.println("[ SYSTEM FAILURE - FATAL ERROR ]");
            System.err.println("--------------------------------");
            System.err.println("\nAn error was encountered during program execution and cannot continue.");

            System.err.println("\nError Details: " + Err);

            System.err.println("\nThe following stack trace may indicate the cause of failure.");

            System.err.println("\n*****************************");
			System.err.println("!    PROGRAM STACK TRACE    !");
			System.err.println("*****************************\n");
			E.printStackTrace();
			System.err.println("\n*****************************");
			System.err.println("!       STACK TRACE END     !");
			System.err.println("*****************************\n");

            System.out.println("\nThe above data will be written to a log file which can, optionally,\nbe uploaded to help the developers to solve any potential issues.");

            StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			E.printStackTrace(pw);

            new Truncheon.API.Logger().Log(sw.toString(), "./System/Public/Logs/Truncheon/Error");
            
            Console console=System.console();
            System.err.println("\n[ OPTIONAL ] : Any relevant information to add to file?");
            new Truncheon.API.Logger().Log("User Comments: " + console.readLine("User Comments: "), "./System/Public/Logs/Truncheon/Error");

            sw.close();
            pw.close();

            System.err.println("\nError information has been written to log file successfully.");
            System.err.println("Program cannot be recovered to previous state. Unsaved data will be lost.\nPress enter to restart program.");
            System.in.read();
            System.exit(1);
        }
        catch(Exception Ex)
        {
            Ex.printStackTrace();
            System.exit(0);
        }
    }
}