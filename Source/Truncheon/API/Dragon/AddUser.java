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
    //Stores the current name of the logged in user
    private String curName = "";
    //Stores the current username of the logged in user (in the hashed format)
    private String curUser = "";
    
    //The new account name
    private String NAME= "";
    //The new account username (in hashed format)
    private String UNM = "";
    //The new account password (in hashed format)
    private String PWD = "";
    //The new account security key (in hashed format)
    private String KEY = "";
    //The new account PIN (in hashed format)
    private String PIN = "";
    //The new account Administrator status
    private String ADM = "No";
    private boolean Admin=false;
    
    //
    private Console console = System.console();
    
    /**
    * Default constructor to create user while setting the program up
    */
    public AddUser()
    {
    }
    
    
    /**
    * Parametrized constructor to create a user when setting up Truncheon
    *
    * @param u : The Username of the currently logged in user
    * @param n : The name of the user, currently logged in
    */
    public AddUser(String u, String n)
    {
        curName=n;
        curUser=u;
    }
    
    /**
    * Authenticates the user currently logged in
    *
    * @return boolean : Returns true if the credentials are correct, else false
    * @throws Exception
    */
    private final boolean authenticateUser()throws Exception
    {
        //Clear the screen and display the build information
        new Truncheon.API.BuildInfo().versionViewer();
        
        System.out.println("[ ATTENTION ] : Please authenticate credentials before creating a new account.");
        System.out.println("Username: "+curName);
        String CurrentPassword=new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(String.valueOf(console.readPassword("Password: ")));
        String CurrentKey=new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(String.valueOf(console.readPassword("Security Key: ")));
        return new Truncheon.API.Dragon.LoginAPI(curUser, CurrentPassword, CurrentKey).status();
    }
    
    private final void checkPrivileges()throws Exception
    {
        try
        {
            String url = "jdbc:sqlite:./System/Private/Truncheon/mud.db";
            Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement("SELECT Administrator FROM FCAD WHERE Username = ? ;");
            pstmt.setString(1, curName);
            ResultSet rs = pstmt.executeQuery();
            
            if(rs.getString("Administrator").equals("Yes"))
            Admin = true;
            
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
    * @throws Exception
    */
    public final void addUserLogic() throws Exception
    {
        try
        {
            if(! new Truncheon.API.Minotaur.PolicyEnforcement().checkPolicy("usermgmt"))
            return;
            if(! authenticateUser())
            {
                console.readLine();
                return;
            }
            else
            {
                checkPrivileges();
                if(Admin)
                userType();
                while (! getUserDetails());
            }
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
    * @throws Exception
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
                    ADM="Yes";
                    return;
                    
                    case "no":
                    ADM="No";
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
    * @throws Exception
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
    * @throws Exception
    */
    public final void setupAdminUser()throws Exception
    {
        Admin= true;
        NAME = "Administrator";
        UNM  = new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256("Administrator");
        ADM  = "Yes";
        while(! getPassword());
        while(! getKey());
        while(! getPIN());
        add();
    }
    
    /**
    *
    * @return
    * @throws Exception
    */
    private final boolean getName()throws Exception
    {
        displayDetails();
        System.out.println("\nName Policy\n");
        System.out.println("* Name cannot be Administrator");
        System.out.println("* Name must be in english, can contain alphabet and number combination");
        System.out.println("* Name must have a minimum of 2 characters or more.");
        System.out.println("* Name cannot contain spaces");
        NAME = console.readLine("\nAccount Name: ");
        if(NAME == null | NAME.equals("") | ! (NAME.matches("^[a-zA-Z0-9]*$")) | NAME.equalsIgnoreCase("Administrator") | NAME.length() < 2)
        {
            NAME="";
            console.readLine("Name Policy has not been followed. Please try again.");
            return false;
        }
        return true;
    }
    
    /**
    *
    * @return
    * @throws Exception
    */
    private final boolean getUsername()throws Exception
    {
        displayDetails();
        System.out.println("\nUsername Policy\n");
        System.out.println("* Username cannot contain the word \"Administrator\"\n");
        UNM = console.readLine("\nAccount Username: ");
        if(UNM.equals("") | UNM.contains("Administrator"))
        {
            UNM="";
            console.readLine("Username Policy not followed. Please change the username and try again.");
            return false;
        }
        UNM  = new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(UNM);
        return true;
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
        PWD = String.valueOf(console.readPassword("\nAccount Password : "));
        String CPWD = String.valueOf(console.readPassword("Confirm Password : "));
        if(PWD.length() < 8 | ! (PWD.equals(CPWD)) )
        {
            PWD="";
            console.readLine("Password Policy not followed. Please try again which follows the Password Policy.");
            return false;
        }
        CPWD = "";
        PWD  = new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(PWD);
        return true;
    }
    
    /**
    *
    * @return
    * @throws Exception
    */
    private final boolean getKey()throws Exception
    {
        displayDetails();
        System.out.println("\nSecurity Key Policy\n");
        System.out.println("* Security Key must be the same as the Security Key confirmation\n");
        KEY = String.valueOf(console.readPassword("\nSecurity Key : "));
        String CKEY = String.valueOf(console.readPassword("Confirm Key  : "));
        if(! KEY.equals(CKEY))
        {
            KEY="";
            console.readLine("Security Key Policy not followed. Please try again which follows the Security Key Policy.");
            return false;
        }
        CKEY = "";
        KEY  = new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(KEY);
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
        PIN = String.valueOf(console.readPassword("\nUnlock PIN   : "));
        String CPIN = String.valueOf(console.readPassword("Confirm PIN  : "));
        if(PIN.length() < 4 | ! PIN.equals(CPIN))
        {
            PIN = "";
            console.readLine("PIN Policy not followed. Please use a valid PIN and try again.");
            return false;
        }
        CPIN = "";
        PIN  = new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(PIN);
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
        System.out.println("Administrator Account: " + ADM);
        
        if(! (NAME == null | NAME.equals("")) )
        System.out.println("Account Name : " + NAME);
        
        if(! (UNM == null | UNM.equals("")) )
        System.out.println("Username     : " + UNM);
        
        if(! (PWD == null | PWD.equals("")) )
        System.out.println("Password     : ********");
        
        if(! (KEY == null | KEY.equals("")) )
        System.out.println("Security Key : ********");
        
        if(! (PIN == null | PIN.equals("")) )
        System.out.println("Unlock PIN   : ****");
    }
    
    /**
    *
    * @return
    * @throws Exception
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
            pstmt.setString(1, NAME);
            pstmt.setString(2, UNM);
            pstmt.setString(3, PWD);
            pstmt.setString(4, KEY);
            pstmt.setString(5, PIN);
            pstmt.setString(6, ADM);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
            System.gc();
            console.readLine("The user \"" + NAME + "\" was successfully created! Press ENTER to continue..");
            createDir();
            return true;
        }
        catch (Exception E)
        {
            System.out.println("Failed to create user. Please try again.");
            System.in.read();
        }
        return false;
    }
    
    /**
    *
    * @throws Exception
    */
    private final void createDir()throws Exception
    {
        try
        {
            String[] dirNames = {UNM, UNM + "/Scripts", UNM + "/Properties"};
            for(int i = 0 ; i < dirNames.length ; i++)
            new File("./Users/Truncheon/" + dirNames[i]).mkdir();
        }
        catch(Exception E)
        {
            E.printStackTrace();
        }
    }
}