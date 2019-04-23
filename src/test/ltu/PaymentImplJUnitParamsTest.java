package ltu;

import junitparams.FileParameters;
import junitparams.JUnitParamsRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

import static java.lang.Integer.parseInt;
import static org.junit.Assert.assertEquals;

/**
 * Testing the PaymentImpl class is different as the methods throw IOException.
 * JUnit has different ways to handle this. See a good description at:
 * https://blog.goyello.com/2015/10/01/different-ways-of-testing-exceptions-in-java-and-junit/
 *
 * In Brief the old method is to use the standard try catch blocks for the unit tests.
 * JUnit4+ has @Test annotation and expected element. e.g. @Test(expected = IOException.class)
 *
 * todo Also it is possible for the test to throw an exception. Good or bad?
 *
 * Currently double tests are implemented one which passes the exception up and the other
 * which will catch the exception from called method in subject class.
 *
 * This test class relies on the old try catch blocks.
 */
// @RunWith(Parameterized.class)
@RunWith(JUnitParamsRunner.class)
public class PaymentImplJUnitParamsTest {

    private CalendarFactory cf;
    private ICalendar cal;

    /**
     * student100loan=7088
     * student100subsidy=2816
     * student50loan=3564
     * student50subsidy=1396
     * student0loan=0
     * student0subsidy=0
     * fulltimeIncome=85813
     * parttimeIncome=128722";
     */
    private final String rules = "student100loan=7088\nstudent100subsidy=2816\nstudent50loan=3564\nstudent50subsidy=1396\nstudent0loan=0\nstudent0subsidy=0\nfulltimeIncome=85813\nparttimeIncome=128722\n";
    private Properties props;


    private PaymentImpl paymentImpl = null;

    private int FULL_LOAN;
    private int HALF_LOAN;
    private int ZERO_LOAN;
    private int FULL_SUBSIDY;
    private int HALF_SUBSIDY;
    private int ZERO_SUBSIDY;
    private int FULLTIME_INCOME;
    private int PARTTIME_INCOME;

    public PaymentImplJUnitParamsTest() {}

    // Parameterized test with constructor injection
    private String pnr;
    private int income;
    private int studyPace;
    private int completionRatio;



/*
    //public void loadData(String pnr, int income, int studyPace, int completionRatio) { }
    private final CSVReaderImpl debugReader = new CSVReaderImpl("inputfiles/debug.csv");
    public Collection<String[]> loadData() {
        List<String[]> testData = debugReader.getAllLines();
        return testData;
    }
*/

    @Before
    public void entryPoint() throws IOException {
        cf = new CalendarFactory();
        cal = cf.getCalendar();
        paymentImpl = new PaymentImpl(cal);

        props = new Properties();
        props.load(new StringReader(rules));

        FULL_LOAN = parseInt((String) props.get("student100loan"));
        HALF_LOAN = parseInt((String) props.get("student50loan"));
        ZERO_LOAN = parseInt((String) props.get("student0loan"));
        FULL_SUBSIDY = parseInt((String) props.get("student100subsidy"));
        HALF_SUBSIDY = parseInt((String) props.get("student50subsidy"));
        ZERO_SUBSIDY = parseInt((String) props.get("student0subsidy"));
        FULLTIME_INCOME = parseInt((String) props.get("fulltimeIncome"));
        PARTTIME_INCOME = parseInt((String) props.get("parttimeIncome"));
    }


    @Test
    public void instantiateWithCalendar() throws IOException {
        PaymentImpl paymentImpl = new PaymentImpl(cal);
    }

    @Test
    public void instantiateWithCalendarAndCatchException() {

        try {
            PaymentImpl payment = new PaymentImpl(cal);

        } catch (IOException e) {
            Assert.fail("This should not throw an IOException.");
        }
    }

    @Test
    public void instantiateWithCalendarAndGoodRules() throws IOException {
        PaymentImpl payment = new PaymentImpl(cal, rules);
    }

    @Test
    public void instantiateWithCalendarAndGoodRulesAndCatchException() {
        String rules = "student100loan=7088\nstudent100subsidy=2816\nstudent50loan=3564\nstudent50subsidy=1396\nstudent0loan=0\nstudent0subsidy=0\nfulltimeIncome=85813\nparttimeIncome=128722\n";

        try {
            PaymentImpl payment = new PaymentImpl(cal, rules);

        } catch (IOException e) {
            Assert.fail("This should not throw an IOException.");
        }
    }

    @Test
    public void instantiateWithCalendarAndBadRules() {
        String rules = "xyz!!!";

        try {
            PaymentImpl payment = new PaymentImpl(cal, rules);

        } catch (NumberFormatException e) {
            //Test has passed because NumberFormatException is thrown.
            return;

        } catch (IOException e) {
            Assert.fail("This should not throw an IOException.");
        }
    }

    @Test
    public void getNextPaymentDay() {
        
    }

    @Test
    @FileParameters("test/resources/debug.csv")
    public void getMonthlyAmount() {
        System.out.println("PaymentImplJUnitParamsTest.getMonthlyAmount");
        System.out.printf("%s, %s, %s, %s", pnr, income, studyPace, completionRatio);
        int allowence = paymentImpl.getMonthlyAmount(pnr, income, studyPace, completionRatio);
        assertEquals(allowence, FULL_SUBSIDY);
    }

    @Test
    @FileParameters("test/resources/debug.csv")
    public void getMonthlyAmountTooYoung() {
        System.out.println("PaymentImplJUnitParamsTest.getMonthlyAmountTooYoung");
        System.out.printf("%s, %s, %s, %s", pnr, income, studyPace, completionRatio);
        int allowance = paymentImpl.getMonthlyAmount("20190101-0000", income, studyPace, completionRatio);
        assertEquals(allowance, 0);
    }

    @Test
    //@FileParameters("src/resources/debug.csv")
    //@FileParameters("classpath:inputfiles/debug.csv")
    @FileParameters("test/resources/debug.csv")
    public void getMonthlyAmountBadCompletionRatio() throws IOException {
        System.out.println("PaymentImplJUnitParamsTest.getMonthlyAmountBadCompletionRatio");
        System.out.printf("%s, %s, %s, %s", pnr, income, studyPace, completionRatio);
        int allowance = paymentImpl.getMonthlyAmount(pnr, income, studyPace, 49);
        assertEquals(allowance, 0);
    }
}