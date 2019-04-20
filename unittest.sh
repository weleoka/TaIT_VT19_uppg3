#!/bin/bash
echo "Running unittests..."
#java -javaagent:./lib/org.jacoco.agent-0.7.7.jar -cp ./lib/org.junit4-4.3.1.jar:./bin org.junit.runner.JUnitCore ltu.PaymentImplTest ltu.MainTest ltu.CalendarImplTest ltu.CalendarFactoryTest
java -javaagent:./lib/jacocoagent.jar -cp ./lib/junit-4.12.jar:./lib/hamcrest-core-1.3.jar:./bin/tests:./bin/production org.junit.runner.JUnitCore ltu.PaymentImplTest ltu.MainTest ltu.CalendarImplTest ltu.CalendarFactoryTest


echo "Generating report..."
#java -jar ./lib/org.jacoco.examples-0.7.7.jar .
java -jar lib/jacococli.jar report jacoco.exec --classfiles ./bin/tests --sourcefiles ./src/tests --html ./jacocoreporthtml