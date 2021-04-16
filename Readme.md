
# **Nion: Truncheon**
13-April-2021 | Time To Read: 30 minutes

---

# Table of Contents
1. [Important Links](#important-links)
2. [Introduction](#introduction)
3. [Getting Started](#getting-started)
4. [Building The Program](#building-the-program)
5. [Working with APIs](#working-with-apis)
6. [Creating custom APIs](#creating-custom-apis)
7. [Database and JDBC](#database-and-jdbc)
8. [Contribution Etiquettes](#contribution-etiquettes)
8. [Documentation](#documentation)
10. [Contact Information](#contact-information)

---

# IMPORTANT LINKS

Here are a few quick links to prerequisite software to help you get started right away. No need to read the entire document and hunt for important links. All of them, right here.

**MANDATORY SOFTWARE**
* [SQLite JDBC Driver](https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/)
* [OpenJDK](https://jdk.java.net)

**RECOMMENDED SOFTWARE/RESOURCES**
* [Visual Studio Code](https://code.visualstudio.com/) or [Notepad++](https://notepad-plus-plus.org/)
* [GitHub Account](httpsL//github.com)
* Basic Knowledge on [Java](https://docs.oracle.com/en/java/)
* Basic RDBMS Concepts including JDBC
* Patience and Time :)

**MISCELLANEOUS QUICK LINKS**

* [Website](https://dak404.github.io/Truncheon)
* [License](License.md)
* [Source Code](https://github.com/DAK404/Truncheon)
* [Program Documentation](https://dak404.github.io/Truncheon/Documentation/index.html)
* Download Latest Release (Coming Soon!)

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

The program can be build by using the following steps:

1. Copy `Tools.bat` or `Tools.sh` from `./Tools` directory to `./Source` directory.
2. Run the Batch File or the Shell Script.
3. Choose the desired option as displayed.
4. Builds will be in the `./Binaries` directory.
5. \<Optional\> You can copy `RunTools.bat` or `RunTools.sh` from `./Tools` directory and paste it in `./Binaries`.
6. \<Optional\> Run the Batch file or shell script and follow the on screen instructions OR run the program by using the command `java ProgramLauncher \<kernelName\> \<BootMode\>

<span style="color:#05e401">**Fun Fact!  
The program and tools have contextual help built into them. In case you will need to refer to the same, you can always type `help` or choose the help option.**</span>

[Back To Top](#table-of-contents)

---

# Working With APIs

There are a few Truncheon APIs which are available that are either used by Truncheon itself or other programs which are built upon Truncheon.

Right now, since the code is still being worked on, the API information is very limited and incomplete. Please check this space after the stable code base has been released publicly.

APIs will be extremely useful for user written modules, which will save time by not having to repeatedly write the same code for every module. A fine example of this is the Program Information. The Program Information API contains the program information (obviously) which the user need not type it out manually or update it manually for every release. This will help in saving the code size and flexibility to make user written modules.

When working with APIs, all the accessible APIs are saved inside the `./Source/Truncheon/API` directory. These programs will have their own method of accessing a specific API which is explained inside the program documentation. The program documentation also has explanations, notes and information regarding every API, making it easier for the core program developers to modify and create their version of the API.

<span style="color:#e6b400">**NOTE:  
APIs will be updated over time, which may have changed in terms of the accessible methods used or the implementation of the API, which might break the supporting programs built on top of Truncheon. Although the changes will be specified in the changelog and the commit details, please do check the APIs' documentation from time to time for changes and do the needful.**</span>

---

# Creating Custom APIs

Users can create their custom APIs by adding their classes to the package `Truncheon.API.<API_Package>.API_Name`

This will help in expanding the functionalities of the program as other modules too can leverage on the functionalities provided by the various, user defined APIs.

For example, when writing a new class, say `HelloWorld.java`, as an API to print Hello World on the screen, the code can be implemented in the following way:

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

            //Basically, the program logic is implemented here
            //Do whatever you want to do here.
        }
        catch(Exception E)
        {
            E.printStackTrace();
        }
    }
}
```

The above example can be invoked successfully by adding the following lines of code to any of the modules. Say, in this case, we create a new class called `ProgExe.java`.

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

Now, compile both the programs and the API can be utilized by the class invoking the API.

To take things to the next level, the APIs have their own folders in the source files, which will help in putting certain APIs in a single folder. This will help in having a clean and organized workspace.

In the above example, `HelloWorld` is in the root of the API folder. To have HelloWorld in a dedicated API folder, say, `CustomAPIExample`, the `HelloWorld.java` program must be inside the `CustomAPIExample` package. The following line should replace the 1st line in the previously executed `HelloWorld.java`.

```java
package Truncheon.API.CustomAPIExample.HelloWorld;
```

AND the following line should replace the API call in the `ProgExe.java`.

```java
new Truncheon.API.CustomAPIExample.HelloWorld.printHelloWorld();
```

Summary:

```
Truncheon        : Main Package Name
API              : Contains all the Truncheon APIs and dependencies.
CustomAPIExample : Contains a program which will print "Hello World!"
HelloWorld       : The program which contains the implementation inside the printHelloWorld() method to print "Hello World!"
```
Easy and simple, right?

<span style="color:#05e401">**Fun Fact!  
The code given above can be directly copied and pasted to understand the working of the API system in Truncheon!**</span>

<span style="color:#FF0000">**ATTENTION:  
Core Program Developers implement APIs which can be used by user written modules. If you do not know or fully understand how to create an API, it is recommended to first practice and then begin programming the additional Truncheon APIs. The source code and this Readme file contains the information to build your own API with an example.  If you are developing user modules, the API documentation will help you to understand the purpose of the API and on invoking the APIs in your modules.**</span>

[Back To Top](#table-of-contents)

---

# Database and JDBC

Truncheon uses the SQLite 3 JDBC system to work with the databases. It also has built in APIs to interface with the JDBC system in a more simplified format for the user defined APIs and modules to leverage functionalities easily.

The Dragon API is one such example, which uses classes to create, delete, update credentials, login, pseudo, promote and demote a given user. All this is possible by using JDBC and the subsequent APIs used to interface with the JDBC easily.

<span style="color:#FF0000">**ATTENTION:  
This JDBC system is incomplete for the moment, please watch this space for this will be updated real soon!**</span>

[Back To Top](#table-of-contents)

---

# Contribution Etiquettes

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
