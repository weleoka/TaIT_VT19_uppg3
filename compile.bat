@echo off
rem Classpath info at:
rem https://docs.oracle.com/javase/8/docs/technotes/tools/windows/classpath.html
set JDK_HOME=c:\PROGRA~1\Java\jdk1.8.0_45

@echo "Compiling code ..."
rem @mkdir bin
@mkdir bin/production

rem %JDK_HOME%/bin/javac.exe -d bin -cp src src/ltu/Main.java src/ltu/*.java
%JDK_HOME%/bin/javac.exe -g -d bin -cp src src/main/ltu/*.java

@echo "Compiling tests ..."
rem set JUNIT=lib\org.junit4-4.3.1.jar
set JUNIT=lib\junit-4.12.jar

set HAMCREST=lib\hamcrest-core-1.3.jar
set OPENCSV=lib\opencsv-4.5.jar

%JDK_HOME%/bin/javac.exe -g -d bin/tests -cp %JUNIT%;%HAMCREST%;%OPENCSV%;src/main src/tests/ltu/*.java