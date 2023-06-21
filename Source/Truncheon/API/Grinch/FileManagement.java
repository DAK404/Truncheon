package Truncheon.API.Grinch;

import java.io.Console;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;

import Truncheon.API.BuildInfo;
import Truncheon.API.IOStreams;

public class FileManagement
{
    private String _username = "";
    private String _name = "";
    private String _defaultPath = "";
    private String _presentWorkingDir = "/";

    //during ops, do the following
    // _defaultPath + _presentWorkingDir
    //_default Path need not be repeated for all ops
    //Also the string is shorter and is able to be managed well.

    Console console = System.console();
    
    public FileManagement(String username)throws Exception
    {
        _username = username;
        _name = new Truncheon.API.Dragon.LoginAuth(_username).getNameLogic();
        _defaultPath = "./Users/Truncheon/" + _username;
    }
    
    public final void fileManagerLogic()throws Exception
    {
        if(! loginChallenge())
            IOStreams.printError("Invalid Credentials! Grinch aborted!");
        else
        {
            BuildInfo.viewBuildInfo();
            String inputValue = "";
            do
            {
                inputValue = console.readLine(_name + "@" + _presentWorkingDir + "&> ");
                grinchInterpreter(inputValue);
            }
            while(! inputValue.equalsIgnoreCase("exit"));
        }
    }
    
    public boolean fileManagerLogic(File scriptFileName, int lineNumber)
    {
        boolean status = true;
        int i = 0;

        try
        {
            //Initialize a stream to read the given file.
            BufferedReader br = new BufferedReader(new FileReader(scriptFileName));
            
            //Initialize a string to hold the contents of the script file being executed.
            String scriptLine;
            
            while ((scriptLine = br.readLine()) != null)
            {
                if(! (i < lineNumber))
                {
                    //Check if the line is a comment or is blank in the script file and skip the line.
                    if(scriptLine.startsWith("#") || scriptLine.equalsIgnoreCase(""))
                        continue;
                
                    //Check if End Script command is encountered, which will stop the execution of the script.
                    else if(scriptLine.equalsIgnoreCase("End Script"))
                        break;
                    
                    //Read the command in the script file, and pass it on to menuLogic(<command>) for it to be processed.
                    grinchInterpreter(scriptLine);
                }
                i++;
            }
            
            br.close();
        }
        catch(Exception e)
        {
            IOStreams.printError(e.toString());
        }
        
        System.gc();

        return status;
    }
    
    private boolean loginChallenge()throws Exception
    {
        boolean status = false;
        if(! (_username == null || _username.equalsIgnoreCase("")))
        {
            IOStreams.println("Username: " + _name);
            status =  new Truncheon.API.Dragon.LoginAuth(_username).authenticationLogic(new Truncheon.API.Minotaur.Cryptography().stringToSHA3_256(String.valueOf(console.readPassword("Password: "))), new Truncheon.API.Minotaur.Cryptography().stringToSHA3_256(String.valueOf(console.readPassword("Security Key: "))));
        }
        else
            System.exit(1);
        
        return status;
    }
    
    private void grinchInterpreter(String command)throws Exception
    {
        try
        {
            String[] commandArray = Truncheon.API.Anvil.splitStringToArray(command);
            switch(commandArray[0].toLowerCase())
            {
                case "execute":
                break;

                case "cut":
                break;
                
                case "copy":
                break;
                
                case "delete":
                break;
                
                case "rename":
                break;
                
                case "mkdir":
                if(commandArray.length < 2)
                    IOStreams.printError("Invalid Syntax for command \'mkdir\'.");
                else
                    makeDir(commandArray[1]);
                break;
                
                case "edit":
                break;
                
                case "open":
                break;
                
                case "home":
                break;
                
                case "pwd":
                IOStreams.println(_defaultPath);
                IOStreams.println(_defaultPath + _presentWorkingDir);
                break;
                
                case "cd":
                if(commandArray.length < 2)
                    IOStreams.printError("Invalid Syntax for command \'cd\'.");
                else
                    changeDirectory(commandArray[1]);
                break;
                
                case "tree":
                break;
                
                case "dir":
                case "ls":
                    listEntitiesInDirectory();
                break;
                
                case "download":
                break;
                
                //Override exit to quit the module than to quit the program
                case "exit":
                case "":
                break;
                
                //Use the Anvil functions if none of the cases are followed
                default:
                Truncheon.API.Anvil.anvilInterpreter(commandArray[0]);
                break;
            }
        }
        catch(Exception E)
        {
            
        }
    }

    private void changeDirectory(String destination)throws Exception
    {
        if(destination.equals(".."))
        {
            navToPreviousDir();
        }
        else
        {
            if(destination.startsWith("/")) 
                destination = destination.substring(1, destination.length());
            if(destination.endsWith("/"))
                destination = destination.substring(0, destination.length()-1);

            String tempPath = _presentWorkingDir + destination + "/";
            if(checkFileExistence(_defaultPath + tempPath))
                _presentWorkingDir = tempPath;
            else
                IOStreams.printError("The specified destination path/file does not exist.");
        }
    }

    private boolean checkFileExistence(String fileName)throws Exception
    {
        return new File(fileName).exists();
    }

    private void navToPreviousDir()throws Exception
    {
        if(_presentWorkingDir.length() > 1)
        {
            _presentWorkingDir = _presentWorkingDir.substring(0, _presentWorkingDir.length()-1);
            _presentWorkingDir = _presentWorkingDir.replace(_presentWorkingDir.substring(_presentWorkingDir.lastIndexOf('/'), _presentWorkingDir.length()), "/");
        }
        else
        {
            IOStreams.printError("Illegal Operation. Permission Denied.");
            resetToHomeDir();
        }
    }

    private void resetToHomeDir()throws Exception
    {
        _presentWorkingDir = "/";
    }

    private void makeDir(String fileName)throws Exception
    {
        new File(_defaultPath + _presentWorkingDir + fileName).mkdirs();
    }

    private void listEntitiesInDirectory()throws Exception
    {
        //String format = "%1$-60s|%2$-50s|%3$-20s\n";
        String format = "%1$-32s| %2$-24s| %3$-10s\n";
        String c = "-";
        if(checkFileExistence(_presentWorkingDir))
        {
            File dPath=new File(_defaultPath + _presentWorkingDir);
            System.out.println("\n");
            String disp = (String.format(format, "Directory/File Name", "File Size [In KB]","Type"));
            System.out.println(disp + c.repeat(disp.length()) + "\n");
            for(File file : dPath.listFiles())
            {
                //System.out.format(String.format(format, file.getPath().replace(User,Name), file.getName().replace(User,Name), file.length()/1024+" KB"));
                System.out.format(String.format(format, file.getName().replace(_username, _name), file.length()/1024+" KB", file.isDirectory()?"Directory":"File"));
            }
            System.out.println();
        }
        else
        System.out.println("[ ERROR ] : The specified file/directory does not exist.");
        System.gc();
    }
}