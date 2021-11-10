/*
*    ███    ██ ██  ██████  ███    ██        ████████ ██████  ██    ██ ███    ██  ██████ ██   ██ ███████  ██████  ███    ██
*    ████   ██ ██ ██    ██ ████   ██ ██        ██    ██   ██ ██    ██ ████   ██ ██      ██   ██ ██      ██    ██ ████   ██
*    ██ ██  ██ ██ ██    ██ ██ ██  ██           ██    ██████  ██    ██ ██ ██  ██ ██      ███████ █████   ██    ██ ██ ██  ██
*    ██  ██ ██ ██ ██    ██ ██  ██ ██ ██        ██    ██   ██ ██    ██ ██  ██ ██ ██      ██   ██ ██      ██    ██ ██  ██ ██
*    ██   ████ ██  ██████  ██   ████           ██    ██   ██  ██████  ██   ████  ██████ ██   ██ ███████  ██████  ██   ████
*/

/*
* ---------------!DISCLAIMER!--------------- *
*                                            *
*         THIS CODE IS RELEASE READY         *
*                                            *
*  THIS CODE HAS BEEN CHECKED, REVIEWED AND  *
*   TESTED. THIS CODE HAS NO KNOWN ISSUES.   *
*    PLEASE REPORT OR OPEN A NEW ISSUE ON    *
*     GITHUB IF YOU FIND ANY PROBLEMS OR     *
*              ERRORS IN THE CODE.           *
*                                            *
*   THIS CODE FALLS UNDER THE LGPL LICENSE.  *
*    YOU MUST INCLUDE THIS DISCLAIMER WHEN   *
*        DISTRIBUTING THE SOURCE CODE.       *
*   (SEE LICENSE FILE FOR MORE INFORMATION)  *
*                                            *
* ------------------------------------------ *
*/

package Truncheon.API;

import java.util.Random;

/**
* Program to display the information of the current build
*
* @version 0.3.1
* @since 0.0.1
* @author DAK404
*/
public final class BuildInfo
{
    /**The String Constant which holds the version number of the program*/
    public final String _version = "1.0.1";

    /**The String Constant which holds the name of the kernel used*/
    public final String _kernel = "Synergy";

    /**The String Constant which holds the Build ID of the program*/
    public final String _buildID = _version + "_10.11.2021_0245_TRNCHN";

    /**The String Constant which holds the date compiled of the build*/
    public final String _buildDate = "11-November-2021";

    /**The String which will store the MOTD value */
    public final static String _MOTD = motdGenerator();

    /**The boolean which will store if the MOTD is allowed or not */
    public boolean _motdPolicy;

    public BuildInfo()throws Exception
    {
        _motdPolicy = ( new Truncheon.API.Minotaur.PolicyEnforcement().retrievePolicyValue("motd") == null ||  new Truncheon.API.Minotaur.PolicyEnforcement().retrievePolicyValue("motd").equalsIgnoreCase("error") || new Truncheon.API.Minotaur.PolicyEnforcement().retrievePolicyValue("motd").equalsIgnoreCase("on"));
    }

    /**
    * Displays the basic program information on the screen
    */
    public final void versionViewer()throws Exception
    {
        clearScreen();
        
        System.out.println("  _   _  _____  ____   _   _ ");
        System.out.println(" | \\ | ||_   _|/ __ \\ | \\ | |");
        System.out.println(" |  \\| |  | | | |  | ||  \\| |");
        System.out.println(" | . ` |  | | | |  | || . ` |");
        System.out.println(" | |\\  | _| |_| |__| || |\\  |");
        System.out.println(" |_| \\_||_____|\\____/ |_| \\_|");

        //System.out.println("                          ");
        System.out.println("______________________________\n");
        System.out.println("      Truncheon v" + _version + "\n      ----------------");
        if(_motdPolicy)
            System.out.println("\nMessage Of The Day:\n" + _MOTD);
        System.out.println("______________________________\n\n");
        //debugMemoryInformation();
    }

