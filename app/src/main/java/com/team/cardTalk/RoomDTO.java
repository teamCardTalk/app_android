package com.team.cardTalk;

/**
 * Created by eunjooim on 15. 3. 31..
 */
public class RoomDTO {
    private String id;
    private String articleid;
    private int authorid;
    private String icon;
    private String title;
    private String time;
    private String chat;

    public RoomDTO(String articleid, int authorid, String icon, String title, String time, String chat) {
        this.articleid = articleid;
        this.authorid = authorid;
        this.icon = icon;
        this.title = title;
        this.time = time;
        this.chat = chat;
    }

    public String getArticleid() {
        return articleid;
    }

    public int getAuthorid() {
        return authorid;
    }

    public String getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getChat() {
        return chat;
    }
}
