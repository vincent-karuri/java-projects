import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

/**
 *  This class takes a String path name to a csv file
 *  and processes the file, providing an aggregation of loan amounts
 *  based on a tuple key (Network, Product, Month).
 *
 *  The result is then written to a csv file OUTPUT_CSV_FILE.
 */
public class LoanAggregator {

    private String outputCsvFile = "Output.csv";
    private static final Logger log = LogManager.getRootLogger();

    public void aggregateLoans(String loansFilePath) throws InvalidFormatException {

        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(loansFilePath));
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
        }

        // process file, tuple represents (Network, Product, Month)
        Map<String, Double> tupleToAggregateAmount = new HashMap<>();
        Map<String, Integer> tupleToCount = new HashMap<>();
        List<String> parsedRows = parseCsvFile(scanner);
        aggregationHelper(parsedRows, tupleToAggregateAmount, tupleToCount);
        writeToCsvFile(tupleToAggregateAmount, tupleToCount);

        if (scanner != null) {
            scanner.close();
        }
    }

    private List<String> parseCsvFile(Scanner scanner) throws InvalidFormatException {

        final int ROW_LENGTH = 5;
        final int NETWORK_COL_INDEX = 1;
        final int PRODUCT_COL_INDEX = 3;
        final int MONTH_COL_INDEX = 2;
        final int AMT_COL_INDEX = 4;
        final int NETWORK_VAL_OFFSET = 1;
        final int PRODUCT_VAL_OFFSET = 2;
        final int MONTH_VAL_OFFSET = 1;
        final int NETWORK_FIELD_LENGTH = 2;
        final int PRODUCT_FIELD_LENGTH = 3;
        final int MONTH_FIELD_LENGTH = 3;

        if (scanner.hasNextLine()) {
            scanner.nextLine(); // skip header row
        }

        List<String> rows = new ArrayList<>();
        while (scanner != null && scanner.hasNext()) {
            String row = scanner.nextLine();
            String[] colVals = validate(row,",", ROW_LENGTH);

            String network = validate(colVals[NETWORK_COL_INDEX], " ", NETWORK_FIELD_LENGTH)[NETWORK_VAL_OFFSET];
            network = network.substring(0, network.length() - 1); // remove trailing '

            String product = validate(colVals[PRODUCT_COL_INDEX], " ", PRODUCT_FIELD_LENGTH)[PRODUCT_VAL_OFFSET];
            product = product.substring(0, product.length() - 1); // remove trailing '

            String month = validate(colVals[MONTH_COL_INDEX], "-", MONTH_FIELD_LENGTH)[MONTH_VAL_OFFSET];
            String amount = colVals[AMT_COL_INDEX];

            rows.add(network + "+" + product + "+" + month + "," + amount);
        }
        return rows;
    }

    private String[] validate(String vals, String delimeter, int expectedArrLength) throws InvalidFormatException {

        String[] result = vals.split(delimeter);
        if (result.length < expectedArrLength) {
           throw new InvalidFormatException("The csv value is wrongly formatted! : " + vals);
        }
        return result;
    }

    private void aggregationHelper(List<String> parsedRows, Map<String, Double> tupleToAggregateAmount, Map<String, Integer> tupleToCount) {

        for (String row : parsedRows) {
            String[] tupleAndAmount = row.split(",");
            String tuple = tupleAndAmount[0];
            double amount = Double.parseDouble(tupleAndAmount[1]);
            if (tupleToAggregateAmount.containsKey(tuple)) {
                tupleToAggregateAmount.put(tuple, tupleToAggregateAmount.get(tuple) + amount);
                tupleToCount.put(tuple, tupleToCount.get(tuple) + 1);
            } else {
                tupleToAggregateAmount.put(tuple, amount);
                tupleToCount.put(tuple, 1);
            }
        }
    }

    private void writeToCsvFile(Map<String, Double> tupleToAggregateAmount, Map<String, Integer> tupleToCount) {

        final String header = "Network,Product,Month,Amount,Count";

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(getOutputCsvFile()));
            writer.write(header);
            writer.write("\n");
            for (Map.Entry<String, Double> entry : tupleToAggregateAmount.entrySet()) {
                String[] tuple = entry.getKey().split("\\+");
                DecimalFormat decimalFormat = new DecimalFormat("#.00");
                String amount = decimalFormat.format(entry.getValue());

                writer.write(tuple[0] + "," + tuple[1] + "," + tuple[2] + "," + amount + "," + tupleToCount.get(entry.getKey()));
                writer.write("\n");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
    }

    public String getOutputCsvFile() {
        return outputCsvFile;
    }

    public void setOutputCsvFile(String outputCsvFile) {
        this.outputCsvFile = outputCsvFile;
    }

    public static void main(String[] args) {
        LoanAggregator loanAggregator = new LoanAggregator();
        URL url = LoanAggregator.class.getResource("Loans.csv");

        try {
            loanAggregator.aggregateLoans(url.getPath());
        } catch (InvalidFormatException e) {
            log.error(e.getMessage());
        }
    }
}
