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
     Version : 0.1.14

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
 * @version 0.1.6
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
     */
    private String _username="";
    private String _name = "";
    private String _PIN = "";
    private String _scriptName = "";
    private String _privilegeStatus = "";
    
    
    Console console=System.console();
    
    
    /**
     * Method which handles the login and mainmenu logics.
     * 
     * @throws Exception : Handle general exceptions during thrown during runtime. : Handle general exceptions during thrown during runtime.
     */
    public final void mainMenuLogic()throws Exception
    {
        try
        {
            while(login() == false);
            getUserDetails();
            System.gc();
            menuShell();
        }
        catch(Exception E)
        {
            new Truncheon.API.ErrorHandler().handleException(E);
        }
    }
    
    /**
     * Logic to handle the login implementation.
     * 
     * @return boolean : Returns the status of the login inputs.
     * @throws Exception : Handle general exceptions during thrown during runtime. : Handle general exceptions during thrown during runtime.
     */
    private final boolean login()throws Exception
    {
        try
        {
            //Initialize the login status to be false by default.
            boolean loginStatus = false;

            //Display the Program Information.
            new Truncheon.API.BuildInfo().versionViewer();
            System.out.println("Login to Continue.\n");

            //Display the number of login attempts remaining.
            System.out.println("Login Attempts Remaining: "+_count+"\n");

            

            //Query the login details to check the credentials.
            loginStatus = challenge();

            //If the login stasus is false, pass it on to decrement the login counter.
            if(loginStatus==false)
                counterLogic();
            else
                _count = 5;

            System.gc();

            //Return the status back to the mainMenuLogic() method.
            return loginStatus;
            
        }
        catch(Exception E)
        {
            new Truncheon.API.ErrorHandler().handleException(E);
        }
        return false;
    }

    private final boolean challenge()
    {
        try
        {
            //Accept the Username, Password and SecurityKey in a hashed SHA3-256 format.
            _username = new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(console.readLine("Username: "));
            String password = new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(String.valueOf(console.readPassword("Password: ")));
            String securityKey = new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(String.valueOf(console.readPassword("Security Key: ")));

            System.gc();
            return new Truncheon.API.Dragon.LoginAPI(_username, password, securityKey).status();
        }
        catch(Exception E)
        {
            new Truncheon.API.ErrorHandler().handleException(E);
        }
        return false;
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
            System.gc();
            //Check if the script file specifed exists.
            if(new File(fileName).exists() == false)
            {
                //Return an error and pass the control back in case the file is not found.
                System.out.println("[ ATTENTION ] : Script file "+fileName.replace(_username, _name)+" has not been found.\nPlease check the directory of the script file and try again.");
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
                menuLogic(scriptLine.toString());
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
     * Implemented to wrap the menuLogic() and simplify the implementation
     * 
     * @throws Exception : Handle general exceptions during thrown during runtime.
     */
    private final void menuShell()throws Exception
    {
        //Display the Program Information.
        new Truncheon.API.BuildInfo().versionViewer();
        
        System.gc();

        //Display if the user is an Administrator or not.
        System.out.println("User Privilege : " + _privilegeStatus);

        //Execute the menuLogic() method.
        while(true)
            menuLogic(console.readLine(_name + "@SYSTEM" + _prompt + "> "));
    }
    
    /**
     * The implementation and the logic of the Truncheon shell.
     * 
     * It is now made to be compatible with the ScriptEngine implementation.
     * 
     * @param input : Accept the input from the menuShell() or the ScriptEngine.
     * @throws Exception : Handle general exceptions during thrown during runtime.
     */
    private final void menuLogic(String input)throws Exception
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

            //Split the inputs at the occurence of every space.
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

                if(new Truncheon.API.Minotaur.PolicyEnforcement().checkPolicy("script") == false)
                {
                    return;
                }
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
                    scriptEngine("./Users/"+_username+"/"+cmd[1]+".nScript");
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
                new Truncheon.API.BuildInfo().versionViewer();
                System.out.println("Administrator : " + _privilegeStatus);
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

                if(challenge() == false)
                {
                    System.out.println("User Authentication failed. Cannot execute command \"syshell\"");
                    return;
                }
                if(System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd").inheritIO().start().waitFor();
                else
                new ProcessBuilder("/bin/bash", "-c" , cmd[1]).inheritIO().start().waitFor();
                break;
                
                /**
                 * Sys functionality
                 * 
                 * executes the specified operation in the native OS's shell.
                 */
                case "sys":
                if(cmd.length < 2)
                {
                    System.out.println("Syntax\n\nsys <host_OS_command>");
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
                 * 
                 */
                case "update":
                new Truncheon.API.Wyvern.UpdateFrontEnd().updateLogic();
                break;

                /**
                 * 
                 */
                case "grinch":
                new Truncheon.API.Grinch.FileManager(_username, _name).fileManagerLogic();
                break;

                /**
                 * 
                 */
                case "pconfig":
                new Truncheon.API.Minotaur.PolicyEditor().policyEditorLogic();
                break;

                /**
                 * 
                 */
                case "pseudo":
                pseudo();
                break;

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
            _privilegeStatus = "Standard User";
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
            while(! (console.readLine("AFK@IDLE> ").equalsIgnoreCase("unlock") ));
            while(! (new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(console.readLine("Plase Authenticate with the Unlock PIN:\nPIN : ")).equals(_PIN)))
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
        _count--;
        if(_count <= 0)
        {
            System.out.println("\n\n[ ERROR ] : Too many requests. Inputs blocked for 10 minutes.");
            Thread.sleep(600000);
            _count = 1;
            console.readLine("Attempt has been rearmed. Please try again.");
        }
    }

    private final void pseudo()
    {
        try
        {
            System.gc();
            if(_admin == true)
            {
                System.out.println("Unable to run Pseudo: Administrator privileges already available.");
                return;
            }
            else
            {
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

    private void elevateStatus()
    {
        _privilegeStatus = "Administrator";
        _admin = true;
        _prompt = '!';
    }
}