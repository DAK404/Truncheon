package Truncheon.API.Grinch;

//Import the required Java IO classes
import java.io.Console;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
* Program which provides the file manager functionality
*
* @version 0.4.8
* @since 0.2.9
* @author DAK404
*/
public class FileManager
{
    private String _user;
    private String _name;
    private String _curDir;
    private String _scriptName;
    private boolean _admin = false;
    private boolean _scriptMode = false;


    private Console console = System.console();

    /**
     * Constructor which will help in using the functionalities of the file manager
     */
    public FileManager()
    {

    }

    /**
    * Constructor to initialize the user details
    *
    * @param usn : The username of the account currently logged into the main menu
    * @param nm : The name of the account currently logged into the main menu
    * @param admin : The privileges of the account, determining if the user has admin rights
    */
    public FileManager(String usn, String nm, boolean admin)
    {
        _user = usn;
        _name = nm;
        _admin = admin;
    }

    // ------------------------------------------------------------------------------------ //
    //                                    PUBLIC METHODS                                    //
    // ------------------------------------------------------------------------------------ //

    /**
    * The logic of the file manager
    *
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    public final void fileManagerLogic()throws Exception
    {
        try
        {
            prerequisites();
            
            new Truncheon.API.BuildInfo().versionViewer();
            System.out.println("Grinch File Manager 1.10.0");
            while(fileManagerShell(console.readLine(_name+"@"+_curDir.replace(_user, _name)+">: ")));
        }
        catch(Exception E)
        {
            //Handle any exceptions thrown during runtime
            new Truncheon.API.ErrorHandler().handleException(E);
        }
    }

    /**
    *
    * @param sName
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    public final void fileManagerLogic(String sName)throws Exception
    {
        try
        {
            if( ! _admin  && !(new Truncheon.API.Minotaur.PolicyEnforcement().checkPolicy("script")))
            return;

            System.gc();
            if(sName == null || sName.equalsIgnoreCase("") || sName.startsWith(" ") || new File(sName).isDirectory() )
            {
                System.out.println("[ ERROR ] : The name of the script file is invalid.");
                return;
            }

            //Check if the script file specified exists.
            if( ! (new File("./Users/Truncheon/"+_user+"/"+sName+".fmx").exists()) )
            {
                //Return an error and pass the control back in case the file is not found.
                System.out.println("[ ATTENTION ] : Script file "+sName.replace(_user, _name)+" has not been found.\nPlease check the directory of the script file and try again.");
                return;
            }


            if(! authenticationLogic())
            {
                System.out.println("Authentication failed. Please try again with valid credentials.");
                return;
            }

            resetToHomeDir();
            _scriptName = "./Users/Truncheon/"+_user+"/"+sName+".fmx";

            //else begin executing the script.
            executeScriptFile();
            System.gc();
        }
        catch (Exception E)
        {
            E.printStackTrace();
        }
    }

    // ------------------------------------------------------------------------------------ //
    //                                   PRIVATE METHODS                                    //
    // ------------------------------------------------------------------------------------ //

    // ************************************************************************************ //
    //                                 LOGIN PROCEDURE START                                //
    // ************************************************************************************ //

    private void prerequisites()throws Exception
    {
        try
        {
            if( !_admin  && ! (new Truncheon.API.Minotaur.PolicyEnforcement().checkPolicy("filemanager")) )
            return;

            if(! authenticationLogic())
            {
                System.out.println("Authentication failed. Cannot access Grinch.");
                return;
            }

            resetToHomeDir();
        }
        catch(Exception E)
        {

        }
    }


    /**
    * Logic to authenticate the user into the file manager module.
    *
    * @return boolean : Returns the status of the credential authentication 
    * @throws Exception : Handle exceptions thrown during program runtime
    */
    private final boolean authenticationLogic()throws Exception
    {
        try
        {
            new Truncheon.API.BuildInfo().versionViewer();
            System.out.println("[ ATTENTION ] : This module requires the user to authenticate to continue. Please enter the user credentials.");

            System.out.println("Username: " + _name);
            String password=new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(String.valueOf(console.readPassword("Password: ")));
            String securityKey=new Truncheon.API.Minotaur.HAlgos().stringToSHA3_256(String.valueOf(console.readPassword("Security Key: ")));

            return new Truncheon.API.Dragon.LoginAPI(_user, password, securityKey).status();
        }
        catch(Exception E)
        {
            //Handle any exceptions thrown during runtime
            new Truncheon.API.ErrorHandler().handleException(E);
        }
        return false;
    }

