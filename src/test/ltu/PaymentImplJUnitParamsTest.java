package ltu;

import junitparams.FileParameters;
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
import static org.junit.Assert.assertEquals;

/**
 * This class implements a parameterized test using the
 * JUnitParams library.
 *
 * todo Solve the delimiter issue in CSV file
 * todo initial line problem when loading data from CSV.
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
    private final PaymentImpl paymentImpl;

    private int FULL_LOAN;
    private int HALF_LOAN;
    private int ZERO_LOAN;
    private int FULL_SUBSIDY;
    private int HALF_SUBSIDY;
    private int ZERO_SUBSIDY;
    private int FULLTIME_INCOME;
    private int PARTTIME_INCOME;

    public PaymentImplJUnitParamsTest() throws IOException {
        cf = new CalendarFactory();
        cal = cf.getCalendar();

        paymentImpl = new PaymentImpl(cal);
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
    @Parameters({
            "19700615-5441", "128722", "100", "50",
            "19700322-3006", "128723", "100", "50",
    })
    public void getMonthlyAmount(String pnr, int income, int studyPace, int completionRatio) {
        System.out.println("PaymentImplJUnitParamsTest.getMonthlyAmount");
        System.out.printf("%s, %s, %s, %s", pnr, income, studyPace, completionRatio);
        int allowence = paymentImpl.getMonthlyAmount(pnr, income, studyPace, completionRatio);
        assertEquals(allowence, FULL_SUBSIDY);
    }

    @Test
    @FileParameters("inputfiles/debug.csv")
    public void getMonthlyAmountTooYoung(String pnr, int income, int studyPace, int completionRatio) {
        System.out.println("PaymentImplJUnitParamsTest.getMonthlyAmountTooYoung");
        System.out.printf("%s, %s, %s, %s", pnr, income, studyPace, completionRatio);
        int allowance = paymentImpl.getMonthlyAmount("20190101-0000", income, studyPace, completionRatio);
        assertEquals(allowance, 0);
    }

    @Test
    //@FileParameters("inputfiles/debug.csv")
    @Parameters(method = "paramsFromFile")
    public void getMonthlyAmountBadCompletionRatio(String pnr, int income, int studyPace, int completionRatio) {
        System.out.println("PaymentImplJUnitParamsTest.getMonthlyAmountBadCompletionRatio");
        System.out.printf("%s, %s, %s, %s", pnr, income, studyPace, completionRatio);
        int allowance = paymentImpl.getMonthlyAmount(pnr, income, studyPace, 49);
        assertEquals(allowance, 0);
    }

    @Test
    @Parameters(method = "paramsByComma")
    public void byComma(String parameter) {
        System.out.println("parameter: " + parameter);
    }


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