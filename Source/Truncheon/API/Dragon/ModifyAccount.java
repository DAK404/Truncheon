/*
*    ███    ██ ██  ██████  ███    ██        ████████ ██████  ██    ██ ███    ██  ██████ ██   ██ ███████  ██████  ███    ██
*    ████   ██ ██ ██    ██ ████   ██ ██        ██    ██   ██ ██    ██ ████   ██ ██      ██   ██ ██      ██    ██ ████   ██
*    ██ ██  ██ ██ ██    ██ ██ ██  ██           ██    ██████  ██    ██ ██ ██  ██ ██      ███████ █████   ██    ██ ██ ██  ██
*    ██  ██ ██ ██ ██    ██ ██  ██ ██ ██        ██    ██   ██ ██    ██ ██  ██ ██ ██      ██   ██ ██      ██    ██ ██  ██ ██
*    ██   ████ ██  ██████  ██   ████           ██    ██   ██  ██████  ██   ████  ██████ ██   ██ ███████  ██████  ██   ████
*/

/*
* ---------------!DISCLAIMER!--------------- *
*                                            *
*         THIS CODE IS RELEASE READY         *
*                                            *
*  THIS CODE HAS BEEN CHECKED, REVIEWED AND  *
*   TESTED. THIS CODE HAS NO KNOWN ISSUES.   *
*    PLEASE REPORT OR OPEN A NEW ISSUE ON    *
*     GITHUB IF YOU FIND ANY PROBLEMS OR     *
*              ERRORS IN THE CODE.           *
*                                            *
*   THIS CODE FALLS UNDER THE LGPL LICENSE.  *
*    YOU MUST INCLUDE THIS DISCLAIMER WHEN   *
*        DISTRIBUTING THE SOURCE CODE.       *
*   (SEE LICENSE FILE FOR MORE INFORMATION)  *
*                                            *
* ------------------------------------------ *
*/

package Truncheon.API.Dragon;

//Import the required Java IO classes
import java.io.Console;

//Import the required Java SQL classes
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
* Program to modify user credentials.
*
* Provides an interface to Administrators to either promote or demote a user status.
*
* @version 0.3.42
* @since 0.1.0
* @author DAK404
*/
public final class ModifyAccount
{
    /**
    * -----------------------------------
    * |        STRING VARIABLES         |
    * -----------------------------------
    *
    * String Variables required by the program.
    * These are dependent to perform program operations.
    */

    /**
    * Variable to store the username
    */
    private String _user = "";

    /**
    * Variable to store the user account name
    */
    private String _name = "";

    /**
    * Variable to store the new password
    */
    private String _password = "";

    /**
    * Variable to store the new Security Key
    */
    private String _key = "";

    /**
    * Variable to store the new PIN value
    */
    private String _pin = "";

    /**
    * Variable to store the administrator privileges
    */
    private boolean _admin = false;

    /**
    * Initialize the console class for Input operations
    */
    private Console console = System.console();

    /**
    * Constructor which will initialize the username, name and the administrator rights.
    *
    * @param User : Receive the username from the program
    * @param Name : Receive the account name from the program
    */
    public ModifyAccount(String User, String Name)
    {
        _user = User;
        _name = Name;
    }

