package Truncheon.Core;

import java.io.Console;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;

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
            while(login() == false);
            getUserDetails();
            menuShell();
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
            System.out.println("Login Attempts Remaining: "+count+"\n");
            username = new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(console.readLine("Username: "));
            String password = new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(String.valueOf(console.readPassword("Password: ")));
            String securityKey = new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(String.valueOf(console.readPassword("Security Key: ")));
            loginStatus = new Truncheon.API.Dragon.LoginAPI(username, password, securityKey).status();
            if(loginStatus==false)
                counterLogic();
            password = "";
            securityKey = "";
            count = 5;
            return loginStatus;
            
        }
        catch(Exception E)
        {
            new Truncheon.API.ErrorHandler().handleException(E);
        }
        return false;
    }

    private final void scriptEngine(String fileName)throws Exception
    {
        try
        {
            if(new File(fileName).exists() == false)
            {
                System.out.println("[ ATTENTION ] : Script file "+fileName+".nScript has not been found.\nPlease check the directory of the script file and try again.");
                return;
            }
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
        catch(Exception E)
        {
            new Truncheon.API.ErrorHandler().handleException(E);
        }
    }

    private final void menuShell()throws Exception
    {
        new Truncheon.API.BuildInfo().versionViewer();
        System.out.println("Administrator : " + admin);
        while(true)
            menuLogic(console.readLine(name+"@SYSTEM> ").toLowerCase());
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
                //Implementation of the script functionality
                case "script":
                    if(cmd.length <= 1)
                    {
                        System.out.println("\nScript Syntax:\nscript <script_name/path>\n");
                        return;
                    }
                    if(scriptMode==true)
                    {
                        System.out.println("Cannot execute another script while running a script.");
                        return;
                    }
                    scriptEngine("./Users/"+username+"/"+cmd[1] +".nScript");
                    break;

                case "about":
                    new Truncheon.API.BuildInfo().about();
                    break;

                //Exits the program at the user's command
                case "exit":
                    System.exit(0);

                //Restarts the program
                case "restart":
                    System.exit(1);

                //clears the screen
                case "clear":
                    new Truncheon.API.BuildInfo().versionViewer();
                    break;

                //Invokes the help file... Right now its the help menu
                case "?":
                case "help":
                    System.out.println("Available options:\n");
                    System.out.println("\nContextual Help\n- Script : Run a script in Truncheon Shell\n- Exit : Exit the program\n- Restart : Restart the program\n- Clear : Clear the screen\n- Help : Open this contextual Help\n- FeatureX : run a feature with X being the ID of the feature");
                    break;

                //Ignore any empty inputs and pass it back to the shell
                case "":
                    break;
                
                //Invokes the system shell while running Truncheon
                case "syshell":
                    if(System.getProperty("os.name").contains("Windows"))
                        new ProcessBuilder("cmd").inheritIO().start().waitFor();
                    else
                        new ProcessBuilder("/bin/bash", "-c" , cmd[1]).inheritIO().start().waitFor();
                    break;

                //Runs a system command within Truncheon Shell
                case "sys":
                    if(System.getProperty("os.name").contains("Windows"))
                        new ProcessBuilder("cmd", "/c", cmd[1]).inheritIO().start().waitFor();
                    else
                        new ProcessBuilder("/bin/bash", "-c" , cmd[1]).inheritIO().start().waitFor();
                    break;

                //Prints a line onto the terminal, used mostly in scripting stuff
                case "echo":
                    if(cmd[1].equalsIgnoreCase(null))
                        System.out.println("null");
                    else
                        System.out.println(cmd[1]);
                    break;

                //Implement the Lock console functionality
                case "lock":
                    lockConsole();
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

    private final void getUserDetails()throws Exception
    {
        
        PIN  = retrieveInfo("SELECT PIN FROM FCAD WHERE Username = ? ;", "PIN");
        name = retrieveInfo("SELECT Name FROM FCAD WHERE Username = ? ;", "Name");
        if( retrieveInfo("SELECT Administrator FROM FCAD WHERE Username = ? ;", "Administrator").equals("Yes") )
            admin=true;
        System.gc();
    }

    private final String retrieveInfo(String command, String info)throws Exception
    {
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

    private final void lockConsole()
    {
        try
        {
            while(! (console.readLine("AFK@IDLE> ").equalsIgnoreCase("unlock console") ));
            while(! (new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(console.readLine("Plase Authenticate with the Unlock PIN:\nPIN : ")).equals(PIN)))
            {
                counterLogic();
            }
            new Truncheon.API.BuildInfo().versionViewer();

            return;
        }
        catch(Exception E)
        {
            new Truncheon.API.ErrorHandler().handleException(E);
        }
    }

    private void counterLogic()throws Exception
    {
        count--;
        if(count <= 0)
        {
            System.out.println("\n\n[ ERROR ] : Too many requests. Inputs blocked for 10 minutes.");
            Thread.sleep(600000);
            count = 1;
            console.readLine("Attempt has been rearmed. Please try again.");
        }
    }
}