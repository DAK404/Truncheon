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

/**
 * 
 */
public class DeleteUser
{
    private String _user;
    private String _name;

    private Console console = System.console();

    /**
     * 
     * @param User
     * @param Name
     * @throws Exception
     */
    public DeleteUser(String User, String Name)throws Exception
    {
        _user = User;
        _name = Name;
    }

    /**
     * 
     * @throws Exception
     */
    public void deleteUserLogic()throws Exception
    {
        if(_user.equalsIgnoreCase("Administrator"))
        {
            System.out.println("Cannot delete Administrator account.");
            return;
        }

        if(authenticateUser())
        {
            System.out.println("Incorrect Credentials. Access Denied.");
            return;
        }
        deleteLogic();

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

    /**
     * 
     * @throws Exception
     */
    private final void cleanUp()throws Exception
    {
        try
        {
            File f = new File("./Users/"+_user);
            if(f.exists())
            {
                if(f.isDirectory())
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

    /**
     * 
     * @param delfile
     * @throws Exception
     */
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
