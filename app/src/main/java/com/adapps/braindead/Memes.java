package com.adapps.braindead;

/**
 * Created by 1405006 on 9/8/2016.
 */
public class Memes {
    String name,image;

    public Memes(String name,String image){
        this.name=name;
        this.image=image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
