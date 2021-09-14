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
    private String _user;

    /**
     * Variable to store the user account name
     */
    private String _name;

    /**
     * Variable to store the new password
     */
    private String _password;

    /**
     * Variable to store the new Security Key
     */
    private String _key;

    /**
     * Variable to store the new PIN value
     */
    private String _pin;

    /**
     * Variable to store the administrator privileges
     */
    private boolean _admin;
    
    /**
     * Initialize the console class for Input operations
     */
    private Console console = System.console();
    
    /**
    * Constructor which will initialize the username, name, pin and the administrator rights.
    *
    * @param User : Receive the username from the program
    * @param Name :
    * @param Pin
    * @param Admin
    */
    public ModifyAccount(String User, String Name, String Pin, boolean Admin)
    {
        _user = User;
        _name = Name;
        _pin = Pin;
        _admin = Admin;
    }
    
    /**
    * 
    * @throws Exception
    */
    public final void modifyAccountLogic()throws Exception
    {
        System.gc();
        if(! authenticateUser())
        {
            System.out.println("Incorrect Credentials. Access Denied.");
            return;
        }
        checkPrivileges();
        while(modifyAccountMenu());
    }
    
    /**
    * 
    * @return
    * @throws Exception
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
     * 
     * @throws Exception
     */
    private final void checkPrivileges()throws Exception
    {
        try
        {
            String url = "jdbc:sqlite:./System/Private/Truncheon/mud.db";
            Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement("SELECT Administrator FROM FCAD WHERE Username = ? ;");
            pstmt.setString(1, _user);
            ResultSet rs = pstmt.executeQuery();
            
            if(rs.getString("Administrator").equals("Yes"))
            _admin = true;
            
            rs.close();
            pstmt.close();
            conn.close();
            
            System.gc();
        }
        catch(Exception E)
        {
            //Handle any exceptions thrown during runtime
            new Truncheon.API.ErrorHandler().handleException(E);
        }
    }
    
    /**
    * 
    * @return
    * @throws Exception
    */
    private final boolean modifyAccountMenu()throws Exception
    {
        new Truncheon.API.BuildInfo().versionViewer();
        System.out.println("User Credential Modification Dashboard 1.0");
        System.out.println("------------------------------------------");
        System.out.println("\nEnter the credential parameter to change value\n");
        System.out.println("* Open Help");
        System.out.println("* User Account Password");
        System.out.println("* User Account Security Key");
        System.out.println("* User Account PIN");
        if(_admin)
        System.out.println("* Promote an Account");
        System.out.println("* Demote an Account");
        System.out.println("* Exit\n");
        System.out.println("------------------------------------------");
        System.out.println("\n[ HELP | PSW | KEY | PIN | EXIT ]\n");
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
     * @throws Exception
     */
    private final void displayDetails()throws Exception
    {
        new Truncheon.API.BuildInfo().versionViewer();
        System.gc();
        System.out.println("Administrator Account: " + _admin);
        
        if(! (_name == null | _name.equals("")) )
        System.out.println("Account Name : " + _name);
        
        if(! (_user == null | _user.equals("")) )
        System.out.println("Username     : " + _user);
        
        if(! (_password == null | _password.equals("")) )
        System.out.println("Password     : ********");
        
        if(! (_key == null | _key.equals("")) )
        System.out.println("Security Key : ********");
        
        if(! (_pin == null | _pin.equals("")) )
        System.out.println("Unlock PIN   : ****");
    }
    
    /**
     * 
     * @return
     * @throws Exception
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
    * @throws Exception :  Throws any exception caught during runtime/execution
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
    * @throws Exception :  Throws any exception caught during runtime/execution
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
     * 
     * @param status
     * @throws Exception
     */
    private void userStatusChange(String status)throws Exception
    {
        try
        {
            if(! _admin)
            return;
            
            String user = console.readLine("Enter the name of the user to " + status + ": ");
            if(user.equalsIgnoreCase("Administrator"))
            {
                System.out.println("Cannot promote or demote the user Administrator.");
                return;
            }
            if(console.readLine("[ ATTENTION ] : ARE YOU SURE YOU WANT TO " + status.toUpperCase() + " " + user + "?").equalsIgnoreCase("y"))
            {
                user = new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(user);
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
    * 
    * @param credential
    * @param value
    * @param targetUser
    * @throws Exception
    */
    private void updateValues(String credential, String value, String targetUser)throws Exception
    {
        try
        {
            String url = "jdbc:sqlite:./System/Private/Truncheon/mud.db";
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(url);
            String sql = "UPDATE FCAD SET " + credential + " = ? WHERE Username = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, value);
            pstmt.setString(2, targetUser);
            pstmt.executeUpdate();
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