/*

----- PROGRAM DOCUMENTATION -----

THIS PROGRAM IS UNDER DEVELOPMENT
AND SHOULD NOT BE CONSIDERED
RELEASE READY. FEATURES MAY BE
BROKEN OR INCOMPLETE. COMPILE AND
TEST AT YOUR OWN RISK.

---------------------------------

     --- Program Details ---

     Author  : DAK404
     Date    : 17-June-2021
     Version : 0.1.22

     -----------------------

*/


package Truncheon.Core;

//import Java IO packages and classes
import java.io.Console;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;

//Import the JDBC/SQL classes and packages
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Program to authenticate users and provide additional functionality.
 *
 * Provides a login interface and a main menu program to access additional functionality and the program features.
 *
 * @version 0.1.22
 * @since 0.0.3
 * @author DAK404
 */
public final class MainMenu
{

    /**
    * Sole constructor. (For invocation by subclass constructors, typically implicit.)
    */
    public MainMenu()
    {

    }

    /**
     * The following are the numeric datatypes in this program:
     *
     * count : A variable which will hold the number of attempts remaining for the login and unlock console functionality.
     */
    private byte _count = 5;

    private char _prompt = '*';

    /**
    * The following are the boolean datatype in this program:
    *
    * _scriptMode : Denotes to the interpreter that a script is already running.
    * _admin : denotes to the interpreter that the user who has logged in is an administrator or not.
    * _pseudo : denotes to the interpreter that if a module is run with the admin privileges.
    */
    private boolean _scriptMode = false;
    private boolean _admin = false;
    //private boolean pseudo = false;

    /**
     * The following are the String datatypes in this program.
     *
     * _username : Holds the username of the authenticated user.
     * _name : Holds the name of the account holder authenticated.
     * _PIN : holds the unlock PIN, required to unlock the console.
     * _scriptName : holds the name of the script being currently run.
     * _privilegeStatus : The string displayed for the type of user logged in.
     * _sysName : Loads the name of the system defined in the policy file.
     */
    private String _username="";
    private String _name = "";
    private String _PIN = "";
    private String _scriptName = "";
    private String _privilegeStatus = "Standard";
    private String _sysName = "SYSTEM";

    //Initialize the Console class for IO operations
    Console console=System.console();


    /**
     * Method which handles the login, information retrieval and the menu shell of the program
     *
     * @throws Exception : Handle general exceptions during thrown during runtime. : Handle general exceptions during thrown during runtime.
     */
    public final void mainMenuLogic()throws Exception
    {
        try
        {
            //By default, check the _count value and provive the user a chance to login to the program
            while(login() == false && _count <= 5 && _count > 0);
            /*
            * Retrieve the important information that the program requires.
            * See the method getUserDetails() for more information
            */
            getUserDetails();

            //set the system name using the PolicyEnforcement API
            _sysName = new Truncheon.API.Minotaur.PolicyEnforcement().retrivePolicyValue("sysname");
            System.gc();

            //pass control over to the menu shell
            menuShell();
        }
        catch(Exception E)
        {
            //Catch any exceptions thrown by the program
            new Truncheon.API.ErrorHandler().handleException(E);
        }
    }

    /**
     * The method will help in validate the credentials and provide a frontend for the login information to MainMenu program
     *
     * @return boolean : Returns the status of the login inputs.
     * @throws Exception : Handle general exceptions during thrown during runtime. : Handle general exceptions during thrown during runtime.
     */
    private final boolean login()throws Exception
    {
        //Initialize the login status to be false by default.
        boolean loginStatus = false;
        try
        {
            //Display the Program Information.
            new Truncheon.API.BuildInfo().versionViewer();

            //Display the number of login attempts remaining.
            System.out.println("Login Attempts Remaining: "+_count+"\n===========================\n");

            //Query the login details to check the credentials.
            loginStatus = challenge();

            //If the login stasus is false, pass it on to decrement the login counter.
            if(loginStatus==false)
                counterLogic();
            else
                _count = 5;
        }
        catch(Exception E)
        {
            new Truncheon.API.ErrorHandler().handleException(E);
        }

        System.gc();

        //Return the status back to the mainMenuLogic() method.
        return loginStatus;
    }

