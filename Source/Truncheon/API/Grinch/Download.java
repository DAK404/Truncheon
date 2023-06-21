package Truncheon.API.Grinch;

//Import the required Java IO classes
import java.io.FileOutputStream;

//Import the required Java New IO classes
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

//Import the required Java Net classes
import java.net.URL;

import Truncheon.API.IOStreams;

/**
*
*/
public class Download
{
    private boolean policyStatus = false;
    /**
    *
    * @param URL
    * @param fileName
    * @return
    * @throws Exception : Handle exceptions thrown during program runtime.
    */

    public Download()throws Exception
    {
        policyStatus = new Truncheon.API.Minotaur.PolicyEnforce().checkPolicy("download");
    }

    public final boolean downloadFile(String URL, String fileName)throws Exception
    {
        boolean status = false;
        if(URL == null || fileName == null || URL.equalsIgnoreCase("")  || fileName.equalsIgnoreCase(""))
            IOStreams.printError("Invalid File Name. Enter a valid file name.");
        else if(policyStatus)
            status = downloadUsingNIO(URL, fileName);
        return status;
    }

    /**
    *
    * @return
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    public final boolean downloadUpdate()throws Exception
    {
        String updateFileURL = "https://github.com/DAK404/Truncheon/releases/download/TestBuilds/Truncheon.zip";
        IOStreams.printInfo("Downloading update file from : " + updateFileURL);
        //RELEASE BUILDS: return downloadUsingNIO("https://github.com/DAK404/Truncheon/releases/latest/download/Truncheon.zip", "Update.zip");
        return downloadUsingNIO(updateFileURL, "Update.zip");
    }

    /**
    *
    * @param urlStr
    * @param file
    * @return
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final boolean downloadUsingNIO(String urlStr, String file) throws Exception
    {
        boolean status = false;
        try
        {
            URL website = new URL(urlStr);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(file);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
            System.gc();
            status = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return status;
    }
}
