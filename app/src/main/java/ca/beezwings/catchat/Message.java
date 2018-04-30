package ca.beezwings.catchat;

import java.util.Date;

/**
 * Created by beezw on 2018-03-21.
 */

public class Message {
    private String messageText;
    private String profileName;
    private Long dateStamp;
    private int randomGenNumber;

    public Message(String messageText, String profileName, int randomGenNumber) {
        this.messageText = messageText;
        this.profileName = profileName;
        this.dateStamp = new Date().getTime();
        this.randomGenNumber = randomGenNumber;

    }

    public String getMessageText() {
        return messageText;
    }

    public String getProfileName() {
        return profileName;
    }

    public Long getDateStamp() {
        return dateStamp;
    }

    public int getRandomGenNumber() { return randomGenNumber;  }

// This is used by firebase and google, under the hood

    public Message (){

    };
}
