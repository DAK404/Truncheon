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

    public boolean fileManagerLogic()
    {
        if(! login)
        {
            IOStreams.printError("Invalid Credentials! Grinch aborted!")
        }
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
        IOStreams.printError("WORK IN PROGRESS!");
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
        return status;
    }

    private void grinchInterpreter(String command)throws Exception
    {
        try
        {

        }
        catch(Exception E)
        {

        }
    }
}