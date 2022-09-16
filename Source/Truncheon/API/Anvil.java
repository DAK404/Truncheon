package Truncheon.API;

import java.io.Console;
import java.io.File;
//Scripting featureset

// COMMON COMPONENTS LIKE WAIT, INPUT, ECHO ARE HERE. NO REDUNDANCY
//ANY EXTRA FEATURES ARE PASSED BACK TO THE PROGRAM AND ARE PROCESSED BY THE CALLING PROGRAM
public class Anvil
{
    Console console = System.console();
    public boolean anvilInterpreter(String command)
    {
        boolean status = true;
        String[] commandArray = splitStringToArray(command);
        
        switch(commandArray[0].toLowerCase())
        {
            case "clear":
                BuildInfo.viewBuildInfo();
            break;

            case "echo":
                if(commandArray.length < 2)
                    IOStreams.println("echo <STRING> \n\nOR\n\necho \"<STRING_WITH_SPACES>\"");
                else
                {
                    try
                    {
                        IOStreams.println(commandArray[1]);
                    }
                    catch(Exception e)
                    {
                        IOStreams.printError("ANVIL : ERROR IN ECHO MODULE!");
                        e.printStackTrace();
                        console.readLine();
                    }
                }
            break;

            case "about":
                BuildInfo.aboutProgram();
            break;

            case "wait":
                try
                {
                    if(Integer.parseInt(commandArray[1]) < 1)
                        IOStreams.println("This will make the prompt wait for a given number of milliseconds.\nSyntax: wait (1000)\n\nPrompt shall wait for 1 second.");
                    else
                        Thread.sleep(Integer.parseInt(commandArray[1]));
                }
                catch(NumberFormatException e)
                {
                    System.err.println("error! Please provide a numeric input for the wait timer!");
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            break;

            case "confirm":
                console.readLine("\n\nPress ENTER to Continue...");
            break;

            default:
                status = false;
            break;
        }
        return status;
    }

    public String[] splitStringToArray(String command)
    {
        return command.split(" (?=([^\"]*\"[^\"]*\")*[^\"]*$)"); 
    }

    private void printError()
    {
        IOStreams.printError("Incorrect Syntax! Please use the following syntax:");
    }
}
