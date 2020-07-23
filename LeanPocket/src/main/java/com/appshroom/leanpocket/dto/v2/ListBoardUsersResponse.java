package com.appshroom.leanpocket.dto.v2;

import com.appshroom.leanpocket.dto.BoardUser;

import java.util.List;

public class ListBoardUsersResponse {
    private PageMeta pageMeta;
    private List<BoardUser> boardUsers;

    public List<BoardUser> getBoardUsers() {
        return boardUsers;
    }
}
