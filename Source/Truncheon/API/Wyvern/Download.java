package Truncheon.API.Wyvern;

import java.io.Console;
import java.io.FileOutputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.net.URL;

public class Download
{
    Console console=System.console();

    public final boolean downloadFile(String URL, String fileName)throws Exception
    {
        try
        {
            if(URL.equalsIgnoreCase("") || fileName.equalsIgnoreCase("") || URL.equalsIgnoreCase(null) || fileName.equalsIgnoreCase(null))
            {
                System.out.println("[ ERROR ] : Invalid File Name. Enter a valid file name.");
                return false;
            }

            return downloadUsingNIO(URL, fileName);
        }
        catch(Exception E)
        {
            new Truncheon.API.ErrorHandler().handleException(E);
        }
        return false;
    }

    public final boolean downloadUpdate()throws Exception
    {
        try
        {
            System.out.println("Downloading update file from : https://gitreleases.dev/gh/DAK404/Truncheon/latest/Truncheon.zip");
            return downloadUsingNIO("https://gitreleases.dev/gh/DAK404/Truncheon/latest/Truncheon.zip", "Update.zip");
        }
        catch(Exception E)
        {
            new Truncheon.API.ErrorHandler().handleException(E);
        }
        return false;
    }

    private final boolean downloadUsingNIO(String urlStr, String file) throws Exception {
        try 
        {
            URL website = new URL(urlStr);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(file);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
            System.gc();
            return true;
        } 
        catch (Exception E) 
        {
            return false;
        }
    }
}