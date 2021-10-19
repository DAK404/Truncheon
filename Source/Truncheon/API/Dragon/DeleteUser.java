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

/**
* Program to delete the currently logged in user
* 
* @version 0.7.34
* @since 0.1.4
* @author DAK404
*/
public class DeleteUser
{
    private String _user;
    private String _name;

    private Console console = System.console();

    /**
    * Initialize the username and the name of the account to be deleted.
    *
    * @param User : The username of the account to be deleted
    * @param Name : The name of the account, used to display it instead of the hashed value of username
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    public DeleteUser(String User, String Name)throws Exception
    {
        _user = User;
        _name = Name;
    }

    /**
    * The logic to delete the user.
    *
    * The implementation will not allow the Administrator account to be deleted.
    *
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    public void deleteUserLogic()throws Exception
    {
        //Check if the current user is an Administrator
        if(_user.equalsIgnoreCase("Administrator"))
        {
            //Do not allow the administrator account to be deleted
            System.out.println("Cannot delete Administrator account.");
            return;
        }

        //else, try to authenticate the credentials provided by the user
        if(!authenticateUser())
        {
            //return if the credentials are incorrect
            System.out.println("Incorrect Credentials. Access Denied.");
            return;
        }

        //else, perform the user account deletion logic
        deleteLogic();
    }

    /**
    * The authentication logic to verify the credentials before account deletion.
    *
    * @return boolean : Returns true if the credentials can be successfully authenticated.
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final boolean authenticateUser()throws Exception
    {
        //Clear the screen and display the program details
        new Truncheon.API.BuildInfo().versionViewer();

        //Accept the credentials from the user and store it in a hashed format
        System.out.println("[ ATTENTION ] : Please authenticate credentials before modifying account details.");
        System.out.println("Username: "+_name);
        String currentPassword=new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(String.valueOf(console.readPassword("Password: ")));
        String currentKey=new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(String.valueOf(console.readPassword("Security Key: ")));

        //Authenticate the credentials via the LoginAPI and return the status
        return new Truncheon.API.Dragon.LoginAPI(_user, currentPassword, currentKey).status();
    }

    /**
    * The logic to remove the user details from the database entries.
    *
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final void deleteLogic()throws Exception
    {
        //Clear the screen and display the program details
        new Truncheon.API.BuildInfo().versionViewer();

        //Ask for a confirmation from the user if they really want to delete the account
        if(console.readLine("Are you sure you want to delete your user account?\nALL files and directories will be deleted!\n\nAvailable Options : [ Y | N ]\n> ").equalsIgnoreCase("y"))
        {
            //Set the url of the database
            String url = "jdbc:sqlite:./System/Private/Truncheon/mud.db";

            //Establish the database connection
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(url);

            //Set the command used to delete the user account
            String sql = "DELETE FROM FCAD WHERE Username = ?";

            //Initialize the prepared statement with the SQLite command to delete the account
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, _user);

            //Perform the DELETE command on the database
            pstmt.executeUpdate();

            //Close the database connections
            pstmt.closeOnCompletion();
            conn.close();

            //Remove the user directories from ./Users/Truncheon/
            cleanUp();
            System.gc();

            //Restart the program to return to the Boot Menu
            console.readLine("User deletion complete. Press ENTER to restart program.");
            System.exit(1);
        }
    }

    /**
    * The logic to clean up the user directories and delete the user home directory.
    *
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final void cleanUp()throws Exception
    {
        try
        {
            //Initialize the user home directory path
            File f = new File("./Users/Truncheon/" + _user);

            //pass on the file object to the delHelper method
            delHelper(f);
        }
        catch (Exception E)
        {
            E.printStackTrace();
        }
    }

    /**
    * Helper method to delete the files.
    *
    * @param delfile : Name of the file or directory to be deleted.
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final void delHelper(File delfile)throws Exception
    {
        //Check if the directory is empty
        if (delfile.listFiles() != null)
        {
            //if not, list the files and send them for deletion
            for (File fock : delfile.listFiles())
            delHelper(fock);
        }
        //if the directory does not contain any other directories, delete it
        delfile.delete();
    }
}
