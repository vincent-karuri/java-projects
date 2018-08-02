# Loan Aggregator 

The `LoanAggregator#aggregateLoans` method takes a String path-name (absolute or relative to LoanAggregator's current path) 
to a csv file and processes the file, providing an aggregation of loan amounts based on a tuple key (Network, Product, Month)
which can be specified by the client.

The result is then written to a csv file OUTPUT_CSV_FILE (absolute or relative to LoanAggregator's current path)
which can be specified by the client.

## Assumptions 

- File to be processed is in CSV format.
- File has correct permissions to be read.
- File is in correct format for each of its rows. If not, an IllegalFormatException is thrown. An alternative might have 
been to skip 'dirty' rows and create some report of this but it all depends on how important the overall document 
integrity is to the application.

## Implementation

- This solution is written in Java using JUnit for testing and uses Maven for dependency management.

## Scale and performance considerations

The implementation uses a HashMap to allow fast (constant time) lookups when updating tuple amounts.

This solution can be extended to support other formats e.g. Excel or PDF. For example, an Adapter class can be written 
to convert the Excel or PDF to CSV and the same LoanAggregator class can be used to perform the aggregations.

Additionally, if the aggregations need to done on a different tuple, one can specify the required 
tuple values via constants at the top of the `LoanAggregator#parseCsvFile` method. The tuple lengths and offsets can also be 
adjusted in a similar way.

