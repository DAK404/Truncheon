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

## Structures
---

Nion compatible programs must follow the basic directory, documentation and program structures.

These help in creating kernels which can be used alongside Truncheon and can be launched via the Program Launcher.

This program conforms to various standards, which is to be followed if it needs to work with Truncheon onwards.

This is a reference on how to write programs and build applications on top of Truncheon and kernels which follows the Nion Standards