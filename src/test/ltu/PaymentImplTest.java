package ltu;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import static java.lang.Integer.parseInt;
import static org.junit.Assert.assertEquals;

/**
 * Test class implementing the standard JUnit4 Params
 */
@RunWith(Parameterized.class)
public class PaymentImplTest {

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

    private static CSVReaderImpl debugReader = new CSVReaderImpl("test/resources/debug.csv");

    private PaymentImpl paymentImpl = null;

    private int FULL_LOAN;
    private int HALF_LOAN;
    private int ZERO_LOAN;
    private int FULL_SUBSIDY;
    private int HALF_SUBSIDY;
    private int ZERO_SUBSIDY;
    private int FULLTIME_INCOME;
    private int PARTTIME_INCOME;


    // Parameterized test with field injection
    @Parameterized.Parameter // default 0
    private String pnr;
    @Parameterized.Parameter(1)
    private int income;
    @Parameterized.Parameter(2)
    private int studyPace;
    @Parameterized.Parameter(3)
    private int completionRatio;


    /*// Parameterized test with constructor injection
    private String pnr;
    private int income;
    private int studyPace;
    private int completionRatio;

    public PaymentImplTest(String pnr, int income, int studyPace, int completionRatio) {
        this.pnr = pnr;
        this.income = income;
        this.studyPace = studyPace;
        this.completionRatio = completionRatio;
    }*/

    //@Parameters(name = "Run {index}: pnr={0}, income={1}, studyPace={2}, completionRatio={3}")
    //@Parameters
    @Parameters(name = "{index}: testDomain - {0}")
    public static Collection<String[]> loadData() {
        List<String[]> testData = debugReader.getAllLines();
        return testData;
    }

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
    public void getMonthlyAmount() {
        System.out.println("PaymentImplTest.getMonthlyAmount");
        System.out.printf("%s, %s, %s, %s", pnr, income, studyPace, completionRatio);
        int allowence = paymentImpl.getMonthlyAmount(pnr, income, studyPace, completionRatio);
        assertEquals(allowence, FULL_SUBSIDY);
    }

    @Test
    public void getMonthlyAmountTooYoung() {
        System.out.println("PaymentImplTest.getMonthlyAmountTooYoung");
        System.out.printf("%s, %s, %s, %s", pnr, income, studyPace, completionRatio);
        int allowance = paymentImpl.getMonthlyAmount("20190101-0000", income, studyPace, completionRatio);
        assertEquals(allowance, 0);
    }

    @Test
    public void getMonthlyAmountBadCompletionRatio() {
        int allowance = paymentImpl.getMonthlyAmount(pnr, income, studyPace, 49);
        assertEquals(allowance, 0);
    }
}