package com.example.sanjay.selectorphotolibrary.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdapterBase<T> extends BaseAdapter {

    public List<T> mList = new ArrayList<T>();

    @Override
    public int getCount() {

        if (mList == null)
            return -1;
        return mList.size();
    }

    @Override
    public Object getItem(int positon) {
        return mList.get(positon);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView);
    }


    public View getView(int position, View convertView) {
        return null;
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

}
