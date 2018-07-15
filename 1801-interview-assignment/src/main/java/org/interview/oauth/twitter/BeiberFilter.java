package org.interview.oauth.twitter;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class BeiberFilter {

    private final String CONSUMER_KEY = "vp8qXAMoZzy6jowJdtouPLUUb";
    private final String CONSUMER_SECRET = "IMx3eIRfXXbRimoIz7cNpZCl0dr9dYEdRuDVTr2C4LdResXjN7";
    private final GenericUrl BEIBER_FILTER_URL = new GenericUrl("https://api.twitter.com/1.1/search/tweets.json?q=twitterdev&src=typd");

    private final PrintStream OUT = new PrintStream(System.out);
    private TwitterAuthenticator authenticator = new TwitterAuthenticator(OUT, CONSUMER_KEY, CONSUMER_SECRET);

    public void filter() {
        Map<Long, PriorityQueue<Tweet>> userToMessages = new HashMap<>();
        PriorityQueue<TwitterUser> users = new PriorityQueue();
        filter(userToMessages, users);
    }

    private void filter(Map<Long, PriorityQueue<Tweet>>  usersToMessages, PriorityQueue<TwitterUser> users) {

        try {
            HttpRequestFactory requestFactory = authenticator.getAuthorizedHttpRequestFactory();
            HttpResponse response = requestFactory.buildGetRequest(BEIBER_FILTER_URL).execute();

            String resp = IOUtils.toString(response.getContent(), "UTF-8");

            OUT.println(resp);

            JSONArray respJson = convertToJSONArray(resp);
            for (int i = 0; i < respJson.length(); i++) {
                extractFields(respJson.getJSONObject(i));
            }
        } catch (TwitterAuthenticationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JSONArray convertToJSONArray(String jsonString) {
        return new JSONObject(jsonString).getJSONArray("statuses");
    }

    private void extractFields(JSONObject jsonObject) {

        OUT.println();
        OUT.println("Tweet info: ");
        OUT.println(jsonObject.get("created_at"));
        OUT.println(jsonObject.get("id"));
        OUT.println(jsonObject.get("text"));
        OUT.println(jsonObject.getJSONObject("user").get("id"));

        OUT.println();
        OUT.println("User info: ");
        OUT.println(jsonObject.getJSONObject("user").get("id"));
        OUT.println(jsonObject.getJSONObject("user").get("screen_name"));
        OUT.println(jsonObject.getJSONObject("user").get("created_at"));
    }

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

        public int compareTo(TwitterUser tweetUser2) {
            return this.getCreationDate().compareTo(tweetUser2.getCreationDate());
        }
    }

    public static void main(String[] args) {

        BeiberFilter beiberFilter = new BeiberFilter();
        beiberFilter.filter();
    }
}
