package ltu;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static java.lang.Integer.parseInt;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


/**
 * This class implements a parameterized test using the
 * JUnitParams library.
 *
 * todo Not certain that two fields are required for PaymentImpl,
 *  one for amounts, and the other for dates. Perhaps combinable.
 *
 * todo normally parametersFor methods are default goto's for Parameterized test methods
 *  but that seems to raise some errors. This could be bug in JUnitParms library or other problem.
 */
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
    private PaymentImpl paymentImpl;
    private PaymentImpl paymentImpl2; // Second instance reserved for paymentDay testing.

    private int FULL_LOAN;
    private int HALF_LOAN;
    private int ZERO_LOAN;
    private int FULL_SUBSIDY;
    private int HALF_SUBSIDY;
    private int ZERO_SUBSIDY;
    private int FULLTIME_INCOME;
    private int PARTTIME_INCOME;


    // [ID: 101] The student must be at least 20 years old to receive subsidiary and student loans.
    // [ID: 102] The student may receive subsidiary until the year they turn 56.
    // [ID: 103] The student may not receive any student loans from the year they turn 47.

    // [ID: 201] The student must be studying at least half time to receive any subsidiary.
    // [ID: 202] A student studying less than full time is entitled to 50% subsidiary.
    // test the system for the spring-term of 2016 (2016-01-01 to 2016-06-30

    public PaymentImplJUnitParamsTest() throws IOException {
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

    @Before
    public void entryPoint() throws IOException {

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
        String rules;
        rules = "xyz!!!";

        try {
            PaymentImpl payment = new PaymentImpl(cal, rules);

        } catch (NumberFormatException e) {
            //Test has passed because NumberFormatException is thrown.
            return;

        } catch (IOException e) {
            Assert.fail("This should throw a NumberFormatException.");
        }

        rules = null;

        try {
            PaymentImpl payment = new PaymentImpl(cal, rules);

        } catch (IOException e) {
            //Test has passed because IOException is thrown.
            return;

        } catch (Exception e) {
            Assert.fail("This should throw an IOException.");
        }
    }



    /* ================ Subsidy and Loan Testing =============== */
    @Test
    @Parameters(method="parametersForMonthlyAmountFullTime")
    public void monthlyAmountFullTime(String pnr, int income, int studyRate, int completionRatio) {
        int allowance = paymentImpl.getMonthlyAmount(pnr, income, studyRate, completionRatio);
        assertThat(allowance, is(FULL_SUBSIDY + FULL_LOAN));
    }

    @Test
    @Parameters(method="parametersForMonthlyAmountPartTime")
    public void monthlyAmountPartTime(String pnr, int income, int studyRate, int completionRatio) {
        int allowance = paymentImpl.getMonthlyAmount(pnr, income, studyRate, completionRatio);
        assertThat(allowance, is(HALF_SUBSIDY + HALF_LOAN));
    }


    /* === bad input data related = */
    @Test
    @Parameters(method="parametersForMonthlyAmountNullPnr")
    public void getMonthlyAmountNullPnr(String pnr, int income, int studyRate, int completionRatio) {

        try {
            int allowance = paymentImpl.getMonthlyAmount(pnr, income, studyRate, completionRatio);

        } catch (IllegalArgumentException e) {
            //Test has passed because IllegalArgumentException is thrown.
            return;
        }
        Assert.fail("This should throw IllegalArgumentException");
    }

    @Test
    @Parameters(method="parametersForMonthlyAmountLongPnr")
    public void getMonthlyAmountLongPnr(String pnr, int income, int studyRate, int completionRatio) {

        try {
            int allowance = paymentImpl.getMonthlyAmount(pnr, income, studyRate, completionRatio);

        } catch (IllegalArgumentException e) {
            //Test has passed because IllegalArgumentException is thrown.
            return;
        }
        Assert.fail("This should throw IllegalArgumentException");
    }


    /* === age related ====== */
    @Test
    @Parameters(method="parametersForTooOld")
    public void tooOld(String pnr, int income, int studyRate, int completionRatio) {
        int allowance = paymentImpl.getMonthlyAmount(pnr, income, studyRate, completionRatio);
        assertThat(allowance, is(ZERO_SUBSIDY + ZERO_LOAN));
    }

    @Test
    @Parameters(method="parametersForTooYoung")
    public void tooYoung(String pnr, int income, int studyRate, int completionRatio) {
        int allowance = paymentImpl.getMonthlyAmount(pnr, income, studyRate, completionRatio);
        assertThat(allowance, is(ZERO_SUBSIDY + ZERO_LOAN));
    }

    @Test
    @Parameters(method="parametersForTooOldForLoanFullTimeStudyRate")
    public void tooOldForLoanWithFullTimeStudies(String pnr, int income, int studyRate, int completionRatio) {
        int allowance = paymentImpl.getMonthlyAmount(pnr, income, studyRate, completionRatio);
        assertThat(allowance, is(FULL_SUBSIDY + ZERO_LOAN));
    }

    @Test
    @Parameters(method="parametersForTooOldForLoanPartTimeStudyRate")
    public void tooOldForLoanWithPartTimeStudies(String pnr, int income, int studyRate, int completionRatio) {
        int allowance = paymentImpl.getMonthlyAmount(pnr, income, studyRate, completionRatio);
        assertThat(allowance, is(HALF_SUBSIDY + ZERO_LOAN));
    }


    /* === income related === */
    @Test
    @Parameters(method="parametersForPartTimeIncomeWithFullTimeStudy")
    public void partTimeIncomeWithFullTimeStudy(String pnr, int income, int studyRate, int completionRatio) {
        int allowance = paymentImpl.getMonthlyAmount(pnr, income, studyRate, completionRatio);
        assertThat(allowance, is(ZERO_SUBSIDY + ZERO_LOAN));
    }

    @Test
    @Parameters(method="parametersForTooHighIncome")
    public void tooHighIncome(String pnr, int income, int studyRate, int completionRatio) {
        int allowance = paymentImpl.getMonthlyAmount(pnr, income, studyRate, completionRatio);
        assertThat(allowance, is(ZERO_SUBSIDY + ZERO_LOAN));
    }


    /* === studyRate related === */
    @Test
    @Parameters(method="parametersForLessThanHalfTimeStudyRate")
    public void lessThanHalfTimeStudyRate(String pnr, int income, int studyRate, int completionRatio) {
        int allowance = paymentImpl.getMonthlyAmount(pnr, income, studyRate, completionRatio);
        assertThat(allowance, is(ZERO_SUBSIDY + ZERO_LOAN));
    }

    @Test
    @Parameters(method="parametersForLessThanFullTimeStudyRate")
    public void lessThanFullTimeStudyRate(String pnr, int income, int studyRate, int completionRatio) {
        int allowance = paymentImpl.getMonthlyAmount(pnr, income, studyRate, completionRatio);
        assertThat(allowance, is(HALF_SUBSIDY + HALF_LOAN));
    }


    /* === completionRatio related === */
    @Test
    @Parameters(method="parametersForBadCompletionRatio")
    public void tooLowCompletionRatio(String pnr, int income, int studyRate, int completionRatio) {
        int allowance = paymentImpl.getMonthlyAmount(pnr, income, studyRate, completionRatio);
        assertThat(allowance, is(ZERO_SUBSIDY + ZERO_LOAN));
    }



    /* ================= Data source methods ================= */
    // If we do not specify anything in the @Parameters annotation, JUnitParams tries to
    // load a test data provider method based on the test method name. The method name
    // is constructed as “parametersFor”+ <test method name>.
    private Object[] parametersForMonthlyAmountFullTime () {
        return new Object[] {
            new Object[] {"19880615-5441", 0, 100, 100},
            new Object[] {"19880322-3006", 1, 100, 50},
        };
    }

    private Object[] parametersForMonthlyAmountPartTime () {
        return new Object[] {
            new Object[] {"19880615-5441", 0, 90, 100},
            new Object[] {"19880322-3006", 1, 50, 50},
        };
    }

    private Object[] parametersForMonthlyAmountNullPnr () {
        return new Object[] {
            new Object[] {null, 0, 100, 100},
        };
    }

    private Object[] parametersForMonthlyAmountLongPnr () {
        return new Object[] {
            new Object[] {"19880615-544199", 0, 100, 100},
            new Object[] {"19880322-300699", 1, 100, 50},
        };
    }

    private Object[] parametersForBadCompletionRatio () {
        return new Object[] {
            new Object[] {"19990615-5441", 0, 100, 49},
            new Object[] {"19990322-3006", 0, 100,49},
            new Object[] {"19990322-3006", 0, 100, 0},
            new Object[] {"19990322-3006", 0, 100, 0}
        };
    }

    private Object[] parametersForTooYoung () {
        return new Object[] {
            new Object[] {"20110615-5441", PARTTIME_INCOME, 50, 50},
            new Object[] {"20110322-3006", 0, 100, 100}
        };
    }

    private Object[] parametersForTooOld () {
        return new Object[] {
            new Object[] {"18110615-5441", PARTTIME_INCOME, 50, 50},
            new Object[] {"18110322-3006", 0, 100, 100}
        };
    }

    private Object[] parametersForTooOldForLoanFullTimeStudyRate () {
        return new Object[] {
            new Object[] {"19700615-5441", 1, 100, 100},
            new Object[] {"19700322-3006", FULLTIME_INCOME, 100, 100},
            new Object[] {"19700322-3006", 0, 100, 100},
            new Object[] {"19700322-3006", 0, 100, 100}
        };
    }

    private Object[] parametersForTooOldForLoanPartTimeStudyRate () {
        return new Object[] {
            new Object[] {"19700615-5441", 1, 75, 100},
            new Object[] {"19700322-3006", PARTTIME_INCOME, 50, 100},
            new Object[] {"19700322-3006", 0, 90, 100},
            new Object[] {"19700322-3006", 0, 50, 100}
        };
    }

    private Object[] parametersForTooHighIncome () {
        return new Object[] {
            new Object[] {"19880322-3006", PARTTIME_INCOME + 1, 99, 100},
            new Object[] {"19880322-3006", PARTTIME_INCOME + 1, 50, 100},
            new Object[] {"19880322-3006", FULLTIME_INCOME + 1, 100, 100}
        };
    }

    private Object[] parametersForPartTimeIncomeWithFullTimeStudy () {
        return new Object[] {
            new Object[] {"19880322-3006", PARTTIME_INCOME, 100, 50},
            new Object[] {"19880322-3006", PARTTIME_INCOME, 100, 100},
            new Object[] {"19880322-3006", PARTTIME_INCOME, 100, 99}
        };
    }

    private Object[] parametersForLessThanHalfTimeStudyRate() {
        return new Object[] {
            new Object[] {"19880322-3006", PARTTIME_INCOME, 25, 100},
            new Object[] {"19880322-3006", 1, 1, 100},
            new Object[] {"19880322-3006", 0, 49, 50}
        };
    }

    private Object[] parametersForLessThanFullTimeStudyRate() {
        return new Object[] {
            new Object[] {"19880322-3006", PARTTIME_INCOME, 75, 100},
            new Object[] {"19880322-3006", FULLTIME_INCOME, 99, 100},
            new Object[] {"19880322-3006", 0, 50, 50}
        };
    }



    /* ================= Payment days ================= */

    /**
     * Test for checking paydays of the month.
     * Note: the month is 0 - based so January is 0 (zero).
     *
     * @param month integer of month. Required to be 0-based
     * @param expected ineger the human verified day of payment in integer form
     */
    @Test
    @Parameters(method="parametersForNextPaymentDay")
    public void nextPaymentDay(int month, int expected) {

        try{
            paymentImpl2 = new PaymentImpl(new CalendarImplStub(month));

        } catch(IOException e) {
            e.printStackTrace();
        }
        int integerDate = Integer.parseInt(paymentImpl2.getNextPaymentDay());
        assertThat(integerDate, is(expected));
    }

    private Object[] parametersForNextPaymentDay() {
        return new Object[] {
            new Object[] {0, 20160129},
            new Object[] {1, 20160229},
            new Object[] {2, 20160331},
            new Object[] {3, 20160429},
            new Object[] {4, 20160531},
            new Object[] {5, 20160630}
        };
    }



    /* =============== Test data loading ============== */
    // Currently only fore example purposes.


    /**
     * Load parameters from CSV file
     *
     * todo Accept filename parameter. Requires the JUnitParams annotation to call with argument...
     */
    public List<String[]> paramsFromFile() {
        // Can't read from CSV at runtime so disabled.
        CSVReaderImpl debugReader = new CSVReaderImpl("test/resources/debug.csv");

        return debugReader.getAllLines();
    }

    /**
     * Workaround as JUnitParams does not support changing the delimiter.
     */
    public List<String> paramsByComma() {

        return splitBy("hello, goodmorning|goodnight", ",");
    }

    private ArrayList<String> splitBy(String params, String splitter) {
        // return Lists.newArrayList(params.split(splitter)); // This format requires the Guava libraries.
        ArrayList<String> parts = new ArrayList<>(Arrays.asList(params.split(splitter)));

        return parts;
    }
}
