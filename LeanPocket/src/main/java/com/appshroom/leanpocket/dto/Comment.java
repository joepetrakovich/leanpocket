package com.appshroom.leanpocket.dto;

import android.text.Html;

/**
 * Created by jpetrakovich on 8/2/13.
 */
public class Comment {

    private String id;
    private String text;
    private String postDate;
    private String postedByGravatarLink;
    private String postedById;
    private String postedByFullName;
    private boolean editable;

    @Override
    public String toString() {
        return Html.fromHtml(text).toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getPostedByGravatarLink() {
        return postedByGravatarLink;
    }

    public void setPostedByGravatarLink(String postedByGravatarLink) {
        this.postedByGravatarLink = postedByGravatarLink;
    }

    public String getPostedById() {
        return postedById;
    }

    public void setPostedById(String postedById) {
        this.postedById = postedById;
    }

    public String getPostedByFullName() {
        return postedByFullName;
    }

    public void setPostedByFullName(String postedByFullName) {
        this.postedByFullName = postedByFullName;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }
}