    /**
     * A reusable method which can be used for various login challenges.
     * 
     * @return boolean : Returns the LoginAPI return value for the provided username, password and the security key
     * @throws Exception : Handle general exceptions during thrown during runtime. : Handle general exceptions during thrown during runtime.
     */
    private final boolean challenge()throws Exception
    {
        //Accept the Username, Password and SecurityKey in a hashed SHA3-256 format.
        _username = new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(console.readLine("Username: "));
        String password = new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(String.valueOf(console.readPassword("Password: ")));
        String securityKey = new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(String.valueOf(console.readPassword("Security Key: ")));

        System.gc();
        return new Truncheon.API.Dragon.LoginAPI(_username, password, securityKey).status();
    }

    /**
     * The logic to handle the scipt files.
     *
     * @param fileName : The script file name to be processed by the interpreter.
     * @throws Exception : Handle general exceptions during thrown during runtime. : Handle general exceptions during thrown during runtime.
     */
    private final void scriptEngine(String fileName)throws Exception
    {
        try
        {
            //Verify is the script functionality is allowed by the Administrator policy
            if(new Truncheon.API.Minotaur.PolicyEnforcement().checkPolicy("script") == false)
            //bypass the policy if the user has the administrator permissions, either by pseudo or by account privileges
                if(_admin == false)
                    return;

            System.gc();

            //Check if the script file specifed exists.
            if(new File(fileName).exists() == false)
            {

                //Return an error and pass the control back in case the file is not found.
                System.out.println("[ ATTENTION ] : Script file "+fileName.replace(_username, _name)+" has not been found.\nPlease check the directory of the script file and try again.");
                return;
            }
            if(fileName.equalsIgnoreCase(""))
            {
                System.out.println("[ ERROR ] : The name of the script file cannot be be blank.");
                return;
            }

            //else begin executing the script.

            //Activate the script mode.
            _scriptMode = true;

            //Initialize a stream to read the given file.
            BufferedReader br = new BufferedReader(new FileReader(fileName));

            //Initialize a string to hold the contents of the script file being executed.
            String scriptLine;

            //Read the script file, line by line.
            while ((scriptLine = br.readLine()) != null)
            {
                //Check if the line is a comment or is blank in the script file and skip the line.
                if(scriptLine.toString().startsWith("#") || scriptLine.equalsIgnoreCase(""))
                    continue;

                //Check if End Script command is encountered, which will stop the execution of the script.
                else if(scriptLine.equalsIgnoreCase("End Script"))
                    break;

                //Read the command in the script file, and pass it on to menuLogic(<command>) for it to be processed.
                commandProcessor(scriptLine.toString());
            }

            //Close the streams, run the garbage collector and clean.
            br.close();
            System.gc();

            //Deactivate the script mode.
            _scriptMode = false;
        }
        catch(Exception E)
        {
            new Truncheon.API.ErrorHandler().handleException(E);
        }
    }


    /**
     * The method which will invoke the menuLogic()
     *
     * Implemented to wrap the menuLogic()
     * Intended shell output format is as follows
     * 
     * Standard Account:
     * user@SYSTEM*> _
     * 
     * Administrator Account:
     * Administrator@SYSTEM!> _
     *
     * @throws Exception : Handle general exceptions during thrown during runtime.
     */
    private final void menuShell()throws Exception
    {
        System.gc();

        mainMenuVerView();

        if(new File("./Users/Truncheon/" + _username + "/Scripts/Startup.shx").exists() == true)
            scriptEngine("./Users/Truncheon/" + _username + "/Scripts/Startup.shx");

        System.gc();
        //Execute the menuLogic() method.
        while(true)
            commandProcessor(console.readLine(_name + "@" + _sysName + _prompt + "> "));
    }

