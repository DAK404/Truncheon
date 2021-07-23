package Truncheon.API.Dragon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public final class LoginAPI 
{
    //a universal string to read the file
    
    private String _User, _Pass, _SecKey;

    private String url = "jdbc:sqlite:./System/Private/Truncheon/mud.db";

    public LoginAPI(String Us, String Pa, String SK)throws Exception
    {
        _User = Us;
        _Pass = Pa;
        _SecKey = SK;

        Class.forName("org.sqlite.JDBC");

    }

    public final boolean status()throws Exception
    {
        try
        {
            return checkDetails();
        }
        catch(Exception E)
        {
            E.printStackTrace();
            return false;
        }
    }

    private final boolean checkDetails()throws Exception
    {
        Connection conn = null;
        boolean loginStatus = false;
        try 
        {
            conn = DriverManager.getConnection(url);
            String sql = "SELECT Username, Password, SecurityKey FROM FCAD WHERE Username = ? AND Password = ? AND SecurityKey = ?;";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, _User);
            pstmt.setString(2, _Pass);
            pstmt.setString(3, _SecKey);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) 
                if (rs.getString("Username").equals(_User) & rs.getString("Password").equals(_Pass) & rs.getString("SecurityKey").equals(_SecKey))
                    loginStatus = true;

            if(loginStatus == false)
                System.out.println("Incorrect Credentials, Please try again.");

            rs.close();
            conn.close();

            System.gc();

            return loginStatus;
        } 
        catch (Exception E) 
        {
            E.printStackTrace();
            System.out.println("[ ATTENTION ] : Incorrect Credentials. Please check details and try again.");
            return false;
        }
    }
}