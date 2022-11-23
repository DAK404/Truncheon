package Truncheon.API.Wraith;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.Console;

import Truncheon.API.IOStreams;

public class WraithRead
{
    boolean helpMode = false;

    static File fileName = null;
    Console console = System.console();

    private static final boolean checkFileValidity()throws Exception
    {
        return !(fileName == null || fileName.equals("") || fileName.getName().startsWith(" "));
    }

    private void readFileLogic()throws Exception
    {
        if(! checkFileValidity())
        {
            IOStreams.printError("Invalid File Name! Please Enter A Valid File Name.");
        }
        else if(! fileName.exists())
        {
            IOStreams.printError("The Specified File Does Not Exist. Please Enter A Valid File Name.");
        }
        else
        {
            boolean continueFileRead = true;

            Truncheon.API.BuildInfo.viewBuildInfo();

            BufferedReader bufferObject = new BufferedReader(new FileReader(fileName));

            String fileContents = "";
            
            if(helpMode)
            {

                do
                {
                    fileContents = bufferObject.readLine();

                    if(fileContents.equalsIgnoreCase("<end of page>"))
                    {
                        if(console.readLine("\nPress ENTER to Continue, else type EXIT to quit help viewer.\n~DOC_HLP?> ").equalsIgnoreCase("exit"))
                            continueFileRead = false;
                        else
                        {
                            Truncheon.API.BuildInfo.viewBuildInfo();
                            continue;
                        }
                    }
                    else if(fileContents.equalsIgnoreCase("<end of help>"))
                    {
                        System.out.println("\n\nEnd of Help File.");
                        break;
                    }
                    else if(fileContents.startsWith("#"))
                    {
                        continue;
                    }
                    IOStreams.println(fileContents);
                }
                while(fileContents != null || continueFileRead);
            }
            else
            {
                do
                {
                    fileContents = bufferObject.readLine();
                    IOStreams.println(fileContents);
                }
                while(fileContents != null);
            }
            
            bufferObject.close();
            System.gc();

            Truncheon.API.IOStreams.confirmReturnToContinue();
        }
    }

    public void readUserFile(String userFileName)throws Exception
    {
        fileName = new File("./Users/Truncheon/" + userFileName);
        readFileLogic();
    }

    public void readHelpFile(String helpFile)throws Exception
    {
        helpMode = true;
        fileName = new File("./Docs/Truncheon/Help/" + helpFile);
        readFileLogic();
    }
}
