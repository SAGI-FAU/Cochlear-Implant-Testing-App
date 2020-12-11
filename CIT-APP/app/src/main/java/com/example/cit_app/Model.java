/**
 * Created by Christoph Popp
 */

package com.example.cit_app;

public class Model {

    private int image;
    private String title;
    private String desc;

    public Model(int image, String title, String desc) {
        this.image = image;
        this.title = title;
        this.desc = desc;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }
}