    /**
    * The logic used by the program to modify the account credentials
    *
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    public final void modifyAccountLogic()throws Exception
    {
        System.gc();
        if(! authenticateUser())
        {
            System.out.println("Incorrect Credentials. Access Denied.");
            return;
        }
        initPrivileges();
        while(modifyAccountMenu());
    }

    /**
    * Authenticates the user by using the LoginAPI and returns the success
    * 
    * @return boolean : The result of the user authentication challenge.
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final boolean authenticateUser()throws Exception
    {
        new Truncheon.API.BuildInfo().versionViewer();
        System.out.println("[ ATTENTION ] : Please authenticate credentials before modifying account details.");
        System.out.println("Username: "+_name);
        String currentPassword=new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(String.valueOf(console.readPassword("Password: ")));
        String currentKey=new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(String.valueOf(console.readPassword("Security Key: ")));
        return new Truncheon.API.Dragon.LoginAPI(_user, currentPassword, currentKey).status();
    }

    /**
    * Begins to check the Administrator privileges from the database and retrieves the PIN
    *
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final void initPrivileges()throws Exception
    {
        try
        {
            _pin = retrieveInfo("SELECT PIN FROM FCAD WHERE Username = ? ;", "PIN");
            if( retrieveInfo("SELECT Administrator FROM FCAD WHERE Username = ? ;", "Administrator").equals("Yes") )
                _admin = true;
        }
        catch(Exception E)
        {
            //Handle any exceptions thrown during runtime
            new Truncheon.API.ErrorHandler().handleException(E);
        }
    }

    /**
    * A helper method which will help the program to retrieve the information from the database.
    *
    * @param command : The statement that needs to be executed to retrieve information from the database.
    * @param info : Specified the parameter that needs to be queried against the database such as Name, PIN, etc.
    * @return String : Returns the string containing the data fetched from the database.
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final String retrieveInfo(String command, String info)throws Exception
    {
        String temp = "";
        try
        {
            //Initialize the database connection
            String url = "jdbc:sqlite:./System/Private/Truncheon/mud.db";
            Connection conn = DriverManager.getConnection(url);

            //Execute the statement to retrieve the criteria specified by command (the SQL command) and info (the name of the column)
            PreparedStatement pstmt = conn.prepareStatement(command);
            pstmt.setString(1, _user);

            //Store the result of the query in the resultset
            ResultSet rs = pstmt.executeQuery();

            //Store the value of the result after retrieving the value of the query
            temp = rs.getString(info);

            //close connections and cleanup memory space
            rs.close();
            pstmt.close();
            conn.close();

            System.gc();

            //Return the result in the string format
        }
        catch(Exception E)
        {
            //Handle any exceptions thrown during runtime
            new Truncheon.API.ErrorHandler().handleException(E);
        }
        return temp;
    }

    /**
    * The menu logic to handle the account details modification
    *
    * @return boolean : To indicate if the account modification program needs to stop
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final boolean modifyAccountMenu()throws Exception
    {
        //Clear the screen and display the program information
        new Truncheon.API.BuildInfo().versionViewer();
        System.out.println("User Credential Modification Dashboard 1.0");
        System.out.println("------------------------------------------");
        System.out.println("\nEnter the credential parameter to change value\n");
        System.out.println("* Open Help");
        System.out.println("* User Account Password");
        System.out.println("* User Account Security Key");
        System.out.println("* User Account PIN");
        System.out.println("* Exit\n");
        System.out.println("------------------------------------------");
        System.out.println("[ HELP | PSW | KEY | PIN | EXIT ]");
        System.out.println("------------------------------------------");
        //Display the options for an administrator account to further modify the other account parameters
        if(_admin)
        {
            System.out.println("\nAdministrator rights detected.\n");
            System.out.println("------------------------------------------");
            System.out.println(" ADMINISTRATOR ACCOUNT MANAGEMENT TOOLKIT ");
            System.out.println("------------------------------------------");
            System.out.println("\nEnter the account parameter to change value\n");
            System.out.println("! Promote an Account");
            System.out.println("! Demote an Account\n");
            System.out.println("------------------------------------------");
            System.out.println("[ PROMOTE | DEMOTE ]");
            System.out.println("------------------------------------------");
        }
        System.out.println();

        switch(console.readLine(_name+"} ").toLowerCase())
        {
            case "":
            break;

            case "?":
            case "help":
            new Truncheon.API.Wraith.ReadFile().showHelp("HelpDocuments/ModifyAccount.manual");
            break;

            case "psw":
            while(! getPassword());
            updateValues("Password", _password, _user);
            break;

            case "key":
            while(! getKey());
            updateValues("SecurityKey", _key, _user);
            break;

            case "pin":
            while(! getPIN());
            updateValues("PIN", _pin, _user);
            break;

            case "promote":
            userStatusChange("promote");
            break;

            case "demote":
            userStatusChange("demote");
            break;

            case "exit":
            return false;

            default:
            System.out.println("Invalid option. Please try again.");
            break;
        }
        return true;
    }

    /**
    *
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final void displayDetails()throws Exception
    {
        new Truncheon.API.BuildInfo().versionViewer();
        System.gc();
        System.out.println("===================");
        System.out.println("- Account Details -");
        System.out.println("===================\n");
        System.out.println("Administrator Account: " + _admin);
        System.out.println("Account Name : " + _name);
        System.out.println("Username     : " + _user);
        System.out.println("\n===================");
    }

    /**
    *
    * @return
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final boolean getPassword()throws Exception
    {
        displayDetails();
        System.out.println("\nPassword Policy\n");
        System.out.println("* Password must be atleast 8 characters long.");
        System.out.println("* Password must be the same as the password confirmation\n");
        _password  = String.valueOf(console.readPassword("\nAccount Password : "));
        String C_password = String.valueOf(console.readPassword("Confirm Password : "));
        if(_password.length() < 8 | ! ( _password.equals(C_password)) )
        {
            _password="";
            console.readLine("Password Policy not followed. Please try again which follows the Password Policy.");
            return false;
        }
        C_password = "";
        _password  = new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(_password);
        return true;
    }

    /**
    * Method to receive the account Security _key.
    *
    * @throws Exception : Handle exceptions thrown during program runtime. :  Throws any exception caught during runtime/execution
    */
    private final boolean getKey()throws Exception
    {
        displayDetails();
        System.out.println("\nSecurity _key Policy\n");
        System.out.println("* Security _key must be the same as the Security _key confirmation\n");
        _key  = String.valueOf(console.readPassword("\nSecurity _key : "));
        String C_key = String.valueOf(console.readPassword("Confirm _key  : "));
        if(! _key.equals(C_key))
        {
            _key="";
            console.readLine("Security _key Policy not followed. Please try again which follows the Security _key Policy.");
            return false;
        }
        C_key = "";
        _key  = new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(_key);
        return true;
    }

