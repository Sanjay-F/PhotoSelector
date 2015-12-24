package com.example.sanjay.photoselector.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 图片实体
 * Created by Nereo on 2015/4/7.
 */
public class ImageBean implements Parcelable {
    public String path;
    public String name;
    public long time;




    public ImageBean(String path, String name, long time) {
        this.path = path;
        this.name = name;
        this.time = time;
    }

    protected ImageBean(Parcel in) {
        path = in.readString();
        name = in.readString();
        time = in.readLong();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImageBean)) return false;

        ImageBean imageBean = (ImageBean) o;
        return !(path != null ? !path.equalsIgnoreCase(imageBean.path) : imageBean.path != null);

    }

    @Override
    public int hashCode() {
        return path != null ? path.hashCode() : 0;
    }




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeString(name);
        dest.writeLong(time);
    }

    public static final Creator<ImageBean> CREATOR = new Creator<ImageBean>() {
        @Override
        public ImageBean createFromParcel(Parcel in) {
            return new ImageBean(in);
        }

        @Override
        public ImageBean[] newArray(int size) {
            return new ImageBean[size];
        }
    };

}
