echo "Running unittests..."
java -javaagent:./lib/org.jacoco.agent-0.7.7.jar -cp ./lib/org.junit4-4.3.1.jar:./bin org.junit.runner.JUnitCore ltu.PaymentImplTest ltu.MainTest ltu.CalendarImplTest ltu.CalendarFactoryTest
echo "Generating report..."
java -jar ./lib/org.jacoco.examples-0.7.7.jar .