    /**
    * Method to receive the account Unlock PIN.
    *
    * @throws Exception : Handle exceptions thrown during program runtime. :  Throws any exception caught during runtime/execution
    */
    private final boolean getPIN()throws Exception
    {
        displayDetails();
        System.out.println("\nPIN Policy\n");
        System.out.println("* PIN must be atleast 4 characters long.");
        System.out.println("* PIN must be the same as the PIN confirmation\n");
        _pin  = String.valueOf(console.readPassword("\nUnlock PIN   : "));
        String CPIN = String.valueOf(console.readPassword("Confirm PIN  : "));
        if(_pin.length() < 4 | ! ( _pin.equals(CPIN)))
        {
            _pin="";
            console.readLine("PIN Policy not followed. Please use a valid PIN and try again.");
            return false;
        }
        CPIN = "";
        _pin  = new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(_pin);
        return true;
    }

    /**
    * The logic to promote or demote an account.
    *
    * @param status : The status denoting if the user account is to be promoted or demoted
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private void userStatusChange(String status)throws Exception
    {
        try
        {
            //The functionality will not work if the account has a non administrator status
            if(! _admin)
            return;

            //Logic to either promote or demote the user.
            String user = console.readLine("Enter the name of the user to " + status + ": ");
            
            //Reject any attempts to promote or demote the user
            if(user.equalsIgnoreCase("Administrator"))
            {
                System.out.println("Cannot promote or demote the user Administrator.");
                return;
            }

            //Confirm if the user selected is to be promoted to or demoted from an account with administrator rights
            if(console.readLine("[ ATTENTION ] : ARE YOU SURE YOU WANT TO " + status.toUpperCase() + " " + user + "? [ Y | N ]\n\n} ").equalsIgnoreCase("y"))
            {
                //encode the selected user to a hashed format
                user = new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(user);
                
                //Update the values in the database to reflect the changes
                switch(status)
                {
                    case "promote":
                    updateValues("Administrator", "Yes", user);
                    break;

                    case "demote":
                    updateValues("Administrator", "No", user);
                    break;
                }
            }
            System.gc();
        }
        catch (Exception E)
        {
            E.printStackTrace();
        }
    }

    /**
    * The logic to update the values in the database to reflect the changes desired
    * 
    * @param credential : The credential parameter to be modified
    * @param value : The value of the credential parameter
    * @param targetUser : The user targeted to have the credential modified
    * @throws Exception : Handle exceptions thrown during program runtime
    */
    private void updateValues(String credential, String value, String targetUser)throws Exception
    {
        try
        {
            //Initialize and open the connection to database
            String url = "jdbc:sqlite:./System/Private/Truncheon/mud.db";
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(url);

            //set the values to be updated in the database
            String sql = "UPDATE FCAD SET " + credential + " = ? WHERE Username = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, value);
            pstmt.setString(2, targetUser);

            //Execute the update statement
            pstmt.executeUpdate();

            //Close the streams and clean up memory allocated
            pstmt.close();
            conn.close();
            System.gc();
        }
        catch(Exception E)
        {
            E.printStackTrace();
            console.readLine();
        }
    }
}