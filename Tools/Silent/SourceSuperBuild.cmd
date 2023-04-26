:: -----------------------------
::   Truncheon Tools Suite 3.1
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
:: in compiling everything!
::
:: THIS IS NOT RECOMMENDED FOR
::         END USERS!
::
:: =============================
::
:: NOTE: THIS IS A SILENT TOOL!
::   USER INTERACTION IS NOT
::         RECOMMENDED!
::
:: This tool is to be used in a
:: terminal session or in editor
::  terminal sessions for quick
::     and easy compilation
::
:: =============================

:: Set the terminal echo mode to off
@ECHO OFF

:: Clear the screen
CLS

:: Display the build information
ECHO ========================
ECHO     Nion Tools Suite   
ECHO ========================
ECHO VERSION : 3.1
ECHO DATE    : 13-MAY-2022
ECHO ------------------------

:: Write the documentation information to the log file
ECHO "Tulzscha Tools 3.1" > "CompileDoc.log" 2>&1

:: Compile the launcher first
ECHO [1] Compiling Launcher...
javac -d ../Binaries Main.java

:: Compile the Program then
ECHO [2] Compiling Program...
javac -d ../Binaries ./Truncheon/Core/Loader.java

:: Compile the documentation
ECHO [3] Compiling Documentation
ECHO Compiling Documentation >> "CompileDoc.log" 2>&1

:: List all the files in Source directory and its subdirectories to a sources.txt file
dir /s /B *.java > sources.txt

:: Run the javadoc command to compile the documentation by parsing every file found in sources.txt
:: additionally, write the statuses to the compile log
ECHO [4] Creating Javadoc documentation...
javadoc -d ../../TruncheonDocs/InternalDocs -author -version --show-members private @sources.txt >> "CompileDoc.log" 2>&1
javadoc -d ../../TruncheonDocs/DeveloperDocs -author -version @sources.txt >> "CompileDoc.log" 2>&1

:: Delete the sources.txt file after use
DEL /s /q sources.txt

:: Sign the build binaries for use
ECHO [5] Signing Build...
cd  ../Binaries/
java BuildSigner.java

:: Confirm the status
ECHO [ ATTENTION ] Build (Super) complete.