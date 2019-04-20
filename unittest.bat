@echo off
set JDK_HOME=c:\PROGRA~1\Java\jdk1.8.0_45

rem set REPORT=lib\org.jacoco.examples-0.7.7.jar
set REPORT=lib\jacocoagent.jar

rem set AGENT=lib\org.jacoco.agent-0.7.7.jar
set AGENT=lib\jacocoagent.jar

rem set JUNIT=lib\org.junit4-4.3.1.jar
set JUNIT=lib\org.junit4-4.3.1.jar

set HAMCREST=lib\hamcrest-core-1.3.jar

@echo "Running unittests ..."
rem %JDK_HOME%/bin/java.exe -javaagent:%AGENT% -cp %JUNIT%;bin org.junit.runner.JUnitCore ltu.PaymentImplTest ltu.MainTest ltu.CalendarImplTest ltu.CalendarFactoryTest
%JDK_HOME%/bin/java.exe -javaagent:%AGENT% -cp %JUNIT%;%HAMCREST%;bin/tests;bin/production org.junit.runner.JUnitCore ltu.PaymentImplTest ltu.MainTest ltu.CalendarImplTest ltu.CalendarFactoryTest

@echo "Generating report ..."
rem %JDK_HOME%/bin/java.exe -jar %REPORT% .
%JDK_HOME%/bin/java.exe -jar %REPORT% report jacoco.exec --classfiles bin/tests --sourcefiles src/tests --html jacocoreporthtml