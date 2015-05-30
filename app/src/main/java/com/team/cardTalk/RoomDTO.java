package com.team.cardTalk;

/**
 * Created by eunjooim on 15. 3. 31..
 */
public class RoomDTO {
    public String articleid;
    public String title;
    public String nickname;
    public String authorid;
    public String icon;
    public String time;
    public String chat;

    public RoomDTO(String articleid, String title, String nickname, String authorid, String icon, String time, String chat) {
        this.articleid = articleid;
        this.title = title;
        this.nickname = nickname;
        this.authorid = authorid;
        this.icon = icon;
        this.time = time;
        this.chat = chat;
    }

    public String getArticleid() {  return articleid; }

    public String getAuthorid() {
        return authorid;
    }

    public String getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public String getNickname() {
        return nickname;
    }

    public String getTime() {
        return time;
    }

    public String getChat() {
        return chat;
    }
}
