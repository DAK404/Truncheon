package Truncheon.API.Dragon;

import java.io.Console;

public final class ModifyAccount
{
    private String _user;
    private String _name;
    private String _password;
    private String _key;
    private String _pin;
    private boolean _admin;

    Console console = System.console();

    public ModifyAccount(String User, String Name, String Pin, boolean Admin)
    {
        _user = User;
        _name = Name;
        _pin = Pin;
        _admin = Admin;
    }

    public final void modifyAccountLogic()throws Exception
    {
        System.gc();
        authenticateUser();
        while(modifyAccountMenu() == true);
        //return;
    }

    private final boolean authenticateUser()throws Exception
    {
        new Truncheon.API.BuildInfo().versionViewer();

        System.out.println("[ ATTENTION ] : Please authenticate credentials before modifying account details.");
        System.out.println("Username: "+_name);
        String CurrentPassword=new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(String.valueOf(console.readPassword("Password: ")));
        String Current_key=new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(String.valueOf(console.readPassword("Security Key: ")));
        return new Truncheon.API.Dragon.LoginAPI(_user, CurrentPassword, Current_key).status();
    }

    private final boolean modifyAccountMenu()throws Exception
    {
        new Truncheon.API.BuildInfo().versionViewer();
        System.out.println("User Credential Modification Dashboard 1.0");
        System.out.println("------------------------------------------");
        System.out.println("\nEnter the credential parameter to change value\n");
        System.out.println("* User Account Password");
        System.out.println("* User Account Security Key");
        System.out.println("* User Account PIN");
        if(_admin==true)
            System.out.println("* Promote an Account");
            System.out.println("* Demote an Account");
        System.out.println("* Exit\n");
        System.out.println("------------------------------------------");
        System.out.println("\n[ PSW | KEY | PIN | EXIT ]\n");
        switch(console.readLine(_name+"} ").toLowerCase())
        {
            case "":
                break;

            case "psw":
                //password changing logic
                break;

            case "key":
                //_key changing logic
                break;

            case "pin":
                //pass on the pin cred changing logic
                break;

            case "promote":
                break;

            case "demote":
                break;
            
            case "exit":
                return false;

            default:
                System.out.println("Invalid option. Please try again.");
                break;
        }
        return true;
    }

    private final void displayDetails()throws Exception
    {
        new Truncheon.API.BuildInfo().versionViewer();
        System.gc();
        System.out.println("Administrator Account: "+_admin);

        if(! (_name.equals(null) | _name.equals("")) )
            System.out.println("Account Name : " + _name);

        if(! (_user.equals(null) | _user.equals("")) )
            System.out.println("Username     : " + _user);

        if(! (_password.equals(null) | _password.equals("")) )
            System.out.println("Password     : ********");

        if(! (_key.equals(null) | _key.equals("")) )
            System.out.println("Security Key : ********");

        if(! (_pin.equals(null) | _pin.equals("")) )
            System.out.println("Unlock PIN   : ****");
        return;
    }

    private final boolean getPassword()throws Exception
    {
        displayDetails();
        System.out.println("\nPassword Policy\n");
        System.out.println("* Password must be atleast 8 characters long.");
        System.out.println("* Password must be the same as the password confirmation\n");
        _password  = String.valueOf(console.readPassword("\nAccount Password : "));
        String C_password = String.valueOf(console.readPassword("Confirm Password : "));
        if(_password.length() < 8 | ( _password.equals(C_password) == false ) )
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
    private final boolean get_key()throws Exception
    {
        displayDetails();
        System.out.println("\nSecurity _key Policy\n");
        System.out.println("* Security _key must be the same as the Security _key confirmation\n");
        _key  = String.valueOf(console.readPassword("\nSecurity _key : "));
        String C_key = String.valueOf(console.readPassword("Confirm _key  : "));
        if(_key.equals(C_key) == false)
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
        if(_pin.length() < 4 | ( _pin.equals(CPIN) == false ))
        {
            _pin="";
            console.readLine("PIN Policy not followed. Please use a valid PIN and try again.");
            return false;
        }
        CPIN = "";
        _pin  = new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(_pin);
        return true;
    }
}