package ltu;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

import static java.lang.Integer.parseInt;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Test class implementing the standard JUnit4 Params
 * This method requires quite a lot of boilerplate code and
 * many sources recommend using JUnitParams library to write
 * parameterized tests.
 *
 * todo Make dynamic runtime loading of test data possible.
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

    // Can't read from CSV at runtime so disabled.
    //private final CSVReaderImpl debugReader = new CSVReaderImpl("test/resources/debug.csv");

    private final PaymentImpl paymentImpl;

    private int FULL_LOAN;
    private int HALF_LOAN;
    private int ZERO_LOAN;
    private int FULL_SUBSIDY;
    private int HALF_SUBSIDY;
    private int ZERO_SUBSIDY;
    private int FULLTIME_INCOME;
    private int PARTTIME_INCOME;

    // Parameterized test with constructor injection
    private String pnr;
    private int income;
    private int studyPace;
    private int completionRatio;

    public PaymentImplTest(String pnr, int income, int studyPace, int completionRatio) throws IOException {
        cf = new CalendarFactory();
        cal = cf.getCalendar();

        this.paymentImpl = new PaymentImpl(cal);

        this.pnr = pnr;
        this.income = income;
        this.studyPace = studyPace;
        this.completionRatio = completionRatio;
    }

    @Parameters(name = "{index}: Student - {0}, income: {1}, studyPace={2}, completionRatio={3}")
    public static Collection<Object[]> loadData() {
        return Arrays.asList(new Object[][] {
                {"19700615-5441", 85813, 100, 50},
                {"19700322-3006", 85813, 100, 100},
                {"19700322-3006", 128722, 100, 100}
        });
        // This is the issue here that I can't load values for parameterized tests at runtime.
        //List<String[]> testData = debugReader.getAllLines();
        //return testData;
    }

    @Before
    public void entryPoint() throws IOException {
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


    /* ================== Constructor Testing ================== */
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


    /* ================ Payment amounts =============== */
    @Test
    public void monthlyAmount() {
        int allowence = paymentImpl.getMonthlyAmount(pnr, income, studyPace, completionRatio);
        assertThat(allowence, is(FULL_SUBSIDY));
    }

    @Test
    public void tooYoung() {
        int allowance = paymentImpl.getMonthlyAmount("20190101-0000", income, studyPace, completionRatio);
        assertThat(allowance, is(ZERO_SUBSIDY + ZERO_LOAN));
    }

    @Test
    public void badCompletionRatio() {
        int allowance = paymentImpl.getMonthlyAmount(pnr, income, studyPace, 49);
        assertThat(allowance, is(ZERO_SUBSIDY + ZERO_LOAN));
    }

    @Test
    public void fulltimeStudyWithHighIncome() {
        int allowance = paymentImpl.getMonthlyAmount(pnr, PARTTIME_INCOME, 100, completionRatio);
        assertThat(allowance, is(ZERO_SUBSIDY + ZERO_LOAN));
    }

    @Test
    public void partTimeStudies() {
        int allowance = paymentImpl.getMonthlyAmount(pnr, income, 99, completionRatio);
        assertThat(allowance, is(HALF_SUBSIDY + HALF_LOAN));
    }

    @Test
    public void lowStudyRate() {
        int allowance = paymentImpl.getMonthlyAmount(pnr, income, 49, completionRatio);
        assertThat(allowance, is(ZERO_SUBSIDY + ZERO_LOAN));
    }

    @Test
    public void highIncome() {
        int allowance = paymentImpl.getMonthlyAmount(pnr, PARTTIME_INCOME + 1, studyPace, completionRatio);
        assertThat(allowance, is(ZERO_SUBSIDY + ZERO_LOAN));
    }


    /* ================= Payment days ================= */
    @Test
    public void getNextPaymentDay() {


    }
}