    // ************************************************************************************ //
    //                                  LOGIN PROCEDURE END                                 //
    // ************************************************************************************ //

    // ************************************************************************************ //
    //                                 SCRIPT HANDLING START                                //
    // ************************************************************************************ //

    /**
    * Runs the script file when there is a script file as an argument
    *
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final void executeScriptFile()throws Exception
    {
        try
        {
            //Activate the script mode.
            _scriptMode = true;

            //Initialize a stream to read the given file.
            BufferedReader br = new BufferedReader(new FileReader(_scriptName));

            //Initialize a string to hold the contents of the script file being executed.
            String scriptLine;

            //Read the script file, line by line.
            while ((scriptLine = br.readLine()) != null)
            {
                //Check if the line is a comment or is blank in the script file and skip the line.
                if(scriptLine.startsWith("#") || scriptLine.equalsIgnoreCase(""))
                continue;

                //Check if End Script command is encountered, which will stop the execution of the script.
                else if(scriptLine.equalsIgnoreCase("End Script"))
                break;

                //Read the command in the script file, and pass it on to menuLogic(<command>) for it to be processed.
                fileManagerShell(scriptLine);
            }

            //Close the streams, run the garbage collector and clean.
            br.close();

            //Deactivate the script mode.
            _scriptMode = false;
        }
        catch(Exception E)
        {
            E.printStackTrace();
        }
    }

    // ************************************************************************************ //
    //                                  SCRIPT HANDLING END                                 //
    // ************************************************************************************ //

    // ************************************************************************************ //
    //                                FILE MANAGER LOGIC START                              //
    // ************************************************************************************ //

    /**
    *
    * @param input
    * @return
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final boolean fileManagerShell(String input)throws Exception
    {
        try
        {
            String[] cmd = input.split(" (?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            if(cmd.length > 1)
            cmd[1] = cmd[1].replaceAll("\"", "");

            switch(cmd[0].toLowerCase())
            {
                case "home":
                resetToHomeDir();
                break;

                case "script":
                //Check for the correct script syntax.
                if(cmd.length <= 1)
                {
                    //Print the correct syntax if script syntax is malformed and return the program control.
                    System.out.println("\nScript Syntax:\n\nscript <script_name/path>\n");
                    break;
                }

                if(_scriptMode & _scriptName.equals(cmd[1]))
                {
                    System.out.println(_scriptName + " - Cannot Recursively Execute scripts.");
                    break;
                }
                else
                {
                    _scriptName = cmd[1];
                    fileManagerLogic("./Users/"+_user+"/"+cmd[1]+".fmx");
                    _scriptName = "";
                }
                break;

                case "echo":
                if(cmd.length < 2)
                {
                    System.out.println("Echo Syntax: echo \"<string>\"");
                    break;
                }
                if(cmd[1].equalsIgnoreCase(null))
                System.out.println("null");
                else
                System.out.println(cmd[1]);
                break;

                case "exit":
                new Truncheon.API.BuildInfo().versionViewer();
                return false;

                case "?":
                case "help":
                new Truncheon.API.Wraith.ReadFile().showHelp("HelpDocuments/Grinch.manual");
                break;

                case "":
                break;

                case "clear":
                new Truncheon.API.BuildInfo().clearScreen();
                break;

                case "cd":
                if(cmd.length < 2)
                {
                    System.out.println("Syntax:\n\ncd <directory_name>\n\nOR\n\ncd ..\n");
                    break;
                }
                changeDir(cmd[1]);
                break;

                case "ls":
                ls();
                break;

                case "tree":
                treeView();
                break;

                case "mkdir":
                if(cmd.length < 2)
                {
                    System.out.println("Syntax:\n\nmkdir <directory_name>\n");
                    break;
                }
                makeDir(cmd[1]);
                break;

                case "rm":
                if(cmd.length < 2)
                {
                    System.out.println("Syntax:\n\nrm <directory_name>\n\nOR\n\nrm <filename>\n");
                    break;
                }
                del(cmd[1]);
                break;

                case "rename":
                if(cmd.length < 3)
                {
                    System.out.println("Syntax:\n\nrename <directory_name> <new_directory_name>\n\nOR\n\nrename <filename> <new_filename>\n");
                    break;
                }
                rename(cmd[1], cmd[2]);
                break;

                case "cp":
                case "copy":
                if(cmd.length < 3)
                {
                    System.out.println("Syntax:\n\ncopy <directory_name><new_directory_name>\ncp <directory_name><new_directory_name>\n\nOR\n\ncopy <filename> <new_filename>\ncp <filename> <new_filename>\n");
                    break;
                }
                copyMove(false, cmd[1], cmd[2]);
                break;

                case "mv":
                case "move":
                if(cmd.length < 3)
                {
                    System.out.println("Syntax:\n\nmove <directory_name><new_directory_name>\nmv <directory_name><new_directory_name>\n\nOR\n\nmove <filename> <new_filename>\nmv <filename> <new_filename>\n");
                    break;
                }
                copyMove(true, cmd[1], cmd[2]);
                break;

                case "read":
                if(cmd.length < 2)
                {
                    System.out.println("Syntax:\n\nread <filename>\n");
                    break;
                }
                new Truncheon.API.Wraith.ReadFile().readUserFile(cmd[1], _curDir);
                break;

                case "write":
                if(cmd.length < 2)
                {
                    System.out.println("Syntax:\n\nwrite <filename>\n");
                    break;
                }
                new Truncheon.API.Wraith.WriteFile().editFile(cmd[1], _curDir);
                break;

                case "download":
                if(cmd.length < 3)
                {
                    System.out.println("Syntax:\n\ndownload <URL> <filename>\n");
                    break;
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
            //Handle any exceptions thrown during runtime
            new Truncheon.API.ErrorHandler().handleException(E);
        }
        return false;
    }

    // ************************************************************************************ //
    //                                 FILE MANAGER LOGIC END                               //
    // ************************************************************************************ //

    // ************************************************************************************ //
    //                             COMMAND PROCESSOR LOGIC START                            //
    // ************************************************************************************ //

    /**
    *
    * @param tPath
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final void changeDir(String tPath)throws Exception
    {
        if(tPath.equals(".."))
        {
            prevDir();
            System.gc();
            return;
        }

        tPath = _curDir + tPath + "/";
        if(checkFile(tPath))
        _curDir=tPath;
        else
        System.out.println("[ ERROR ] : The specified file/directory does not exist.");
        System.gc();
    }

    /**
    *
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final void prevDir()throws Exception
    {
        _curDir = _curDir.substring(0, _curDir.length() - 1);
        _curDir = _curDir.replace(_curDir.substring(_curDir.lastIndexOf('/'), _curDir.length()), "/");

        if(_curDir.equals("./Users/Truncheon/"))
        {
            System.out.println("[ WARNING ] : Permission Denied.");
            resetToHomeDir();
        }
        System.gc();
    }

    /**
    *
    * @param fName
    * @return
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final boolean checkFile(String fName)throws Exception
    {
        return new File(fName).exists();
    }

    /**
    *
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final void treeView()throws Exception
    {
        try
        {
            File tree=new File(_curDir);
            System.out.println("\n--- [ TREE VIEW ] ---\n");
            treeViewHelper(0, tree);
            System.out.println();
            System.gc();
        }
        catch(Exception E)
        {
            E.printStackTrace();
        }
    }

    /**
    *
    * @param indent
    * @param file
    */
    private final void treeViewHelper(int indent, File file)
    {
        System.out.print("|");

        for (int i = 0; i < indent; ++i)
        System.out.print('-');

        System.out.println(file.getName().replace(_user, _name + " [ USER ROOT DIRECTORY ]"));

        if (file.isDirectory())
        {
            File[] files = file.listFiles();

            for (int i = 0; i < files.length; ++i)
            treeViewHelper(indent + 2, files[i]);
        }
    }

