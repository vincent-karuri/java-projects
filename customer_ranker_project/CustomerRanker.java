import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.List;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Date;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Map;
import java.util.Collections;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import java.io.File;
import java.io.FileNotFoundException;

public class CustomerRanker {
	
	public static String getNBestCustomers(String transactionsCsvFilePath, int N) {

        HashMap<String, Integer> currConsecutiveDaysCount = new HashMap<>();
		HashMap<String, Integer> maxConsecutiveDaysCount = new HashMap<>();
        HashMap<String, Date> lastPaymentDate = new HashMap<>();

        Scanner scanner = null;
        try {
        	scanner = new Scanner(new File(transactionsCsvFilePath));	
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
        }

        scanner.nextLine(); // skip header row
        List<String> rows = new ArrayList<>();
        while (scanner != null && scanner.hasNext()) {
        	String row = scanner.nextLine();
        	String[] vals = row.split(",");
        	String accountAndDate = vals[0] + "," + vals[2];
        	rows.add(accountAndDate);
        }

        Collections.sort(rows);
		for (String row : rows) {
		    String[] vals = row.split(",");
		    String account = vals[0];
		    String dateTime = vals[vals.length - 1];
		    String date = dateTime.split(" ")[0];
		    updateConsecutiveDays(account, date, currConsecutiveDaysCount, maxConsecutiveDaysCount, lastPaymentDate);
		}
		
		PriorityQueue<Customer> customerRankings = rankCustomers(maxConsecutiveDaysCount);
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < N; i++) {
			Customer customer = customerRankings.poll();
			result.add(customer.getAccount());
		}

		return formatResult(result);
	}

	private static String formatResult(List<String> bestCustomers) {

		String formattedResult = "[";
		for (String customer : bestCustomers) {
			formattedResult += "\'" + customer  + "\'" + ", ";
		}
		formattedResult = formattedResult.substring(0, formattedResult.length() - 2);
		formattedResult += "]";

	    return formattedResult;
	}
	private static void updateConsecutiveDays(String account, String date, HashMap<String, Integer> currConsecutiveDaysCount, HashMap<String, Integer> maxConsecutiveDaysCount, HashMap<String, Date> lastPaymentDate) {

		if (lastPaymentDate.containsKey(account)) {

			int currCount = currConsecutiveDaysCount.get(account);
			int isConsecutiveDayRes = isConsecutiveDay(account, date, lastPaymentDate);
			if (isConsecutiveDayRes == 1) {	
				currConsecutiveDaysCount.put(account, currCount + 1);
			} else if (isConsecutiveDayRes == - 1){
				if (currCount > maxConsecutiveDaysCount.get(account)){
					maxConsecutiveDaysCount.put(account, currCount);
				}
				currConsecutiveDaysCount.put(account, 0);
			}
		} else {
			currConsecutiveDaysCount.put(account, 0);
			maxConsecutiveDaysCount.put(account, 0);
		}
		lastPaymentDate.put(account, parseDate(date));
	}


	private static int isConsecutiveDay(String account, String date, HashMap<String, Date> lastPaymentDate) {

		Date currDate = parseDate(date);
		Date prevDate = lastPaymentDate.get(account);

		Calendar prevCalendar = Calendar.getInstance();
		prevCalendar.setTime(prevDate);

		Calendar currCalendar = Calendar.getInstance();
		currCalendar.setTime(currDate);
		if (prevCalendar.get(Calendar.YEAR) == currCalendar.get(Calendar.YEAR) 
			&& prevCalendar.get(Calendar.MONTH) == currCalendar.get(Calendar.MONTH)
			&& prevCalendar.get(Calendar.DAY_OF_MONTH) == currCalendar.get(Calendar.DAY_OF_MONTH)) {
			return 0;
		}

		prevCalendar.add(Calendar.DATE, 1);
		if (prevCalendar.get(Calendar.YEAR) == currCalendar.get(Calendar.YEAR) 
			&& prevCalendar.get(Calendar.MONTH) == currCalendar.get(Calendar.MONTH)
			&& prevCalendar.get(Calendar.DAY_OF_MONTH) == currCalendar.get(Calendar.DAY_OF_MONTH)) {
			return 1;
		}
		return -1;
	}

    private static Date parseDate(String date) {

    	Date result = null;
    	try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			result = dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
    }

    private static PriorityQueue<Customer> rankCustomers(HashMap<String, Integer> maxConsecutiveDays) {

    	PriorityQueue<Customer> result = new PriorityQueue<>();
    	for (Map.Entry<String, Integer> entry : maxConsecutiveDays.entrySet()) {
    		Customer customer = new Customer(entry.getKey(), entry.getValue());
    		result.add(customer);
    	}
    	return result;
    }

    private static class Customer implements Comparable<Customer> {

    	private String account;
    	private int maxConsecutiveDays;
    	public Customer(String account, int maxConsecutiveDays) {	
    		this.account = account;
    		this.maxConsecutiveDays = maxConsecutiveDays;
    	}

		public String getAccount() {
			return account;
		}

		public void setAccount(String account) {
			this.account = account;
		}

		public int getMaxConsecutiveDays() {
			return maxConsecutiveDays;
		}

		public void setMaxConsecutiveDays(int maxConsecutiveDays) {
			this.maxConsecutiveDays = maxConsecutiveDays;
		}

		public int compareTo(Customer c2) {

    		if (c2.getMaxConsecutiveDays() - this.getMaxConsecutiveDays() != 0) {
    			return c2.getMaxConsecutiveDays() - this.getMaxConsecutiveDays();
    		}
    		return this.getAccount().compareTo(c2.getAccount());
    	}
    }

	public static void main(String[] args) {

        String nBestCustomers = CustomerRanker.getNBestCustomers("transaction_data_3.csv", 3);
		System.out.println(nBestCustomers);
	}
}