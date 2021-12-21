:: Restbox launcher.
::
:: @author TSS

@echo off
cls

set app=""
set version=""
set db=""

echo "1) Run Restbox with bugs"
echo "2) Run Restbox without bugs"
echo "3) Clean database"
echo "4) Exit"

choice /c 1234 /m "Restbox launcher, choose the option:"

if errorlevel 4 goto Exit
if errorlevel 3 goto Clean
if errorlevel 2 goto NoBugs
if errorlevel 1 goto Bugs

:Bugs
echo "Running Restbox with bugs on port 8080..."
java -jar %app%-%version%.jar --spring.profiles.active=invalid
goto :End

:NoBugs
echo "Running Restbox without bugs on port 8081..."
java -jar %app%-%version%.jar --spring.profiles.active=valid
goto :End

:Clean
echo "Cleaning database..."
rmdir /s /q %db%
echo "Cleaning completed"
goto :End

:Exit
goto :End

:End
pause