    /**
    *
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final void ls()throws Exception
    {
        //String format = "%1$-60s|%2$-50s|%3$-20s\n";
        String format = "%1$-32s| %2$-24s| %3$-10s\n";
        String c = "-";
        if(checkFile(_curDir))
        {
            File dPath=new File(_curDir);
            System.out.println("\n");
            String disp = (String.format(format, "Directory/File Name", "File Size [In KB]","Type"));
            System.out.println(disp + c.repeat(disp.length()) + "\n");
            for(File file : dPath.listFiles())
            {
                //System.out.format(String.format(format, file.getPath().replace(User,Name), file.getName().replace(User,Name), file.length()/1024+" KB"));
                System.out.format(String.format(format, file.getName().replace(_user, _name), file.length()/1024+" KB", file.isDirectory()?"Directory":"File"));
            }
            System.out.println();
        }
        else
        System.out.println("[ ERROR ] : The specified file/directory does not exist.");
        System.gc();
    }

    /**
    *
    * @param mkFile
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final void makeDir(String mkFile)throws Exception
    {
        try
        {
            mkFile=_curDir+mkFile + "/";
            if(! checkFile(mkFile))
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

    /**
    *
    * @param delFile
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final void del(String delFile)throws Exception
    {
        try
        {
            delFile = _curDir+delFile;
            if(checkFile(delFile))
            {
                File f=new File(delFile);
                if(f.isDirectory())
                delHelper(f);
                else
                f.delete();
            }
            else
            System.out.println("[ ERROR ] : The specified file/directory does not exist.");
            System.gc();
        }
        catch (Exception E)
        {
            //troubleshooting tips here
            E.printStackTrace();
        }
    }

    /**
    *
    * @param delfile
    * @throws Exception : Handle exceptions thrown during program runtime.
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

    /**
    *
    * @param oldFileName
    * @param newFileName
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final void rename(String oldFileName, String newFileName)throws Exception
    {
        try
        {
            oldFileName = _curDir + oldFileName;
            newFileName = _curDir + newFileName;
            
            if(checkFile(oldFileName))
            new File(oldFileName).renameTo(new File(newFileName));
            else
            System.out.println("[ ERROR ] : The specified file/directory does not exist.");
            System.gc();
        }
        catch (Exception E)
        {
            //troubleshooting tips here
            E.printStackTrace();
        }
    }

    /**
    *
    * @param move
    * @param source
    * @param destination
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    private final void copyMove(boolean move, String source, String destination)throws Exception
    {
        try
        {
            if(! checkFile(_curDir + source))
            {
                System.out.println("The Source File Does Not Exist");
                return;
            }
            if(! checkFile(_curDir + destination))
            {
                System.out.println("The Destination File Does Not Exist");
                return;
            }
            copyMoveHelper(new File(_curDir+source), new File(_curDir+destination + "/" + source));
            if(move)
            delHelper(new File(_curDir+source));
            System.gc();
            return;
        }
        catch(Exception E)
        {
            E.printStackTrace();
        }
    }

    /**
    *
    * @param src
    * @param dest
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    public final void copyMoveHelper( File src, File dest ) throws Exception
    {
        try
        {
            if( src.isDirectory() )
            {
                dest.mkdirs();
                for( File sourceChild : src.listFiles() )
                {
                    File destChild = new File( dest, sourceChild.getName() );
                    copyMoveHelper( sourceChild, destChild );
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
        }
        catch(Exception E)
        {
            E.printStackTrace();
        }
        System.gc();
    }

    /**
     * 
     */
    private final void resetToHomeDir()
    {
        _curDir="./Users/Truncheon/"+_user+'/';
    }

    // ************************************************************************************ //
    //                              COMMAND PROCESSOR LOGIC END                             //
    // ************************************************************************************ //
}
