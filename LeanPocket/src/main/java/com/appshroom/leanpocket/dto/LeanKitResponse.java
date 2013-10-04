package com.appshroom.leanpocket.dto;

import java.util.List;

/**
 * Created by jpetrakovich on 8/3/13.
 */
public class LeanKitResponse<T> {


    private int replyCode;
    private String replyText;
    private List<T> replyData;


    public int getReplyCode() {
        return replyCode;
    }

    public String getReplyText() {
        return replyText;
    }

    public List<T> getReplyData() {
        return replyData;
    }

    public void setReplyCode(int replyCode) {
        this.replyCode = replyCode;
    }

    public void setReplyText(String replyText) {
        this.replyText = replyText;
    }

    public void setReplyData(List<T> replyData) {
        this.replyData = replyData;
    }
}


