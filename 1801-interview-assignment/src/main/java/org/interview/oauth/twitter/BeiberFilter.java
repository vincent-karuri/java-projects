package org.interview.oauth.twitter;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class BeiberFilter {

    private final String CONSUMER_KEY = "vp8qXAMoZzy6jowJdtouPLUUb";
    private final String CONSUMER_SECRET = "IMx3eIRfXXbRimoIz7cNpZCl0dr9dYEdRuDVTr2C4LdResXjN7";
    private final GenericUrl BEIBER_FILTER_URL = new GenericUrl("https://stream.twitter.com/1.1/statuses/filter.json?track=bieber");
    private final int TIME_LAPSE = 30000;
    private final int MSG_LIMIT = 100;

    private final PrintStream OUT = new PrintStream(System.out);
    private TwitterAuthenticator authenticator = new TwitterAuthenticator(OUT, CONSUMER_KEY, CONSUMER_SECRET);

    public void filter() {

        Map<Long, PriorityQueue<Tweet>> userToMessages = new HashMap<>();
        PriorityQueue<TwitterUser> users = new PriorityQueue();
        Set<Long> userSet = new HashSet<>();

        filter(userToMessages, users, userSet);
        printFormattedOutput(users, userToMessages);
    }

    private void filter(Map<Long, PriorityQueue<Tweet>>  usersToMessages, PriorityQueue<TwitterUser> users, Set<Long> userSet) {

        try {
            HttpRequestFactory requestFactory = authenticator.getAuthorizedHttpRequestFactory();
            HttpRequest request = requestFactory.buildGetRequest(BEIBER_FILTER_URL).setNumberOfRetries(5);
            HttpResponse response = request.execute();

            Scanner scanner = new Scanner(response.getContent(), "UTF-8");

            long start = System.currentTimeMillis();
            long timeElapsed = 0;
            List<String> tweets = new ArrayList<>();
            while (scanner.hasNextLine() && tweets.size() < MSG_LIMIT && timeElapsed < TIME_LAPSE) {
                String status = scanner.nextLine();
                tweets.add(status);
                // calculate time difference
                long finish = System.currentTimeMillis();
                timeElapsed = finish - start;
            }

            for (int i = 0; i < tweets.size(); i++) {
                extractAndSaveFields(new JSONObject(tweets.get(i)), usersToMessages, users, userSet);
            }
            response.disconnect();
        } catch (TwitterAuthenticationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void extractAndSaveFields(JSONObject jsonObject, Map<Long, PriorityQueue<Tweet>>  usersToMessages, PriorityQueue<TwitterUser> users, Set<Long> userSet) {

        Long userId = jsonObject.getJSONObject("user").getLong("id");
        if (!userSet.contains(userId)) {
            userSet.add(userId);
            TwitterUser user = new TwitterUser(
                    userId,
                    formatDate(jsonObject.getJSONObject("user").getString("created_at")),
                    jsonObject.getJSONObject("user").getString("screen_name")
            );
            users.add(user);
            usersToMessages.put(userId, new PriorityQueue<>());
        }

        Tweet tweet = new Tweet(
                jsonObject.getLong("id"),
                jsonObject.getString("text"),
                formatDate(jsonObject.getString("created_at")),
                jsonObject.getJSONObject("user").getLong("id")
        );
        PriorityQueue<Tweet> tweets = usersToMessages.get(userId);
        tweets.add(tweet);
        usersToMessages.put(userId, tweets);
    }

    private void printFormattedOutput(PriorityQueue<TwitterUser> users, Map<Long, PriorityQueue<Tweet>> userToMessages) {

        while (!users.isEmpty()) {

            TwitterUser user = users.poll();
            PriorityQueue<Tweet> tweets = userToMessages.get(user.getUserId());
            while (!tweets.isEmpty()) {
                Tweet tweet = tweets.poll();

                // print user info
                OUT.println();
                OUT.println("User info:");
                OUT.println();
                OUT.println("id: " + user.getUserId());
                OUT.println("screen_name: " + user.getScreenName());
                OUT.println("creation_date: " + user.getCreationDate());
                OUT.println();

                // print message info
                OUT.println("Message info: ");
                OUT.println();
                OUT.println("message_id: " + tweet.getMessageId());
                OUT.println("message_text: " + tweet.getMessage());
                OUT.println("user_id: " + tweet.getUserId());
                OUT.println("creation_date: " + tweet.getCreationDate());
                OUT.println();

                OUT.println("===================");
            }
        }
    }

    private Date formatDate(String dateString) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
        try {
            return dateFormat.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**************** INNER CLASSES *****************/

    private class Tweet implements Comparable<Tweet> {

        private Long messageId;
        private String message;
        private Date creationDate;
        private Long userId;

        public Tweet(Long messageId, String message, Date creationDate, Long userId) {
            this.messageId = messageId;
            this.message = message;
            this.creationDate = creationDate;
            this.userId = userId;
        }

        public Date getCreationDate() {
            return creationDate;
        }

        public Long getMessageId() {
            return messageId;
        }

        public String getMessage() {
            return message;
        }

        public Long getUserId() {
            return userId;
        }

        public int compareTo(Tweet tweet2) {
            return this.getCreationDate().compareTo(tweet2.getCreationDate());
        }
    }

    private class TwitterUser implements Comparable<TwitterUser> {

        private Long userId;
        private Date creationDate;
        private String screenName;

        public TwitterUser(Long userId, Date creationDate, String screenName) {
            this.userId = userId;
            this.creationDate = creationDate;
            this.screenName = screenName;
        }

        public Date getCreationDate() {
            return creationDate;
        }

        public Long getUserId() {
            return userId;
        }

        public String getScreenName() {
            return screenName;
        }

        public int compareTo(TwitterUser tweetUser2) {
            return this.getCreationDate().compareTo(tweetUser2.getCreationDate());
        }
    }

    public static void main(String[] args) {
        BeiberFilter beiberFilter = new BeiberFilter();
        beiberFilter.filter();
    }
}
