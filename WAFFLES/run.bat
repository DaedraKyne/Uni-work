@echo off
REM===============================================================================
REM
REM          FILE: run.bat
REM
REM         USAGE: run.bat
REM                This script must be run from top project folder, i.e folder
REM                where the \src\ and \lib\ subfolders exist.
REM
REM   DESCRIPTION: Windows build script for the WAFFLES CS126 coursework.
REM
REM       OPTIONS: [-h, -b, -c, -d, -j, -r, -t, -z], see :HELP for more details
REM
REM  REQUIREMENTS: java, javac, jar
REM
REM       AUTHORS: Ian Tu, i.tu@warwick.ac.uk
REM                James Van Hinsbergh, j.van-hinsbergh@warwick.ac.uk
REM
REM  ORGANIZATION: University of Warwick
REM
REM       CREATED: 01/01/20 00:00:00
REM
REM      REVISION: 1.2.6
REM
REM===============================================================================


SET currentDir="%cd%"
SET projectRoot=%~dp0
CD %projectRoot%
SET buildFolder=target
SET sourceFolder=src\main\java
SET portNo=8080
SET compileError=0
SET submissionName=cs126-waffles-coursework
SET packageName=uk\ac\warwick\cs126
SET mainClass=""

IF "%~1"=="" (
    ECHO.
    ECHO     No input argument...
    CALL :HELP
    GOTO DONEANDOPENCMD
)
IF "%~1"=="-b" (
    CALL :COMPILEALL
    GOTO DONE
)
IF "%~1"=="-j" (
    IF NOT "%~2"=="" (
        SET mainClass=%2
    )
    CALL :RUNMAIN
    GOTO DONE
)
IF "%~1"=="-c" (
    CALL :CLEAN
    GOTO DONE
)
IF "%~1"=="-d" (
    CALL :DATAEXTRACT
    GOTO DONE
)
IF "%~1"=="-h" (
    CALL :HELP
    GOTO DONE
)
IF "%~1"=="-r" (
    IF NOT "%~2"=="" (
        SET portNo=%2
    )
    CALL :RUN
    GOTO DONE
)
IF "%~1"=="-t" (
    CALL :TEST
    GOTO DONE
)
IF "%~1"=="-z" (
    CALL :ZIP
    GOTO DONE
IF NOT "%~1"=="-h" (
    ECHO.
    ECHO     Invalid input argument, see -h for valid arguments.
    ECHO.
    GOTO DONE
)


:HELP
    ECHO.
    ECHO     This is a batch file to be run in Command Prompt or PowerShell.
    ECHO.
    ECHO     Required installation:
    ECHO        Java Development Kit 8 (JDK 8)
    ECHO.
    ECHO     Make sure the path to this JDK installation is added to the environment variables.
    ECHO.
    ECHO     In the Start Menu navigate to "Edit the system environment variables".
    ECHO     Inside "Environment Variables -> System Variables -> Path" there should be:
    ECHO        C:\Program Files\Java\jdk1.8.0_181\bin
    ECHO.
    ECHO     Version numbers and paths may vary. A restart may be required after updating the environment variables.
    ECHO     But in most cases, if you close and re-open Command Prompt/PowerShell after adding the path it should work.
    ECHO.
    ECHO     You can check if the JDK is properly installed by calling in Command Prompt or PowerShell:
    ECHO        where.exe java
    ECHO        where.exe javac
    ECHO        where.exe jar
    ECHO     The output of these should be the paths where they reside in.
    ECHO.
    ECHO     This batch file should be started from project root folder (where the \src \lib \data folders reside).
    ECHO.
    ECHO     Usage: %~n0%~x0 [-h,-c,-d,-b,-j,-t,-r,-z]
    ECHO.
    ECHO        -h      Display the help
    ECHO.
    ECHO        -c      Removes \target folder
    ECHO.
    ECHO        -d      Copy full and original data to \data folder
    ECHO.
    ECHO        -b      Compile sources in \%sourceFolder% and put compiled classes in \target 
    ECHO                Note: This first will clean the \target\classes folder
    ECHO.
    ECHO        -j      Run the main class of given input
    ECHO                Note: The class must be compiled beforehand, so argument -b should be ran before this
    ECHO.
    ECHO        -t      Compile uk\ac\warwick\cs126\test\TestRunner.java and run its main class
    ECHO                Note: This first will clean the \target\classes folder
    ECHO.
    ECHO        -r      Run the WAFFLES website on port 8080
    ECHO                Note: This first will clean the \target\BOOT-INF folder and delete \target\*.jar 
    ECHO.
    ECHO        -z      Zip up files for submission
    ECHO                Note: If target files are not compilable or do not exist this prcoess fails
    ECHO.
    ECHO     To specify the port number of website:
    ECHO        -r 9090     Run the WAFFLES website on port 9090, replace 9090 with your choice of port
    ECHO.
    ECHO     Examples for Command Prompt:
    ECHO        %~n0 -b      Will compile current sources
    ECHO        %~n0 -r      Will compile current sources and run the WAFFLES website
    ECHO        %~n0 -t      Will compile TestRunner and run its main class
    ECHO        %~n0 -c      Will delete the \target folder
    ECHO.
    ECHO     This will run the main class of TestRunner.java, it must be compiled beforehand before running:
    ECHO        %~n0 -j uk.ac.warwick.cs126.test.TestRunner     
    ECHO.
    ECHO     This will compile sources and try to run the WAFFLES website on port 9090:
    ECHO        %~n0 -r 9090    
    ECHO.
    ECHO     If you are using PowerShell, the syntax becomes:
    ECHO        ./%~n0%~x0 -r 9090
    ECHO.
GOTO EOF


:CLEAN
    ECHO.
    ECHO     Removing \target folder: "%projectRoot%%buildFolder%"
    IF NOT EXIST "%projectRoot%\%buildFolder%" (
        ECHO     No \target folder to clean.
    )
    IF EXIST "%projectRoot%\%buildFolder%" (
        RD /S /Q "%projectRoot%\%buildFolder%"
        IF EXIST "%projectRoot%\%buildFolder%" (
            ECHO     Could not remove \target folder.
            ECHO     Exiting...
            ECHO.
            GOTO EOF
        )
        ECHO     Removing \target folder successful.
    )
    ECHO.
GOTO EOF


:DATAEXTRACT
    ECHO.
    jar xf "%projectRoot%\waffles.jar" "BOOT-INF\classes\data\placeData.tsv" "BOOT-INF\classes\data\favouriteData.csv" "BOOT-INF\classes\data\reviewData.tsv" "BOOT-INF\classes\data\customerData.csv" "BOOT-INF\classes\data\restaurantData.csv"
    IF NOT EXIST "%projectRoot%\data\full" MD "%projectRoot%\data\full"  
    MOVE /Y "BOOT-INF\classes\data\*.csv" "%projectRoot%\data\full" > nul 2>&1
    MOVE /Y "BOOT-INF\classes\data\*.tsv" "%projectRoot%\data\full" > nul 2>&1
    RD /S /Q "BOOT-INF" > nul 2>&1
    IF EXIST "%projectRoot%\data\full\customerData.csv" (
        IF EXIST "%projectRoot%\data\full\favouriteData.csv" (
            IF EXIST "%projectRoot%\data\full\restaurantData.csv" (
                IF EXIST "%projectRoot%\data\full\reviewData.tsv" (
                    IF EXIST "%projectRoot%\data\full\placeData.tsv" (
                        ECHO     Successfully copied full data files into /data/full folder. 
                    ) ELSE (
                        ECHO     Copying data files unsuccessful. Exiting...
                    )
                ) ELSE (
                    ECHO     Copying data files unsuccessful. Exiting...
                )
            ) ELSE (
                ECHO     Copying data files unsuccessful. Exiting...
            )
        ) ELSE (
            ECHO     Copying data files unsuccessful. Exiting...
        )        
    ) ELSE (
        ECHO     Copying data files unsuccessful. Exiting...
    )
GOTO EOF


:COMPILEALL
    IF NOT EXIST "%projectRoot%\%sourceFolder%" (
        ECHO     Source directory "%projectRoot%%sourceFolder%" does not exist.
        ECHO     Exiting...
        ECHO.
        GOTO EOF
    )
    IF NOT EXIST "%projectRoot%\lib" (
        ECHO     Lib directory "%projectRoot%lib" does not exist.
        ECHO     Exiting...
        ECHO.
        GOTO EOF
    )
    ECHO.
    ECHO     Removing \target\classes folder: "%projectRoot%%buildFolder%\classes"
    ECHO.
    IF NOT EXIST "%projectRoot%\%buildFolder%\classes" (
        ECHO     No \target\classes folder to clean.
    )
    IF EXIST "%projectRoot%\%buildFolder%\classes" (
        RD /s /q "%projectRoot%\%buildFolder%\classes"
        IF EXIST "%projectRoot%\%buildFolder%\classes" (
            ECHO     Could not remove \target\classes folder.
            ECHO     Exiting...
            ECHO.
            GOTO EOF
        )
        ECHO     Removing \target\classes folder successful.
    )
    ECHO.
    ECHO     Building...
    ECHO.
    IF NOT EXIST "%buildFolder%\classes" MD "%buildFolder%\classes"    
    FOR %%i IN ("%projectRoot%\%sourceFolder%\uk\ac\warwick\cs126\test\*.java") DO (
        IF NOT EXIST "%projectRoot%\%buildFolder%\classes\uk\ac\warwick\cs126\test\%%~ni.class" (        
            javac -d "%projectRoot%\%buildFolder%\classes" -encoding "UTF-8" -cp "%projectRoot%\%sourceFolder%;%projectRoot%\lib\commons-io-2.6.jar;%projectRoot%\lib\cs126-interfaces-1.2.6.jar;%projectRoot%\lib\cs126-models-1.2.6.jar;" "%projectRoot%\%sourceFolder%\uk\ac\warwick\cs126\test\%%~nxi"
            IF NOT EXIST "%projectRoot%\%buildFolder%\classes\uk\ac\warwick\cs126\test\%%~ni.class" (
                ECHO.
                ECHO     Error when compiling %%~nxi
                ECHO     Exiting...
                ECHO.
                GOTO EOF
            )
        )
    )
    FOR %%i IN ("%projectRoot%\%sourceFolder%\uk\ac\warwick\cs126\stores\*.java") DO (
        IF NOT EXIST "%projectRoot%\%buildFolder%\classes\uk\ac\warwick\cs126\stores\%%~ni.class" (        
            javac -d "%projectRoot%\%buildFolder%\classes" -encoding "UTF-8" -cp "%projectRoot%\%sourceFolder%;%projectRoot%\lib\commons-io-2.6.jar;%projectRoot%\lib\cs126-interfaces-1.2.6.jar;%projectRoot%\lib\cs126-models-1.2.6.jar;" "%projectRoot%\%sourceFolder%\uk\ac\warwick\cs126\stores\%%~nxi"
            IF NOT EXIST "%projectRoot%\%buildFolder%\classes\uk\ac\warwick\cs126\stores\%%~ni.class" (
                ECHO.
                ECHO     Error when compiling %%~nxi
                ECHO     Exiting...
                ECHO.
                GOTO EOF
            )
        )
    )
    FOR %%i IN ("%projectRoot%\%sourceFolder%\uk\ac\warwick\cs126\util\*.java") DO (
        IF NOT EXIST "%projectRoot%\%buildFolder%\classes\uk\ac\warwick\cs126\util\%%~ni.class" (        
            javac -d "%projectRoot%\%buildFolder%\classes" -encoding "UTF-8" -cp "%projectRoot%\%sourceFolder%;%projectRoot%\lib\commons-io-2.6.jar;%projectRoot%\lib\cs126-interfaces-1.2.6.jar;%projectRoot%\lib\cs126-models-1.2.6.jar;" "%projectRoot%\%sourceFolder%\uk\ac\warwick\cs126\util\%%~nxi"
            IF NOT EXIST "%projectRoot%\%buildFolder%\classes\uk\ac\warwick\cs126\util\%%~ni.class" (
                ECHO.
                ECHO     Error when compiling %%~nxi
                ECHO     Exiting...
                ECHO.
                GOTO EOF
            )
        )
    )
    FOR %%i IN ("%projectRoot%\%sourceFolder%\uk\ac\warwick\cs126\structures\*.java") DO (
        IF NOT EXIST "%projectRoot%\%buildFolder%\classes\uk\ac\warwick\cs126\structures\%%~ni.class" (        
            javac -d "%projectRoot%\%buildFolder%\classes" -encoding "UTF-8" -cp "%projectRoot%\%sourceFolder%;%projectRoot%\lib\commons-io-2.6.jar;%projectRoot%\lib\cs126-interfaces-1.2.6.jar;%projectRoot%\lib\cs126-models-1.2.6.jar;" "%projectRoot%\%sourceFolder%\uk\ac\warwick\cs126\structures\%%~nxi"
            IF NOT EXIST "%projectRoot%\%buildFolder%\classes\uk\ac\warwick\cs126\structures\%%~ni.class" (
                ECHO.
                ECHO     Error when compiling %%~nxi
                ECHO     Exiting...
                ECHO.
                GOTO EOF
            )
        )
    )
    ECHO     Build done, compiled classes are in "%projectRoot%%buildFolder%"
    ECHO.
GOTO EOF


:RUNMAIN
    ECHO.
    IF NOT EXIST "%projectRoot%\%buildFolder%\classes" (
        ECHO     Target classes directory "%projectRoot%%buildFolder%\classes" does not exist.
        ECHO     Exiting...
        ECHO.
        GOTO EOF
    )
    IF NOT EXIST "%projectRoot%\lib" (
        ECHO     Lib directory "%projectRoot%lib" does not exist.
        ECHO     Exiting...
        ECHO.
        GOTO EOF
    )
    IF %mainClass% =="" (
        ECHO     No argument given. 
        ECHO     Should be of the form: uk.ac.warwick.cs126.test.TestRunner
        ECHO     Exiting...
        ECHO.
        GOTO EOF
    )
    ECHO     Running main class of "%mainClass%" ...
    ECHO.
    java -Xmx1700m -cp "%projectRoot%\%buildFolder%\classes;%projectRoot%\lib\commons-io-2.6.jar;%projectRoot%\lib\cs126-interfaces-1.2.6.jar;%projectRoot%\lib\cs126-models-1.2.6.jar;" %mainClass%
    ECHO.
GOTO EOF


:BUILD
    ECHO     Building...
    ECHO.
    IF NOT EXIST "%projectRoot%\%sourceFolder%" (
        ECHO     Source directory "%projectRoot%%sourceFolder%" does not exist.
        ECHO     Exiting...
        ECHO.
        GOTO EOF
    )
    IF NOT EXIST "%projectRoot%\lib" (
        ECHO     Lib directory "%projectRoot%lib" does not exist.
        ECHO     Exiting...
        ECHO.
        GOTO EOF
    )
    IF NOT EXIST "%buildFolder%\BOOT-INF\classes" MD "%buildFolder%\BOOT-INF\classes"    
    IF NOT EXIST "%projectRoot%\%buildFolder%\BOOT-INF\classes\uk\ac\warwick\cs126\stores\CustomerStore.class" (        
        javac -d "%projectRoot%\%buildFolder%\BOOT-INF\classes" -encoding "UTF-8" -cp "%projectRoot%\%sourceFolder%;%projectRoot%\lib\commons-io-2.6.jar;%projectRoot%\lib\cs126-interfaces-1.2.6.jar;%projectRoot%\lib\cs126-models-1.2.6.jar;" "%projectRoot%\%sourceFolder%\uk\ac\warwick\cs126\stores\CustomerStore.java"
        IF NOT EXIST "%projectRoot%\%buildFolder%\BOOT-INF\classes\uk\ac\warwick\cs126\stores\CustomerStore.class" (
            SET compileError=1
            ECHO.
            ECHO     Error when compiling CustomerStore.java
            ECHO     Exiting...
            ECHO.
            GOTO EOF
        )
    )
    IF NOT EXIST "%projectRoot%\%buildFolder%\BOOT-INF\classes\uk\ac\warwick\cs126\stores\FavouriteStore.class" (
        javac -d "%projectRoot%\%buildFolder%\BOOT-INF\classes" -encoding "UTF-8" -cp "%projectRoot%\%sourceFolder%;%projectRoot%\lib\commons-io-2.6.jar;%projectRoot%\lib\cs126-interfaces-1.2.6.jar;%projectRoot%\lib\cs126-models-1.2.6.jar;" "%projectRoot%\%sourceFolder%\uk\ac\warwick\cs126\stores\FavouriteStore.java"
        IF NOT EXIST "%projectRoot%\%buildFolder%\BOOT-INF\classes\uk\ac\warwick\cs126\stores\FavouriteStore.class" (
            SET compileError=1
            ECHO.
            ECHO     Error when compiling FavouriteStore.java
            ECHO     Exiting...
            ECHO.
            GOTO EOF
        )
    )
    IF NOT EXIST "%projectRoot%\%buildFolder%\BOOT-INF\classes\uk\ac\warwick\cs126\stores\RestaurantStore.class" (
        javac -d "%projectRoot%\%buildFolder%\BOOT-INF\classes" -encoding "UTF-8" -cp "%projectRoot%\%sourceFolder%;%projectRoot%\lib\commons-io-2.6.jar;%projectRoot%\lib\cs126-interfaces-1.2.6.jar;%projectRoot%\lib\cs126-models-1.2.6.jar;" "%projectRoot%\%sourceFolder%\uk\ac\warwick\cs126\stores\RestaurantStore.java"
        IF NOT EXIST "%projectRoot%\%buildFolder%\BOOT-INF\classes\uk\ac\warwick\cs126\stores\RestaurantStore.class" (
            SET compileError=1
            ECHO.
            ECHO     Error when compiling RestaurantStore.java
            ECHO     Exiting...
            ECHO.
            GOTO EOF
        )
    )
    IF NOT EXIST "%projectRoot%\%buildFolder%\BOOT-INF\classes\uk\ac\warwick\cs126\stores\ReviewStore.class" (
        javac -d "%projectRoot%\%buildFolder%\BOOT-INF\classes" -encoding "UTF-8" -cp "%projectRoot%\%sourceFolder%;%projectRoot%\lib\commons-io-2.6.jar;%projectRoot%\lib\cs126-interfaces-1.2.6.jar;%projectRoot%\lib\cs126-models-1.2.6.jar;" "%projectRoot%\%sourceFolder%\uk\ac\warwick\cs126\stores\ReviewStore.java"
        IF NOT EXIST "%projectRoot%\%buildFolder%\BOOT-INF\classes\uk\ac\warwick\cs126\stores\ReviewStore.class" (
            SET compileError=1
            ECHO.
            ECHO     Error when compiling ReviewStore.java
            ECHO     Exiting...
            ECHO.
            GOTO EOF
        )
    )
    ECHO     Build done, compiled classes are in "%projectRoot%%buildFolder%"
    ECHO.
GOTO EOF


:TEST
    ECHO.
    ECHO     Removing \target\classes folder: "%projectRoot%%buildFolder%\classes"
    ECHO.
    IF NOT EXIST "%projectRoot%\%buildFolder%\classes" (
        ECHO     No \target\classes folder to clean.
    )
    IF EXIST "%projectRoot%\%buildFolder%\classes" (
        RD /s /q "%projectRoot%\%buildFolder%\classes"
        IF EXIST "%projectRoot%\%buildFolder%\classes" (
            ECHO     Could not remove \target\classes folder.
            ECHO     Exiting...
            ECHO.
            GOTO EOF
        )
        ECHO     Removing \target\classes folder successful.
    )
    ECHO.
    ECHO     Compiling TestRunner...
    ECHO.
    IF NOT EXIST "%projectRoot%\lib" (
        ECHO     Lib directory "%projectRoot%lib" does not exist.
        ECHO     Exiting...
        ECHO.
        GOTO EOF
    )
    IF NOT EXIST "%buildFolder%\classes" MD "%buildFolder%\classes"
    javac -d "%projectRoot%\%buildFolder%\classes" -encoding "UTF-8" -cp "%projectRoot%\%sourceFolder%;%projectRoot%\lib\commons-io-2.6.jar;%projectRoot%\lib\cs126-interfaces-1.2.6.jar;%projectRoot%\lib\cs126-models-1.2.6.jar;" "%projectRoot%\%sourceFolder%\uk\ac\warwick\cs126\test\TestRunner.java"
    IF NOT EXIST "%projectRoot%\%buildFolder%\classes\uk\ac\warwick\cs126\test\TestRunner.class" (
        ECHO.
        ECHO     Error when compiling TestRunner.java
        ECHO     Exiting...
        ECHO.
        GOTO EOF
    )
    IF NOT EXIST "%projectRoot%\%buildFolder%\classes\uk\ac\warwick\cs126\test\TestRunner.class" (
        ECHO     File "%projectRoot%%buildFolder%classes\uk\ac\warwick\cs126\test\TestRunner.class" does not exist.
        ECHO     Exiting...
        ECHO.
        GOTO EOF
    )
    ECHO     Running TestRunner...
    ECHO.
    java -cp "%projectRoot%\%buildFolder%\classes;%projectRoot%\lib\commons-io-2.6.jar;%projectRoot%\lib\cs126-interfaces-1.2.6.jar;%projectRoot%\lib\cs126-models-1.2.6.jar;" uk.ac.warwick.cs126.test.TestRunner
    ECHO.
GOTO EOF


:CHECKFORNETSTAT
    SET FOUND=
    FOR %%X IN ("netstat.exe") DO (
        SET FOUND=%%~$PATH:X
    )
    IF NOT DEFINED FOUND (
        SET compileError=1
        ECHO.
        ECHO     "netstat" command not found
        ECHO.
        ECHO     The "netstat" command is used to check for open ports
        ECHO.
        ECHO     The "system32" folder is where the "NETSTAT.EXE" executable is located
        ECHO     So in order for it to work, please make sure the "system32" folder is added to your PATH
        ECHO.
        ECHO     Generally, the "system32" file path is: "C:\WINDOWS\system32"
        ECHO     But in some cases this varies, so please verify this for your installation
        ECHO.
        ECHO     Go to "Environment Variables > System Variables > PATH" and add the "system32" file path there
        ECHO.
        ECHO     After adding it to your PATH, a restart of Command Prompt/PowerShell or even your PC may be required
        ECHO.
        ECHO     Check your PATH in Command Prompt by using:
        ECHO         ^echo %%PATH%%
        ECHO.
        ECHO     Check your PATH in PowerShell by using:
        ECHO         $env:Path -split ';'
        ECHO.
        ECHO     Exiting...
        ECHO.
    )
GOTO EOF


:RUN
    ECHO.
    ECHO     Running...   
    SET isPortInUse=
    CALL :CHECKFORNETSTAT
    IF %compileError% == 1 (
        GOTO EOF        
    )
    FOR /F "USEBACKQ TOKENS=*" %%F IN (`"netstat -a -n | findstr %portNo% | findstr LISTENING"`) DO (
        SET isPortInUse=%%F
    )
    IF DEFINED [%isPortInUse%] (        
        ECHO.
        ECHO     Port %portNo% is in use.
        ECHO.
        ECHO     Please close the application that is using it or try a different port.
        GOTO EOF        
    )
    IF NOT EXIST "%projectRoot%\waffles.jar" (
        ECHO.
        ECHO     File %projectRoot%waffles.jar does not exist.
        ECHO     Exiting...
        ECHO.
        GOTO EOF
    )    
    ECHO.
    ECHO     Removing \target\BOOT-INF\classes folder: "%projectRoot%%buildFolder%\BOOT-INF\classes"
    ECHO.
    IF NOT EXIST "%projectRoot%\%buildFolder%\BOOT-INF\classes" (
        ECHO     No \target\BOOT-INF\classes folder to clean.
    )
    IF EXIST "%projectRoot%\%buildFolder%\BOOT-INF\classes" (
        RD /s /q "%projectRoot%\%buildFolder%\BOOT-INF\classes"
        IF EXIST "%projectRoot%\%buildFolder%\BOOT-INF\classes" (
            ECHO     Could not remove \target\BOOT-INF\classes folder.
            ECHO     Exiting...
            ECHO.
            GOTO EOF
        )
        ECHO     Removing \target\BOOT-INF\classes folder successful.
    )
    ECHO.
    CALL :BUILD
    IF %compileError% == 1 (
        GOTO EOF        
    )
    IF NOT EXIST "%projectRoot%\%buildFolder%\BOOT-INF\classes" (
        ECHO Compiled directory "%projectRoot%%buildFolder%\BOOT-INF\classes" does not exist.
        ECHO Exiting...
        ECHO.
        GOTO EOF
    )
    DEL /F /Q "%projectRoot%%buildFolder%\*.tmp" > nul 2>&1
    DEL /F /Q "%projectRoot%%buildFolder%\*.jar" > nul 2>&1
    COPY /Y "%projectRoot%waffles.jar" "%projectRoot%%buildFolder%\waffles-current-build-port-%portNo%.jar" > nul 2>&1
    IF NOT EXIST "%projectRoot%\%buildFolder%\waffles-current-build-port-%portNo%.jar" (
        ECHO     File "%projectRoot%%buildFolder%\waffles-current-build-port-%portNo%.jar" copy failed.
        ECHO     Exiting...
        ECHO.
        GOTO EOF
    )    
    ECHO     Copying over source files into jar...
    ECHO.
    jar uf "%projectRoot%\%buildFolder%\waffles-current-build-port-%portNo%.jar" -C "%projectRoot%\%buildFolder%" BOOT-INF
    ECHO     Copying done, new jar file: %projectRoot%%buildFolder%\waffles-current-build-port-%portNo%.jar
    ECHO.
    ECHO     Running website...
    ECHO.
    CMD /C java -jar "%projectRoot%\%buildFolder%\waffles-current-build-port-%portNo%.jar" --server.port=%portNo%
GOTO EOF


:ZIPCLEANANDBUILD
    ECHO.
    ECHO     Removing \target\classes folder: "%projectRoot%%buildFolder%\classes"
    ECHO.
    IF NOT EXIST "%projectRoot%\%buildFolder%\classes" (
        ECHO     No \target\classes folder to clean.
    )
    IF EXIST "%projectRoot%\%buildFolder%\classes" (
        RD /s /q "%projectRoot%\%buildFolder%\classes"
        IF EXIST "%projectRoot%\%buildFolder%\classes" (
            SET compileError=1
            ECHO.
            ECHO     Could not remove \target\classes folder.
            ECHO.
            ECHO     Exiting...
            ECHO.
            GOTO EOF
        )
        ECHO     Removing \target\classes folder successful.
    )
    ECHO.
    ECHO     Testing existence and compilation...
    ECHO.
    IF NOT EXIST "%buildFolder%\%submissionName%\%sourceFolder%" (
        SET compileError=1
        ECHO.
        ECHO     Source directory "%projectRoot%%submissionName%%sourceFolder%" does not exist.
        ECHO.
        ECHO     Exiting...
        ECHO.
        GOTO 
        EOF
    )
    IF NOT EXIST "%projectRoot%\lib" (
        SET compileError=1
        ECHO.
        ECHO     Lib directory "%projectRoot%lib" does not exist.
        ECHO.
        ECHO     Exiting...
        ECHO.
        GOTO EOF
    )        
    IF NOT EXIST "%buildFolder%\classes" MD "%buildFolder%\classes"
    IF NOT EXIST "%projectRoot%\%buildFolder%\classes\uk\ac\warwick\cs126\util\ConvertToPlace.class" (
        javac -d "%projectRoot%\%buildFolder%\classes" -encoding "UTF-8" -cp "%projectRoot%\%buildFolder%\%submissionName%\%sourceFolder%;%projectRoot%\lib\commons-io-2.6.jar;%projectRoot%\lib\cs126-interfaces-1.2.6.jar;%projectRoot%\lib\cs126-models-1.2.6.jar;" "%projectRoot%\%buildFolder%\%submissionName%\%sourceFolder%\uk\ac\warwick\cs126\util\ConvertToPlace.java"
        IF NOT EXIST "%projectRoot%\%buildFolder%\classes\uk\ac\warwick\cs126\util\ConvertToPlace.class" (
            SET compileError=1
            ECHO.
            ECHO        [ERROR] Error when compiling ConvertToPlace.java
            ECHO                Exiting...
            ECHO.
            GOTO EOF
        ) ELSE (
            ECHO     [COMPILED] ConvertToPlace.java 
        )
    ) ELSE (
            ECHO     [COMPILED] ConvertToPlace.java 
    )
    IF NOT EXIST "%projectRoot%\%buildFolder%\classes\uk\ac\warwick\cs126\util\DataChecker.class" (
        javac -d "%projectRoot%\%buildFolder%\classes" -encoding "UTF-8" -cp "%projectRoot%\%buildFolder%\%submissionName%\%sourceFolder%;%projectRoot%\lib\commons-io-2.6.jar;%projectRoot%\lib\cs126-interfaces-1.2.6.jar;%projectRoot%\lib\cs126-models-1.2.6.jar;" "%projectRoot%\%buildFolder%\%submissionName%\%sourceFolder%\uk\ac\warwick\cs126\util\DataChecker.java"
        IF NOT EXIST "%projectRoot%\%buildFolder%\classes\uk\ac\warwick\cs126\util\DataChecker.class" (
            SET compileError=1
            ECHO.
            ECHO        [ERROR] Error when compiling DataChecker.java
            ECHO                Exiting...
            ECHO.
            GOTO EOF
        ) ELSE (
            ECHO     [COMPILED] DataChecker.java 
        )
    ) ELSE (
            ECHO     [COMPILED] DataChecker.java 
    )
    IF NOT EXIST "%projectRoot%\%buildFolder%\classes\uk\ac\warwick\cs126\util\HaversineDistanceCalculator.class" (
        javac -d "%projectRoot%\%buildFolder%\classes" -encoding "UTF-8" -cp "%projectRoot%\%buildFolder%\%submissionName%\%sourceFolder%;%projectRoot%\lib\commons-io-2.6.jar;%projectRoot%\lib\cs126-interfaces-1.2.6.jar;%projectRoot%\lib\cs126-models-1.2.6.jar;" "%projectRoot%\%buildFolder%\%submissionName%\%sourceFolder%\uk\ac\warwick\cs126\util\HaversineDistanceCalculator.java"
        IF NOT EXIST "%projectRoot%\%buildFolder%\classes\uk\ac\warwick\cs126\util\HaversineDistanceCalculator.class" (
            SET compileError=1
            ECHO.
            ECHO        [ERROR] Error when compiling HaversineDistanceCalculator.java
            ECHO                Exiting...
            ECHO.
            GOTO EOF
        ) ELSE (
            ECHO     [COMPILED] HaversineDistanceCalculator.java 
        )
    ) ELSE (
            ECHO     [COMPILED] HaversineDistanceCalculator.java 
    )
    IF NOT EXIST "%projectRoot%\%buildFolder%\classes\uk\ac\warwick\cs126\util\KeywordChecker.class" (
        javac -d "%projectRoot%\%buildFolder%\classes" -encoding "UTF-8" -cp "%projectRoot%\%buildFolder%\%submissionName%\%sourceFolder%;%projectRoot%\lib\commons-io-2.6.jar;%projectRoot%\lib\cs126-interfaces-1.2.6.jar;%projectRoot%\lib\cs126-models-1.2.6.jar;" "%projectRoot%\%buildFolder%\%submissionName%\%sourceFolder%\uk\ac\warwick\cs126\util\KeywordChecker.java"
        IF NOT EXIST "%projectRoot%\%buildFolder%\classes\uk\ac\warwick\cs126\util\KeywordChecker.class" (
            SET compileError=1
            ECHO.
            ECHO        [ERROR] Error when compiling KeywordChecker.java
            ECHO                Exiting...
            ECHO.
            GOTO EOF
        ) ELSE (
            ECHO     [COMPILED] KeywordChecker.java 
        )
    ) ELSE (
            ECHO     [COMPILED] KeywordChecker.java 
    )
    IF NOT EXIST "%projectRoot%\%buildFolder%\classes\uk\ac\warwick\cs126\util\StringFormatter.class" (
        javac -d "%projectRoot%\%buildFolder%\classes" -encoding "UTF-8" -cp "%projectRoot%\%buildFolder%\%submissionName%\%sourceFolder%;%projectRoot%\lib\commons-io-2.6.jar;%projectRoot%\lib\cs126-interfaces-1.2.6.jar;%projectRoot%\lib\cs126-models-1.2.6.jar;" "%projectRoot%\%buildFolder%\%submissionName%\%sourceFolder%\uk\ac\warwick\cs126\util\StringFormatter.java"       
        IF NOT EXIST "%projectRoot%\%buildFolder%\classes\uk\ac\warwick\cs126\util\StringFormatter.class" (
            SET compileError=1
            ECHO.
            ECHO        [ERROR] Error when compiling StringFormatter.java
            ECHO                Exiting...
            ECHO.
            GOTO EOF
        ) ELSE (
            ECHO     [COMPILED] StringFormatter.java 
        )
    ) ELSE (
            ECHO     [COMPILED] StringFormatter.java 
    )
    IF NOT EXIST "%projectRoot%\%buildFolder%\classes\uk\ac\warwick\cs126\stores\CustomerStore.class" (        
        javac -d "%projectRoot%\%buildFolder%\classes" -encoding "UTF-8" -cp "%projectRoot%\%buildFolder%\%submissionName%\%sourceFolder%;%projectRoot%\lib\commons-io-2.6.jar;%projectRoot%\lib\cs126-interfaces-1.2.6.jar;%projectRoot%\lib\cs126-models-1.2.6.jar;" "%projectRoot%\%buildFolder%\%submissionName%\%sourceFolder%\uk\ac\warwick\cs126\stores\CustomerStore.java"
        IF NOT EXIST "%projectRoot%\%buildFolder%\classes\uk\ac\warwick\cs126\stores\CustomerStore.class" (
            SET compileError=1
            ECHO.
            ECHO        [ERROR] Error when compiling CustomerStore.java
            ECHO                Exiting...
            ECHO.
            GOTO EOF
        ) ELSE (
            ECHO     [COMPILED] CustomerStore.java 
        )
    ) ELSE (
            ECHO     [COMPILED] CustomerStore.java 
    )
    IF NOT EXIST "%projectRoot%\%buildFolder%\classes\uk\ac\warwick\cs126\stores\FavouriteStore.class" (
        javac -d "%projectRoot%\%buildFolder%\classes" -encoding "UTF-8" -cp "%projectRoot%\%buildFolder%\%submissionName%\%sourceFolder%;%projectRoot%\lib\commons-io-2.6.jar;%projectRoot%\lib\cs126-interfaces-1.2.6.jar;%projectRoot%\lib\cs126-models-1.2.6.jar;" "%projectRoot%\%buildFolder%\%submissionName%\%sourceFolder%\uk\ac\warwick\cs126\stores\FavouriteStore.java"
        IF NOT EXIST "%projectRoot%\%buildFolder%\classes\uk\ac\warwick\cs126\stores\FavouriteStore.class" (
            SET compileError=1
            ECHO.
            ECHO        [ERROR] Error when compiling FavouriteStore.java
            ECHO                Exiting...
            ECHO.
            GOTO EOF
        ) ELSE (
            ECHO     [COMPILED] FavouriteStore.java 
        )
    ) ELSE (
            ECHO     [COMPILED] FavouriteStore.java 
    )
    IF NOT EXIST "%projectRoot%\%buildFolder%\classes\uk\ac\warwick\cs126\stores\RestaurantStore.class" (
        javac -d "%projectRoot%\%buildFolder%\classes" -encoding "UTF-8" -cp "%projectRoot%\%buildFolder%\%submissionName%\%sourceFolder%;%projectRoot%\lib\commons-io-2.6.jar;%projectRoot%\lib\cs126-interfaces-1.2.6.jar;%projectRoot%\lib\cs126-models-1.2.6.jar;" "%projectRoot%\%buildFolder%\%submissionName%\%sourceFolder%\uk\ac\warwick\cs126\stores\RestaurantStore.java"
        IF NOT EXIST "%projectRoot%\%buildFolder%\classes\uk\ac\warwick\cs126\stores\RestaurantStore.class" (
            SET compileError=1
            ECHO.
            ECHO        [ERROR] Error when compiling RestaurantStore.java
            ECHO                Exiting...
            ECHO.
            GOTO EOF
        ) ELSE (
            ECHO     [COMPILED] RestaurantStore.java 
        )
    ) ELSE (
            ECHO     [COMPILED] RestaurantStore.java 
    )
    IF NOT EXIST "%projectRoot%\%buildFolder%\classes\uk\ac\warwick\cs126\stores\ReviewStore.class" (
        javac -d "%projectRoot%\%buildFolder%\classes" -encoding "UTF-8" -cp "%projectRoot%\%buildFolder%\%submissionName%\%sourceFolder%;%projectRoot%\lib\commons-io-2.6.jar;%projectRoot%\lib\cs126-interfaces-1.2.6.jar;%projectRoot%\lib\cs126-models-1.2.6.jar;" "%projectRoot%\%buildFolder%\%submissionName%\%sourceFolder%\uk\ac\warwick\cs126\stores\ReviewStore.java"
        IF NOT EXIST "%projectRoot%\%buildFolder%\classes\uk\ac\warwick\cs126\stores\ReviewStore.class" (
            SET compileError=1
            ECHO.
            ECHO        [ERROR] Error when compiling ReviewStore.java
            ECHO                Exiting...
            ECHO.
            GOTO EOF
        ) ELSE (
            ECHO     [COMPILED] ReviewStore.java 
        )
    ) ELSE (
            ECHO     [COMPILED] ReviewStore.java 
    )
    
    IF NOT EXIST "%projectRoot%\Report.md" (
        SET compileError=1
        ECHO.
        ECHO        [ERROR] Report.md does not exist
        ECHO                Exiting...
        ECHO.
        GOTO EOF
    ) ELSE (
        ECHO       [EXISTS] Report.md
    )
    ECHO.
    ECHO     All neccessary files exist and have compiled successfully.
GOTO EOF


:ZIP
    ECHO.
    ECHO     Processing submission...   
    IF EXIST "%projectRoot%\submission.zip" (
        DEL /Q "%projectRoot%\submission.zip" > nul 2>&1
        IF EXIST "%projectRoot%\submission.zip" (
            ECHO.
            ECHO     Could not remove existing zip: "%projectRoot%submission.zip"
            ECHO.
            ECHO     Exiting...
            ECHO.
            GOTO EOF
        )        
        ECHO.
        ECHO     Removing old submission.zip successful
    )
    IF EXIST "%projectRoot%\%buildFolder%\%submissionName%" (
        RD /S /Q "%projectRoot%\%buildFolder%\%submissionName%" > nul 2>&1
        IF EXIST "%projectRoot%\%buildFolder%\%submissionName%" (
            ECHO.
            ECHO     Could not remove %projectRoot%%buildFolder%\%submissionName% folder.
            ECHO.
            ECHO     Exiting...
            ECHO.
            GOTO EOF
        )
        ECHO.
        ECHO     Removing old submission.zip successful
    )
    MD "%projectRoot%\%buildFolder%\%submissionName%\%sourceFolder%\%packageName%\stores"
    XCOPY /E /I /Y "%projectRoot%\%sourceFolder%\%packageName%\stores" "%projectRoot%\%buildFolder%\%submissionName%\%sourceFolder%\%packageName%\stores" > nul 2>&1
    MD "%projectRoot%\%buildFolder%\%submissionName%\%sourceFolder%\%packageName%\structures"
    XCOPY /E /I /Y "%projectRoot%\%sourceFolder%\%packageName%\structures" "%projectRoot%\%buildFolder%\%submissionName%\%sourceFolder%\%packageName%\structures" > nul 2>&1
    MD "%projectRoot%\%buildFolder%\%submissionName%\%sourceFolder%\%packageName%\util"
    XCOPY /E /I /Y "%projectRoot%\%sourceFolder%\%packageName%\util" "%projectRoot%\%buildFolder%\%submissionName%\%sourceFolder%\%packageName%\util" > nul 2>&1
    COPY /Y "%projectRoot%\Report.md" "%projectRoot%\%buildFolder%\%submissionName%" > nul 2>&1
    CALL :ZIPCLEANANDBUILD
    IF %compileError% == 1 (
        GOTO EOF        
    )
    ECHO.
    ECHO     Now zipping...  
    ECHO.
    jar cvfM "%projectRoot%\submission.zip" -C "%projectRoot%\%buildFolder%" %submissionName% > nul 2>&1
    IF EXIST "%projectRoot%\submission.zip" (
        ECHO     Zip successful.
        ECHO     Zipped submission located at: %projectRoot%submission.zip
    ) ELSE (
        ECHO.
        ECHO     Zip failed.
    )
    ECHO.
    ECHO     Cleaning up...
    RD /S /Q "%projectRoot%\%buildFolder%\%submissionName%" > nul 2>&1
    ECHO.
    ECHO     Successfully zipped up a submission.
GOTO EOF


:DONE
    CD %currentDir%
GOTO EOF


:DONEANDOPENCMD
    CD %currentDir%
    CMD /K
GOTO EOF


:EOF