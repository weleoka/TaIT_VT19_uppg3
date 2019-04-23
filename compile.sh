#!/bin/bash
echo "Compiling code..."
mkdir -p ./bin/production
javac -g -d ./bin/production -cp ./src ./src/main/ltu/*.java

echo "Compiling tests..."
mkdir -p ./bin/test
javac -g -d ./bin/test \
-classpath ./src \
-sourcepath ./src/test/ltu