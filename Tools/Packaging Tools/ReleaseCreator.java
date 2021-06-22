import java.io.Console;

public class ReleaseCreator 
{
    private boolean encrypt = false;

    
    public static void main(String[] Args)throws Exception
    {
        new ReleaseCreator().logic();       
    }

    private void logic()
    {
        Console console=System.console();
        while(isTestBuild() == false);

    }

    private boolean isTestBuild()throws Exception
    {
        String a = console.readLine("Is this a test build? [Y/N]    ");
        if(a.equalsIgnoreCase("Y"))
            encrypt = true;
        else if(a.equalsIgnoreCase("N"))
            encrypt = false;
        else
            return false;
        return true;
    }

    private void createRelease()throws Exception
    {

    }

    private void checksumBuild()throws Exception
    {

    }

    private void checksumFiles()throws Exception
    {

    }

    
}
