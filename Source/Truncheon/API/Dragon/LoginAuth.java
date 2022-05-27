package Truncheon.API.Dragon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginAuth
{
    private String _username;

    public LoginAuth(String usn)throws Exception
    {
        _username = usn;
    }

    public boolean authenticationLogic(String psw, String key)throws Exception
    {
        return retrieveDatabaseEntry("SELECT Password FROM MUD WHERE Username = ?", "Password").equals(psw) && retrieveDatabaseEntry("SELECT SecurityKey FROM MUD WHERE Username = ?", "SecurityKey").equals(key);
    }

    public boolean checkPrivilegeLogic()throws Exception
    {
        return retrieveDatabaseEntry("SELECT Privileges FROM MUD WHERE Username = ?", "Privileges").equals("Yes");
    }

    public String getNameLogic()throws Exception
    {
        return retrieveDatabaseEntry("SELECT Name FROM MUD WHERE Username = ?", "Name");
    }

    public String getPINLogic()throws Exception
    {
        return retrieveDatabaseEntry("SELECT PIN FROM MUD WHERE Username = ?", "PIN");
    }

    private String retrieveDatabaseEntry(String sqlCommand, String parameter)throws Exception
    {
        String result = "DEFAULT_STRING";
        try
        {
            Class.forName("org.sqlite.JDBC");
            String databasePath = "jdbc:sqlite:./System/Conch/Private/mud.dbx";

            Connection dbConnection = DriverManager.getConnection(databasePath);
            PreparedStatement preparedStatement = dbConnection.prepareStatement(sqlCommand);
            
            preparedStatement.setString(1, _username);
            ResultSet resultSet = preparedStatement.executeQuery();

            result = resultSet.getString(parameter);
            preparedStatement.close();
            dbConnection.close();
            resultSet.close();
        }
        catch(Exception e)
        {
            result = "ERROR";
        }
        System.gc();
        return result;
    }
}
