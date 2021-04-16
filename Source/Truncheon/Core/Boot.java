package Truncheon.Core;

import java.io.Console;

public class Boot
{
    public static void main(String[] Args)throws Exception
    {
        try
        {
            Console console=System.console();
            new Truncheon.API.BuildInfo().versionViewer();
            System.out.println("Truncheon has booted in "+Args[0]+" mode");
            console.readLine();
        }
        catch(Exception E)
        {
            //Error handling API
        }
    }
}
