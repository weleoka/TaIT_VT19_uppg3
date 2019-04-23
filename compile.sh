#!/bin/bash
# Compile script for java 8.
# More info on javac and especially path construction at:
# https://docs.oracle.com/javase/8/docs/technotes/tools/unix/classpath.html
# javac -g is to include debug info (line numbers also) in compiled classes.
# javac -d flag is output directory
echo "Compiling code..."
mkdir -p ./bin/production

javac -g -d ./bin/production \
-classpath ./src/main \
./src/main/ltu/Main.java \
./src/main/ltu/CalendarImpl.java

# javac -d ./bin -cp ./src/main ./src/main/ltu/Main.java ./src/main/ltu/CalendarImpl.java


echo "Compiling tests..."
mkdir -p ./bin/test

javac -g -d ./bin/test \
-classpath "./src/main:\
./lib/hamcrest-2.1.jar:\
./lib/junit-4.12.jar:\
./lib/JUnitParams-1.1.1.jar:\
./lib/opencsv-4.5.jar" \
./src/test/ltu/*.java

# javac -d ./bin -cp ./lib/org.junit4-4.3.1.jar:./src/main ./src/test/ltu/PaymentImplTest.java