package com.appshroom.leanpocket.dto.v2;

import java.util.Date;

public class Comment {
    public String id;
    public Date createdOn;
    public CommentCreatedByUser createdBy;
    public String text;

    public class CommentCreatedByUser {
        public String id;
        public String emailAddress;
        public String fullName;
        public String avatar;
    }
}