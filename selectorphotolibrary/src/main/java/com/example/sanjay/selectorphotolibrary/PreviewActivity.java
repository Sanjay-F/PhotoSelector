package com.example.sanjay.selectorphotolibrary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.sanjay.selectorphotolibrary.adapter.PreviewPageAdapter;
import com.example.sanjay.selectorphotolibrary.bean.ImageBean;

import java.util.ArrayList;

public class PreviewActivity extends AppCompatActivity {


    private static final String EXTRA_COUNT = "extra_count";
    private final String TAG = PreviewActivity.class.getSimpleName();
    ViewPager mViewPager;
    public static final String EXTRA_DATA = "EXTRA_DATA";
    private CheckBox selectedCb;
    private ArrayList<ImageBean> data;
    private ArrayList<ImageBean> unSelectedData = new ArrayList<>();
    private PreviewPageAdapter adapter;

    private TextView tvTitle;

    private TextView tvFinish;
    private int maxCount;

    public static Intent makeIntent(Context mContext, ArrayList<ImageBean> data, int maxSelectedCount) {
        return new Intent(mContext, PreviewActivity.class).putExtra(EXTRA_DATA, data).putExtra(EXTRA_COUNT, maxSelectedCount);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        data = getIntent().getParcelableArrayListExtra(EXTRA_DATA);
        maxCount = getIntent().getIntExtra(EXTRA_COUNT, 1);

        setActionBar();
        selectedCb = (CheckBox) findViewById(R.id.ap_selected_cb);
        selectedCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selectedCb.isChecked()) {
                    unSelectedData.add(data.get(mViewPager.getCurrentItem()));
                } else {
                    unSelectedData.remove(data.get(mViewPager.getCurrentItem()));
                }
                tvFinish.setText(String.format(getString(R.string.confirm_format), data.size() - unSelectedData.size(), maxCount));
                tvTitle.setText(String.format(getString(R.string.page_number_format), 1, data.size()));
            }
        });

        mViewPager = (ViewPager) findViewById(R.id.ap_photo_vp);
        adapter = new PreviewPageAdapter(data, this);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e(TAG, " post=" + position + " unSelected=" + unSelectedData.contains(data.get(position)));
                selectedCb.setChecked(!unSelectedData.contains(data.get(position)));
                tvFinish.setText(String.format(getString(R.string.confirm_format), data.size() - unSelectedData.size(), maxCount));
                tvTitle.setText(String.format(getString(R.string.page_number_format), position + 1, data.size()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setActionBar() {
        findViewById(R.id.ab_back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        tvFinish = (TextView) findViewById(R.id.ab_confirm_tv);
        tvFinish.setEnabled(true);
        tvFinish.setText(String.format(getString(R.string.confirm_format), data.size() - unSelectedData.size(), maxCount));
        tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.removeAll(unSelectedData);
                setResult(RESULT_OK, SelectedPhotoActivity.makeResult(PreviewActivity.this, data));
                finish();
            }
        });

        tvTitle = ((TextView) findViewById(R.id.ab_title_tv));
        tvTitle.setText(String.format(getString(R.string.page_number_format), 1, data.size()));
    }

}
