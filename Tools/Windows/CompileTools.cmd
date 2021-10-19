:: -----------------------------
::   Truncheon Tools Suite 3.0
:: -----------------------------
::
:: This tool suite is written to
::  enable developers to easily
::   develop and run Truncheon
::
:: -----------------------------
::
:: =============================
::      Program Information
:: =============================
::
:: Author  : DAK404
:: Purpose : A tool which helps
:: in compiling the program and
:: the documentation.
::
:: THIS IS NOT RECOMMENDED FOR
::         END USERS!
::
:: =============================

:: Set the echo off to stop displaying all command execution on console
@echo off

:: A label to display the menu to the user
:MENU

:: Clear the screen
CLS

:: Display the menu with choices
ECHO :::::::::::::::::::::::::::::
ECHO : TRUNCHEON TOOLS SUITE 3.0 :
ECHO :::::::::::::::::::::::::::::
ECHO.
ECHO  1. COMPILE EVERYTHING
ECHO  2. COMPILE LAUNCHER ONLY
ECHO  3. COMPILE TRUNCHEON ONLY
ECHO  4. COMPILE DOCUMENTATION
ECHO  5. OPEN COMMAND PROMPT HERE
ECHO  6. OPEN WSL PROMPT HERE
ECHO  7. HELP
ECHO  8. EXIT
ECHO.
ECHO :::::::::::::::::::::::::::::
ECHO.

:: Accept input from user for the desired action
ECHO Please enter your choice [ 1 - 7 ]
choice /N /C 12345678 /M "MAKE_TOOLS ->  "

:: Logic to handle the choices

:: Exit if the key pressed is 8
IF errorlevel == 8 GOTO :EOF

:: Go to the HELP label if the key pressed is 7
IF errorlevel == 7 GOTO HELP

:: Open the WSL Prompt if the key pressed is 6
IF errorlevel == 6 GOTO WSL_PRMPT

:: Open the Command Prompt if the key pressed is 5
IF errorlevel == 5 GOTO CMD_PRMPT

:: > COMPILATION TOOLS <
:: ---------------------
::
:: The choices from 1-4, are about
:: compiling Truncheon, the Launcher
:: and the documentation.
::
:: IF YOU DO NOT KNOW WHAT THIS IS
:: PLEASE DO NOT MODIFY THE CODE BELOW!

:: Go to MKDOC (Make Documentation) if the key pressed is 4
IF errorlevel == 4 GOTO MKDOC

:: Go to MKTR (Make Truncheon) if the key pressed is 3
IF errorlevel == 3 GOTO MKTR

:: Go to MKLNCHR (Make Launcher) if the key pressed is 2
IF errorlevel == 2 GOTO MKLNCHR

:: Go to MKALL (Make All) if the key pressed is 1
IF errorlevel == 1 GOTO MKALL

:: Display the help section to the user
:HELP

:: Clear the screen
CLS

:: Display the help section strings
ECHO ___________________________
ECHO.
ECHO  TRUNCHEON TOOLS SUITE 3.0
ECHO ___________________________
ECHO.
ECHO This tool will help the developers to
ECHO compile the program, launcher and the
ECHO documentation. These tasks are mainly
ECHO discussed in the README page. Please
ECHO refer to it in the following link:
ECHO.
ECHO https://dak404.github.io/Truncheon/Readme.html
ECHO.
ECHO ___________________________
ECHO.
ECHO To choose an option, simply press the desired
ECHO number corresponding to the specified option.
ECHO.
ECHO 1. COMPILE EVERYTHING
ECHO This will compile Truncheon, the Launcher and
ECHO the documentation.
ECHO.
ECHO 2. COMPILE LAUNCHER ONLY
ECHO This will compile the Launcher only.
ECHO.
ECHO 3. COMPILE TRUNCHEON ONLY
ECHO This will compile Truncheon only.
ECHO.
ECHO 4. COMPILE DOCUMENTATION ONLY
ECHO This will compile program documentation only.
ECHO.
ECHO 5. OPEN COMMAND PROMPT HERE
ECHO This will open the Command Prompt in the
ECHO current directory.
ECHO.
ECHO 6. OPEN WSL PROMPT HERE
ECHO This will open the Windows Subsystem for
ECHO Linux in the current directory.
ECHO.
ECHO 7. HELP
ECHO This will display this help section.
ECHO.
ECHO 8. EXIT
ECHO The tool will quit and exit to Windows.
ECHO.
ECHO ___________________________
GOTO MENU

:: The logic to compile everything
:MKALL

:: Compile the ProgramLauncher.java file
javac -d ../Binaries ProgramLauncher.java
ECHO Completed compiling Program Launche

:: Compile the Boot.java file
javac -d ../Binaries ./Truncheon/Core/Boot.java
ECHO Completed compiling Truncheon

:: Compile the documentation
GOTO MKDOC

:: Logic to compile the ProgramLauncher.java file
:MKLNCHR
javac -d ../Binaries ProgramLauncher.java
ECHO Program Launcher compilation complete.
PAUSE
GOTO MENU

:: Logic to compile the Boot.java file
:MKTR
javac -d ../Binaries ./Truncheon/Core/Boot.java
ECHO Truncheon compilation complete.
PAUSE
GOTO MENU

:: Logic to compile the Program Documentation
:MKDOC
dir /s /B *.java > sources.txt
javadoc -d ../Documentation -author -version @sources.txt > CompileDoc.log
ECHO Completed Compiling Documentation.
ECHO NOTE: Please access the documentation from the /Documentation/index.html directory.
ECHO If you are accessing the documentation from a web browser or a local server, please
ECHO check the "INDEX.MD" which can be found in the root of the project directory.
DEL /s /q sources.txt
PAUSE
GOTO MENU

:: Logic to open the Command Prompt in the current directory
:CMD_PRMPT
START cmd.exe
GOTO MENU


:: Logic to open the WSL Prompt in the current directory
:WSL_PRMPT
START wsl
GOTO MENU
