package de.greenrobot.tutorial.twitter;

/*
 * Twitter Tutorial.
 * 
 * (c) Copyright Markus Junginger 2010.
 */
public class Tweet {
    private final String user;
    private final String userIconUrl;
    private final String text;

    public Tweet(String user, String userIconUrl, String text) {
        super();
        this.user = user;
        this.userIconUrl = userIconUrl;
        this.text = text;
    }

    public String getUser() {
        return user;
    }

    public String getUserIconUrl() {
        return userIconUrl;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Tweet from " + user + ": " + text;
    }
}
