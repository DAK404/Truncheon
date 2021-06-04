package Truncheon.Core;

import java.io.Console;
import java.io.BufferedReader;
import java.io.FileReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public final class MainMenu
{
    private byte count = 5;
    private boolean scriptMode = false;

    private String username="";
    private String name = "";
    private String PIN = "";
    private boolean admin = false;
    //private boolean pseudo = false;

    Console console=System.console();

    public final void mainMenuLogic()throws Exception
    {
        try
        {
            //login();
            while(login() == false);
            getUserDetails();
            menuShell();

        }
        catch(Exception E)
        {

        }
    }

    private final boolean login()throws Exception
    {
        try
        {
            boolean loginStatus = false;
            new Truncheon.API.BuildInfo().versionViewer();
            System.out.println("Login to Continue.\n");
            System.out.println("Login Attempts Remaining: "+count+"\n");
            username = new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(console.readLine("Username: "));
            String password = new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(String.valueOf(console.readPassword("Password: ")));
            String securityKey = new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(String.valueOf(console.readPassword("Security Key: ")));
            loginStatus = new Truncheon.API.Dragon.LoginAPI(username, password, securityKey).status();
            if(loginStatus==false)
            {
                count--;
                if(count <= 0)
                {
                    System.out.println("\n\n[ ERROR ] : Too many requests. Inputs blocked for 10 minutes.");
                    Thread.sleep(600000);
                    count = 1;
                    console.readLine("Login attempt has been rearmed.\nIf you want to quit, press the CTRL + C keys, or press enter to continue.");
                }
            }
            password = "";
            securityKey = "";
            return loginStatus;
            
        }
        catch(Exception E)
        {
            new Truncheon.API.ErrorHandler().handleException(E);
        }
        return false;
    }

    private final void menuShell()throws Exception
    {
        //Console console=System.console();
        /*while(true)
            menuLogic(console.readLine(name+"@SYSTEM> ").toLowerCase());*/

        System.out.println(name+"@SYSTEM> ");
        System.out.println("PIN : " + PIN);
        System.out.println("Username : " + username);
        System.out.println("Administrator : " + admin);
            
    }

    private final void getUserDetails()throws Exception
    {
        
        PIN  = retrieveInfo("SELECT PIN FROM FCAD WHERE Username = ? ;", "PIN");
        name = retrieveInfo("SELECT Name FROM FCAD WHERE Username = ? ;", "Name");
        if( retrieveInfo("SELECT Administrator FROM FCAD WHERE Username = ? ;", "Administrator").equals("Yes") )
            admin=true;
        System.gc();
        System.out.println("AAAAA");
    }

    private final String retrieveInfo(String command, String info)throws Exception
    {
        System.out.println("BBBBB");
        String url = "jdbc:sqlite:./System/Private/Truncheon/mud.db";
        Connection conn = DriverManager.getConnection(url);

        PreparedStatement pstmt = conn.prepareStatement(command);
		pstmt.setString(1, username);
        ResultSet rs = pstmt.executeQuery();
		
        String temp = rs.getString(info);

        rs.close();
        pstmt.close();
        conn.close();

        System.gc();
        return temp;
    }
}

    /*private final void pseudoLogic()throws Exception
    {
        try
        {
            System.out.println("Please login Administrator credentials to continue...");
            //Logic for the login part of the program.
        }
        catch(Exception E)
        {
            new Truncheon.API.ErrorHandler().handleException(E);
        }
    }*/

    /*private final void scriptEngine(String fileName)throws Exception
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

    

    private final void menuLogic(String input)
    {
        try
        {
            //String[] cmd = input.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            String[] cmd = input.split(" (?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            if(cmd.length > 1)
                cmd[1] = cmd[1].replaceAll("\"", "");
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
                            System.out.println("\nContextual Help\n- Script : Run a script in Truncheon Shell\n- Exit : Exit the program\n- Restart : Restart the program\n- Clear : Clear the screen\n- Help : Open this contextual Help\n- FeatureX : run a feature with X being the ID of the feature");
                            break;

                case "":
                            break;

                case "feature11":
                            throw new Exception();

                case "feature1":
                            System.out.println("SHA3-256 for Hello World! is : " + new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256("Hello World!"));
                            System.out.println("SHA3-256 for File ProgramLauncher.class is : " + new Truncheon.API.Minotaur.HAlgos().fileToSHA3_256("./ProgramLauncher.class"));

                            System.out.println("MD5 for Hello World! is : " + new Truncheon.API.Minotaur.HAlgos().stringToMD5("Hello World!"));
                            System.out.println("MD5 for File ProgramLauncher.class is : " + new Truncheon.API.Minotaur.HAlgos().fileToMD5("./ProgramLauncher.class"));
                            break;

                case "feature2":
                            new Truncheon.API.Wraith.WriteFile().editFile("./Trash/");
                            break;

                case "feature3":
                            new Truncheon.API.Wraith.ReadFile().readUserFile("./Trash/");
                            break;

                case "feature12":
                            System.exit(900);

                case "sys":
                case "feature4":
                            //Run system cmd from Truncheon
                            //pseudo?
                            new ProcessBuilder("cmd", "/c", cmd[1]).inheritIO().start().waitFor();
                            break;
                
                case "syshell":
                case "feature5":
                            new ProcessBuilder("cmd").inheritIO().start().waitFor();
                            break;

                case "echo":
                case "feature6":
                            if(cmd[1].equalsIgnoreCase(null))
                                System.out.println("null");
                            else
                                System.out.println(cmd[1]);
                            break;

                case "setup":
                            new Truncheon.Core.Setup().setupLogic();
                            break;

                case "lock console":
                            while(console.readLine("AFK@IDLE> ").equalsIgnoreCase("unlock console"));
                            break;

                case "login":
                            if(login()==false)
                                System.out.println("Login Failed");
                            else
                                System.out.println("Login success");
                            break;


                default:
                            System.out.println(input+" - Command not found.");
                            break;
            }
            return;
        }
        catch(Exception E)
        {
            new Truncheon.API.ErrorHandler().handleException(E);
        }
    }

    private final boolean login()throws Exception
    {
        try
        {
            boolean loginStatus = false;
            new Truncheon.API.BuildInfo().versionViewer();
            System.out.println("Login to Continue.\n");
            System.out.println("Login Attempts Remaining: "+count+"\n\n");
            String Username = new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(console.readLine("Username: "));
            String Password = new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(String.valueOf(console.readPassword("Password: ")));
            String SecurityKey = new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(String.valueOf(console.readPassword("Security Key: ")));
            loginStatus = new Truncheon.API.Dragon.LoginAPI(Username, Password, SecurityKey).status();
            if(loginStatus==false)
            {
                count--;
                if(count <= 0)
                {
                    System.out.println("\n\n[ ERROR ] : Too many requests. Inputs blocked for 10 minutes.");
                    Thread.sleep(600000);
                    count = 1;
                    console.readLine("Login attempt has been rearmed.\nIf you want to quit, press the CTRL + C keys, or press enter to continue.");
                }
            }
            return loginStatus;
            
        }
        catch(Exception E)
        {
            new Truncheon.API.ErrorHandler().handleException(E);
        }
        return false;
    }
}
*/

