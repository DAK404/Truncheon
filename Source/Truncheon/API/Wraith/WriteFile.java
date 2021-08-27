package Truncheon.API.Wraith;

import java.util.*;
import java.text.*;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileWriter;

public final class WriteFile
{
    public final void logToFile(String PrintToFile, String fileName)
    {
        try
        {
            if(checkFileValidity(fileName))
            {
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = new Date();
                System.gc();
                File logFile = new File("./Logs");
                if(! logFile.exists())
                {
                    logFile.mkdir();
                }
                BufferedWriter obj = new BufferedWriter(new FileWriter("./" + fileName + ".log", true));
                PrintWriter pr = new PrintWriter(obj);
                pr.println(dateFormat.format(date) + ": " + PrintToFile);
                pr.close();
                obj.close();
                System.gc();
            }
        }
        catch(Exception E)
        {
            new Truncheon.API.ErrorHandler().handleException(E);
        }
    }

    private boolean checkFileValidity(String fn)throws Exception
    {
        if(fn == null || fn.equals("") || fn.startsWith(" "))
        {
            System.out.println("Please enter a valid file name to open.");
            return false;
        }
        return true;
    }

    public final void editFile(String fileName, String dir)
    {
        try
        {
            if(! new Truncheon.API.Minotaur.PolicyEnforcement().checkPolicy("write"))
                return;

            if(checkFileValidity(fileName))
            {
                boolean appendFile = true;
                String message = "";

                System.out.println("Wraith Text Editor 1.5");
                System.out.println("______________________\n");
                
                Console console=System.console();

                File writeToFile = new File(dir + fileName);
                System.out.println("\nEditing File : " + fileName + "\n\n");

                if(writeToFile.exists())
                {
                    switch(console.readLine("[ ATTENTION ] : A file with the same name has been found in this directory. Do you want to OVERWRITE it, APPEND to the file, or GO BACK? \n\nOptions:\n[ OVERWRITE | APPEND | RETURN | HELP ]\n\n> ").toLowerCase())
                    {
                        case "overwrite":

                                appendFile = false;
                                System.out.println("The new content will overwrite the previous content present in the file!");
                                break;

                        case "append":

                                System.out.println("The new content will be added to the end of the file! Previous data will remain unchanged.");
                                break;

                        case "return":

                                return;

                        case "help":

                                System.out.println("Work in Progress");
                                break;

                        default:
                        
                                System.out.println("Invalid choice. Exiting...");
                                return;
                    }
                }

                BufferedWriter obj = new BufferedWriter(new FileWriter(writeToFile, appendFile));
                PrintWriter pr = new PrintWriter(obj);

                do
                {
                    pr.println(message);
                    message = console.readLine();
                }
                while( !(message.equals("<exit>")) );
                
                pr.close();
                obj.close();
                System.gc();
            }
        }
        catch(Exception E)
        {
            new Truncheon.API.ErrorHandler().handleException(E);
        }
    }
}