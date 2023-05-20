package Truncheon.API.Dragon;

//Import the required Java IO classes
import java.io.Console;

//Import the required Java SQL classes
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import Truncheon.API.BuildInfo;
import Truncheon.API.IOStreams;

public class AccountModify
{
    private String _currentUsername = "";
    private String _currentAccountName = "";
    private boolean _currentAccountAdmin = false;

    private String _newPassword = "";
    private String _newSecKey = "";
    private String _newPIN = "";

    private Console console = System.console();

    public AccountModify(String user, String name)throws Exception
    {
        _currentUsername = user;
        _currentAccountName = name;
        _currentAccountAdmin = new LoginAuth(user).checkPrivilegeLogic();
    }

    public final void accountModifyLogic()throws Exception
    {
        System.gc();

        System.out.println(_currentUsername);
        System.out.println(_currentAccountName);
        System.out.println(_currentAccountAdmin);

        console.readLine();

        if(!login())
        IOStreams.printError("Incorrect Credentials! Aborting...");
        else
        accountManagementMenu();
    }

    private boolean login()throws Exception
    {
        BuildInfo.viewBuildInfo();
        IOStreams.printAttention("Please authenticate to continue.");
        IOStreams.println("Username: " + _currentAccountName);
        String password = new Truncheon.API.Minotaur.Cryptography().stringToSHA3_256(String.valueOf(console.readPassword("Password: ")));
        String key = new Truncheon.API.Minotaur.Cryptography().stringToSHA3_256(String.valueOf(console.readPassword("Security Key: ")));

        return new Truncheon.API.Dragon.LoginAuth(_currentUsername).authenticationLogic(password, key);
    }

    private void accountManagementMenu()throws Exception
    {
        BuildInfo.viewBuildInfo();

        System.out.println("Hello World! this is still under construct. Please try again later.");
        IOStreams.confirmReturnToContinue();

        // String tempInput;

        // do
        // {
        //     tempInput = console.readLine("\n" + _currentAccountName + "} ");

        //     String[] usermgmtModifyCommandArray = Truncheon.API.Anvil.splitStringToArray(tempInput);

        //     switch(usermgmtModifyCommandArray[0].toLowerCase())
        //     {
        //         case "name":
        //         break;

        //         case "password":
        //         break;

        //         case "key":
        //         break;

        //         case "pin":
        //         break;

        //         default:
        //         break;
        //     }
        // }
        // while(!tempInput.equalsIgnoreCase("exit"));
    }

    private void accountManagementMenuDisplay()
    {
        IOStreams.println("-------------------------------------------------");
        IOStreams.println("| User Management Console: Account Modification |");
        IOStreams.println("-------------------------------------------------\n");

        IOStreams.println("How do you wish to manage or modify your account?\n");

        IOStreams.println("[1] Change Account Name");
        IOStreams.println("[2] Change Account Password");
        IOStreams.println("[3] Change Account Security Key");
        IOStreams.println("[4] Change Session Unlock PIN\n");

        IOStreams.println("[ NAME | PASSWORD | KEY | PIN | HELP | EXIT ]");
        if(_currentAccountAdmin)
        {
            IOStreams.println("\n\n--- ADMINISTRATOR TOOLKIT ---");
            IOStreams.println("[!] Promote Account to Administrator");
            IOStreams.println("[!] Demote Account to Standard User\n");

            IOStreams.println("[ PROMOTE | DEMOTE ]");
        }
        IOStreams.println("");
    }
}
