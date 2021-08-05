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
            file=new File("./Information/Truncheon/"+helpFile);
            readFile(true);
            return;
        }
        catch(Exception E)
        {
            E.printStackTrace();
        }
    }

    public final void readUserFile(String fileName, String dir)throws Exception
    {
        try
        {
            if(new Truncheon.API.Minotaur.PolicyEnforcement().checkPolicy("read") == false)
                return;
            if(fileName.equals("") | fileName.equals(null))
            {
                System.out.println("Please enter a file name to open.");
                return;
            }
            file = new File( dir + fileName);
            readFile(false);
            return;
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
        if (file.exists() == false)
            System.out.println("[ ERROR ] : The specified file cannot be read, found or loaded.");

        //If the file exists, the file is displayed on the terminal.
        else
        {
            //Open the method to read files and read it
            BufferedReader ob = new BufferedReader(new FileReader(file));

            //Initialize the string to be null
            String p = "";

            if(helpMode == false)
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
                    else if(p.toString().startsWith("#"))
                        continue;
                    
                    System.out.println(p);
                }
            }

            //After reading the file, close the streams opened.
            ob.close();
        }
        console.readLine("Press ENTER to continue.");
        new Truncheon.API.BuildInfo().versionViewer();
        return;
    }

}
