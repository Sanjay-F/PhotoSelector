package com.example.sanjay.selectorphotolibrary.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.example.sanjay.selectorphotolibrary.R;
import com.example.sanjay.selectorphotolibrary.utils.ImageSchemeUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;


/***
 * 封装的一个基本的简单的网络图片类。
 * 可以参考Volley里面的
 */
public class NetworkImageView extends ImageView {


    DisplayImageOptions defaultImageOptions = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.default_error)
            .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
            .cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();

    private static final String TAG = NetworkImageView.class.getSimpleName();
    protected String mImageUrl;
    private DisplayImageOptions mDefaultOptions;
    private ImageLoadingListener mLoadingListener;
    private ImageLoadingProgressListener mProgressListener;

    public NetworkImageView(Context context) {
        super(context);
    }

    public NetworkImageView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        if (getDrawable() != null) {
            setDefaultDrawable(getDrawable());
        }
    }

    public NetworkImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public boolean setImageWithURL(String url) {
        return this.setImageWithURL(url, true, null);
    }

    public boolean setImageWithURL(String url, Bitmap defaultImage) {
        return this.setImageWithURL(url, true, new BitmapDrawable(getResources(), defaultImage));

    }

    public boolean setImageWithURL(String url, Drawable defaultImage) {
        return this.setImageWithURL(url, true, defaultImage);
    }

    public boolean setImageWithURL(String url, boolean useLocal) {
        return this.setImageWithURL(url, useLocal, null);
    }

    public boolean setImageWithURL(String url, boolean useLocal, Drawable defaultImage) {

        Log.d(TAG, "setImageWithURL " + url);
        if (defaultImage != null) {
            setDefaultDrawable(defaultImage);
        }
        try {
            ImageLoader.getInstance().displayImage(ImageSchemeUtils.autoWrapUrl(url), this, defaultImageOptions, mLoadingListener, mProgressListener);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void setDefaultDrawable(Bitmap defaultImage) {
        if (defaultImage != null) {
            setDefaultDrawable(new BitmapDrawable(getResources(), defaultImage));
        }
    }

    public void setDefaultDrawable(Drawable defaultDrawable) {
        if (defaultDrawable != null) {
            setmDefaultOptions(new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.default_error)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                    .showImageOnFail(defaultDrawable).showImageForEmptyUri(defaultDrawable).showImageOnLoading(defaultDrawable)
                    .cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build());
        }
    }

    public void setDefaultDrawable(int resId) {
        if (resId != 0) {
            setmDefaultOptions(new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.default_error)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                    .showImageOnFail(resId).showImageForEmptyUri(resId).showImageOnLoading(resId)
                    .cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build());
        }
    }

    public DisplayImageOptions getmDefaultOptions() {
        if (mDefaultOptions == null) {
            mDefaultOptions = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.default_error)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                    .cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();
        }
        return mDefaultOptions;
    }

    public void setmDefaultOptions(DisplayImageOptions mDefaultOptions) {
        this.mDefaultOptions = mDefaultOptions;
    }

    public ImageLoadingListener getmLoadingListener() {
        return mLoadingListener;
    }

    public void setmLoadingListener(ImageLoadingListener mLoadingListener) {
        this.mLoadingListener = mLoadingListener;
    }

    public ImageLoadingProgressListener getmProgressListener() {
        return mProgressListener;
    }

    public void setmProgressListener(ImageLoadingProgressListener mProgressListener) {
        this.mProgressListener = mProgressListener;
    }

    @Override
    protected void finalize() throws Throwable {
        this.setImageDrawable(null);
        super.finalize();
    }

}
