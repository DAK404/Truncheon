package Truncheon.Core;

import java.io.Console;

public class Setup
{
    Console console = System.console();
    public void setupLogic()
    {
        new Truncheon.API.BuildInfo().versionViewer();
        console.readLine("Welcome to Truncheon! This program needs an initial configuration before using it. To begin the setup, press the Enter/Return key. Else press the Ctrl + C keys.\nWe recommend the Computer Administrator to setup Truncheon. Please contact your Administrator if you are a user and you're seeing this message.");
    }

    private void showPrerequisites()
    {

    }

    private void createDirs()
    {

    }

    private void createFiles()
    {

    }

    private void initializeDatabase()
    {

    }

    private void createAdminUser()
    {

    }

    private void cleanup()
    {
        
    }
}