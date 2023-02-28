package Truncheon.Core;

import java.io.Console;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import Truncheon.API.IOStreams;

//Previously known as MainMenu.java
//Implement all the older functionalities here
public class NionKernel extends ClassLoader
{
    private String _username = "DEFAULT_USER";
    private String _accountName = "DEFAULT_ACC_NAME";
    private String _systemName = "";
    private String _PIN = "ERROR_PIN";
    private boolean _admin = false;

    private boolean _scriptMode = false;
    private String _scriptFileName = "";
    private int _lineNumber = 0;

    private boolean _moduleAckStatus = false;

    private char _prompt;

    Console console = System.console();
    public void startNionKernel()throws Exception
    {
        Truncheon.API.BuildInfo.viewBuildInfo();

        if(!login())
        {
            IOStreams.printError("Invalid Credentials.");
        }
        else
        {
            _admin = new Truncheon.API.Dragon.LoginAuth(_username).checkPrivilegeLogic();
            _accountName = new Truncheon.API.Dragon.LoginAuth(_username).getNameLogic();
            _PIN = new Truncheon.API.Dragon.LoginAuth(_username).getPINLogic();
            _systemName = new Truncheon.API.Minotaur.PolicyEnforce().retrievePolicyValue("sysname");
            _prompt = (_admin)?'!':'*';
            kernelLogic();
        }
    }

    private boolean login()throws Exception
    {
        _username = new Truncheon.API.Minotaur.Cryptography().stringToSHA3_256(console.readLine("Username: "));
        return  new Truncheon.API.Dragon.LoginAuth(_username).authenticationLogic(new Truncheon.API.Minotaur.Cryptography().stringToSHA3_256(String.valueOf(console.readPassword("Password: "))), new Truncheon.API.Minotaur.Cryptography().stringToSHA3_256(String.valueOf(console.readPassword("Security Key: "))));
    }

    private void kernelLogic()
    {
        customBuildInfoViewer();

        //add logic to check if there is a startup script file

        String tempInput = "";
        do
        {
            tempInput = console.readLine(_accountName + "@" + _systemName + _prompt + "> ");
            commandProcessor(tempInput);
            System.gc();
        }
        while(! tempInput.equalsIgnoreCase("logout"));
        Truncheon.API.BuildInfo.viewBuildInfo();
    }

