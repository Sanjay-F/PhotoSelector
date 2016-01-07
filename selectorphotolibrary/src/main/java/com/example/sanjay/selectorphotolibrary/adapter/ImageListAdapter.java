package com.example.sanjay.selectorphotolibrary.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.sanjay.selectorphotolibrary.R;
import com.example.sanjay.selectorphotolibrary.bean.ImageBean;
import com.example.sanjay.selectorphotolibrary.bean.ImgOptions;
import com.example.sanjay.selectorphotolibrary.utils.ImageSchemeUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ImageListAdapter extends AdapterBase<String> {

    private static final int TYPE_CAMERA = 0;
    private static final int TYPE_NORMAL = 1;
    private final ImageSize targetSize;

    private Context mContext;

    private LayoutInflater mInflater;
    private List<ImageBean> mImages = new ArrayList<>();
    private ArrayList<ImageBean> mSelectedImages = new ArrayList<>();
    private int mItemSize;
    private ImgOptions options;
    private onImageClickListener listener;
    private AbsListView.LayoutParams mItemLayoutParams;
    private boolean isInSubCatalog = false;

    public ImageListAdapter(Context context, ImgOptions options) {
        this.options = options;

        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        targetSize = new ImageSize(80, 50);

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        mItemSize = dm.widthPixels / 3;

        mItemLayoutParams = new AbsListView.LayoutParams(mItemSize, mItemSize);

    }

    public boolean isShowCamera() {
        return options.isShowCamera();
    }

    public void setInSubCatalog(boolean isInSubCatalog) {
        this.isInSubCatalog = isInSubCatalog;
    }

    public void setData(List<ImageBean> images) {
        if (images != null && images.size() > 0) {
            mImages = images;
        } else {
            mImages.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        if (options.isShowCamera()) {
            return 2;
        } else {
            return 1;
        }

    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 && options.isShowCamera() && !isInSubCatalog ? TYPE_CAMERA : TYPE_NORMAL;
    }

    @Override
    public int getCount() {
        return options.isShowCamera() && !isInSubCatalog ? mImages.size() + 1 : mImages.size();
    }

    @Override
    public ImageBean getItem(int i) {
        if (options.isShowCamera() && !isInSubCatalog) {
            if (i == 0) {
                return null;
            }
            return mImages.get(i - 1);
        } else {
            return mImages.get(i);
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        int type = getItemViewType(position);
        if (type == TYPE_CAMERA) {
            view = mInflater.inflate(R.layout.list_item_camera, viewGroup, false);
            view.setTag(null);
        } else if (type == TYPE_NORMAL) {
            ViewHolde holde;
            if (view == null) {
                view = mInflater.inflate(R.layout.list_item_image, viewGroup, false);
                holde = new ViewHolde(view);
            } else {
                holde = (ViewHolde) view.getTag();
                if (holde == null) {
                    view = mInflater.inflate(R.layout.list_item_image, viewGroup, false);
                    holde = new ViewHolde(view);
                }
            }
            holde.bindData(getItem(position));
        }


        GridView.LayoutParams lp = (GridView.LayoutParams) view.getLayoutParams();
        if (lp.height != mItemSize) {
            //存在一些图片比较小，所以不能占满一个，需要调整下
            view.setLayoutParams(mItemLayoutParams);
        }

        return view;
    }

    class ViewHolde {
        ImageView image;
        CheckBox selectedCb;
        View mask;

        ViewHolde(View view) {
            image = (ImageView) view.findViewById(R.id.image);
//            indicator = (ImageView) view.findViewById(R.id.checkmark);
            selectedCb = (CheckBox) view.findViewById(R.id.select_checkbox);
            mask = view.findViewById(R.id.mask);
            view.setTag(this);
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
                ImageLoader.getInstance().displayImage(ImageSchemeUtils.autoWrapUrl(data.path), image, targetSize);

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