    /**
    * Clears the screen, dependent on the OS
    */
    public final void clearScreen()
    {
        try
        {
            /*
            * Clear Screen Notes:

            * The program is reliant on clearing the screen based on the OS being run
            * Clear screen has been tested on Windows and Linux platforms only
            * Clear screen should have the IO Flush right after clearing the screen

            */

            if(System.getProperty("os.name").contains("Windows"))

            //Spawns a new process within cmd to clear the screen
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

            else
            //invokes bash to clear the screen
            new ProcessBuilder("/bin/bash", "-c" ,"reset").inheritIO().start().waitFor();

            System.out.flush();
        }
        catch(Exception E)
        {
            System.err.println("\n\nERROR WHILE CLEARING SCREEN");
            System.err.println("ERROR: " + E + "\n\n");
        }
    }

    private final static String motdGenerator()
    {
        String [] message = {"You got this",
        "You'll figure it out",
        "You're a smart cookie",
        "I believe in you",
        "Sucking at something is the first step towards being good at something",
        "Struggling is part of learning",
        "Everything has cracks - that's how the light gets in",
        "Mistakes don't make you less capable",
        "We are all works in progress",
        "You are a capable human",
        "You know more than you think",
        "10x engineers are a myth",
        "If everything was easy you'd be bored",
        "I admire you for taking this on",
        "You're resourceful and clever",
        "You'll find a way",
        "I know you'll sort it out",
        "Struggling means you're learning",
        "You're doing a great job",
        "It'll feel magical when it's working",
        "I'm rooting for you",
        "Your mind is full of brilliant ideas",
        "You make a difference in the world by simply existing in it",
        "You're learning valuable lessons from yourself every day",
        "You're worthy and deserving of respect",
        "You know more than you knew yesterday",
        "You're an inspiration",
        "Your life is already a miracle of chance waiting for you to shape its destiny",
        "Your life is about to be incredible",
        "Nothing is impossible. The word itself says \"I'm possible!\"",
        "Failure is just another way to learn how to do something right",
        "I give myself permission to do what is right for me",
        "You can do it",
        "It is not a sprint, it is a marathon. One step at a time",
        "Success is the progressive realization of a worthy goal",
        "People with goals succeed because they know where they're going",
        "All you need is the plan, the roadmap, and the courage to press on to your destination",
        "The opposite of courage in our society is not cowardice... It is conformity",
        "Whenever we're afraid, it's because we don't know enough. If we understood enough, we'd never be afraid",
        "The past does not equal the future",
        "The path to success is to take massive, determined action",
        "It's what you practice in private that you will be rewarded for in public",
        "Small progress is still progress",
        "Don't worry if you find flaws in your past creations, it's because you've evolved",
        "Starting is the most difficult step - but you can do it",
        "Don't forget to enjoy the journey",
        "It's not a mistake, it's a learning opportunity"};

        Random random = new Random();

        return message[random.nextInt(message.length)];
    }

    /**
    * Displays detailed information about the program.
    *
    * useful for extra information
    * @throws Exception : Handle exceptions thrown during program runtime.
    */
    public final void about()throws Exception
    {
        clearScreen();
        System.out.println("Nion: Truncheon\n_______________\n");

        System.out.println("Iteration   : 8.1");
        System.out.println("Version     : " + _version);
        System.out.println("Kernel      : " + _kernel);
        System.out.println("Date        : " + _buildDate);
        System.out.println("Build ID    : " + _buildID);
        System.out.println("OS          : " + System.getProperty("os.name"));
        System.out.println("\nDescription :\nShell written in Java which is easy and functional to use.\n");
        System.gc();
    }

    /**
    * The Information about the memeory used when this API is called
    */
    /*
    private void debugMemoryInformation()
    {
        // get Runtime instance
        Runtime instance = Runtime.getRuntime();
        System.out.println("*****************************************");
        System.out.println("      ---   DEBUG INFORMATION   ---      ");
        System.out.println("*****************************************");
        System.out.println("\n  - Heap utilization statistics -  \n ");
        System.out.println(" [*]  Process ID   : " + ProcessHandle.current().pid());
        // available memory
        System.out.println(" [*]  Total Memory : " + instance.totalMemory()  + " Bytes");
        // free memory
        System.out.println(" [*]  Free Memory  : " + instance.freeMemory()  + " Bytes");
        // used memory
        System.out.println(" [*]  Used Memory  : " + (instance.totalMemory() - instance.freeMemory())  + " Bytes");
        // Maximum available memory
        System.out.println(" [*]  Max Memory   : " + instance.maxMemory()  + " Bytes");
        System.out.println("\n*****************************************\n\n");
    } */
}