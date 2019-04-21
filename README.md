# TaIT_VT19_uppg3

Bash scripts for linux are up to date with the latest libraries of JUnit and JaCoCo and all the rest.


Windows batch scripts are updated but need testing! Please let me know if things look like they are working on windows as I dont have time to check my self.


Linux just execute the whole thing with `./ct`, which is short for compile_and_test. Windows `ct.bat`.

## Empty Unit tests
Not counting empty unit tests towards overall overage. If the test method is empty then why should that count towards coverage in test coverage reports? They are actually not testing anything!

Looking around in JUnit I find the `@Ignore("Please write this test!")` annotation. This ignores the tests from JUnit, but Jacoco will complain that the runtime classfiles are not the same as supplied for the coverage report.

Another method which I tried was to comment out all the @Test annotations, but then the compiling fails with `java.lang.Exception: No runnable methods`. The same thing happens if I remove empty methods and the class is declared but has no methods in it.

So far there is only one relatively satisfactory solution to this problem; if there is a test class then it has to have at least one test method. Jococo does not understand JUnits @Ignore annotation, which is a pitty. But if the test is not implemented then you can do this in each test block `throw new RuntimeException("implement me");`. That's quite a good compromise.



## Requirements

    Age requirements
        [ID: 101] The student must be at least 20 years old to receive subsidiary and student loans.
        [ID: 102] The student may receive subsidiary until the year they turn 56.
        [ID: 103] The student may not receive any student loans from the year they turn 47.
    Study pace requirements
        [ID: 201] The student must be studying at least half time to receive any subsidiary.
        [ID: 202] A student studying less than full time is entitled to 50% subsidiary.
        [ID: 203] A student studying full time is entitled to 100% subsidiary.
    Income while studying requirements
        [ID: 301] A student who is studying full time or more is permitted to earn a maximum of 85 813SEK per year in order to receive any subsidiary or student loans.
        [ID: 302] A student who is studying less than full time is allowed to earn a maximum of 128 722SEK per year in order to receive any subsidiary or student loans.
    Completion ratio requirement
        [ID: 401] A student must have completed at least 50% of previous studies in order to receive any subsidiary or student loans.
  
    When and amount paid requirements
    Full time students are entitled to:
        [ID: 501] Student loan: 7088 SEK / month
        [ID: 502] Subsidiary: 2816 SEK / month
    Less than full time students are entitled to:
        [ID: 503] Student loan: 3564 SEK / month
        [ID: 504] Subsidiary: 1396 SEK / month
        [ID: 505] A person who is entitled to receive a student loan will always receive the full amount.
        [ID: 506] Student loans and subsidiary is paid on the last weekday (Monday to Friday) every month.

Your task is to run the system under test and verify that these requirements are correctly implemented. The implementation you have been given uses the current month to calculate payment date, but you have been asked to test the system for the spring-term of 2016 (2016-01-01 to 2016-06-30). 