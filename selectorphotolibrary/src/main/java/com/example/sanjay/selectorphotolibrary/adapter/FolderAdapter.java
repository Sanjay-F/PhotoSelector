package com.example.sanjay.selectorphotolibrary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sanjay.selectorphotolibrary.R;
import com.example.sanjay.selectorphotolibrary.bean.ImageFolder;
import com.example.sanjay.selectorphotolibrary.utils.ImageSchemeUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.util.ArrayList;
import java.util.List;

//import com.squareup.picasso.Picasso;


public class FolderAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;

    private List<ImageFolder> mFolders = new ArrayList<>();

    int mImageSize;

    int lastSelected = 0;
    private ImageSize targetSize;

    public FolderAdapter(Context context) {
        targetSize = new ImageSize(80, 50);
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mImageSize = mContext.getResources().getDimensionPixelOffset(R.dimen.folder_cover_size);
    }

    /**
     * 设置数据集
     *
     * @param folders
     */
    public void setData(List<ImageFolder> folders) {
        if (folders != null && folders.size() > 0) {
            mFolders = folders;
        } else {
            mFolders.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mFolders.size() + 1;
    }

    @Override
    public ImageFolder getItem(int i) {
        if (i == 0) return null;
        return mFolders.get(i - 1);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = mInflater.inflate(R.layout.list_item_folder, viewGroup, false);
            holder = new ViewHolder(view);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (holder != null) {
            if (i == 0) {
                holder.name.setText("所有图片");
                holder.size.setText(getTotalImageSize() + "张");
                if (mFolders.size() > 0) {
                    ImageFolder f = mFolders.get(0);

//                    ImageLoader.getInstance().displayImage("file://" + f.cover.path, holder.cover);
                    //TODO 后面改用封装后的NetWorkImageView代替。
                    ImageLoader.getInstance().displayImage(ImageSchemeUtils.autoWrapUrl(f.cover.path), holder.cover);
                }
            } else {
                holder.bindData(getItem(i));
            }
            if (lastSelected == i) {
                holder.indicator.setVisibility(View.VISIBLE);
            } else {
                holder.indicator.setVisibility(View.INVISIBLE);
            }
        }
        return view;
    }

    private int getTotalImageSize() {
        int result = 0;
        if (mFolders != null && mFolders.size() > 0) {
            for (ImageFolder f : mFolders) {
                result += f.images.size();
            }
        }
        return result;
    }

    public void setSelectIndex(int i) {
        if (lastSelected == i) return;

        lastSelected = i;
        notifyDataSetChanged();
    }

    public int getSelectIndex() {
        return lastSelected;
    }

    class ViewHolder {
        ImageView cover;
        TextView name;
        TextView size;
        ImageView indicator;

        ViewHolder(View view) {
            cover = (ImageView) view.findViewById(R.id.cover);
            name = (TextView) view.findViewById(R.id.name);
            size = (TextView) view.findViewById(R.id.size);
            indicator = (ImageView) view.findViewById(R.id.indicator);
            view.setTag(this);
        }

        void bindData(ImageFolder data) {
            name.setText(data.name);
            size.setText(data.images.size() + "张");
            // 显示图片
            ImageLoader.getInstance().displayImage(ImageSchemeUtils.autoWrapUrl(data.cover.path), cover);

        }
    }

}
