package Truncheon.API.Wraith;

import java.io.Console;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;

public final class ReadFile
{
    File file = null;

    public void ShowHelp(String helpFile)throws Exception
    {
        try
        {
            file=new File("./Information/"+helpFile);
            readFile();
            return;
        }
        catch(Exception E)
        {
            E.printStackTrace();
        }
    }

    public void readUserFile(String dir)throws Exception
    {
        try
        {
            Console console = System.console();
            file = new File( dir + console.readLine("Enter the name of the file to be read: "));
            readFile();
            return;
        }
        catch(Exception E)
        {
            E.printStackTrace();
        }
    }

    private final void readFile() throws Exception
    {
        //A link to show the build info to the user's terminal
        new Truncheon.API.BuildInfo().versionViewer();

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

            //Logic to read the file line by line.
            while ((p = ob.readLine()) != null)
                System.out.println(p);

            //After reading the file, close the streams opened.
            ob.close();
        }
        System.out.println("Press Enter to Continue.");
        System.in.read();
        return;
    }

}
