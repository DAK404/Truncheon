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
                    case "exit":
                                System.exit(0);

                    case "restart":
                                System.exit(1);

                    case "clear":
                                new Truncheon.API.BuildInfo().versionViewer();
                                break;

                    case "help":
                                System.out.println("Work in Progress.");
                                break;

                    case "":
                                break;

                    case "feature1":
                                throw new Exception();

                    case "feature2":
                                System.out.println("SHA3-256 for Hello World! is : " + new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256("Hello World!"));
                                System.out.println("SHA3-256 for File ProgramLauncher.class is : " + new Truncheon.API.Minotaur.HAlgos().fileToSHA3_256("./ProgramLauncher.class"));

                                System.out.println("MD5 for Hello World! is : " + new Truncheon.API.Minotaur.HAlgos().stringToMD5("Hello World!"));
                                System.out.println("MD5 for File ProgramLauncher.class is : " + new Truncheon.API.Minotaur.HAlgos().fileToMD5("./ProgramLauncher.class"));
                                break;

                    case "feature3":
                                new Truncheon.API.Wraith.WriteFile().editFile("./Trash/");
                                break;

                    case "feature4":
                                new Truncheon.API.Wraith.ReadFile().readUserFile("./Trash/");
                                break;

                    case "feature5":
                                System.exit(900);

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

    /*private final void login()
    {
        try
        {

        }
        catch(Exception E)
        {

        }
    }*/
}