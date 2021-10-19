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

//Import the required Java SQL classes
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


/**
* API to authenticate the credentials.
*
* checks the credentials against the entries in the Multi User Database.
* @version 0.5.12
* @since 0.2.1
* @author DAK404
*/
public final class LoginAPI
{
    //A string to store the username for validation (The main identifier for the account entry in the database)
    private String _User;

    //A string to store the password for validation
    private String _Pass;

    //A String to store the Username for validation (this is supplemental to the password)
    private String _SecKey;

    private String url = "jdbc:sqlite:./System/Private/Truncheon/mud.db";

    /**
    * Constructor to initialize the username, password and security key.
    *
    * @param Us : Stores the username
    * @param Pa : Stores the password
    * @param SK : Stores the security key
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    public LoginAPI(String Us, String Pa, String SK)throws Exception
    {
        //Assign the parameter values to the instance variables
        _User = Us;
        _Pass = Pa;
        _SecKey = SK;

        //Initialize the database
        Class.forName("org.sqlite.JDBC");
    }

    /**
    * API frontend which will handle the return value of the authentication
    *
    * @return boolean : Pass on the return value to the calling class
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    public final boolean status()throws Exception
    {
        try
        {
            return checkDetails();
        }
        catch(Exception E)
        {
            E.printStackTrace();
            return false;
        }
    }

    /**
    * The authentication logic which will validate te credential entries in the database
    * 
    * @return boolean : The status of the login attempt, a pass results in a true and a fail results in a false
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final boolean checkDetails()throws Exception
    {
        //Initialize a local variable to store the login attemot result in a boolean
        boolean loginStatus = false;
        try
        {
            //Open the database connection
            Connection conn = DriverManager.getConnection(url);

            //Set the SQL Statement to be queried against the database
            String sql = "SELECT Username, Password, SecurityKey FROM FCAD WHERE Username = ? AND Password = ? AND SecurityKey = ?;";

            //Use PreparedStatement to execute the query
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, _User);
            pstmt.setString(2, _Pass);
            pstmt.setString(3, _SecKey);

            //Store the result in a ResultSet, after executing the query
            ResultSet rs = pstmt.executeQuery();

            //Check if the username exists (and since the username is the Primary key, there wont be any multiple entries [since they are stored in the hashed format])
            if (rs.getString("Username").equals(_User) & rs.getString("Password").equals(_Pass) & rs.getString("SecurityKey").equals(_SecKey))
            //Set the login status to be true, if the credentials are found
            loginStatus = true;

            //Print on the screen that the login credentials are invalid
            if(! loginStatus)
            System.out.println("Incorrect Credentials, Please try again.");

            //Close all connections to the database
            rs.close();
            pstmt.close();
            conn.close();

            System.gc();
        }
        catch (Exception E)
        {
            //E.printStackTrace();
            System.out.println("[ ATTENTION ] : Incorrect Credentials. Please check details and try again.");
        }
        //Return the login status value to the status()
        return loginStatus;
    }
}