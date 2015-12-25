package com.example.sanjay.selectorphotolibrary.adapter;


import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.example.sanjay.selectorphotolibrary.R;
import com.example.sanjay.selectorphotolibrary.adapter.base.RecycleAdapterBase;
import com.example.sanjay.selectorphotolibrary.bean.ImageBean;
import com.example.sanjay.selectorphotolibrary.bean.ImgOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Sanjay on 2015/11/23 on project android.
 */
public class ImgListAdapter extends RecycleAdapterBase<ImageBean, RecyclerView.ViewHolder> {


    private static final int TYPE_CAMERA = 0;
    private static final int TYPE_NORMAL = 1;
    private final ImageSize targetSize;

    private Context mContext;

    private LayoutInflater mInflater;
    //    private List<ImageBean> mImages = new ArrayList<>();
    private ArrayList<ImageBean> mSelectedImages = new ArrayList<>();
    private int mItemSize;
    private ImgOptions options;
    private onImageClickListener listener;
    private ViewGroup.LayoutParams mItemLayoutParams;
    private boolean isInSubCatalog = false;

    public ImgListAdapter(Context context, ImgOptions options) {

        this.options = options;
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        targetSize = new ImageSize(80, 50);

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        mItemSize = dm.widthPixels / 3;

        mItemLayoutParams = new ViewGroup.LayoutParams(mItemSize, mItemSize);
    }

    public boolean isShowCamera() {
        return options.isShowCamera() && !isInSubCatalog;
    }

    public void setInSubCatalog(boolean isInSubCatalog) {
        this.isInSubCatalog = isInSubCatalog;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_NORMAL) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_image, parent, false);

            return new ImgViewHolder(v);

        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_camera, parent, false);
            return new CameraViewHolder(v);
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isShowCamera() && position != 0) {
            ((ImgViewHolder) holder).bindData(getItem(position - 1));
        } else if (!isShowCamera()) {
            ((ImgViewHolder) holder).bindData(getItem(position));
        }


    }


    @Override
    public int getItemCount() {
        return options.isShowCamera() && !isInSubCatalog ? mList.size() + 1 : mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 && options.isShowCamera() && !isInSubCatalog ? TYPE_CAMERA : TYPE_NORMAL;
    }


    public class ImgViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        ImageView image;
        CheckBox selectedCb;
        View mask;

        ImgViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.image);
//            indicator = (ImageView) view.findViewById(R.id.checkmark);
            selectedCb = (CheckBox) view.findViewById(R.id.select_checkbox);
            mask = view.findViewById(R.id.mask);
            view.setTag(this);

            view.setOnClickListener(this);
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (lp.height != mItemSize) {
                //存在一些图片比较小，所以不能占满一个，需要调整下
                view.setLayoutParams(mItemLayoutParams);
            }

        }

        void bindData(final ImageBean data) {
            if (data == null) return;

            if (mSelectedImages.contains(data)) {
                selectedCb.setChecked(true);
                mask.setVisibility(View.VISIBLE);
            } else {
                selectedCb.setChecked(false);
                mask.setVisibility(View.GONE);
            }


            File imageFile = new File(data.path);

            if (mItemSize > 0) {
                // 显示图片
                ImageLoader.getInstance().displayImage("file://" + data.path, image, targetSize);
            }
            selectedCb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CheckBox checkBox = (CheckBox) v;
                    if (mSelectedImages.contains(data)) {
                        mSelectedImages.remove(data);
                    } else {
                        if (options.getSelectedCount() == mSelectedImages.size()) {
                            checkBox.setChecked(false);
                        } else {
                            mSelectedImages.add(data);
                        }
                    }

                    int visibilty = checkBox.isChecked() ? View.VISIBLE : View.GONE;
                    mask.setVisibility(visibilty);

                    if (listener != null) {
                        listener.onImageClick(mSelectedImages.size(), options.getSelectedCount(), 0);
                    }

                }
            });


        }

        @Override
        public void onClick(View view) {
            Log.e(TAG, " onclick" + getAdapterPosition());
            if (mListener != null) {
                mListener.onItemClick(getAdapterPosition(), mList.get(getAdapterPosition()));
            }
        }
    }

    public class CameraViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        ImageView image;

        CameraViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.image);
            view.setTag(this);

            view.setOnClickListener(this);
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (lp.height != mItemSize) {
                //存在一些图片比较小，所以不能占满一个，需要调整下
                view.setLayoutParams(mItemLayoutParams);
            }
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(getAdapterPosition(), null);
            }
        }
    }


    public ArrayList<ImageBean> getSelectedImages() {
        return mSelectedImages;
    }

    public interface onImageClickListener {
        void onImageClick(int curSelectedCount, int total, int position);
    }

    public void setOnImageClickListener(onImageClickListener listener) {
        this.listener = listener;
    }
}