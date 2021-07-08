
# **Nion: Truncheon**
20-April-2021 | Time To Read: 30 minutes

---

# Table of Contents
1. [Important Links](#important-links)
2. [Introduction](#introduction)
3. [Getting Started](#getting-started)
4. [Building The Program](#building-the-program)
5. [Working with APIs](#working-with-apis)
6. [Creating custom APIs](#creating-custom-apis)
7. [Nion Script Language](#nion-script-language)
8. [Database and JDBC](#database-and-jdbc)
9. [Contribution Etiquettes](#contribution-etiquettes)
10. [Documentation](#documentation)
11. [Contact Information](#contact-information)

---

# Important Links

Here are a few quick links to prerequisite software to help you get started right away. No need to read the entire document and hunt for important links. All of them, right here.

**MANDATORY SOFTWARE**
* [SQLite JDBC Driver](https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/)
* [OpenJDK](https://jdk.java.net) or any other JDK.

**RECOMMENDED SOFTWARE/RESOURCES**
* [Visual Studio Code](https://code.visualstudio.com/) or [Notepad++](https://notepad-plus-plus.org/) or a text editor of your choice!
* [GitHub Account](https://github.com)
* Basic Knowledge on [Java](https://docs.oracle.com/en/java/)
* Basic RDBMS Concepts including JDBC
* Patience and Time :)

**MISCELLANEOUS QUICK LINKS**

* [Truncheon Website](https://dak404.github.io/Truncheon)
* [License and Legal Information](License.md)
* [Program Source Code](https://github.com/DAK404/Truncheon)
* [Program Documentation](https://dak404.github.io/Truncheon/Documentation/index.html)
* [Latest Release](https://gitreleases.dev/gh/DAK404/Truncheon/latest/Truncheon.zip)

[Back To Top](#table-of-contents)

---

# Introduction

Truncheon iteration is a part of Project Nion, which is aimed to provide a platform to create and deploy modules in a flexible and versatile framework to maximise efficiency of modules and other programs dependent on this project.

Project Nion has been in development since 7 years, and the code has been continually improved and enhanced over time. The project will be free and opensource forever, which means that anyone will be able to fork it and make their own versions of the program.

[Back To Top](#table-of-contents)

---

# Getting Started

You will need to download this repository **entirely**. This will help you to build upon the already available code for a given release.

Currently, the code for Truncheon is on [GitHub](https://github.com/DAK404/Truncheon).

The Documentation for the same is on [the website](https://dak404.github.io/Truncheon).

Once the repository is downloaded, you will need to extract them anywhere as desired. It is usually a good idea to keep the files in a distinct folder with no other contents within to avoid conflicts and issues while developing, compiling and debugging the program.

The folder structure should look like this:
```
| <Folder Root>
|
|- /Binaries         :   Contains the binaries after program compilation
|- /Documentation    :   Contains the documentation after compiling the documentation
|- /Information      :   Contains the program help files and program documents
|- /Source           :   Contains the program source code
|--- /API            :   Contains APIs which powers the programs (and other apps too)
|--- /Core           :   Contains core programs which helps Truncheon to run.
|- /Tools            :   Contains tools for programmers to compile and run Truncheon
```

This should have initialized with setting up the environment to build or customize the program as needed.

[Back To Top](#table-of-contents)

---

# Building the Program

The program can be built by using the following steps:

1. Copy `Tools.bat` or `Tools.sh` from `./Tools` directory to `./Source` directory.
2. Run the Batch File or the Shell Script.
3. Choose the desired option as displayed.
4. Builds will be in the `./Binaries` directory.
5. \<Optional\> You can copy `RunTools.bat` or `RunTools.sh` from `./Tools` directory and paste it in `./Binaries`.
6. \<Optional\> Run the Batch file or shell script and follow the on screen instructions OR run the program by using the command `java ProgramLauncher \<kernelName\> \<BootMode\>

<span style="color:#05e401">**Fun Fact!  
The program and tools have contextual help built into them. In case you will need to refer to the same, you can always type `help` anywhere in the prompt or choose the help option if available.**</span>

[Back To Top](#table-of-contents)

---

# Working With APIs

APIs are built-in programs which help to implement other programs without rewriting certain parts of code repeatedly. APIs also abstracts the implementation of a given functionality, presenting the essential data to the **custom module developers**. This makes it simple to build programs on top of Truncheon easily.

API source code is found in the `./Source/Truncheon/API` directory. The APIs provided out of the box contain all essential programs to be used by external modules and programs. Additional APIs can be written and called as desired by the developers. (See [Creating Custom APIs](#creating-custom-apis))

The API documentation will provide information how to leverage the API, while not providing the implementation of the API itself. However, there are comments in the program code which will help in improving, modifying and developing alternatives to the implementation.

However, the program documentation will contain the details about the implementation of every program in Truncheon. This will ensure that other people interested to work on/modify the program will have a good idea about the working of the program.

<span style="color:#e6b400">**NOTE:  
AS THE PROGRAM IS UPDATED IN THE FUTURE, THE IMPLEMENTAION MAY/MAY NOT CHANGE. SOMETIMES, WHEN THERE IS A NEW VERSION OF THE PROGRAM, THE API CALLS MAY/MAY NOT HAVE CHANGED. THIS WOULD MEAN THAT THE PROGRAMS BUILT ON TOP OF TRUNCHEON WILL BE AFFECTED IF NOT UPDATED. BY USING 3RD PARTY MODULES AND PROGRAMS IN TRUNCHEON, THE AUTHORS OF THE PROGRAMS ARE NOT RESPONSIBLE IN ANY WAY TO ANY DATA LOSS OR DAMAGES.  
ANY CHANGES TO THE API CALLS ARE TO BE TRACKED AND TO BE UPDATED REGULARLY BY THE MODULE DEVELOPERS, WHEN POSSIBLE, TO AVOID ANY ISSUES.**</span>

---

# Creating Custom APIs

Anyone with the source code can create their custom APIs. It is simple and easy!  

## Quick Reference

* API Package Nomenclature : `package Truncheon.API.<API_Package_Name>.<API_Subpackage_Name>`
* API Calls Nomenclature : `new Truncheon.API.<API_PackageName>.<API_Subpackage_Name>`

### Step 1 : Create Truncheon API

* Open a new file in your favorite editor.  
* Save the file in `./Source/Truncheon/API/`. In this example, we shall create a new class called `ExampleClass.java`, which is saved inside the above specified directory.
* Set the package name of the `ExampleClass.java` to `package Truncheon.API;`. This will make the class to reside inside the API directory of Truncheon.
* Begin writing the code for the program. In this example, we shall create a method to print the string "Hello World!" on the screen.

```java
//define the HelloWorld package inside Truncheon's API packages
package Truncheon.API.HelloWorld;

public class HelloWorld
{
    public void printHelloWorld()throws Exception
    {
        try
        {
            //Print Hello World!
            System.out.println("Hello World!");

            //Program implementation
        }
        catch(Exception E)
        {
            E.printStackTrace();
        }
    }
}
```

### Step 2 : Create the driver code

* Now, we need to create a driver code which will run the above code from another program.
* In this example, we shall write a class known as `ProgExe.java`. Inside `ProgExe.java`, insert the API call within the method. The following code demonstrates the above.

```java
//Keeping it public will allow the class to run the program.
public class ProgExe
{
    //An entry point to the program which will run the Example API created previously.
    public static void main(String[] Args)throws Exception
    {
        //Invoke the API that you just created in the previous program!
        new Truncheon.API.HelloWorld.printHelloWorld();
    }
}
```
Compiling the programs by using the command `javac -d ../Binaries ProgExe.java`. The compiled programs will be placed within the `./Binaries` directory.

Navigating to the `./Binaries` directory, you can see the `ProgExe.class` file. You can run this program by using the command `java ProgExe` in the terminal.

### Step 3 : Create your own API with programs within.

* If an API has other new programs to be included in the package, a dedicated package can be created to simplify this. A sub-package can be created easily by setting the package name to `Truncheon.API.<API_PackageName>.<API_PackageName>`.
* The main API can be called via the `new Truncheon.API.<API_PackageName>.<API_Subpackage_Name>`.
* For example, we can name this newly created API as Example. The following code will need to be written in the API being made.
```java
package Truncheon.API.Example.HelloWorld;
```

The following line should replace the API call in the driver code, which is `ProgExe.java` in this case.

```java
new Truncheon.API.Example.HelloWorld.printHelloWorld();
```

Summary:

```
Truncheon        : Main Package Name
API              : Contains all the Truncheon APIs and dependencies.
Example          : Contains a program which will print "Hello World!"
HelloWorld       : The program which contains the implementation inside the printHelloWorld() method to print "Hello World!"
```
Easy and simple, right?

<span style="color:#05e401">**Fun Fact!  
The above examples can be executed by copying and pasting the source code provided above. By following the instructions correctly, you can create an example API which will print "Hello World!" on the screen, which can help you begin creating your own APIs for Truncheon.**</span>

<span style="color:#FF0000">**ATTENTION:  
APIS IMPLEMENTED IN THE OFFICIAL RELEASES OF TRUNCHEON IS TESTED. THE USER IS LIABLE FOR THE LOSS OF DATA OR ANY DAMAGES OR MALFUNCTION THAT ARISES FROM THE USE OF THE PROGRAM APIS. MORE REGARDING THIS IN THE [LICENSE](License.md) FILE. THAT BEING SAID, THE PROGRAM FEATURES AND APIS ARE THOROUGHLY CHECKED AND TESTED. FUTURE UPDATES MAY INCLUDE CHANGES TO THE API IMPLEMENTATION AND/OR TO THE API CALLS. ANY PROGRAM BUILT ON TOP OF TRUNCHEON MAY NEED TO UPDATE THEIR PROGRAM TO ACCOMODATE THE CHANGES. THIS PROGRAM IS OPEN SOURCE AND THE LINK TO THE SOURCE CODE AND DOCUMENTATION IS GIVEN ABOVE.**</span> 

<span style="color:#e6b400">**NOTE:  
DEFINITION OF OPEN-SOURCE : PROGRAM SOURCE CODE WHICH IS WRITTEN IN A HUMAN READABLE LANGUAGE WHICH, EITHER, HAS A LINK PROVIDED TO CODE OR IS SHIPPED WITH THE PROGRAM.**</span>

[Back To Top](#table-of-contents)

---

# Database and JDBC

**FEATURE UNDER DEVELOPMENT!**

Truncheon uses the SQLite 3 JDBC system to work with the databases. It also has built in APIs to interface with the JDBC system in a more simplified format for the user defined APIs and modules to leverage functionalities easily.

The Dragon API is one such example, which uses classes to create, delete, update credentials, login, pseudo, promote and demote a given user. All this is possible by using JDBC and the subsequent APIs used to interface with the JDBC easily.

Currently, the Dragon API has a feature to add a new user into the system, but has not been linked to the mainmenu since the feature is being tested and worked on. A future update shall enable the feature!

[Back To Top](#table-of-contents)

---

# Nion Script Language

**FEATURE UNDER DEVELOPMENT!**

A new functionality has been implemented in this iteration: A Script Engine.

The script engine has been developed to automate and run user written scripts to perform actions easily. Multiple commands can be easily executed by the system with minimal user interaction with the system.

The script language syntax is easy. It is similar to Windows&reg; Batch/Command scripting and Linux Shell scripting. The main advantage is irrespective of the platform that the program is run on, the scripts can be easily written and executed on either system.

**NOTE: THE SCRIPTS CAN BE RUN USING TRUNCHEON SHELL ONLY. IT CANNOT BE USED BY THE NATIVE OS INTERPRETER DIRECTLY.**

Any line starting with the '#' symbol signifies that it is a comment and not a statement to be executed by the interpreter.

This scripting language is straight forward to use. The following script will help the user to display the details of the Truncheon shell and then print Hello World on the screen.

```
# Author: DAK (@DAK404)
# Purpose: A demo scipt which can be executed by the Truncheon Shell.

# Display the program information
about

# Print Hello World string on the screen
echo "Hello World!"

# To run any other command that the user wishes, refer to the commands available in the main menu. Any command in the main menu can be written here to perform the desired task.
# <Name_Of_Command>

# It is generally a good idea to indicate to the interpreter that it has reached the end of the script file. Once it reaches "End Script", any command after this command will not be executed.
End Script
```
The user can also leverage on the available functionality of running the Native OS's commands by either using the `sys` command (Syntax: `sys "<native OS's commands>"`), which will redirect the user's input to the OS's interpreter.

Scripting as of now cannot execute other scripts when a script is already being run. This is to prevent any malicious abuse of the functionality. A future update may or may not implement features to run a script within another script safely.

As of now, the program and the scripting features are still under development and will provide a limited access to the scripting functionality. Feedbacks are always welcome!

[Back To Top](#table-of-contents)

---

# Contribution Etiquettes

**NOTE : THESE GUIDELINES HAVE NOT BEEN FOLLOWED IN THE CURRENT CODEBASE SINCE THE CODE IS NOT RELEASE READY. THIS NOTE SHALL BE REMOVED ONCE THE CODE HAS BEEN FINALIZED AND IS DECLARED TO BE STABLE.**

These are guidelines which describe the best way to implement the code in Truncheon. If the code quality meets the specified guidelines, the code in the pull request shall be reviewed (with changes requested possibly) and then merged with the main branch.

## 1. No Java Style Brackets

Yes. Even though we are using Java, it is highly annoying and hard to read the code (atleast for me) to work with Java style of brackets.

**Correct Style:**
```java
import java.io.Console;

public class Test
{
    Console console=System.console();
    public static void main(String[] Args)
    {
        System.out.println("Hello World!");
        Console console=System.console();
        console.readLine("Press enter to exit.");
    }
}
```

**Incorrect Style:**
```java
import java.io.Console;

public class Test {
    public static void main(String[] Args) {
        System.out.println("Hello World!");
        Console console=System.console();
        console.readLine("Press enter to exit.");
    }
}
```
## 2. Java Style Arrays

Well, Java accepts both C/C++ array styles since they could accomodate programmers from a C/C++ background. But while writing code for Truncheon, please try to write it in the new way while declaring arrays.

**Correct Style**
```java
int[] numbers;
```

**Incorrect Style**
```java
int numbers[];
```

## 3. Spacing

Please leave a single space right before and after binary and ternery operators. This improves the code readability.

**Correct Style**
```java
if( x == y || x != y )
{
    //Do something
}
```

**Incorrect Style**
```java
if(x==y||x!=y)
{
    //Do something
}
```

## 4. Pascal Case

Pascal Case is preferred for class names. They make the code easier to read.

**Correct Style**
```java
public class ThisIsTruncheon
{
}
```

**Incorrect Style**
```java
public class this_is_truncheon
{
}
```

## 5. Camel Case

Camel case is preferred for method names and parameters.

**Correct Style**
```java
public void startTruncheon(int loginCount, String username)
{
}
```

**Incorrect Style**
```java
public void start_Truncheon(int login_Count, String username)
{
}
```

## 6. Variable Nomenclature

Global variables need to begin with an underscore to easily differentiate between the local and global variables in the program. The code will be easily readable.

**Correct Style**
```java
public class HelloWorld
{
    String _greet = "Hello World!";
}
```

**Incorrect Style**
```java
public class HelloWorld
{
    String greet = "Hello World!";
}
```

Please use the variable names which are related to the way it is used. Generic variable names tend to reduce the readability and sometimes may cause ambiguity while maintaining the code.

**Correct Style**
```java
public void greetUser()
{
    String greetingText = "Hello, ";
}
```

**Incorrect Style**
```java
public void greetUser()
{
    String a = "Hello, ";
}
```

## 7. Comment Styling

There are 3 distinct comment types that are used in Truncheon:
* Inline Comments
* Block Comments
* Documentation Comments

### **A. Inline Comments**

These are used when a single statement of code or a short comment is required for a set of statements.

**Correct Style**
```java
public void printStatement()
{
    //This will print the hello world statement on the screen
    System.out.println("Hello World!");
}
```

**Incorrect Style**
```java
public void printStatement()
{
    /*WRONG STYLING :V*/
    /**WRONG STYLING AGAIN!*/
    System.out.println("Hello World!");
}
```

### **B. Block Comments**

These are used for describing a complex logic, or while providing additional notes for a method or class which need not be included in the program documentation. This can also be used for "deactivating" code in a program which is being currently tested out.

**Correct Style**
```java
/*
* Purpose: To print a hello world statement on the terminal.
* This code is not stable and is being worked on. DO NOT INCLUDE IN RELEASES.
*/
public void printStatement()
{
    /*
        System.out.println("Welcome to Truncheon");
        //accept name here
        System.out.println("Hello World, " + console.readLine("Name: "));
    */
    System.out.println("Hello World!");
}
```

**Incorrect Style**
```java
//Purpose: To print a hello world statement on the terminal.
//This code is not stable and is being worked on. DO NOT INCLUDE IN RELEASES.

public void printStatement()
{
    //System.out.println("Welcome to Truncheon");
    //accept name here
    //System.out.println("Hello World, " + console.readLine("Name: "));
    System.out.println("Hello World!");
}
```

### **C. Documentation Comments**

These are used to describe a method and its functionalities. This will help in utilising the APIs efficiently, especially for those who do not want to delve into the code itself to create a custom module, but rather understand the use of the method and the way to efficiently use it. Do not use documentation comments for anything else, except for a class, constructor and method description. This will help in maintaining a simple yet effective documentation to read and maintain.

**Correct Style**
```java
/**
* Description: Create a class to print HelloWorld when invoked.
* Author : DAK (@DAK404)
*/
public void printStatement()
{
    //Print Hello world here!
    System.out.println("Hello World!");
}
```

**Incorrect Style**
```java
//Create a class to print HelloWorld when invoked.
//Author : DAK (@DAK404)
/*
THIS IS WRONG STYLING:V
*/
public void printStatement()
{
    //Print Hello world here!
    System.out.println("Hello World!");
}
```

## 8. Prefer to use Pre-Increment over Post-Increment

Well, use it for the sake of code consistency.

[Back To Top](#table-of-contents)

---

# Documentation

Please add remarks and documentation comments as necessary which will help in denoting a statement's meaning. This will enhance the code readability and will make the code easier to maintain and possibily, even troubleshoot and debug the errors.

Documentation will also help in providing an overview of the public classes which are used as APIs. The module developers can glance over through the documentation page to create their own modules with the help of the existing APIs.

Any user defined APIs should have significant documentation specified to signify its use and the way it can be efficiently invoked.

[Back To Top](#table-of-contents)

---

# Contact Information


Website : [https://dak404.github.io/Truncheon](https://dak404.github.io/Truncheon)

GitHub : [https://github.com/DAK404/Truncheon](https://github.com/DAK404/Truncheon)

Contact me : [https://dak404.github.io/Assets/Pages/Contact.html](https://dak404.github.io/Assets/Pages/Contact.html)

[Back To Top](#table-of-contents)

---

END OF FILE

---
