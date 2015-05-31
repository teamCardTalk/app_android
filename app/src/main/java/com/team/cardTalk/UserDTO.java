package com.team.cardTalk;

/**
 * Created by eunjooim on 15. 5. 31..
 */
public class UserDTO {
    public String userid;
    public String nickname;
    public String icon;

    public UserDTO(String userid, String nickname, String icon) {
        this.userid = userid;
        this.nickname = nickname;
        this.icon = icon;
    }

    public String getUserid() {  return userid; }

    public String getIcon() {
        return icon;
    }

    public String getNickname() {
        return nickname;
    }

}
