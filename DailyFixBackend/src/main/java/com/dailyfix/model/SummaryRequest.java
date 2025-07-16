package com.dailyfix.model;

import java.util.List;

public class SummaryRequest {
    private List<String> messages;

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}
