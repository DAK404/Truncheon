package Truncheon.API.Dragon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public final class LoginAPI 
{
    //a universal string to read the file
    
    private String User, Pass, SecKey;

    private String url = "jdbc:sqlite:./System/Private/Truncheon/mud.db";

    public LoginAPI(String Us, String Pa, String SK)throws Exception
    {
        User = Us;
        Pass = Pa;
        SecKey = SK;

        Class.forName("org.sqlite.JDBC");

    }

    public boolean status()throws Exception
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

    private boolean checkDetails()throws Exception
    {
        Connection conn = null;
        try 
        {
            conn = DriverManager.getConnection(url);
            String sql = "SELECT Username, Password, SecurityKey FROM FCAD WHERE Username = ? AND Password = ? AND SecurityKey = ?;";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, User);
            pstmt.setString(2, Pass);
            pstmt.setString(3, SecKey);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) 
            {
                if (rs.getString("Username").equals(User) & rs.getString("Password").equals(Pass) & rs.getString("SecurityKey").equals(SecKey))
                    return true;
                else
                    continue;
            }
            System.out.println("Incorrect Credentials, Please try again.");
            rs.close();
            conn.close();
            return false;
        } 
        catch (Exception E) 
        {
            E.printStackTrace();
            System.out.println("[ATTENTION] Incorrect Credentials. Please check details and try again.");
            return false;
        }
    }
}