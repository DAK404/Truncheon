package Truncheon.API.Dragon;

//Import the required Java SQL classes
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * 
 */
public class LoginAuth
{
    private String _username;

    /**
     * 
     * @param usn
     * @throws Exception
     */
    public LoginAuth(String usn)throws Exception
    {
        _username = usn;
    }

    /**
     * 
     * @param psw
     * @param key
     * @return
     * @throws Exception
     */
    public boolean authenticationLogic(String psw, String key)throws Exception
    {
        return retrieveDatabaseEntry("SELECT Password FROM MUD WHERE Username = ?", "Password").equals(psw) && retrieveDatabaseEntry("SELECT SecurityKey FROM MUD WHERE Username = ?", "SecurityKey").equals(key);
    }

    /**
     * 
     * @return
     * @throws Exception
     */
    public boolean checkPrivilegeLogic()throws Exception
    {
        return retrieveDatabaseEntry("SELECT Privileges FROM MUD WHERE Username = ?", "Privileges").equals("Yes");
    }

    /**
     * 
     * @return
     * @throws Exception
     */
    public String getNameLogic()throws Exception
    {
        return retrieveDatabaseEntry("SELECT Name FROM MUD WHERE Username = ?", "Name");
    }

    /**
     * 
     * @return
     * @throws Exception
     */
    public String getPINLogic()throws Exception
    {
        return retrieveDatabaseEntry("SELECT PIN FROM MUD WHERE Username = ?", "PIN");
    }

    /**
     * 
     * @param sqlCommand
     * @param parameter
     * @return
     * @throws Exception
     */
    private String retrieveDatabaseEntry(String sqlCommand, String parameter)throws Exception
    {
        String result = "DEFAULT_STRING";
        try
        {
            Class.forName("org.sqlite.JDBC");
            String databasePath = "jdbc:sqlite:./System/Truncheon/Private/mud.dbx";

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
            e.printStackTrace();
            result = "ERROR";
        }
        System.gc();
        return result;
    }
}
