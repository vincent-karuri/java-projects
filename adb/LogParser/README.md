# Problem Statement

``` 
Write a program that parses the following log messages into a JSON object and prints the JSON out.  

Timestamp should be converted to time since epoch (assume timestamps are UTC).


Log messages:

2017-03-24 15:05:00 127.0.0.1 fname=foo lname=bar id=1

2017-02-14 19:00:00 8.8.4.4 fname=test lname=tester id=2

2017-01-01 00:00:01 1.1.1.1 fname=cool lname=beans id=3


JSON object output:

{

        “time”: 1490367900

        “ip”: “127.0.0.1”

        “fname”: “foo”

        “lname”: “bar”

        “id”: 1

}
```

## My implementation

Run the solution using the command: `javac main/LogToJSONParser.java && java -cp . main.LogToJSONParser` while in the src folder.

Java JDK must be installed on your machine.
