package com.example.yhdj.ad0306bmob;

import cn.bmob.v3.BmobObject;

/**
 * Created by yhdj on 2017/3/5.
 */

public class ImageBean extends BmobObject{
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private  String url;
}
