import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


import java.io.*;
import java.net.URL;
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
        List<String> parsedRows = parseCsvFile(scanner);
        aggregationHelper(parsedRows, tupleToAggregateAmount);
        writeToCsvFile(tupleToAggregateAmount);

        if (scanner != null) {
            scanner.close();
        }
    }

    private List<String> parseCsvFile(Scanner scanner) throws InvalidFormatException {

        if (scanner.hasNextLine()) {
            scanner.nextLine(); // skip header row
        }

        List<String> rows = new ArrayList<>();
        while (scanner != null && scanner.hasNext()) {
            String row = scanner.nextLine();
            String[] colVals = validate(row,",", 5);

            String network = validate(colVals[1], " ", 2)[1];
            network = network.substring(0, network.length() - 1); // remove trailing '

            String product = validate(colVals[3], " ", 3)[2];
            product = product.substring(0, product.length() - 1); // remove trailing '

            String month = validate(colVals[2], "-", 3)[1];
            String amount = colVals[4];

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

    private void aggregationHelper(List<String> parsedRows, Map<String, Double> tupleToAggregateAmount) {

        for (String row : parsedRows) {
            String[] tupleAndAmount = row.split(",");
            String tuple = tupleAndAmount[0];
            double amount = Double.parseDouble(tupleAndAmount[1]);
            if (tupleToAggregateAmount.containsKey(tuple)) {
                tupleToAggregateAmount.put(tuple, tupleToAggregateAmount.get(tuple) + amount);
            } else {
                tupleToAggregateAmount.put(tuple, amount);
            }
        }
    }

    private void writeToCsvFile(Map<String, Double> tupleToAggregateAmount) {

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(getOutputCsvFile()));
            writer.write("Network,Product,Month,Amount");
            writer.write("\n");
            for (Map.Entry<String, Double> entry : tupleToAggregateAmount.entrySet()) {
                String[] tuple = entry.getKey().split("\\+");
                double amount = entry.getValue();
                writer.write(tuple[0] + "," + tuple[1] + "," + tuple[2] + "," + amount);
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
