package Truncheon.API;

//Scripting featureset

// COMMON COMPONENTS LIKE WAIT, INPUT, ECHO ARE HERE. NO REDUNDANCY
//ANY EXTRA FEATURES ARE PASSED BACK TO THE PROGRAM AND ARE PROCESSED BY THE CALLING PROGRAM
public class Anvil
{
    public static boolean anvilInterpreter(String command)throws Exception
    {
        boolean status = true;
        String[] commandArray = splitStringToArray(command);
        
        switch(commandArray[0].toLowerCase())
        {
            case "clear":
                BuildInfo.viewBuildInfo();
            break;

            case "echo":
                if(commandArray.length < 2)
                    IOStreams.println("echo <STRING> \n\nOR\n\necho \"<STRING_WITH_SPACES>\"");
                else
                {
                    try
                    {
                        IOStreams.println(commandArray[1]);
                    }
                    catch(Exception e)
                    {
                        IOStreams.printError("ANVIL : ERROR IN ECHO MODULE!");
                        e.printStackTrace();
                    }
                }
            break;

            case "about":
                BuildInfo.aboutProgram();
            break;

            case "wait":
                try
                {
                    if(Integer.parseInt(commandArray[1]) < 1)
                        IOStreams.println("This will make the prompt wait for a given number of milliseconds.\nSyntax: wait (1000)\n\nPrompt shall wait for 1 second.");
                    else
                        Thread.sleep(Integer.parseInt(commandArray[1]));
                }
                catch(NumberFormatException e)
                {
                    System.err.println("error! Please provide a numeric input for the wait timer!");
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            break;

            case "confirm":
                IOStreams.printInfo("Press ENTER to Continue...");
                System.in.read();
            break;

            default:
                status = false;
            break;
        }
        return status;
    }

    public static String[] splitStringToArray(String command)
    {
        return command.split(" (?=([^\"]*\"[^\"]*\")*[^\"]*$)");
    }
}
