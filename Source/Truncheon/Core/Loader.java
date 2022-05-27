package Truncheon.Core;

import java.io.Console;
import Truncheon.API.IOStreams;
import Truncheon.API.BuildInfo;
public class Loader 
{
    public static void main(String[] args)throws Exception
    {
        switch(args[0].toLowerCase())
        {
            case "normal":
            new Loader().PLACEHOLDER_ENTRY_POINT();
            break;
            
            case "debug":
                Console console = System.console();
                String debugParam = console.readLine(">");
                switch(debugParam.toLowerCase())
                {
                    case "io":
                        new Loader()._debugPrintStreams();
                        break;

                    case "buildinfo":
                        System.out.println("Testing Build Info...");
                        BuildInfo.viewBuildInfo();
                        System.out.println("Press RETURN to clear screen...");
                        System.in.read();
                        BuildInfo.clearScreen();
                        break;
                    
                    default:
                        System.out.println("INVALID DEBUG ARGUMENT. QUITTING...");
                        break;
                }
            break;
        }        
    }

    private void _debugPrintStreams()
    {
        IOStreams.println("Hello World!");
        IOStreams.printInfo("THIS IS AN INFORMATION STRING");
        IOStreams.printWarning("THIS IS A WARNING STRING");
        IOStreams.printAttention("THIS IS AN ATTENTION STRING");
        IOStreams.printError("THIS IS AN ERROR STRING");
    }

    private void PLACEHOLDER_ENTRY_POINT()
    {
        BuildInfo.viewBuildInfo();

        IOStreams.println("DEFAULT OUTPUT");
        IOStreams.printInfo("DEFAULT INFO");
        IOStreams.printWarning("DEFAULT WARNING");
        IOStreams.printAttention("DEFAULT ATTENTION");
        IOStreams.printError("DEFAULT ERROR");

        System.out.println("End Of Demo.");
    }
}
