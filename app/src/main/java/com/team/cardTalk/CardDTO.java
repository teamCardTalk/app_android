package com.team.cardTalk;

/**
 * Created by eunjooim on 15. 3. 31..
 */
public class CardDTO {
    private String _id;
    private int status;
    private String title;
    private String author;
    private int authorid;
    private String icon;
    private String createtime;
    private String content;
    private int partynumber;
//    private String chattingtime;
//    private String chatting;
    private String photo;

    public CardDTO(String _id, int status, String title, String author, int authorid, String icon, String createtime, String content, int partynumber, String photo) {
//    public CardDTO(String _id, int status, String title, String author, int authorid, String icon, String createtime, String content, int partynumber, String chattingtime, String chatting, String photo) {
        this._id = _id;
        this.status = status;
        this.title = title;
        this.author = author;
        this.authorid = authorid;
        this.icon = icon;
        this.createtime = createtime;
        this.content = content;
        this.partynumber = partynumber;
//        this.chattingtime = chattingtime;
//        this.chatting = chatting;
        this.photo = photo;
    }

    public String getId() { return _id; }
    
    public int getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getAuthorid() {
        return authorid;
    }

    public String getIcon() {
        return icon;
    }

    public String getCreatetime() {
        return createtime;
    }

    public String getContent() {
        return content;
    }

    public int getPartynumber() {
        return partynumber;
    }

//    public String getChattingtime() {
//        return chattingtime;
//    }
//
//    public String getChatting() {
//        return chatting;
//    }

    public String getPhoto() {
        return photo;
    }

}
