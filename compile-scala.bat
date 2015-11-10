@echo off
for %%X in (scalac.bat) do (set SCALA_FOUND=%%~$PATH:X)

if not exist "%SCALA_HOME%\lib\scala-library.jar" (
    echo Unable to find 'scala-library.jar' in SCALA_HOME ["%SCALA_HOME%"]
    exit 1
)

if not exist src\main\scala\Runner.scala (
    echo Unable to find src\main\scala\Runner.scala > compilation.log
    exit 1
)

if not exist src\main\scala\MyStrategy.scala (
    echo Unable to find src\main\scala\MyStrategy.scala > compilation.log
    exit 1
)

rd /Q /S classes
md classes

call "%SCALA_HOME%\bin\scalac" -encoding UTF-8 -sourcepath "src/main/scala" -d classes "src/main/scala/Runner.scala" >compilation.log 2>&1

if not exist classes\Runner.class (
    echo Unable to find classes\Runner.class >> compilation.log
    exit 1
)

if not exist classes\MyStrategy.class (
    echo Unable to find classes\MyStrategy.class >> compilation.log
    exit 1
)

echo Manifest-Version: 1.0>MANIFEST.MF
echo Main-Class: Runner>>MANIFEST.MF
echo Class-Path: scala-library.jar scala-reflect.jar>>MANIFEST.MF

jar -cfm "./scala-cgdk.jar" MANIFEST.MF -C "./classes" . >>compilation.log 2>&1
copy /Y /B "%SCALA_HOME%\lib\scala-library.jar" .
copy /Y /B "%SCALA_HOME%\lib\scala-reflect.jar" .