    /**
     * The implementation and the logic of the Truncheon shell.
     *
     * It is now made to be compatible with the ScriptEngine implementation.
     *
     * @param input : Accept the input from the menuShell() or the ScriptEngine.
     * @throws Exception : Handle general exceptions during thrown during runtime.
     */
    private final void commandProcessor(String input)throws Exception
    {
        try
        {
            System.gc();

            //String[] cmd = input.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

            /**
             * --- DEVELOPER NOTES ---
             *
             * The input string is split into an array,
             * and the string is split at the occurence
             * of a space character.
             *
             * Amy double qoutation marks found after
             * the command will be ignored by the
             * interpreter and will treat the content
             * inside the double quotes as a single
             * string.
             */

            //Split the inputs at the occurence of every space, and dont split strings within the quotes
            String[] cmd = input.split(" (?=([^\"]*\"[^\"]*\")*[^\"]*$)");

            //If the command length is greater than 1, then ignore the quotes.
            if(cmd.length > 1)
                cmd[1] = cmd[1].replaceAll("\"", "");

            //process the command in the String Array.
            switch(cmd[0].toLowerCase())
            {
                /**
                 * Wait Functionality.
                 *
                 * This will pause all execution for the given number of milliseconds.
                 * Functionality can be helpful in displaying a message before executing
                 * the next command.
                 */
                case "wait":

                //Check if the syntax of the command is correct.
                if(cmd.length <= 1)
                {
                    //Print the correct syntax and return the control back.
                    System.out.println("\nWait Syntax:\nwait <timeout>\nWhere timeout is the number of milliseconds for the interpreter to wait.\n\n");
                    return;
                }

                //Initialize the wait time as an int.
                int waitTime = 0;

                //Use a try-catch block to safeguard execution from invalid inputs.
                try
                {
                    //Parse the wait time from String format to Integer format
                    waitTime = Integer.parseInt(cmd[1]);

                    //Implement the wait functionality.
                    Thread.sleep(waitTime);
                }

                //Catch any invalid inouts provided for a number
                catch(NumberFormatException NE)
                {
                    //print the syntax and then return the control back.
                    System.out.println("\nWait Syntax:\nwait <timeout>\nWhere timeout is the number of milliseconds for the interpreter to wait.\n\n");
                    return;
                }
                break;

                /**
                 * Confirm functionality.
                 *
                 * This will wait for the user to press the return key before
                 * continuing. Useful for reading long documents.
                 */
                case "confirm":
                System.out.println("\n\nPress ENTER to continue..");
                console.readLine();
                break;

                /**
                 * Script functionality.
                 *
                 * This will provide the ability for the scripts to be loaded
                 * and executed. More development on this feature will continue
                 * soon.
                 */
                case "script":
                //Check for the correct script syntax.
                if(cmd.length <= 1)
                {
                    //Print the correct syntax if script syntax is malformed and return the program control.
                    System.out.println("\nScript Syntax:\n\nscript <script_name/path>\n");
                    return;
                }

                /**
                 *
                 * --- FOR FURTHER ANALYSIS AND TESTING ---
                 *
                 * DOCUMENTATION UNAVAILABLE.
                 */
                if(_scriptMode==true & _scriptName.equals(cmd[1]))
                {
                    System.out.println(_scriptName + " - Cannot Recursively Execute scripts.");
                    return;
                }
                else
                {
                    _scriptName = cmd[1];
                    scriptEngine("./Users/Truncheon/"+_username+"/"+cmd[1]+".shx");
                    _scriptName = "";
                }
                break;

                /**
                 * About Program functionality.
                 *
                 * NOTE: Uses the API Truncheon.API.BuildInfo
                 *
                 * Prints the program information.
                 */
                case "about":
                new Truncheon.API.BuildInfo().about();
                break;

                /**
                 * Exit functionality
                 *
                 * Exits the program, returns the exit code to the ProgramLauncher with status 0
                 */
                case "exit":
                System.exit(0);

                /**
                 * Restart functionality
                 *
                 * Restarts the program, returns the exit code to the ProgramLauncher with status 1
                 */
                case "restart":
                System.exit(1);

                /**
                 * Clear Screen functionality.
                 *
                 * NOTE: Uses the API Truncheon.API.BuildInfo
                 *
                 * Clears the screen and prints the basic kernel information.
                 */
                case "clear":
                mainMenuVerView();
                break;

                 /**
                 *
                 * --- FOR FURTHER ANALYSIS AND TESTING ---
                 *
                 * DOCUMENTATION UNAVAILABLE.
                 */
                case "?":
                case "help":
                new Truncheon.API.Wraith.ReadFile().showHelp("HelpDocuments/MainMenu.manual");
                break;

                /**
                 * Accepts the blank input and does nothing.
                 *
                 * Literally, do nothing since, nothing was done in the first place.
                 */
                case "":
                break;

                /**
                 * SysHell Functionality.
                 *
                 * Opens the native OS's shell allow the user to execute the native OS's commands.
                 */
                case "syshell":
                if(_admin == false)
                {
                    System.out.println("Cannot execute syshell command as a standard user.");
                    return;
                }
                if(System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd").inheritIO().start().waitFor();
                else
                new ProcessBuilder("/bin/bash").inheritIO().start().waitFor();
                break;

                /**
                 * Sys functionality
                 *
                 * executes the specified operation in the native OS's shell.
                 */
                case "sys":
                if(_admin == false)
                {
                    System.out.println("Cannot execute sys command as a standard user.");
                    return;
                }
                if(cmd.length < 2)
                {
                    System.out.println("Syntax:\n\nsys \"<host_OS_command>\"");
                    return;
                }
                if(System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", cmd[1]).inheritIO().start().waitFor();
                else
                new ProcessBuilder("/bin/bash", "-c" , cmd[1]).inheritIO().start().waitFor();
                break;

                /**
                 * Echo functionality.
                 *
                 * Prints the specified string on the screen.
                 */
                case "echo":
                if(cmd.length < 2)
                {
                    System.out.println("Echo Syntax: echo \"<string>\"");
                    return;
                }
                if(cmd[1].equalsIgnoreCase(null))
                System.out.println("null");
                else
                System.out.println(cmd[1]);
                break;

                /**
                 * Lock Console Functionality
                 *
                 * Will not allow any commands to be executed until the console can be unlocked.
                 * The user can lock the concole at their wish.
                 */
                case "lock":
                lockConsole();
                break;

                /**
                 * OTA Update functionality
                 * 
                 * Lets Truncheon to be updated to the latest release version available.
                 */
                case "update":
                new Truncheon.API.Wyvern.UpdateFrontEnd().updateLogic();
                break;

                /**
                 * File Management Tool, compatible with grinch file management scripts
                 * 
                 * Lets users manage their files in the user home directory
                 */
                case "grinch":
                if(cmd.length > 1)
                {
                    new Truncheon.API.Grinch.FileManager(_username, _name, _admin).fileManagerLogic(cmd[1]);
                    break;
                }
                new Truncheon.API.Grinch.FileManager(_username, _name, _admin).fileManagerLogic();
                break;

                /**
                 * Policy Configuration Tool
                 * 
                 * Provides a front-end for Administrators to configure policies.
                 */
                case "pconfig":
                new Truncheon.API.Minotaur.PolicyEditor().policyEditorLogic();
                break;

                /**
                 * Pseudo Functionality
                 * 
                 * Elevation of standard users to the Administrator privileges
                 */
                case "pseudo":
                pseudo();
                break;

                /*
                 * User Management System
                 * 
                 * Helps in the management of users.
                 */
                case "usermgmt":
                if(cmd.length < 2)
                {
                    System.out.println("Syntax:\n\nusermgmt <option>\n\nConsult the HELP file for more information.\n");
                    return;
                }
                switch(cmd[1].toLowerCase())
                {
                    case "add":
                        //add user functionality
                        new Truncheon.API.Dragon.AddUser(_username, _name, _admin).addUserLogic();
                        break;

                    case "delete":
                        new Truncheon.API.Dragon.DeleteUser(_username, _name).deleteUserLogic();
                        break;

                    case "modify":
                        new Truncheon.API.Dragon.ModifyAccount(_username, _name, _PIN, _admin).modifyAccountLogic();
                        break;

                    default:
                        System.out.println("Invalid option");
                }
                break;

                /**
                 * A default statement that will be executed when a command has not been found.
                 *
                 * Default condition? Check the script or inputs again!
                 */
                default:
                    System.out.println(input+" - Command not found.");
                    break;
            }

            System.gc();

            //Return the program control to the previous method to receive new inputs.
            return;
        }
        catch(Exception E)
        {
            new Truncheon.API.ErrorHandler().handleException(E);
        }
    }


    /**
     * Helper method to retrieve the user details from the database.
     *
     * This is executed right after login to retrieve the details.
     *
     * @throws Exception : Handle general exceptions during thrown during runtime.
     */
    private final void getUserDetails()throws Exception
    {
        try
        {
            //Retrieve the PIN, Name and admin status from the database and store it to the variables.
            _PIN  = retrieveInfo("SELECT PIN FROM FCAD WHERE Username = ? ;", "PIN");
            _name = retrieveInfo("SELECT Name FROM FCAD WHERE Username = ? ;", "Name");
            if( retrieveInfo("SELECT Administrator FROM FCAD WHERE Username = ? ;", "Administrator").equals("Yes") )
                elevateStatus();

            System.gc();
        }
        catch(Exception E)
        {
            E.printStackTrace();
        }
    }

    /**
     * A helper method which will help the program to retrieve the information from the database.
     *
     * This method is dependent on getUserDetails()
     *
     * @param command : The statement that needs to be executed to retrieve information from the database.
     * @param info : Specified the parameter that needs to be queried against the database such as Name, PIN, etc.
     * @return String : Returns the string containing the data fetched from the database.
     * @throws Exception : Handle general exceptions during thrown during runtime.
     */
    private final String retrieveInfo(String command, String info)throws Exception
    {
        String url = "jdbc:sqlite:./System/Private/Truncheon/mud.db";
        Connection conn = DriverManager.getConnection(url);
        PreparedStatement pstmt = conn.prepareStatement(command);
        pstmt.setString(1, _username);
        ResultSet rs = pstmt.executeQuery();

        String temp = rs.getString(info);

        rs.close();
        pstmt.close();
        conn.close();

        System.gc();
        return temp;
    }

    /**
     * Provides the implementation to lock the console.
     *
     * Separate method implemented to keep the code readable and modular.
     *
     * @throws Exception : Handle general exceptions during thrown during runtime.
     */
    private final void lockConsole()throws Exception
    {
        try
        {
            mainMenuVerView();
            while(! (console.readLine("AFK@IDLE> ").equalsIgnoreCase("unlock") ))
                mainMenuVerView();
            while(! (new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(String.valueOf(console.readPassword("\nPlase Authenticate with the Unlock PIN:\nPIN : "))).equals(_PIN)))
                counterLogic();
            new Truncheon.API.BuildInfo().versionViewer();

            return;
        }
        catch(Exception E)
        {
            new Truncheon.API.ErrorHandler().handleException(E);
        }
    }

    /**
     * Logic to handle the incorrect login attempts.
     *
     * Will lock the inputs if the attempts are wrong for a specified number of times.
     *
     * @throws Exception : Handle general exceptions during thrown during runtime.
     */
    private final void counterLogic()throws Exception
    {
        //Reduce the count by one for each failed attempt
        _count--;
        //Do not allow inputs after trying 5 times.
        if(_count <= 0)
        {
            //Block inputs by putting the thread to sleep
            System.out.println("\n\n[ ERROR ] : Too many requests.\nInputs blocked for 10 minutes.");
            Thread.sleep(600000);

            //Rearm the count and provide a single attempt to the user to try again.
            _count = 1;
            console.readLine("Attempt has been rearmed. Please try again.");
        }
    }

    /**
     * The implementation for Pseudo functionality.
     * 
     * @throws Exception : Handle general exceptions during thrown during runtime.
     */
    private final void pseudo()throws Exception
    {
        try
        {
            System.gc();

            //Check if the current user has the Administrator privileges.
            if(_admin == true)
            {
                //Do nothing if the user has administrator privileges.
                System.out.println("Unable to run Pseudo: Administrator privileges already available.");
                return;
            }
            else
            {
                //Ask for the user to enter administrator credentials to elevate status
                if(challenge() == true && retrieveInfo("SELECT Administrator FROM FCAD WHERE Username = ? ;", "Administrator").equals("Yes") )
                    elevateStatus();
            }
            System.gc();
        }
        catch(Exception E)
        {
            new Truncheon.API.ErrorHandler().handleException(E);
        }
    }

    /**
     * The implementation of elevating the suer status
     */
    private void elevateStatus()
    {
        _privilegeStatus = "Administrator";
        _admin = true;
        _prompt = '!';
    }

    /**
     * Main Menu version viewer
     * 
     * View the version of program with a set of extra strings
     *  
     * @throws Exception : Handle general exceptions during thrown during runtime.
     */
    private final void mainMenuVerView()throws Exception
    {
        System.gc();
        new Truncheon.API.BuildInfo().versionViewer();
        System.out.println("Account Type : " + _privilegeStatus + "\n");
        if(new File("./System/Private/Truncheon/Policy.burn").exists() == false)
            System.out.println("[ ATTENTION ] : POLICY FILE CORRUPT!\nPolicy File Reconfiguration Required!\n");
        System.out.println("[ HINT ] : Type \'HELP\' to get the contextual help.\n");
    }
}