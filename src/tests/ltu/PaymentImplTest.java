package ltu;

import org.hamcrest.CoreMatchers.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

import static java.lang.Integer.parseInt;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

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
public class PaymentImplTest {

    private CalendarFactory cf = null;
    private ICalendar cal = null;
    private Properties props;
    private CSVReaderImpl debugReader = null;
    private PaymentImpl paymentImpl = null;

    private int FULL_LOAN;
    private int HALF_LOAN;
    private int ZERO_LOAN;
    private int FULL_SUBSIDY;
    private int HALF_SUBSIDY;
    private int ZERO_SUBSIDY;
    private int FULLTIME_INCOME;
    private int PARTTIME_INCOME;

    private String pnr;
    private int income;
    private int studyPace;
    private int completionRatio;

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


    @Before
    public void entryPoint() throws IOException {
        cf = new CalendarFactory();
        cal = cf.getCalendar();

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

        paymentImpl = new PaymentImpl(cal);
        debugReader = new CSVReaderImpl("inputfiles/debug.csv");
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
    public void getMonthlyAmount() throws IOException {
        String[] csvLine = debugReader.getNextLine();
        String  pnr = csvLine[0];
        int income = Integer.parseInt(csvLine[1]);
        int studyPace = Integer.parseInt(csvLine[2]);
        int completionRatio = Integer.parseInt(csvLine[3]);
        int allowence = paymentImpl.getMonthlyAmount(pnr, income, studyPace, completionRatio);
    }

    @Test
    public void getMonthlyAmountTooYoung() throws IOException {
        String[] csvLine = debugReader.getNextLine();

        if (csvLine.length > 0) {
            setVariables(csvLine);
            int allowance = paymentImpl.getMonthlyAmount("20190101-0000", income, studyPace, completionRatio);
            assertEquals(allowance, 0);
        }
    }

    @Test
    public void getMonthlyAmountBadCompletionRatio() throws IOException {
        String[] csvLine = debugReader.getNextLine();

        if (csvLine.length > 0) {
            setVariables(csvLine);
            int allowance = paymentImpl.getMonthlyAmount("20190101-0000", income, studyPace, completionRatio);
            assertEquals(allowance, 0);
        }
    }


    private void setVariables(String[] csvLine) {
        pnr = csvLine[0];
        income = Integer.parseInt(csvLine[1]);
        studyPace = Integer.parseInt(csvLine[2]);
        completionRatio = Integer.parseInt(csvLine[3]);
    }
}