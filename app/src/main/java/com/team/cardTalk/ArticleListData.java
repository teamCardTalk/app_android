package com.team.cardTalk;

/**
 * Created by eunjooim on 15. 3. 26..
 */
public class ArticleListData {
    private String title;
    private String date;
    private String imgName;
    private String detail;

    public ArticleListData(String title, String date, String imgName, String detail) {
        this.title = title;
        this.date = date;
        this.imgName = imgName;
        this.detail = detail;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {

        return date;
    }

    public String getImgName() {
        return imgName;
    }

    public String getDetail() {
        return detail;
    }
}
