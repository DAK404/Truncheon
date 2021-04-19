package Truncheon.API;

import java.util.*;
import java.text.*;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.FileWriter;

public final class Logger 
{
    public final void Log(String PrintToFile, String FileName) throws Exception 
    {
        try
        {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            System.gc();
            BufferedWriter obj = new BufferedWriter(new FileWriter("./" + FileName + ".log", true));
            PrintWriter pr = new PrintWriter(obj);
            pr.println(dateFormat.format(date) + ": " + PrintToFile);
            pr.close();
            obj.close();
        }
        catch(Exception E)
        {
            System.out.println("Cannot Write to file. Change the path or grant required permissions.");
            return;
        }
    }
}