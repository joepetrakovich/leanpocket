package com.appshroom.leanpocket.dto;

/**
 * Created by jpetrakovich on 8/7/13.
 */
public class OrganizationLink {

    private String link;
    private String text;
    private String hostName;
    private String title;
    private boolean isPasswordVerified;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHostName() {
        return hostName;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isPasswordVerified() {
        return isPasswordVerified;
    }

}
