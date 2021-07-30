package Truncheon.API.Grinch;

import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FileManager 
{
    private String _user, _name, _curDir;
    private boolean _admin = false;


    Console console = System.console();

    public FileManager(String usn, String nm, boolean admin)
    {
        _user = usn;
        _name = nm;
        _admin = admin;
    }

    public final void fileManagerLogic()throws Exception
    {
        try
        {
            if(new Truncheon.API.Minotaur.PolicyEnforcement().checkPolicy("update") == false)
                if(_admin == false)
                    return;

            if(authenticationLogic() == false)
            {
                System.out.println("Authentication failed. Returning to main menu.");
                Thread.sleep(5000);
                return;
            }

            _curDir="./Users/"+_user+'/';
            new Truncheon.API.BuildInfo().versionViewer();
            System.out.println("Grinch File Manager 1.7");
            while(fileManagerShell(console.readLine(_name+"@"+_curDir.replace(_user, _name)+"]]: grinch ")) == true);
        }
        catch(Exception E)
        {
            new Truncheon.API.ErrorHandler().handleException(E);
        }
    }

    private final boolean authenticationLogic()throws Exception
    {
        try
        {
            new Truncheon.API.BuildInfo().versionViewer();
            System.out.println("[ ATTENTION ] : This module requires the user to authenticate to continue. Please enter the user credentials.");

            System.out.println("_user: " + _name);
            String password=new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(String.valueOf(console.readPassword("Password: ")));
            String securityKey=new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(String.valueOf(console.readPassword("Security Key: ")));

            return new Truncheon.API.Dragon.LoginAPI(_user, password, securityKey).status();
        }
        catch(Exception E)
        {
            new Truncheon.API.ErrorHandler().handleException(E);
        }
        return false;
    }

    private final boolean fileManagerShell(String input)throws Exception
    {
        try
        {
            String[] cmd = input.split(" (?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            if(cmd.length > 1)
                cmd[1] = cmd[1].replaceAll("\"", "");

            switch(cmd[0].toLowerCase())
            {
                case "exit":
                    new Truncheon.API.BuildInfo().versionViewer();  
                    return false;

                case "":
                    break;

                case "clear":
                    new Truncheon.API.BuildInfo().versionViewer();
                    break;

                case "cd":
                    if(cmd.length < 2)
                    {
                        System.out.println("Syntax:\n\ncd <directory_name>\n\nOR\n\ncd ..\n");
                        return true;
                    }
                    changeDir(cmd[1]);
                    break;

                case "ls":
                    listFiles();
                    break;

                case "tree":
                    treeView();
                    break;

                case "mkdir":
                    if(cmd.length < 2)
                    {
                        System.out.println("Syntax:\n\nmkdir <directory_name>\n");
                        return true;
                    }
                    makeDir(cmd[1]);
                    break;

                case "rm":
                    if(cmd.length < 2)
                    {
                        System.out.println("Syntax:\n\nrm <directory_name>\n\nOR\n\nrm <filename>\n");
                        return true;
                    }
                    del(cmd[1]);
                    break;

                case "rename":
                    if(cmd.length < 3)
                    {
                        System.out.println("Syntax:\n\nrename <directory_name> <new_directory_name>\n\nOR\n\nrename <filename> <new_filename>\n");
                        return true;
                    }
                    rename(cmd[1], cmd[2]);
                    break;

                case "cp":
                case "copy":
                    if(cmd.length < 3)
                    {
                        System.out.println("Syntax:\n\ncopy <directory_name><new_directory_name>\ncp <directory_name><new_directory_name>\n\nOR\n\ncopy <filename> <new_filename>\ncp <filename> <new_filename>\n");
                        return true;
                    }
                    copy_move_frontend(false, cmd[1], cmd[2]);
                    break;
                    
                case "mv":
                case "move":
                    if(cmd.length < 3)
                    {
                        System.out.println("Syntax:\n\nmove <directory_name><new_directory_name>\nmv <directory_name><new_directory_name>\n\nOR\n\nmove <filename> <new_filename>\nmv <filename> <new_filename>\n");
                        return true;
                    }
                    copy_move_frontend(true, cmd[1], cmd[2]);
                    break;

                case "read":
                    if(cmd.length < 2)
                    {
                        System.out.println("Syntax:\n\nread <filename>\n");
                        return true;
                    }
                    new Truncheon.API.Wraith.ReadFile().readUserFile(cmd[1], _curDir);
                    break;
                
                case "write":
                    if(cmd.length < 2)
                    {
                        System.out.println("Syntax:\n\nwrite <filename>\n");
                        return true;
                    }
                    new Truncheon.API.Wraith.WriteFile().editFile(cmd[1], _curDir);
                    break;
                
                case "download":
                    if(cmd.length < 3)
                    {
                        System.out.println("Syntax:\n\ndownload <URL> <filename>\n");
                        return true;
                    }
                    new Truncheon.API.Wyvern.Download().downloadFile(cmd[1], _curDir+cmd[2]);
                    break;

                default:
                    System.out.println(input+" - Command not found.");
                    break;
            }
            return true;
        }
        catch(Exception E)
        {
            new Truncheon.API.ErrorHandler().handleException(E);
        }
        return false;
    }

    private final void changeDir(String tPath)throws Exception
    {
        if(tPath.equals(".."))
        {
            prevDir();
            System.gc();
            return;
        }
        
        tPath=_curDir+tPath+"/";
        if(checkFile(tPath)==true)
            _curDir=tPath;
        else
            System.out.println("[ ERROR ] : The specified file/directory does not exist.");
        System.gc();
        return;
    }

    private final void prevDir()throws Exception
    {
        _curDir = _curDir.substring(0, _curDir.length()-1);
        _curDir = _curDir.replace(_curDir.substring(_curDir.lastIndexOf('/'), _curDir.length()), "/");

        if(_curDir.equals("./Users/"))
        {
            System.out.println("[ WARNING ] : Permission Denied.");
            _curDir="./Users/"+_user+"/";
        }
        System.gc();
        return;
    }

    private final boolean checkFile(String fName)throws Exception
    {
        if(new File(fName).exists() == false)
            return false;

        return true;
    }

    private final void treeView()throws Exception
    {
        try
        {
            File tree=new File(_curDir);
            System.out.println("\n--- [ TREE VIEW ] ---\n");
            TreeHelper(0, tree);
            System.out.println();
            System.gc();
            return;
        }
        catch(Exception E)
        {
            E.printStackTrace();
        }
    }

    private final void TreeHelper(int indent, File file)
    {
        System.out.print("|");

        for (int i = 0; i < indent; ++i)
            System.out.print('-');
        
        System.out.println(file.getName().replace(_user, _name + " [ USER ROOT DIRECTORY ]"));

        if (file.isDirectory())
        {
            File[] files = file.listFiles();

            for (int i = 0; i < files.length; ++i)
                TreeHelper(indent + 2, files[i]);
        }
    }

    private final void listFiles()throws Exception
    {
        //String format = "%1$-60s|%2$-50s|%3$-20s\n";
        String format = "%1$-50s|%2$-20s\n";
        if(checkFile(_curDir)==true)
        {
            File dPath=new File(_curDir);
            System.out.println("\n");
            System.out.format(String.format(format, "File Name", "File Size [In KB]\n"));
            for(File file : dPath.listFiles()) 
            {
                //System.out.format(String.format(format, file.getPath().replace(User,Name), file.getName().replace(User,Name), file.length()/1024+" KB"));
                System.out.format(String.format(format, file.getName().replace(_user, _name), file.length()/1024+" KB"));
            }
            System.out.println();
        }
        else
            System.out.println("[ ERROR ] : The specified file/directory does not exist.");
        System.gc();
        return;
    }

    private final void makeDir(String mkFile)throws Exception
    {
        try
        {
            mkFile=_curDir+mkFile+"/";
            if(checkFile(mkFile)==false)
                new File(mkFile).mkdir();
            else
                System.out.println("[ ERROR ] : The specified directory name already exists. Please try again.");
            System.gc();
            return;
        }
        catch (Exception E)
        {
            //troubleshooting tips here
            E.printStackTrace();
        }
    }

    private final void del(String delFile)throws Exception
    {
        try
        {
            delFile=_curDir+delFile;
            if(checkFile(delFile)==true)
            {
                File f=new File(delFile);
                if(f.isDirectory()==true)
                    delHelper(f);
                else
                    f.delete();
            }
            else
                System.out.println("[ ERROR ] : The specified file/directory does not exist.");
            System.gc();
            return;
        }
        catch (Exception E)
        {
            //troubleshooting tips here
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

    private final void rename(String oldFileName, String newFileName)throws Exception
    {
        try
        {
            oldFileName = _curDir + oldFileName;
            newFileName = _curDir + newFileName;
            if(checkFile(oldFileName)==true)
                new File(oldFileName).renameTo(new File(newFileName));
            else
                System.out.println("[ ERROR ] : The specified file/directory does not exist.");
            System.gc();            
            return;
        }
        catch (Exception E)
        {
            //troubleshooting tips here
            E.printStackTrace();
        }
    }

    private final void copy_move_frontend(boolean move, String source, String destination)throws Exception
    {
        try
        {
            copy_move_helper(new File(_curDir+source), new File(_curDir+destination));
            if(move==true)
                delHelper(new File(_curDir+source));
            System.gc();
            return;
        }
        catch(Exception E)
        {
            E.printStackTrace();
        }
    }

    private final void copy_move_helper( File src, File dest ) throws Exception 
    {
        try
        {
            if( src.isDirectory() )
            {
                dest.mkdirs();
                for( File sourceChild : src.listFiles() ) 
                {
                    File destChild = new File( dest, sourceChild.getName() );
                    copy_move_helper( sourceChild, destChild );
                }
            } 
            else
            {
                InputStream in = new FileInputStream(src);
                OutputStream out = new FileOutputStream(dest);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) 
                {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            }
            System.gc();
            return;
        }
        catch(Exception E)
        {
            E.printStackTrace();
        }
    }
}
