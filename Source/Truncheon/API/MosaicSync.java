package Truncheon.API;

import java.io.Console;
import java.io.File;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MosaicSync
{
    Console console=System.console();

    public final boolean mosaicSyncLogic(String username, String pin)
    {
        try
        {
            if(new File("./System/Private/Truncheon/mud.db").exists() == true)
                if(checkAdminStatus(username, pin) == false)
                    return false;
            return syncLogic();
        }
        catch(Exception E)
        {
            new Truncheon.API.ErrorHandler().handleException(E);
        }
        return false;
    }

    private final boolean checkAdminStatus(String u, String p)throws Exception
    {
        String url = "jdbc:sqlite:./System/Private/Truncheon/mud.db";
        Connection conn = DriverManager.getConnection(url);

        PreparedStatement pstmt = conn.prepareStatement("SELECT Administrator FROM FCAD WHERE Username = ? AND PIN = ?");
        pstmt.setString(1, u);
        pstmt.setString(2, p);
        ResultSet rs = pstmt.executeQuery();
        String temp = rs.getString("Administrator");

        rs.close();
        pstmt.close();
        conn.close();

        System.gc();

        if(temp.equalsIgnoreCase("Yes"))
            return true;
        return false;
    }

    private final boolean syncLogic()
    {
        try
        {

            if(new File("./System/Private/Truncheon").exists() == false)
                new File("./System/Private/Truncheon").mkdir();

            syncHelper(new File("./System/Private/Fractal.db"), new File("./System/Private/Truncheon/mud.db"));
            syncHelper(new File("./Users/Mosaic"), new File("./Users/Truncheon"));
            
            System.gc();

            return true;
        }
        catch(Exception E)
        {
            E.printStackTrace();
        }
        return false;
    }

    private final void syncHelper(File src, File dest ) throws Exception 
    {
        try
        {
            if( src.isDirectory() )
            {
                dest.mkdirs();
                for( File sourceChild : src.listFiles() ) 
                {
                    File destChild = new File( dest, sourceChild.getName() );
                    syncHelper( sourceChild, destChild );
                }
            } 
            else
            {
                InputStream in = new FileInputStream(src);
                OutputStream out = new FileOutputStream(dest);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0)
                    out.write(buf, 0, len);
                in.close();
                out.close();
            }
            System.gc();
        }
        catch(Exception E)
        {
            E.printStackTrace();
        }
    }
}