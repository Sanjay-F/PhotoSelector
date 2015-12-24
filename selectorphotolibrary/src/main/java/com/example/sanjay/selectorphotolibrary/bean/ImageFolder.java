package com.example.sanjay.selectorphotolibrary.bean;

import java.util.List;

public class ImageFolder {
    public String name;
    public String path;
    public ImageBean cover;
    public List<ImageBean> images;

    @Override
    public boolean equals(Object o) {
        try {
            ImageFolder other = (ImageFolder) o;
            return this.path.equalsIgnoreCase(other.path);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
