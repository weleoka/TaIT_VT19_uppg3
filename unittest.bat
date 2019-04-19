@echo off
set JDK_HOME=c:\PROGRA~1\Java\jdk1.8.0_45
set REPORT=lib\org.jacoco.examples-0.7.7.jar
set AGENT=lib\org.jacoco.agent-0.7.7.jar
set JUNIT=lib\org.junit4-4.3.1.jar
@echo "Running unittests ..."
%JDK_HOME%/bin/java.exe -javaagent:%AGENT% -cp %JUNIT%;bin org.junit.runner.JUnitCore ltu.PaymentImplTest ltu.MainTest ltu.CalendarImplTest ltu.CalendarFactoryTest
@echo "Generating report ..."
%JDK_HOME%/bin/java.exe -jar %REPORT% .