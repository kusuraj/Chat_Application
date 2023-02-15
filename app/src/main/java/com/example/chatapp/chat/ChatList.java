package com.example.chatapp.chat;

public class ChatList {
    final String mobile, name, messages, date,time;

    public ChatList(String mobile, String name, String messages, String date, String time) {
        this.mobile = mobile;
        this.name = name;
        this.messages = messages;
        this.date = date;
        this.time = time;
    }

    public String getMobile() {
        return mobile;
    }

    public String getName() {
        return name;
    }

    public String getMessages() {
        return messages;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
