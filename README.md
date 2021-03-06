# TaIT_VT19_uppg3

Bash scripts for linux are up to date with the latest libraries of JUnit and JaCoCo and all the rest.

Windows batch scripts are updated but need testing! Please let me know if things look like they are working on windows as I dont have time to check my self.

Linux just execute the whole thing with `./ct`, which is short for compile_and_test. Windows `ct.bat`.

TODO: Update the windows .bat files for current build and testing requirements.
TODO: Fix compile scripts to make test coverage reports on actual test classes optional.

In the repository root is a file called 'PaymentImpl.class' and it is a compiled binary which contains errors for then "testing-the-tests". Replace the compiled binary in /bin with this file to try it out.


## Empty Unit tests
Not counting empty unit tests towards overall overage. If the test method is empty then why should that count towards coverage in test coverage reports? They are actually not testing anything!

Looking around in JUnit I find the `@Ignore("Please write this test!")` annotation. This ignores the tests from JUnit, but Jacoco will complain that the runtime classfiles are not the same as supplied for the coverage report.

Another method which I tried was to comment out all the @Test annotations, but then the compiling fails with `java.lang.Exception: No runnable methods`. The same thing happens if I remove empty methods and the class is declared but has no methods in it.

So far there is only one relatively satisfactory solution to this problem; if there is a test class then it has to have at least one test method. Jococo does not understand JUnits @Ignore annotation, which is a pitty. But if the test is not implemented then you can do this in each test block `throw new RuntimeException("implement me");`. That's quite a good compromise.


## Parameterized test class
JUnit has the parameterized test where fields or constructor injection can be used to run tests using a data set. What is not clear is how individual items in the collection are cast/parsed to the required data types.

There is a good helper library, that seems to then have had ideas from it partially incorporated into JUnit5 called JUnitParams. It allows reading of parameters directly in to unit tests, and reducing boilerplate code for test classes by making it unnecessary to inject fields or constructor with params, instead annotating each method. It should also make it possible to have multiple test methods with different parameters withing the same class, whereas the JUnit4 is basically one method one class if it's parameterized.

The trouble with using JUnit's built in parameterized test is that it requires the method returning the Collection object of parameters to be static, which in turn means that the CSVReader can't be instantiated.

The solution should be to use the helper library JUnitParams but when doing that the following ocurs due to an initial line problem in the CSv file. `java.lang.IllegalArgumentException: Cannot parse parameters. Did you use ',' or '|' as column separator? #Social Security Number;Income;Study pace;Completion ratio`

```
@FileParameters("inputfiles/debug.csv")
@FileParameters(classpath:inputfiles/debig.csv)`
```

The good summary of the JUnit4 and JUnitPArams discussion is at [this link](https://www.testwithspring.com/lesson/writing-parameterized-tests-with-junit-4/)

When running from commandline and IDE the paths in the @FileParameters for JUnitParams get resolved differently. The two annotation methods most usable seem to be: 


## Exceptions in tests
Testing the PaymentImpl class is different as the methods throw IOException.

JUnit has different ways to handle this. See a good description at:
https://blog.goyello.com/2015/10/01/different-ways-of-testing-exceptions-in-java-and-junit/

 In Brief the old method is to use the standard try catch blocks for the unit tests.

JUnit4+ has @Test annotation and expected element. e.g. @Test(expected = IOException.class)

 todo Also it is possible for the test to throw an exception. Good or bad?
 Currently double tests are implemented one which passes the exception up and the other which will catch the exception from called method in subject class.This test class relies on the old try catch blocks.

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




## CHEATER Sheet
 Below you will find the errors that were introduced in the code (`PaymentImpl.class` found in repo root). The error may have symptoms in several requirement IDs so if your ID does not match the one in the list it may simply be that you caught the error as part of another requirement. This is one of the reasons why it may sometimes be difficult to find the actual cause of an error, even if unit-testing is performed.

The following bugs were introduced in the production code:  

```java
[ID 101] "age <= 20" instead of "age < 20" for subsidy
[ID 103] "age > 47" instead of "age >= 47" for loan
[ID 302] HALF_SUBSIDY instead of ZERO_SUBSIDY for part time students with too high income
[ID 503 (could also be caught in 202)] student50loan=4564 instead of student50loan=3564
[ID 506} Maximum day of February is always 28

[ID 102 || ID 203 || ID 502]
// Special case for debugging, located in getSubsidy
// Error: debug flag is true in production code

if (debug && studyRate == 100 && age > 56 && completionRatio >= 50 && income <= FULLTIME_INCOME) 
{
    return Integer.MAX_VALUE;
}
```

It is not possible to get 100% test coverage without changing the code due to the IOException that is supposed to be thrown by the load function. The function takes an InputStream that is created by the StringReader function that takes a String argument. It is not possible to get this exception to throw without modifying the code.  

[ID 506] Can be unit-tested by using the fact that the Calendar is instantiated using a CalendarFactory. By utilising code/class injection it is possible to create a new calendar class that can return another date than current time.


Additional error in the requirements specification:  

ID203 and ID501 says that a full time student should receive 100% subsidiary, 7088 SEK / month.
While ID202 says that a student studying less that full time is entitled to 50% subsidiary.
2816/2 = 1408 (1) but ID504 says that a student whose study pace is less than 100% should receive 1396 SEK / month. The requirement ID505 says that a student who is entitled to student loan should always get the full amount.  

But a full time student shall receive 7088 SEK / month says ID501, and ID503 says that a student
whose study pace is less than 100% should only receive 3564 SEK / month. ID503 and ID505's
requirements contradict each other.