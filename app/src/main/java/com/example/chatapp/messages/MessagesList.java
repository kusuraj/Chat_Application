package com.example.chatapp.messages;

public class MessagesList {

    final String name,mobile, lastmessage,profilePic,chatKey;
    final int unseenMessages;

    public MessagesList(String name, String mobile, String lastmessage, String profilePic, int unseenMessages,String chatKey) {
        this.name = name;
        this.mobile = mobile;
        this.profilePic = profilePic;
        this.lastmessage = lastmessage;
        this.unseenMessages = unseenMessages;
        this.chatKey = chatKey;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getLastmessage() {
        return lastmessage;
    }

    public int getUnseenMessages() {
        return unseenMessages;
    }

    public String getChatKey() {
        return chatKey;
    }
}
