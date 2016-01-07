package com.example.sanjay.selectorphotolibrary.utils;

import android.text.TextUtils;
import android.util.Log;

import com.nostra13.universalimageloader.core.download.ImageDownloader;


/**
 * 用来取消代码里面粗暴的加"file://"来转换格式的问题
 */
public class ImageSchemeUtils {

    public static final  String TAG=ImageSchemeUtils.class.getSimpleName();

    public static String autoWrapUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            Log.w(TAG,"逗我？空图片地址");
            return "";
        }
        if (url.startsWith("www")) {
            url = ImageDownloader.Scheme.HTTP.wrap(url);
        } else if (url.startsWith("/")) { //本地图片以斜杠开始本地路径
            url = ImageDownloader.Scheme.FILE.wrap(url);
        }
        return url;
    }

}