    private boolean anvilScriptEngine(String scriptFileName)throws Exception
    {
        boolean status = false;

        if(! _admin && ! new Truncheon.API.Minotaur.PolicyEnforce().checkPolicy("script"))
        {
            //to process the anvil script files
            try
            {
                //Check if the name of the script file is a valid string
                if(scriptFileName == null || scriptFileName.equalsIgnoreCase("") || scriptFileName.startsWith(" "))
                    IOStreams.printError("The name of the script file cannot be be blank.");

                //Check if the script file specified exists.
                else if(! new File(scriptFileName).exists())
                    //Return an error and pass the control back in case the file is not found.
                    IOStreams.printAttention("Script file "+scriptFileName.replace(_username, _accountName)+" has not been found.\nPlease check the directory of the script file and try again.");

                else
                {
                    //Activate the script mode.
                    _scriptMode = true;

                    //Initialize a stream to read the given file.
                    BufferedReader br = new BufferedReader(new FileReader(scriptFileName));

                    //Initialize a string to hold the contents of the script file being executed.
                    String scriptLine = "";

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
                        commandProcessor(scriptLine);
                        _lineNumber++;
                    }

                    //Close the streams, run the garbage collector and clean.
                    br.close();
                    _lineNumber = 0;
                    System.gc();

                    //Deactivate the script mode.
                    _scriptMode = false;
                }
            }
            catch(Exception e)
            {
                //handle exception
            }
        }
        else
        {
            IOStreams.printError("Insufficient Privileges to run scripts! Please contact the Administrator for more information.");
        }
        return status;
    }

    private void commandProcessor(String command)
    {
        try
        {
            
            {
                //then check the module related commandset
                String[] commandArray = Truncheon.API.Anvil.splitStringToArray(command);

                switch(commandArray[0].toLowerCase())
                {
                    case "clear":
                        customBuildInfoViewer();
                    break;
                    
                    case "mem":
                        debug();
                    break;

                    case "bsod":
                        throw new Exception("Debug BSOD. You failed successfully!");

                    case "exit":
                        System.exit(0);
                    break;

                    case "lock":
                        System.out.println(_PIN);
                    break;

                    case "load":
                        if(commandArray.length < 2)
                        {
                            System.out.println("goober");
                        }
                        else
                        {
                            /* What to implement next:
                             * Module listing
                             * Module import
                             * Module uninstall
                             * Module download (optional)
                             */
                            String[] moduleCommandArray = Arrays.copyOfRange(commandArray, 1, commandArray.length);
                            moduleLoader(commandArray[1], moduleCommandArray);
                        }
                    break;

                    case "update":
                        new Truncheon.API.Grinch.Wyvern.Updater().updaterLogic();
                    break;

                    case "policy":
                        new Truncheon.API.Minotaur.PolicyEdit().policyEditorLogic();
                    break;

                    case "sys":
                    if(! _admin)
                        System.out.println("Cannot execute sys command as a standard user.");
                    else if(commandArray.length < 2)
                    {
                        System.out.println("Syntax:\n\nsys \"<host_OS_command>\"");
                        break;
                    }
                    else
                    {
                        if(System.getProperty("os.name").contains("Windows"))
                            new ProcessBuilder("cmd", "/c", commandArray[1]).inheritIO().start().waitFor();
                        else
                            new ProcessBuilder("/bin/bash", "-c" , commandArray[1]).inheritIO().start().waitFor();
                    }
                    break;

                    case "syshell":
                    if(! _admin)
                        IOStreams.printError("Cannot execute SYSHELL command as a standard user.");
                    else
                    {
                        //Catch any potential errors that may arise from trying to invoke the system shells
                        try
                        {
                            //Condition to detect if the OS is Windows
                            if(System.getProperty("os.name").contains("Windows"))
                            new ProcessBuilder("cmd").inheritIO().start().waitFor();

                            //Defaults to a linux style of BASH, trying to invoke the system shell, inside Truncheon
                            else
                            new ProcessBuilder("/bin/bash").inheritIO().start().waitFor();
                        }
                        //Catch any exceptions raised when trying to invoke the Shell
                        catch(Exception E)
                        {
                            IOStreams.printError("CANNOT INVOKE SYSTEM SHELL!\nPlease contact the System Administrator for more information.");
                            IOStreams.printError("\nERROR DETAILS:\n\n" + E + "\n"); 
                        }
                    }
                    break;

                    //User management logic
                    case "usermgmt":
                        switch(commandArray[1])
                        {
                            case "add":
                                new Truncheon.API.Dragon.AccountCreate().AccountCreateLogic(_username);
                            break;

                            case "delete":
                                new Truncheon.API.Dragon.AccountDelete(_username).userDeletionLogic();
                            break;

                            default:
                                IOStreams.printError(commandArray[1] + " is not a valid User Management program.");
                            break;
                        }
                    break;

                    case "grinch":
                    if(_scriptMode)
                        new Truncheon.API.Grinch.FileManagement(_username).fileManagerLogic(new File(_scriptFileName), _lineNumber);
                    else
                        new Truncheon.API.Grinch.FileManagement(_username).fileManagerLogic();
                    break;

                    case "logout":
                    case "":
                    break;

                    default:
                        //pass the control to Anvil first to check for the core commandset
                        if(! Truncheon.API.Anvil.anvilInterpreter(command))
                            IOStreams.printError(command + " is not recognized as an internal or external command, operable program or batch file");
                    break;
                }
            }
        }
        catch(Exception e)
        {
            new Truncheon.API.ExceptionHandler().handleException(e);
        }
        System.gc();
    }

    //UNSTABLE! WORK IN PROGRESS! DO NOT USE AS PRODUCTION READY CODE!//

    private void moduleLoader(String targetModuleName, String[] parameters)throws Exception
    {
        if(new Truncheon.API.Minotaur.PolicyEnforce().checkPolicyQuiet("module") | _admin)
        {
            if(! _moduleAckStatus)
            {
                String message = """
                YOU ARE LOADING A CUSTOM MODULE INTO TRUNCHEON!

                Custom modules are written and loaded into Truncheon.
                These modules reside in the memory until the program is restarted.

                These programs are not official, and therefore
                THIS ACTION REQUIRES THE USER TO ACKNOWLEDGE THIS MESSAGE!

                BY LOADING CUSTOM MODULES, YOU ARE RESPONSIBLE FOR THE LOSS OF DATA 
                OR ANY DAMAGES THAT MAY ARISE DUE TO THE USE OF THESE MODULES!
                
                Do you wish to still load the module? [ Y | N ]
                """;

                IOStreams.printAttention(message);

                _moduleAckStatus = console.readLine("Load Custom Modules?> ").equalsIgnoreCase("y");
            }
            else
            {
                try
                {
                    // Create a new JavaClassLoader
                    //ClassLoader classLoader = this.getClass().getClassLoader();
                    ClassLoader classLoader = ClassLoader.getSystemClassLoader();

                    // Load the target class using its binary name
                    Class<?> moduleLoader = classLoader.loadClass("Truncheon.Modules." + targetModuleName + ".ModuleRunner");

                    //System.out.println("Loaded Module: " + loadedMyClass.getName());

                    // Create a new instance from the loaded class
                    //Constructor constructor = loadedMyClass.getConstructor();

                    Object moduleInstance = moduleLoader.getDeclaredConstructor().newInstance();

                    // Getting the target method from the loaded class and invoke it using its name
                    Method method = moduleLoader.getMethod("runModule", new Class[]{String[].class});

                    method.invoke(moduleInstance, new Object[]{parameters});

                    System.gc();
                }
                catch (ClassNotFoundException e)
                {
                    IOStreams.printError("The specified module cannot be found!\n\nThe module is either malformed, corrupt, unreadable or does not exist.\nPlease check the module used and try again!");
                }
                catch (SecurityException e)
                {
                    e.printStackTrace();
                }
                catch (IllegalArgumentException e)
                {
                    e.printStackTrace();
                }
                catch (InstantiationException e)
                {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
                catch (NoSuchMethodException e)
                {
                    e.printStackTrace();
                }
                catch (InvocationTargetException e)
                {
                    e.printStackTrace();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        else
            IOStreams.printError("Module Loading has been restricted to user accounts with Administrator privileges only!\nPlease contact the Administrator for more information.");
        System.gc();
    }

    public void customBuildInfoViewer()
    {
        Truncheon.API.BuildInfo.clearScreen();
        IOStreams.println(Truncheon.API.BuildInfo._Branding);
        IOStreams.println("Version: " + Truncheon.API.BuildInfo._Version + " (" + Truncheon.API.BuildInfo._VersionCodeName + ")\n");
        IOStreams.printWarning("TEST BUILD!\nExpect Changes And Errors.\n");
        IOStreams.println("=============================");
        IOStreams.println("Privileges: " + (_admin?"Administrator":"Standard") + "\n");
    }

    private void debug()
    {
        //int mb = 1024 * 1024;
        // get Runtime instance
        Runtime instance = Runtime.getRuntime();
        System.out.println("\n*********************************************");
        System.out.println("        ---   DEBUG INFORMATION   ---        ");
        System.out.println("*********************************************");
        System.out.println("\n   - Heap utilization statistics [MB] -  \n");
        System.out.println("      [*]  Process ID   : "+ProcessHandle.current().pid());
        // available memory
        System.out.println("      [*]  Total Memory : " + instance.totalMemory() + " Bytes");
        // free memory
        System.out.println("      [*]  Free Memory  : " + instance.freeMemory() + " Bytes");
        // used memory
        System.out.println("      [*]  Used Memory  : " + (instance.totalMemory() - instance.freeMemory()) + " Bytes");
        // Maximum available memory
        System.out.println("      [*]  Max Memory   : " + instance.maxMemory() + " Bytes");
        System.out.println("\n*********************************************\n\n");
    }
}
