package com.example.sanjay.photoselector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.sanjay.selectorphotolibrary.SelectedPhotoActivity;
import com.example.sanjay.selectorphotolibrary.bean.ImageBean;
import com.example.sanjay.selectorphotolibrary.bean.ImgOptions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE = 2;
    private static final String EXTRA_DATA = "extra_data";

    private TextView mResultText;
    private RadioGroup mChoiceMode, mShowCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mResultText = (TextView) findViewById(R.id.result);
        mChoiceMode = (RadioGroup) findViewById(R.id.choice_mode);
        mShowCamera = (RadioGroup) findViewById(R.id.show_camera);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedMode;
                if (mChoiceMode.getCheckedRadioButtonId() == R.id.single) {
                    selectedMode = ImgOptions.MODE_SINGLE;
                } else {
                    selectedMode = ImgOptions.MODE_MULTI;
                }
                boolean showCamera = mShowCamera.getCheckedRadioButtonId() == R.id.show;
                ImgOptions options = new ImgOptions(selectedMode, showCamera);
                startActivityForResult(SelectedPhotoActivity.makeIntent(MainActivity.this, options), REQUEST_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                ArrayList<ImageBean> imgList = data.getParcelableArrayListExtra(EXTRA_DATA);

                for (ImageBean imageBean : imgList) {
                    mResultText.append(imageBean.path);
                    mResultText.append("\n");
                }
            }
        }
    }
}