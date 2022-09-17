package Truncheon.Core;

import java.io.Console;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

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

    Console console = System.console();
    public void startNionKernel()throws Exception
    {
        //Placeholder method for the implementation of the Kernel Logic

        IOStreams.printAttention("Work in progress.");
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
        return new Truncheon.API.Dragon.LoginAuth(_username).authenticationLogic(new Truncheon.API.Minotaur.Cryptography().stringToSHA3_256(String.valueOf(console.readPassword("Password: "))), new Truncheon.API.Minotaur.Cryptography().stringToSHA3_256(String.valueOf(console.readPassword("Security Key: "))));
    }

    private void kernelLogic()
    {
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
            //pass the control to Anvil first to check for the core commandset
            if(! Truncheon.API.Anvil.anvilInterpreter(command))
            {
                //then check the module related commandset
                String[] commandArray = Truncheon.API.Anvil.splitStringToArray(command);
                
                switch(commandArray[0].toLowerCase())
                {
                    case "mem":
                        debug();
                    break;

                    case "bsod":
                        throw new Exception("Debug BSOD. You failed successfully!");

                    case "exit":
                        System.exit(0);
                    break;

                    case "load":
                        if(commandArray.length < 2)
                        {
                            System.out.println("goober");
                        }
                        else
                        {
                            moduleLoader(commandArray[1]);
                        }
                    break;

                    case "logout":
                    case "":
                    break;

                    default:
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

    private void moduleLoader(String targetClassName)
    {
        try 
        {
            // Create a new JavaClassLoader 
            ClassLoader classLoader = this.getClass().getClassLoader();
             
            // Load the target class using its binary name
            Class loadedMyClass = classLoader.loadClass(targetClassName);
             
            System.out.println("Loaded class name: " + loadedMyClass.getName());
             
            // Create a new instance from the loaded class
            Constructor constructor = loadedMyClass.getConstructor();
            Object myClassObject = constructor.newInstance();
             
            // Getting the target method from the loaded class and invoke it using its name
            Method method = loadedMyClass.getMethod("runModule");

            System.out.println("Invoked method name: " + method.getName());
            method.invoke(myClassObject);
        } 
        catch (ClassNotFoundException e)
        {
            IOStreams.printError("The specified module cannot be found!\n\nThe module is either malformed, corrupt, unreadable or does not exist.\nPlease check the module used and try again!");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void customBuildInfoViewer()
    {
        System.out.println("Work in progress.");
    }

    private void debug()
    {
        int mb = 1024 * 1024;
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
