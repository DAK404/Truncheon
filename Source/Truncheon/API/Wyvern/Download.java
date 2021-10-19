package Truncheon.API.Wyvern;

//Import the required Java IO classes
import java.io.FileOutputStream;

//Import the required Java New IO classes
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

//Import the required Java Net classes
import java.net.URL;

/**
*
*/
public class Download
{
    /**
    *
    * @param URL
    * @param fileName
    * @return
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    public final boolean downloadFile(String URL, String fileName)throws Exception
    {
        try
        {
            if(! new Truncheon.API.Minotaur.PolicyEnforcement().checkPolicy("download"))
            return false;

            if(URL == null || fileName == null || URL.equalsIgnoreCase("")  || fileName.equalsIgnoreCase(""))
            {
                System.out.println("[ ERROR ] : Invalid File Name. Enter a valid file name.");
                return false;
            }

            return downloadUsingNIO(URL, fileName);
        }
        catch(Exception E)
        {
            //Handle any exceptions thrown during runtime
            new Truncheon.API.ErrorHandler().handleException(E);
        }
        return false;
    }

    /**
    *
    * @return
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    public final boolean downloadUpdate()throws Exception
    {
        try
        {
            System.out.println("Downloading update file from : https://gitreleases.dev/gh/DAK404/Truncheon/latest/Truncheon.zip");
            return downloadUsingNIO("https://gitreleases.dev/gh/DAK404/Truncheon/latest/Truncheon.zip", "Update.zip");
        }
        catch(Exception E)
        {
            //Handle any exceptions thrown during runtime
            new Truncheon.API.ErrorHandler().handleException(E);
        }
        return false;
    }

    /**
    *
    * @param urlStr
    * @param file
    * @return
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
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