package com.appshroom.leanpocket.dto.v2;
import java.util.List;

public class ListBoardsResponse {
    private PageMeta pageMeta;
    private List<ListBoardsBoard> boards;

    public List<ListBoardsBoard> getBoards(){
        return boards;
    }
}

