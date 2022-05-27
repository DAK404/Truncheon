import java.io.File;

public class Main
{
    private static final String errorHeader = """
    *********************************************************
                        NION PROGRAM LOADER
    *********************************************************
    
    #######################
     Build Details -
     Version : 7.241
     Date    : 14-MAY-2022
    #######################

    """;

    private static final String errorFooter = """
    
    *********************************************************
    """;
    
    private static final String INVALID_SYNTAX = """
    [ ERROR ] : INCORRECT LAUNCHER SYNTAX

    Description: The program requires a set of parameters
    that are required to boot the shell. Incorrect inputs
    are not accepted and the shell shall not boot unless
    the right arguments are provided.

    Generally, the syntax to invoke the shell is by using
    the following syntax:

    java <arguments> Main <kernel_name> <boot_mode>

    If the shell is unable to boot even after providing
    the right inputs, please try to download the latest
    version and installing the same.

    Press RETURN to exit.
    """;
    
    private static final String KERNEL_NOT_FOUND = """
    [ ERROR ] : KERNEL NOT FOUND

    Description: The Launcher program can boot any
    compatible kernels which has a Loader class. Please
    check if the kernel that you are trying to boot has
    a Loader class, which is generally found inside the
    Core directly.

    Additionally, the program can also probe bootable
    kernels for you using the probe command. Following
    syntax is used to probe whether the kernel is
    bootable or not:
    
    java <arguments> Main probe <kernel_name>
    
    Press RETURN to exit.
    """;
    
    private static final String UNDEFINED_BOOTMODE = """
    [ ATTENTION ] : UNDEFINED BOOT MODE
    
    Description: The specified boot parameter is not
    recognized by the kernel. Every kernel has its own
    set of boot modes. For more information, please
    try to refer to the documentation or contact the
    developer to fix this issue.

    Press RETURN to exit.
    """;
    
    private static final String FATAL_ERROR_EXIT = """
    [ ERROR ] : UNHANDLED EXIT

    Description: The program has encountered an
    exception which could not be recovered to continue.

    ALL unsaved data will be lost and the program shall
    exit.

    Press RETURN to exit.
    """;
    
    private static final String FATAL_ERROR_RESTART = """
    [ ERROR ] : UNHANDLED RESTART
    
    Description: The program has encountered an
    exception. The program shall restart to recover the
    session.

    ALL unsaved data might be lost.

    Press RETURN to restart.
    """;
    
    private static final String RESTART_UPDATE = """
    [ ATTENTION ] : RESTART UPDATE
    
    Description: The program is restarting to update the
    files. Please Wait...
    """;
    
    private static final String AUTOMATIC_REPAIR_MODE = """
    [ ERROR ] : AUTOMATIC REPAIR MODE

    Description: The program detected that various kernel
    files are corrupt. To provide a safe environment to
    the users, the program shall restart to recovery mode
    to repair it.

    Press RETURN to restart.
    """;
    
    public static void main(String[] args)throws Exception
    {
        if(args.length < 2)
        {
            System.err.println(errorHeader + INVALID_SYNTAX + errorFooter);
            System.in.read();
            System.exit(600);
        }

        ///////////////////////////////////////////////////////////////
        // DEBUG CODE! REMOVE BEFORE RELEASE!
        ///////////////////////////////////////////////////////////////
        if(args[0].equalsIgnoreCase("debug"))
        {
            System.out.println("PRINTING ALL STRINGS AVAILABLE");
            
            System.out.println(INVALID_SYNTAX + "\n");
            System.out.println(KERNEL_NOT_FOUND + "\n");
            System.out.println(UNDEFINED_BOOTMODE + "\n");
            System.out.println(FATAL_ERROR_EXIT + "\n");
            System.out.println(FATAL_ERROR_RESTART + "\n");
            System.out.println(RESTART_UPDATE + "\n");
            System.out.println(AUTOMATIC_REPAIR_MODE + "\n");
            System.exit(100);
        }
        else if(args.length == 2 && args[0].equalsIgnoreCase("probe"))
        {
            System.out.println("Kernel Probe 1.0");
            System.out.println(">Probing Kernel: " + args[1]);
            System.out.println("Result: Kernel Loader " + (new File("./"+args[1]+"/Core/Loader.class").exists()?"Exists":"Not Found, Not Bootable"));
            System.exit(0);
        }
        
        ///////////////////////////////////////////////////////////////
        // DEBUG CODE! REMOVE BEFORE RELEASE!
        ///////////////////////////////////////////////////////////////
        
        while(true)
        {
            System.gc();
            ProcessBuilder sessionMonitor=new ProcessBuilder("java", args[0]+".Core.Loader", args[1]);
            Process processMonitor = sessionMonitor.inheritIO().start();
            
            processMonitor.waitFor();
            
            switch(processMonitor.exitValue())
            {            

                case 0:
                System.exit(0);

                case 1:
                System.err.println(errorHeader + KERNEL_NOT_FOUND + errorFooter);
                System.exit(0);

                case 3:
                System.err.println(errorHeader + UNDEFINED_BOOTMODE + errorFooter);
                System.exit(0);

                case 4:
                System.err.println(errorHeader + FATAL_ERROR_EXIT + errorFooter);
                System.exit(0);

                case 5:
                System.err.println(errorHeader + FATAL_ERROR_RESTART + errorFooter);
                System.exit(0);

                case 6:
                System.err.println(errorHeader + RESTART_UPDATE + errorFooter);
                System.exit(0);

                case 7:
                System.err.println(errorHeader + AUTOMATIC_REPAIR_MODE + errorFooter);
                System.exit(0);

                case 8:
                System.err.println(errorHeader + AUTOMATIC_REPAIR_MODE + errorFooter);
                System.exit(0);
                /*

                -- TODO --

                REDO THE ENTIRE EXIT CODE HANDLING SYSTEM
                
                case 0:
                System.exit(0);
                
                case 1:
                System.err.println(errorHeader + KERNEL_NOT_FOUND + errorFooter);
                System.in.read();
                System.exit(1);
                
                // KERNEL RELATED EXIT CASES //
                case 2:
                System.err.println(errorHeader + UNDEFINED_BOOTMODE + errorFooter);
                System.in.read();
                System.exit(2);
                
                case 3:
                System.out.println("Restarting...");
                break;
                
                case 4:
                System.err.println(errorHeader + RESTART_UPDATE + errorFooter);
                break;

                */
                
                /* Reserved for future implementations
                case 0x1A0004:
                break;
                */
                
                /*
                // KERNEL RELATED ERROR EXIT CASES //
                case 5:
                System.err.println(errorHeader + FATAL_ERROR_EXIT + errorFooter);
                System.in.read();
                System.exit(3);
                
                case 6:
                System.err.println(errorHeader + FATAL_ERROR_RESTART + errorFooter);
                System.in.read();
                break;
                
                case 7:
                System.err.println(errorHeader + AUTOMATIC_REPAIR_MODE + errorFooter);
                args[1] = "repair";
                break;
                
                default:
                System.out.println("Default Unknown Exit");
                System.exit(100001);

                */
            }
        }
    }
}
