# Nion Structures and Standards

LAST UPDATED : 06-JUNE-2022

---

## Introduction
---

Nion programs (starting from Truncheon onwards) shall follow common standards and structures to make sure that enhancements and other additions can easily be accommodated into the existing code base.

These standards and structures must be followed if it is supposed to run alongside Truncheon, using the same installation directory to boot similar kernels and if it is supposed to be launched via the Program Launcher. The standards and structures can also be used to build programs on top of Truncheon, providing a straight forward approach to build, maintain and debug the code to enhance, expand and implement functionalities.

## Standards
---

Programs which are built on top of Truncheon or to be used alongside Truncheon must follow the following standards:

1. Nion Kernel Entry Point
2. Nion Standard Exit Codes

Details for every standards are given below. It is highly recommended to follow the standards if the program needs to be used alongside or along with Truncheon for the best results, convenience and efficiency.

---

### Nion Kernel Entry Point
---

Every Nion compatible Kernel has an entry point from the Loader.java class.

This class will be used to load into the program from the program launcher.

```
Main.java -----------> Loader.java -> OtherPrograms.java
          ^^^^^^^^^^^  ^^^^^^^^^^^
               a            b

a : A new process is created from the Main.java class
b : The entry point of the program where the kernel code begins to run.

EXPLANATION:

[*] When Main.java is started, the Main class accepts the following arguments

java [java_arguments] Main [kernel_name] [boot_mode]

[*] The [java_arguments] contains arguments which are passed to the JVM (Java Virtual Machine).

[*] The [kernel_name] is a name of the kernel that is to be booted. It can be any kernel which follows the Nion Structure.

[*] The [boot_mode] defines the mode that the kernel should use. Generally, the kernel boot modes are "normal", "debug" and "repair". Do note that the Loader program can accommodate more boot modes.
```

### Nion Standard Exit Codes
---

THe Program Launcher has a few pre-defined exit codes which is used to denote a certain outcome of the spawned process. The exit codes need to be defined in the Main.java class, and to add a custom exit code, one must edit the source code and recompile the launcher and reflect the changes.

The standard exit codes shall be updated here once the development of the kernel is complete.

## Structures
---

Every kernel that must be compatible or be similar to Truncheon must follow the directory structure as defined. This makes sure that the program is easy to implement features, debug, modify and manage the code and binaries.

The directory structure of the program must be as follows:

```
INSTALLATION_DIRECTORY_ROOT
|
|--> /.Manifest
|--> /Truncheon
|--> /Information
|       |--> /Truncheon
|--> /org
|--> /System
|       |--> /Truncheon
|              |--> /Public
|              |--> /Private
|--> /Users
        |--> /Truncheon
```

The following shall detail upon the entries in the directory

* .Manifest: This is a directory that shall store the kernel metadata and manifest files. These files are used for providing contexts to the program, such as file hashes for integrity verification.
* Truncheon: This is the kernel directory, where all the programs related to the kernel is stored. You may substitute the name Truncheon with the name of the desired kernel to be used.
* Information: This directory shall contain the help files and any manuals and documentations of any sort which can be there for the user's reference while using the program. Every Kernel has its own Information Workspace, which means that there needs to be a directory under Information corresponding to the Kernel used, to display the documentation.
* org: This is the Java JDBC SQLite Driver used.
* System: This contains the system configuration files, policy files, user database files and any kernel related files which are required for the normal functioning of the kernel. Every kernel needs to have its own System Workspace, which means that there needs to be a directory under System corresponding to the Kernel, to have its set of critical files required by it. Every System Workspace has a set of Public and Private directories, which can accommodate the files under Public and the Private Directories.   