package Truncheon.API.Dragon;

//Import the required Java IO classes
import java.io.Console;
import java.io.File;

//Import the required Java SQL classes
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import Truncheon.API.BuildInfo;
import Truncheon.API.IOStreams;

public class AccountDelete
{
    private String _currentUsername = "";

    private Console console = System.console();

    public AccountDelete(String currentUsername)throws Exception
    {
        _currentUsername = currentUsername;   
    }

    public void userDeletionLogic()throws Exception
    {
        if(_currentUsername.equals(new Truncheon.API.Minotaur.Cryptography().stringToSHA3_256("Administrator")))
            Truncheon.API.IOStreams.printError("You cannot delete the Administrator account!");
        else
        {
            BuildInfo.viewBuildInfo();
            if(! login())
                Truncheon.API.IOStreams.printError("Invalid Login Credentials. Please Try Again.");
            else
            {
                if(console.readLine("Are you sure you wish to delete your user account? [ YES | NO ]\n> ").equalsIgnoreCase("yes"))
                {
                    deleteFromDatabase();
                    Truncheon.API.IOStreams.printAttention("Account Successfully Deleted. Press ENTER to continue.");
                    console.readLine();
                    System.exit(211);
                }
            }
        }
    }

    private boolean login()throws Exception
    {
        boolean status = false;
        try
        {
            IOStreams.println("Please login to continue.");
            IOStreams.println("Username: " + new Truncheon.API.Dragon.LoginAuth(_currentUsername).getNameLogic());
            status = new Truncheon.API.Dragon.LoginAuth(_currentUsername).authenticationLogic(new Truncheon.API.Minotaur.Cryptography().stringToSHA3_256(String.valueOf(console.readPassword("Password: "))), new Truncheon.API.Minotaur.Cryptography().stringToSHA3_256(String.valueOf(console.readPassword("Security Key: "))));
        }
        catch(Exception e)
        {
            status = false;
            e.printStackTrace();
            System.in.read();
        }
        return status;
    }

    private void deleteFromDatabase()
    {
        try
        {
            String databasePath = "jdbc:sqlite:./System/Truncheon/Private/Mud.dbx";
            String sqlCommand = "DELETE FROM MUD WHERE Username = ?";

            Class.forName("org.sqlite.JDBC");
            Connection dbConnection = DriverManager.getConnection(databasePath);
            PreparedStatement preparedStatement = dbConnection.prepareStatement(sqlCommand);
            preparedStatement.setString(1, _currentUsername);
            preparedStatement.executeUpdate();

            preparedStatement.closeOnCompletion();
            dbConnection.close();

            deleteDirectories(new File("./Users/Truncheon/" + _currentUsername));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        System.gc();
    }

    private void deleteDirectories(File delFile)throws Exception
    {
        try
        {
            if (delFile.listFiles() != null)
            {
                for (File fn : delFile.listFiles())
                deleteDirectories(fn);
            }
            delFile.delete();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
