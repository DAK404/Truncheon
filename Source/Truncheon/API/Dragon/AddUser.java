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
import java.io.File;

//Import the required Java SQL classes
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
* Program to add a new user account to the program
*
* @version 0.7.29
* @since 0.1.3
* @author DAK404
*/
public final class AddUser
{
    private String _curUser = "";

    //The new account name
    private String _NAME= "";
    //The new account username (in hashed format)
    private String _UNM = "";
    //The new account password (in hashed format)
    private String _PWD = "";
    //The new account security key (in hashed format)
    private String _KEY = "";
    //The new account _PIN (in hashed format)
    private String _PIN = "";
    //The new account Administrator status
    private String _ADM = "No";
    //The current account Administrator status
    private boolean _admin = false;

    //Initialize the console class to accept inputs
    private Console console = System.console();

    /**
    * Authenticates the user currently logged in
    *
    * @return boolean : Returns true if the credentials are correct, else false
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final boolean authenticateUser()throws Exception
    {
        //Clear the screen and display the build information
        new Truncheon.API.BuildInfo().versionViewer();

        System.out.println("[ ATTENTION ] : Please authenticate credentials before creating a new account.");
        _curUser = new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(console.readLine("Username: "));
        String CurrentPassword = new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(String.valueOf(console.readPassword("Password: ")));
        String CurrentKey = new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(String.valueOf(console.readPassword("Security Key: ")));
        return new Truncheon.API.Dragon.LoginAPI(_curUser, CurrentPassword, CurrentKey).status();
    }

    /**
     * Check the current user privileges which will allow the option for the new user to be an Administrator
     * 
     * @throws Exception
     */
    private final void checkPrivileges()throws Exception
    {
        try
        {
            System.gc();

            //Open a new connection to the database
            String url = "jdbc:sqlite:./System/Private/Truncheon/mud.db";
            Connection conn = DriverManager.getConnection(url);

            //Create a prepared statement to query the administrator status of the currently logged in user
            PreparedStatement pstmt = conn.prepareStatement("SELECT Administrator FROM FCAD WHERE Username = ? ;");
            pstmt.setString(1, _curUser);

            //Store the result of the executed query
            ResultSet rs = pstmt.executeQuery();

            //set the Administrator status value as true in _admin if the result is Yes
            _admin = rs.getString("Administrator").equals("Yes");

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
    * The logic to add a user to the database
    *
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    public final void addUserLogic() throws Exception
    {
        try
        {
            //Check if the user credentials are correct
            if(! authenticateUser())
            {
                console.readLine();
                return;
            }            
            checkPrivileges();
            if(! _admin && ! (new Truncheon.API.Minotaur.PolicyEnforcement().checkPolicy("usermgmt")))
            return;
            userType();
            while (! getUserDetails());

            if(! add())
            {
                System.out.println("Failed to perform requested operations.");
                System.in.read();
                return;
            }
        }
        catch(Exception E)
        {
            E.printStackTrace();
        }
    }

    /**
    *
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final void userType()throws Exception
    {
        try
        {
            while(true)
            {
                new Truncheon.API.BuildInfo().versionViewer();
                System.out.println("[ ATTENTION ] : Do you want this account to be an Administrative account?");
                System.out.println("An Administrator account has additional privileges compared to a standard user account.");

                switch(console.readLine("Choice: [ Yes | No ]\n>> ").toLowerCase())
                {
                    case "yes":
                    _ADM="Yes";
                    return;

                    case "no":
                    _ADM="No";
                    return;

                    default:
                    console.readLine("Please enter a valid choice. Press ENTER to continue..");
                    break;
                }
            }
        }
        catch(Exception E)
        {
            E.printStackTrace();
        }
    }

    /**
    *
    * @return
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final boolean getUserDetails() throws Exception
    {
        try
        {
            while(! getName());
            while(! getUsername());
            while(! getPassword());
            while(! getKey());
            while(! getPIN());

            //Show an account summary after a user has been created.
            displayDetails();
            return true;
        }
        catch(Exception E)
        {
            E.printStackTrace();
            System.exit(0);
        }
        return false;
    }

    /**
    *
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    public final void setupAdminUser()throws Exception
    {
        _admin= true;
        _NAME = "Administrator";
        _UNM  = new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256("Administrator");
        _ADM  = "Yes";
        while(! getPassword());
        while(! getKey());
        while(! getPIN());
        add();
    }

    /**
    *
    * @return
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final boolean getName()throws Exception
    {
        displayDetails();
        System.out.println("\nName Policy\n");
        System.out.println("* Name cannot be Administrator");
        System.out.println("* Name must be in english, can contain alphabet and number combination");
        System.out.println("* Name must have a minimum of 2 characters or more.");
        System.out.println("* Name cannot contain spaces");
        _NAME = console.readLine("\nAccount Name: ");
        if(_NAME == null | _NAME.equals("") | ! (_NAME.matches("^[a-zA-Z0-9]*$")) | _NAME.equalsIgnoreCase("Administrator") | _NAME.length() < 2)
        {
            _NAME="";
            console.readLine("Name Policy has not been followed. Please try again.");
            return false;
        }
        return true;
    }

    /**
    *
    * @return
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final boolean getUsername()throws Exception
    {
        displayDetails();
        System.out.println("\nUsername Policy\n");
        System.out.println("* Username cannot contain the word \"Administrator\"\n");
        _UNM = console.readLine("\nAccount Username: ");
        if(_UNM.equals("") | _UNM.contains("Administrator"))
        {
            _UNM="";
            console.readLine("Username Policy not followed. Please change the username and try again.");
            return false;
        }
        _UNM  = new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(_UNM);
        return true;
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
        _PWD = String.valueOf(console.readPassword("\nAccount Password : "));
        String CPWD = String.valueOf(console.readPassword("Confirm Password : "));
        if(_PWD.length() < 8 | ! (_PWD.equals(CPWD)) )
        {
            _PWD="";
            console.readLine("Password Policy not followed. Please try again which follows the Password Policy.");
            return false;
        }
        CPWD = "";
        _PWD  = new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(_PWD);
        return true;
    }

    /**
    *
    * @return
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final boolean getKey()throws Exception
    {
        displayDetails();
        System.out.println("\nSecurity Key Policy\n");
        System.out.println("* Security Key must be the same as the Security Key confirmation\n");
        _KEY = String.valueOf(console.readPassword("\nSecurity Key : "));
        String CKEY = String.valueOf(console.readPassword("Confirm Key  : "));
        if(! _KEY.equals(CKEY))
        {
            _KEY="";
            console.readLine("Security Key Policy not followed. Please try again which follows the Security Key Policy.");
            return false;
        }
        CKEY = "";
        _KEY  = new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(_KEY);
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
        _PIN = String.valueOf(console.readPassword("\nUnlock PIN   : "));
        String CPIN = String.valueOf(console.readPassword("Confirm PIN  : "));
        if(_PIN.length() < 4 | ! _PIN.equals(CPIN))
        {
            _PIN = "";
            console.readLine("PIN Policy not followed. Please use a valid PIN and try again.");
            return false;
        }
        CPIN = "";
        _PIN  = new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(_PIN);
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
        System.out.println("Administrator Account: " + _ADM);

        if(! (_NAME == null | _NAME.equals("")) )
        System.out.println("Account Name : " + _NAME);

        if(! (_UNM == null | _UNM.equals("")) )
        System.out.println("Username     : " + _UNM);

        if(! (_PWD == null | _PWD.equals("")) )
        System.out.println("Password     : ********");

        if(! (_KEY == null | _KEY.equals("")) )
        System.out.println("Security Key : ********");

        if(! (_PIN == null | _PIN.equals("")) )
        System.out.println("Unlock PIN   : ****");
    }

    /**
    *
    * @return
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final boolean add() throws Exception
    {
        System.gc();
        String url = "jdbc:sqlite:./System/Private/Truncheon/mud.db";
        try
        {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(url);
            String sql = "INSERT INTO FCAD(Name, Username, Password, SecurityKey, PIN, Administrator) VALUES(?,?,?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, _NAME);
            pstmt.setString(2, _UNM);
            pstmt.setString(3, _PWD);
            pstmt.setString(4, _KEY);
            pstmt.setString(5, _PIN);
            pstmt.setString(6, _ADM);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
            System.gc();
            console.readLine("The user \"" + _NAME + "\" was successfully created! Press ENTER to continue..");
            createDir();
            return true;
        }
        catch (Exception E)
        {
            //E.printSTackTrace();
            System.out.println("Failed to create user. Please try again.");
            System.in.read();
        }
        return false;
    }

    /**
    *
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final void createDir()throws Exception
    {
        try
        {
            String[] dirNames = {_UNM, _UNM + "/Scripts", _UNM + "/Properties"};
            for(int i = 0 ; i < dirNames.length ; i++)
            new File("./Users/Truncheon/" + dirNames[i]).mkdir();
        }
        catch(Exception E)
        {
            E.printStackTrace();
        }
    }
}