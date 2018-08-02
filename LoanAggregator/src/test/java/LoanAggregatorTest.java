import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import static junit.framework.TestCase.assertTrue;

@RunWith(JUnit4ClassRunner.class)
public class LoanAggregatorTest {

    private static final Logger log = LogManager.getRootLogger();
    private static final String OUTPUT_TEST_FILE = "test-output.csv";
    private LoanAggregator loanAggregator;

    @Before
    public void setup() {
        loanAggregator = new LoanAggregator();
        loanAggregator.setOutputCsvFile(OUTPUT_TEST_FILE);
    }

    @Test
    public void testCorrectOutputIsWrittenToFileWhenCsvFormatIsCorrect() throws InvalidFormatException {

        Set<String> expectedResults = new HashSet<>();
        expectedResults.add("1,1,Mar,1000.00");
        expectedResults.add("2,1,Mar,2611.00");
        expectedResults.add("3,2,Mar,17653.88");
        expectedResults.add("2,1,Apr,5671.00");
        expectedResults.add("3,3,Apr,1928.00");
        expectedResults.add("2,3,Apr,4700.78");
        expectedResults.add("2,1,Apr,5671.00");
        expectedResults.add("1,2,Apr,1801.00");

        assertTrue(validateResults(expectedResults, "test-file-correct-format.csv"));
    }

    @Test
    public void testNothingIsWrittenToFileWhenCsvContainsNoValueRows() throws InvalidFormatException {

        Set<String> expectedResults = new HashSet<>();
        assertTrue(validateResults(expectedResults, "test-file-empty-file.csv"));
    }

    @Test(expected = InvalidFormatException.class)
    public void testExceptionIsThrownWhenRowHasFewerColumnValuesThanExpected() throws InvalidFormatException {

        Set<String> expectedResults = new HashSet<>();
        expectedResults.add("2,1,Mar,2611.0");
        expectedResults.add("3,2,Mar,17653.0");
        expectedResults.add("2,1,Apr,5671.0");
        expectedResults.add("3,3,Apr,1928.0");
        expectedResults.add("2,3,Apr,4700.0");
        expectedResults.add("2,1,Apr,5671.0");

        assert(validateResults(expectedResults, "test-file-malformed-row.csv"));
    }

    @Test(expected = InvalidFormatException.class)
    public void testExceptionIsThrownWhenNetworkHasFewerValuesThanExpected() throws InvalidFormatException {

        Set<String> expectedResults = new HashSet<>();
        expectedResults.add("2,1,Mar,2611.0");
        expectedResults.add("3,2,Mar,17653.0");
        expectedResults.add("2,1,Apr,5671.0");
        expectedResults.add("3,3,Apr,1928.0");
        expectedResults.add("2,3,Apr,4700.0");
        expectedResults.add("2,1,Apr,5671.0");

        assert(validateResults(expectedResults, "test-file-malformed-network-string.csv"));
    }

    @Test(expected = InvalidFormatException.class)
    public void testExceptionIsThrownWhenProductHasFewerValuesThanExpected() throws InvalidFormatException {

        Set<String> expectedResults = new HashSet<>();
        expectedResults.add("2,1,Mar,2611.0");
        expectedResults.add("3,2,Mar,17653.0");
        expectedResults.add("2,1,Apr,5671.0");
        expectedResults.add("3,3,Apr,1928.0");
        expectedResults.add("2,3,Apr,4700.0");
        expectedResults.add("2,1,Apr,5671.0");

        assert(validateResults(expectedResults, "test-file-malformed-product-string.csv"));
    }

    @Test(expected = InvalidFormatException.class)
    public void testExceptionIsThrownWhenMonthHasFewerValuesThanExpected() throws InvalidFormatException {

        Set<String> expectedResults = new HashSet<>();
        expectedResults.add("2,1,Mar,2611.0");
        expectedResults.add("3,2,Mar,17653.0");
        expectedResults.add("2,1,Apr,5671.0");
        expectedResults.add("3,3,Apr,1928.0");
        expectedResults.add("2,3,Apr,4700.0");
        expectedResults.add("2,1,Apr,5671.0");

        assert(validateResults(expectedResults, "test-file-malformed-date-string.csv"));
    }

    private boolean validateResults(Set<String> expectedResults, String testFilePath) throws InvalidFormatException {

        URL url = LoanAggregator.class.getResource(testFilePath);
        loanAggregator.aggregateLoans(url.getPath());

        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(OUTPUT_TEST_FILE));
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
        }

        scanner.nextLine();
        Set<String> actualResults = new HashSet<>();
        while (scanner != null && scanner.hasNext()) {
            String currRow = scanner.nextLine();
            if (!expectedResults.contains(currRow)) {
                return false;
            }
            actualResults.add(currRow);
        }
        scanner.close();

        return expectedResults.size() == actualResults.size();
    }
}
