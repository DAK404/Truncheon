package Truncheon.API.Grinch;

import java.io.File;

import Truncheon.API.IOStreams;

class FileManagement
{
    private String _username = "";
    private String _name = "";
    
    public FileManagement(String username)
    {
        _username = username;
    }
    
    public final boolean fileManagerLogic()
    {
        if(! login)
        IOStreams.printError("Invalid Credentials! Grinch aborted!")
        else
        {
            String inputValue = "";
            do
            {
                inputValue = console.readLine();
                grinchInterpreter(inputValue);
            }
            while(inputValue.equalsIgnoreCase("exit"));
        }
    }
    
    public boolean fileManagerLogic(File scriptFileName, int lineNumber)
    {
        boolean status = true;

        try
        {
            //Initialize a stream to read the given file.
            BufferedReader br = new BufferedReader(new FileReader(_scriptName));
            
            //Initialize a string to hold the contents of the script file being executed.
            String scriptLine;
            
            for(int i = 0; i < lineNumber; i++)
                // Keep skipping until that specific line number has been reached
                br.readLine();
            
            while ((scriptLine = br.readLine()) != null)
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
            
            br.close();
        }
        catch(Exception E)
        {
            IOStreams.printError(e);
        }
        
        System.gc();

        return false;
    }
    
    private boolean loginChallenge()
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
                break;
                
                case "edit":
                break;
                
                case "open":
                break;
                
                case "home":
                break;
                
                case "pwd":
                break;
                
                case "cd":
                break;
                
                case "tree":
                break;
                
                case "ls":
                break;
                
                case "download":
                break;
                
                //Override exit to quit the module than to quit the program
                case "exit":
                case ""
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
}