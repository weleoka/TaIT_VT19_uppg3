# TaIT_VT19_uppg3

Bash scripts for linux are up to date with the latest libraries of JUnit and JaCoCo and all the rest.


Windows batch scripts are updated but need testing! Please let me know if things look like they are working on windows as I dont have time to check my self.


Linux just execute the whole thing with `./ct`, which is short for compile_and_test. Windows `ct.bat`.

## Empty Unit tests
Not counting empty unit tests towards overall overage. If the test method is empty then why should that count towards coverage in test coverage reports? They are actually not testing anything!
Looking around in JUnit I find the `@Ignore("Please write this test!")` annotation. This ignores the tests from JUnit, but Jacoco will complain that the runtime classfiles are not the same as supplied for the coverage report.
Another method which I tried was to comment out all the @Test annotations, but then the compiling fails with `java.lang.Exception: No runnable methods
`. The same thing happens if I remove empty methods and the class is declared but has no methods in it.
So far there is not satisfactory solution to this problem; if there is a test class then it has to have at least one test method. Jococo does not understand JUnits @Ignore annotation, which is a pitty.
