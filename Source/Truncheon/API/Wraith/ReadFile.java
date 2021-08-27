package Truncheon.API.Wraith;

import java.io.Console;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;

public final class ReadFile
{
    File file = null;

    public final void showHelp(String helpFile)throws Exception
    {
        try
        {
            if(checkFileValidity(helpFile))
            {
                file=new File("./Information/Truncheon/"+helpFile);
                readFile(true);
            }    
        }
        catch(Exception E)
        {
            E.printStackTrace();
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

    public final void readUserFile(String fileName, String dir)throws Exception
    {
        try
        {
            if(! new Truncheon.API.Minotaur.PolicyEnforcement().checkPolicy("read"))
                return;
            if(checkFileValidity(fileName))
            {
                file = new File( dir + fileName);
                readFile(false);
            }
        }
        catch(Exception E)
        {
            E.printStackTrace();
        }
    }

    private final void readFile(boolean helpMode) throws Exception
    {
        //A link to show the build info to the user's terminal
        new Truncheon.API.BuildInfo().versionViewer();

        Console console=System.console();

        //A condition to check if the given file is found or not. This prevents exception, which may or may not disrupt the program.

        //This checks if the file doesnt exist. If it doesnt exist, the error text is shown on terminal.
        if (! file.exists())
            System.out.println("[ ERROR ] : Unable to locate file: The specified file cannot be read, found or loaded.");
        //This checks if the filename points to a directory
        else if (file.isDirectory())
            System.out.println("[ ERROR ] : Unable to read file : The specified file name is a directory.");
        //If the file exists, the file is displayed on the terminal.
        else
        {
            //Open the method to read files and read it
            BufferedReader ob = new BufferedReader(new FileReader(file));

            //Initialize the string to be null
            String p = "";

            if(! helpMode)
            {
                //Logic to read the file line by line.
                while ((p = ob.readLine()) != null)
                    System.out.println(p);
            }
            else
            {
                //Logic to read the help file.
                while ((p = ob.readLine()) != null)
                {
                    if(p.equalsIgnoreCase("<end of page>"))
                    {
                        console.readLine("\nPress ENTER to Continue.");
                        new Truncheon.API.BuildInfo().versionViewer();
                        continue;
                    }                    
                    else if(p.equalsIgnoreCase("<end of help>"))
                    {
                        System.out.println("\n\nEnd of Help File.");
                        break;
                    }
                    else if(p.startsWith("#"))
                        continue;
                    
                    System.out.println(p);
                }
            }

            //After reading the file, close the streams opened.
            ob.close();
        }
        console.readLine("Press ENTER to continue.");
        new Truncheon.API.BuildInfo().versionViewer();
    }

}
