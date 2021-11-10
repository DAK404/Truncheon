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
:: Purpose : A tool which will
:: help in launching Truncheon
:: without requiring users to
:: deal with the CLI.
::
:: =============================

:: Set the echo off to stop displaying all command execution on console
@ECHO off

:: Clear the screen before running the program
CLS

:: Set the JRE path (if it exists) for this session
set PATH=%PATH%;.\JRE\bin

:: Begin the program, and boot it to the "normal" mode
start "Nion Shell 1.0.1" java ProgramLauncher Truncheon normal

:: End of File