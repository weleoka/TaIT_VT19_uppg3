#!/bin/bash
echo "Running unittests..."
#java -javaagent:./lib/org.jacoco.agent-0.7.7.jar -cp ./lib/org.junit4-4.3.1.jar:./bin org.junit.runner.JUnitCore ltu.PaymentImplTest ltu.MainTest ltu.CalendarImplTest ltu.CalendarFactoryTest
#-cp ./lib/junit-4.12.jar:./lib/hamcrest-core-1.3.jar:./lib/JUnitParams-1.1.1.jar:./bin/tests:./bin/production \
java \
-javaagent:./lib/jacocoagent.jar \
-cp "./lib/junit-4.12.jar:\
./lib/hamcrest-2.1.jar:\
./lib/JUnitParams-1.1.1.jar:\
./lib/opencsv-4.5.jar:\
./lib/commons-lang3-3.9.jar:\
./bin/test:./bin/production" \
org.junit.runner.JUnitCore \
ltu.MainTest \
ltu.CalendarImplTest \
ltu.CalendarFactoryTest \
ltu.PaymentImplTest \
#ltu.PaymentImplJUnitParamsTest

# java -javaagent:./lib/org.jacoco.agent-0.7.7.jar -cp ./lib/org.junit4-4.3.1.jar:./bin org.junit.runner.JUnitCore

echo "Generating report..."
java \
-jar lib/jacococli.jar report jacoco.exec \
--classfiles ./bin/test \
--sourcefiles ./src/main \
--sourcefiles ./src/test \
--html ./jacocoreporthtml

# java -jar ./lib/org.jacoco.examples-0.7.7.jar .
#--sourcefiles "./src/test/ltu:./src/main/ltu"