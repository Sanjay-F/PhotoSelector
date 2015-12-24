package com.example.sanjay.selectorphotolibrary.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sanjay on 2015/12/24 on project PhotoSelector.
 */
public class ImgOptions implements Parcelable {


    public static final int MODE_SINGLE = 0;
    public static final int MODE_MULTI = 1;

    public static final int MAX_SELECTED = 9;//最大多选图片为九张;

    public static final int MIN_SELECTED = 1;

    //用于做一定的扩展，是否支持只有4张等多选情况
    private int selectedCount;
    private int mode;
    private boolean showCamera;


    public ImgOptions(Parcel in) {
        selectedCount = in.readInt();
        mode = in.readInt();
        showCamera = in.readByte() != 0;
    }

    public ImgOptions() {
    }

    public ImgOptions(int mode, boolean showCamera) {
        this(MAX_SELECTED, mode, showCamera);
    }

    public ImgOptions(int count, int mode, boolean showCamera) {
        this.mode = mode;
        if (mode == MODE_MULTI) {
            if (count > MAX_SELECTED || count < MIN_SELECTED) {
                this.selectedCount = MAX_SELECTED;
            } else {
                this.selectedCount = count;
            }
        } else {
            this.selectedCount = MIN_SELECTED;
        }
        this.showCamera = showCamera;
    }

    public int getSelectedCount() {
        return selectedCount;
    }

    public void setSelectedCount(int selectedCount) {
        if (selectedCount > MAX_SELECTED) {
            this.selectedCount = MAX_SELECTED;
        } else {
            this.selectedCount = selectedCount;
        }
    }

    public int getMode() {
        return mode;
    }

    public boolean isMultiMode() {
        return mode == MODE_MULTI;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public boolean isShowCamera() {
        return showCamera;
    }

    public void setShowCamera(boolean showCamera) {
        this.showCamera = showCamera;
    }


    public static int getMaxSelected() {
        return MAX_SELECTED;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(selectedCount);
        dest.writeInt(mode);
        dest.writeByte((byte) (showCamera ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ImgOptions> CREATOR = new Creator<ImgOptions>() {
        @Override
        public ImgOptions createFromParcel(Parcel in) {
            return new ImgOptions(in);
        }

        @Override
        public ImgOptions[] newArray(int size) {
            return new ImgOptions[size];
        }
    };
}
