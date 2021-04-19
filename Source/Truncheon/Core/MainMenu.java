package Truncheon.Core;

import java.io.Console;

public final class MainMenu
{
    byte count = 5;
    public final void mainMenu()throws Exception
    {
        try
        {
            //login();
            menu();
        }
        catch(Exception E)
        {

        }
    }

    private final void menu()
    {
        try
        {
            while(true)
            {
                Console console=System.console();
                switch(console.readLine("user@minimal> ").toLowerCase())
                {
                    case "exit": System.exit(0);

                    case "restart": System.exit(1);

                    case "clear":
                              new Truncheon.API.BuildInfo().versionViewer();
                              break;

                    case "":
                              break;

                    case "1": throw new Exception();

                    case "2": 
                              System.out.println("SHA for Hello World! is : " + new Truncheon.API.Minotaur.SHA().stringToSHA("Hello World!"));
                              System.out.println("SHA for File ProgramLauncher.class is : " + new Truncheon.API.Minotaur.SHA().fileToSHA("./ProgramLauncher.class"));
                              break;
                    
                    default:
                              System.out.println("Invalid Command.");
                              break;
                }                
            }
        }
        catch(Exception E)
        {
            new Truncheon.API.ErrorHandler().handleException(E);
        }
    }

    private final void login()
    {
        try
        {

        }
        catch(Exception E)
        {

        }
    }
}