package com.example.sanjay.selectorphotolibrary.adapter.base;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public abstract class RecycleAdapterBase<T, A extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<A> {

    public List<T> mList = new ArrayList<>();
    public onListItemClickListener<T> mListener;

    protected String TAG = this.getClass().getSimpleName();

    @Override
    public int getItemCount() {
        if (mList == null)
            return -1;
        return mList.size();
    }


    /**
     * @param list
     */
    public void appentToList(List<T> list) {
        mList.addAll(list);
        notifyDataSetChanged();
        list = null;
    }

    public void appentToList(T t) {
        mList.add(t);
        notifyDataSetChanged();
    }

    public void appentToListFirst(T t) {
        mList.add(0, t);
        notifyDataSetChanged();
    }

    public void appentToListFirst(List<T> list) {
        mList.addAll(0, list);
        notifyDataSetChanged();
        list = null;
    }

    /**
     * @param list
     */
    public void changeList(List<T> list) {
        mList = list == null ? new ArrayList<T>() : list;
        notifyDataSetChanged();
    }

    /**
     * 清空列表
     */
    public void clearList() {
        mList.clear();
        notifyDataSetChanged();
    }

    /**
     * 删除列表中某一项
     */
    public void removeObject(T t) {
        mList.remove(t);
        notifyDataSetChanged();
        t = null;
    }

    public void updateObject(T t) {
        mList.set(mList.indexOf(t), t);
        notifyDataSetChanged();
    }

    public List<T> getList() {
        return mList;
    }

    public void setOnItemClickListener(onListItemClickListener<T> listener) {
        this.mListener = listener;
    }

    public interface onListItemClickListener<T> {
        void onItemClick(int position, T bean);
    }


    protected T getItem(int position) {
        if (mList != null) {
            return mList.get(position);
        }
        return null;
    }
}
