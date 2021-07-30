package Truncheon.API.Dragon;

import java.io.Console;
import java.io.File;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class DeleteUser 
{
    private String _user;
    private String _name;

    Console console = System.console();

    public DeleteUser(String User, String Name)throws Exception
    {
        _user = User;
        _name = Name;
    }

    public void deleteUserLogic()throws Exception
    {
        if(_user.equalsIgnoreCase("Administrator"))
        {
            System.out.println("Cannot delete Administrator account.");
            return;
        }

        if(authenticateUser() == false)
        {
            System.out.println("Incorrect Credentials. Access Denied.");
            return;
        }
        deleteLogic();
        
    }

    private final boolean authenticateUser()throws Exception
    {
        new Truncheon.API.BuildInfo().versionViewer();
        System.out.println("[ ATTENTION ] : Please authenticate credentials before modifying account details.");
        System.out.println("Username: "+_name);
        String currentPassword=new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(String.valueOf(console.readPassword("Password: ")));
        String currentKey=new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(String.valueOf(console.readPassword("Security Key: ")));
        return new Truncheon.API.Dragon.LoginAPI(_user, currentPassword, currentKey).status();
    }

    private final void deleteLogic()throws Exception
    {
        new Truncheon.API.BuildInfo().versionViewer();
        if(console.readLine("Are you sure you want to delete your user account? ALL files and directories will be deleted!").equalsIgnoreCase("y"))
        {
            String url = "jdbc:sqlite:./System/Private/Truncheon/mud.db";
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(url);
            String sql = "DELETE FROM FCAD WHERE Username = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, _user);
            pstmt.executeUpdate();
            pstmt.closeOnCompletion();
            conn.close();
            cleanUp();
            System.gc();

            console.readLine("User deletion complete. Press ENTER to restart program.");
            System.exit(1);
        }
    }

    private final void cleanUp()throws Exception
    {
        try 
        {
            File f=new File("./Users/"+_user);
            if(f.exists()==true)
            {
                if(f.isDirectory()==true)
                    delHelper(f);
                else
                    f.delete();
            }   
        }
        catch (Exception E)
        {
            E.printStackTrace();
        }
    }
    private final void delHelper(File delfile)throws Exception
    {
        if (delfile.listFiles() != null) 
        {
            for (File fock : delfile.listFiles()) 
                delHelper(fock);
        }
        delfile.delete();
    }
}
