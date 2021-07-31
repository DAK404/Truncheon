package Truncheon.API.Dragon;

//import java libraries
import java.io.File;
import java.io.Console;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;


public final class AddUser
{
    //Variables required for the program
    private String curName = "";
    private String curUser = "";
    private String NAME= "";
    private String UNM = "";
    private String PWD = "";
    private String KEY = "";
    private String PIN = "";
    private String ADM = "No";
    private boolean Admin=false;

    private Console console = System.console();

    public AddUser(String u, String n, boolean Administrator)
    {
        if(Administrator==true)
        {
            Admin=true;
        }
        curName=n;
        curUser=u;
    }

    public AddUser()
    {
    }

    private final boolean authenticateUser()throws Exception
    {
        new Truncheon.API.BuildInfo().versionViewer();

        System.out.println("[ ATTENTION ] : Please authenticate credentials before creating a new account.");
        System.out.println("Username: "+curName);
        String CurrentPassword=new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(String.valueOf(console.readPassword("Password: ")));
        String CurrentKey=new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(String.valueOf(console.readPassword("Security Key: ")));
        return new Truncheon.API.Dragon.LoginAPI(curUser, CurrentPassword, CurrentKey).status();
    }

    public final void addUserLogic() throws Exception
    {
        try
        {
            if(new Truncheon.API.Minotaur.PolicyEnforcement().checkPolicy("usermgmt") == false)
                return;
            if(authenticateUser()==false)
            {
                console.readLine();
                return;
            }
            else
            {
                if(Admin==true)
                    userType();
                while (Details() == false);
            }
            if(add() == false)
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

    private final boolean Details() throws Exception
    {
        try
        {
            while(getName()==false);
            while(getUsername()==false);
            while(getPassword()==false);
            while(getKey()==false);
            while(getPIN()==false);

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

    public final void Setup()throws Exception
    {
        Admin= true;
        NAME = "Administrator";
        UNM  = new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256("Administrator");
        ADM  = "Yes";
        while(getPassword()==false);
        while(getKey()==false);
        while(getPIN()==false);
        add();
    }

    private final boolean getName()throws Exception
    {
        displayDetails();
        System.out.println("\nName Policy\n");
        System.out.println("* Name cannot be Adminstrator");
        System.out.println("* Name must be in english, can contain alphabet and number combination");
        System.out.println("* Name must have a minimum of 2 characters or more.");
        System.out.println("* Name cannot contain spaces");
        NAME=console.readLine("\nAccount Name: ");
        if(NAME.equals("") | NAME.equals(null) | (NAME.matches("^[a-zA-Z0-9]*$")==false) | NAME.equalsIgnoreCase("Administrator") | NAME.length()<2)
        {
            NAME="";
            console.readLine("Name Policy has not been followed. Please try again.");
            return false;
        }
        return true;
    }

    private final boolean getUsername()throws Exception
    {
        displayDetails();
        System.out.println("\nUsername Policy\n");
        System.out.println("* Username cannot contain the word \"Administrator\"\n");
        UNM  = console.readLine("\nAccount Username: ");
        if(UNM.equals("") | UNM.contains("Administrator"))
        {
            UNM="";
            console.readLine("Username Policy not followed. Please change the username and try again.");
            return false;
        }
        UNM  = new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(UNM);
        return true;
    }

    private final boolean getPassword()throws Exception
    {
        displayDetails();
        System.out.println("\nPassword Policy\n");
        System.out.println("* Password must be atleast 8 characters long.");
        System.out.println("* Password must be the same as the password confirmation\n");
        PWD  = String.valueOf(console.readPassword("\nAccount Password : "));
        String CPWD = String.valueOf(console.readPassword("Confirm Password : "));
        if(PWD.length() < 8 | ( PWD.equals(CPWD) == false ) )
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
    * Method to receive the account Security Key.
    *
    * @throws Exception :  Throws any exception caught during runtime/execution
    */
    private final boolean getKey()throws Exception
    {
        displayDetails();
        System.out.println("\nSecurity Key Policy\n");
        System.out.println("* Security Key must be the same as the Security Key confirmation\n");
        KEY  = String.valueOf(console.readPassword("\nSecurity Key : "));
        String CKEY = String.valueOf(console.readPassword("Confirm Key  : "));
        if(KEY.equals(CKEY) == false)
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
        PIN  = String.valueOf(console.readPassword("\nUnlock PIN   : "));
        String CPIN = String.valueOf(console.readPassword("Confirm PIN  : "));
        if(PIN.length() < 4 | ( PIN.equals(CPIN) == false ))
        {
            PIN="";
            console.readLine("PIN Policy not followed. Please use a valid PIN and try again.");
            return false;
        }
        CPIN = "";
        PIN  = new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(PIN);
        return true;
    }

    private final void displayDetails()throws Exception
    {
        new Truncheon.API.BuildInfo().versionViewer();
        System.gc();
        System.out.println("Administrator Account: "+ADM);

        if(! (NAME.equals(null) | NAME.equals("")) )
            System.out.println("Account Name : " + NAME);

        if(! (UNM.equals(null) | UNM.equals("")) )
            System.out.println("Username     : " + UNM);

        if(! (PWD.equals(null) | PWD.equals("")) )
            System.out.println("Password     : ********");

        if(! (KEY.equals(null) | KEY.equals("")) )
            System.out.println("Security Key : ********");

        if(! (PIN.equals(null) | PIN.equals("")) )
            System.out.println("Unlock PIN   : ****");
        return;
    }

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
            console.readLine("The user \""+NAME+"\" was successfully created! Press ENTER to continue..");
            createDir();
            return true;
        }
        catch (Exception E)
        {
            E.printStackTrace();
            System.out.println("Failed to create user. Please try again."); 
            System.in.read();
            return false;
        }
    }

    private final void createDir()throws Exception
    {
        try
        {
            String[] dirNames = {UNM, UNM+"/Scripts", UNM+"/Properties"};
            for(int i = 0; i < dirNames.length; i++)
                new File("./Users/"+dirNames[i]).mkdir();
        }
        catch(Exception E)
        {
            E.printStackTrace();
        }
    }
}