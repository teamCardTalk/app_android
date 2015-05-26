package com.team.cardTalk;

/**
 * Created by eunjooim on 15. 3. 31..
 */
public class ChatDTO {
    private String id;
    private String articleid;
    private String nickname;
    private String nicknameid;
    private String icon;
    private String content;
    private String time;

    public ChatDTO(String id, String articleid, String nickname, String nicknameid, String icon, String content, String time) {
        this.id = id;
        this.articleid = articleid;
        this.nickname = nickname;
        this.nicknameid = nicknameid;
        this.icon = icon;
        this.content = content;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public String getArticleid() {
        return articleid;
    }

    public String getNickname() {
        return nickname;
    }

    public String getNicknameid() {
        return nicknameid;
    }

    public String getIcon() {
        return icon;
    }

    public String getContent() { return content; }

    public String getTime() {
        return time;
    }
}
