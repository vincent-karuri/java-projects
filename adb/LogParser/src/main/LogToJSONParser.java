package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class LogToJSONParser {

    final static String TIME = "time";
    final static String IP = "ip";
    final static String FNAME = "fname";
    final static String LNAME = "lname";
    final static String ID = "id";

    // this assumes valid log input, no validation is performed
    public static void parseLogsToJson(String logFilePath) {

        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(logFilePath));
        } catch (FileNotFoundException e) {
            // usually I'd log error
            System.out.println(e.getMessage());
            return;
        }

        while (scanner.hasNextLine()) {
            String log = scanner.nextLine();
            String JSONObject = createJSONObject(extractValues(log));
            System.out.println(JSONObject);
        }

        if (scanner != null) {
            scanner.close();
        }
    }

    private static Map<String, String> extractValues(String log) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
        String[] vals = log.split(" ");

        // generate epoch timestamp
        Date date;
        String dateTime = vals[0] + " " + vals[1];
        try {
            date = sdf.parse(dateTime);
        } catch (ParseException e) {
            // usually I'd log error
            System.out.println(e.getMessage());
            return new LinkedHashMap<>();
        }
        Long epochTimeStamp = date.getTime();
        String timestamp = String.valueOf(epochTimeStamp);

        // set key-value pairs
        Map<String, String> keyVals = new LinkedHashMap<>();
        keyVals.put(TIME, timestamp);
        String ip = vals[2];
        keyVals.put(IP, ip);
        String fname = vals[3].split("=")[1];
        keyVals.put(FNAME, fname);
        String lname = vals[4].split("=")[1];
        keyVals.put(LNAME, lname);
        String id = vals[5].split("=")[1];
        keyVals.put(ID, id);

        return  keyVals;
    }

    private static String createJSONObject(Map<String, String> keyVals) {

        String json = "{" + "\n";
        for (Map.Entry<String, String> entry : keyVals.entrySet()) {
            json += "\t"+ "\"" + entry.getKey() + "\"" + ": ";

            Number number = convertToNumIfPossible(entry.getValue());
            if (number != null) {
                json += number.longValue() + "\n";
            } else {
                json += "\"" + entry.getValue() +  "\"" + "\n";
            }
        }
        json += "}";

        return json;
    }

    private static Number convertToNumIfPossible(String value) {
        try {
            return Long.parseLong(value);
        }
        catch(Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        parseLogsToJson("main/logs.txt");
    }
}