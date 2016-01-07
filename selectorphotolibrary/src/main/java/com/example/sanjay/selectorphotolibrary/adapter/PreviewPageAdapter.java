package com.example.sanjay.selectorphotolibrary.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sanjay.selectorphotolibrary.R;
import com.example.sanjay.selectorphotolibrary.bean.ImageBean;
import com.example.sanjay.selectorphotolibrary.utils.ImageSchemeUtils;
import com.example.sanjay.selectorphotolibrary.widget.TouchImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Sanjay on 2015/12/25 on project PhotoSelector.
 */
public class PreviewPageAdapter extends PagerAdapter {

    private final String TAG = this.getClass().getSimpleName();
    private ArrayList<ImageBean> data;
    private Context mContext;
    private ArrayList<View> viewArrayList = new ArrayList<>();

    public PreviewPageAdapter(ArrayList<ImageBean> data, Context mContext) {

        this.data = data;
        this.mContext = mContext;
        for (ImageBean bean : data) {
            viewArrayList.add(buildView(bean));
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {
        container.removeView(viewArrayList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewArrayList.get(position));
        return viewArrayList.get(position);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    public View buildView(ImageBean image) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pagelist_item_preview_photo, null);
        TouchImageView ivPhoto = (TouchImageView) view.findViewById(R.id.ltp_photo_iv);

        ImageLoader.getInstance().displayImage(ImageSchemeUtils.autoWrapUrl( image.path), ivPhoto);

        return view;
    }


}
