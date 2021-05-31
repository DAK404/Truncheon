package Truncheon.Core;

import java.io.Console;
import java.io.BufferedReader;
import java.io.FileReader;

public final class MainMenu
{
    private byte count = 5;
    private boolean scriptMode = false;
    public final void mainMenu()throws Exception
    {
        try
        {
            //login();
            menuShell();
        }
        catch(Exception E)
        {

        }
    }

    private void scriptEngine(String fileName)throws Exception
    {
        scriptMode = true;
        BufferedReader br = new BufferedReader(new FileReader(fileName));
 
            String scriptLine;
            while ((scriptLine = br.readLine()) != null) 
            {
                if(scriptLine.toString().startsWith("#"))
                    continue;
                else if(scriptLine.equalsIgnoreCase("End Script"))
                    break;
                menuLogic(scriptLine.toString());
            }
            br.close();
        scriptMode = false;
    }

    private void menuShell()throws Exception
    {
        Console console=System.console();
        String userName = console.readLine("Enter the User name: ");
        String sysName = console.readLine("Enter the system name: ");
        while(true)
            menuLogic(console.readLine(userName+"@"+sysName+"> ").toLowerCase());
            
    }

    //modularize this code in such a way that the program accepts the string from either a file or a string
    private final void menuLogic(String input)
    {
        try
        {
            Console console=System.console();
            //String[] cmd = input.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            String[] cmd = input.split(" (?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            switch(cmd[0])
            {
                case "script":
                            if(scriptMode==true)
                            {
                                System.out.println("Cannot execute another script while running a script.");
                                return;
                            }
                            scriptEngine("./Trash/"+cmd[1] +".nScript");
                            break;

                case "exit":
                            System.exit(0);

                case "restart":
                            System.exit(1);

                case "clear":
                            new Truncheon.API.BuildInfo().versionViewer();
                            break;

                case "?":
                case "help":
                            System.out.println("Available options:\n");
                            System.out.println("exit\nrestart\nclear\nhelp\nfeatureX\n\nWhere X is the feature ID");
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

                case "feature6":
                            //Run system cmd from Truncheon
                            //pseudo?
                            new ProcessBuilder("cmd", "/c", cmd[1]).inheritIO().start().waitFor();
                            break;
                
                case "feature7":
                            new ProcessBuilder("cmd").inheritIO().start().waitFor();
                            break;

                case "feature8":
                            if(cmd[1].equalsIgnoreCase(null))
                                System.out.println("null");
                            else
                                System.out.println(cmd[1].replaceAll("\"", ""));
                            break;

                default:
                            System.out.println("Invalid Command.");
                            break;
            }
            return;
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