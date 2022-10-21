package Truncheon.Core;

import java.io.Console;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import Truncheon.API.Anvil;
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

    private boolean moduleAckStatus = false;

    Console console = System.console();
    public void startNionKernel()throws Exception
    {
        customBuildInfoViewer();

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
        String tempInput = "";
        do
        {
            tempInput = console.readLine(_accountName + "@" + _systemName +"> ");
            commandProcessor(tempInput);
            System.gc();
        }
        while(! tempInput.equalsIgnoreCase("logout"));
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
                    {
                        System.out.println("Cannot execute sys command as a standard user.");
                        break;
                    }
                    if(commandArray.length < 2)
                    {
                        System.out.println("Syntax:\n\nsys \"<host_OS_command>\"");
                        break;
                    }
                    if(System.getProperty("os.name").contains("Windows"))
                        new ProcessBuilder("cmd", "/c", commandArray[1]).inheritIO().start().waitFor();
                    else
                        new ProcessBuilder("/bin/bash", "-c" , commandArray[1]).inheritIO().start().waitFor();
                    break;

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

                    case "charm_1":
                        String[] aaa = Truncheon.API.Anvil.splitStringToArray(console.readLine("DUMMY> "));

                        for(String aa:aaa)
                            System.out.println(aa);
                    break;

                    case "charm_2":
                        System.out.println(new Truncheon.API.Dragon.LoginAuth(new Truncheon.API.Minotaur.Cryptography().stringToSHA3_256(console.readLine("Username> "))).checkUserExistence());
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
            if(! moduleAckStatus)
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

                moduleAckStatus = console.readLine("Load Custom Modules?> ").equalsIgnoreCase("y")?true:false;
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
    }

    public void customBuildInfoViewer()
    {
        Truncheon.API.BuildInfo.clearScreen();
        IOStreams.println("Nion: " + Truncheon.API.BuildInfo._KernelName + "\n");
        IOStreams.println("Version: " + Truncheon.API.BuildInfo._Version + " (" + Truncheon.API.BuildInfo._VersionCodeName + ")\n");
        IOStreams.printWarning("TEST BUILD!\nExpect Changes And Errors.\n");
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