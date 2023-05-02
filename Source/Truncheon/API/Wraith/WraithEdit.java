package Truncheon.API.Wraith;

//Import the required Java IO classes
import java.io.BufferedWriter;
//import java.io.Console;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileWriter;

//Import the required Java Util classes
import java.util.Date;

import Truncheon.API.IOStreams;

//Import the required Java Text Formatting classes
import java.text.SimpleDateFormat;
import java.text.DateFormat;

public class WraithEdit
{
    //private Console console = System.console();

    public static final void logger(String printToFile, String fileName)
    {
        try
        {
            String logfilePath = "./System/Truncheon/Public/Logs/";
            if(checkFileValidity(fileName))
            {
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = new Date();

                logfilePath = (new File(logfilePath).exists()?logfilePath:"./Logs/Truncheon/");

                File logFile = new File(logfilePath);
                if(! logFile.exists())
                    logFile.mkdirs();

                BufferedWriter obj = new BufferedWriter(new FileWriter(logfilePath + fileName + ".log", true));
                PrintWriter pr = new PrintWriter(obj);
                pr.println(dateFormat.format(date) + ": " + printToFile);
                pr.close();
                obj.close();
            }
            else
            {
                System.out.println(fileName);
                IOStreams.printError("The provided file name is invalid. Please provide a valid file name to continue.");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        System.gc();
    }

    private static final boolean checkFileValidity(String fileName)throws Exception
    {
        return !(fileName == null || fileName.equals("") || fileName.startsWith(" "));
    }
}
