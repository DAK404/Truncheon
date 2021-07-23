package Truncheon.API.Dragon;

import java.io.Console;

public final class ModifyAccount
{
    private String _user;
    private String _name;

    public ModifyAccount(String User, String Name)
    {
        _user = User;
        _name = Name;
    }

    public final void modifyAccountLogic()throws Exception
    {
        System.gc();
        //authenticate User first
        //show menu
        //get credential
        //save to db
        //return;
    }

    private final boolean authenticateUser()throws Exception
    {
        new Truncheon.API.BuildInfo().versionViewer();

        System.out.println("[ ATTENTION ] : Please authenticate credentials before modifying account details.");
        System.out.println("Username: "+curName);
        String CurrentPassword=new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(String.valueOf(console.readPassword("Password: ")));
        String CurrentKey=new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(String.valueOf(console.readPassword("Security Key: ")));
        return new Truncheon.API.Dragon.LoginAPI(curUser, CurrentPassword, CurrentKey).status();
    }
}