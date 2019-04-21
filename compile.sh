#!/bin/bash
echo "Compiling code..."
mkdir -p ./bin/production
javac -g -d ./bin/production -cp ./src ./src/main/ltu/*.java

echo "Compiling tests..."
mkdir -p ./bin/tests
javac -g -d ./bin/tests -cp ./lib/junit-4.12.jar:./lib/hamcrest-core-1.3.jar:./lib/opencsv-2.3.jar:./src/main ./src/tests/ltu/*.java