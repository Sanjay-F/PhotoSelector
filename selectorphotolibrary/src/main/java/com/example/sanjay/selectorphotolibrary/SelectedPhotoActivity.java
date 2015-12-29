package com.example.sanjay.selectorphotolibrary;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sanjay.selectorphotolibrary.adapter.FolderAdapter;
import com.example.sanjay.selectorphotolibrary.adapter.ImageListAdapter;
import com.example.sanjay.selectorphotolibrary.bean.ImageBean;
import com.example.sanjay.selectorphotolibrary.bean.ImageFolder;
import com.example.sanjay.selectorphotolibrary.bean.ImgOptions;
import com.example.sanjay.selectorphotolibrary.utils.FileUtils;
import com.example.sanjay.selectorphotolibrary.utils.ScreenUtil;
import com.example.sanjay.selectorphotolibrary.utils.TimeUtils;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SelectedPhotoActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, PopupWindow.OnDismissListener, ImageListAdapter.onImageClickListener {


    private static final String TAG = SelectedPhotoActivity.class.getSimpleName();
    private static final String EXTRA_DATA = "extra_data";
    private Context mContext = this;
    private static final int LOADER_ALL = 0;
    private static final int LOADER_CATEGORY = 1;
    private static final int REQUEST_CAMERA = 100;
    private static final int REQUEST_PREVIEW = 101;
    private ArrayList<ImageFolder> mResultFolder = new ArrayList<>();
    private GridView mGridView;
    private ImageListAdapter mImageAdapter;

    private ListPopupWindow mFolderPopupWindow;
    private TextView mTimeLineText;
    private TextView mCategoryText;
    private Button mPreviewBtn;
    private View mPopupAnchorView;

    private boolean hasFolderGened = false;
    private File mTmpFile;
    private FolderAdapter mFolderAdapter;
    private View maskView;
    private ImgOptions imgOptions;
    private TextView confirmBtn;

    public static Intent makeIntent(Context mContext, ImgOptions imgOptions) {
        return new Intent(mContext, SelectedPhotoActivity.class).putExtra(EXTRA_DATA, imgOptions);
    }

    public static Intent makeResult(Context mContext, ArrayList<ImageBean> data) {
        return new Intent(mContext, SelectedPhotoActivity.class).putExtra(EXTRA_DATA, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_photo);
        imgOptions = getIntent().getParcelableExtra(EXTRA_DATA);
        initImageLoaderOption();
        findView();
        setActionBar();
        initView();


        getSupportLoaderManager().initLoader(LOADER_ALL, null, mLoaderCallback);
    }

    private void initImageLoaderOption() {

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPoolSize(3) // default 3
                .threadPriority(Thread.NORM_PRIORITY - 1) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(3 * 1024 * 1024))
                .memoryCacheSize(3 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100).defaultDisplayImageOptions(new DisplayImageOptions.Builder()
                        .showImageOnLoading(R.drawable.default_error)
                        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                        .cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build())

                .build();
        ImageLoader.getInstance().init(config);

    }

    private void setActionBar() {

        findViewById(R.id.ab_back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();

            }
        });

        ((TextView) findViewById(R.id.ab_title_tv)).setText(R.string.selected_pic);

        confirmBtn = (TextView) findViewById(R.id.ab_confirm_tv);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOkResult(mImageAdapter.getSelectedImages());
            }
        });
    }

    private void findView() {


        maskView = findViewById(R.id.catalog_mask_view);
        mPopupAnchorView = findViewById(R.id.footer);
        mTimeLineText = (TextView) findViewById(R.id.timeline_area);
        mTimeLineText.setVisibility(View.GONE);
        mCategoryText = (TextView) findViewById(R.id.category_btn);
        mCategoryText.setText(R.string.folder_all);
        mPreviewBtn = (Button) findViewById(R.id.preview);
        mGridView = (GridView) findViewById(R.id.grid);

    }

    private void initView() {


        mImageAdapter = new ImageListAdapter(this, imgOptions);
        mImageAdapter.setOnImageClickListener(this);
        mGridView.setOnScrollListener(onScrollListener);
        mGridView.setAdapter(mImageAdapter);
        mGridView.setOnItemClickListener(this);

        mFolderAdapter = new FolderAdapter(mContext);

        mCategoryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFolderPopupWindow == null) {
                    createPopupFolderList();
                }
                if (mFolderPopupWindow.isShowing()) {
                    maskView.setVisibility(View.INVISIBLE);
                    maskView.startAnimation(AnimationUtils
                            .loadAnimation(getApplicationContext(),
                                    R.anim.alpha_to_zero));
                    mFolderPopupWindow.dismiss();
                } else {
                    maskView.setVisibility(View.VISIBLE);
                    maskView.startAnimation(AnimationUtils
                            .loadAnimation(getApplicationContext(),
                                    R.anim.alpha_to_one));
                    mFolderPopupWindow.show();

                    //监听大小，如果界面过大则调小
                    mFolderPopupWindow.getListView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onGlobalLayout() {
                            int height = mFolderPopupWindow.getListView().getHeight();
                            int scrHeight = ScreenUtil.getHeightPixels(SelectedPhotoActivity.this);
                            int limiteHeigt = height + 4 * ScreenUtil.getActionBarHeight(mContext);

                            Log.e(TAG, "onGlobalLayout: height=" + height + " limit height=" + limiteHeigt);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                mFolderPopupWindow.getListView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            } else {
                                mFolderPopupWindow.getListView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            }
                            if (limiteHeigt > scrHeight) {
                                Log.e(TAG, "onGlobalLayout:  update view");
                                mFolderPopupWindow.setHeight(5 * scrHeight / 8);
                                mFolderPopupWindow.getListView().getLayoutParams().height = 5 * scrHeight / 8;
                                mFolderPopupWindow.show();
                            }
                        }
                    });

                    int index = mFolderAdapter.getSelectIndex();
                    index = index == 0 ? index : index - 1;
                    mFolderPopupWindow.getListView().setSelection(index);

                }
            }
        });
    }

    private void createPopupFolderList() {

        mFolderPopupWindow = new ListPopupWindow(mContext);
        mFolderPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        mFolderPopupWindow.setAdapter(mFolderAdapter);
        mFolderPopupWindow.setOnDismissListener(this);
        int width = ScreenUtil.getWidthPixels(this);
        mFolderPopupWindow.setContentWidth(width);
        mFolderPopupWindow.setWidth(width);
        mFolderPopupWindow.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        mFolderPopupWindow.setAnchorView(mPopupAnchorView);

        mFolderPopupWindow.setModal(true);
        mFolderPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, final int position, long l) {
                mFolderAdapter.setSelectIndex(position);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mFolderPopupWindow.dismiss();
                        if (position == 0) {
                            getSupportLoaderManager().restartLoader(LOADER_ALL, null, mLoaderCallback);
                            mCategoryText.setText(R.string.folder_all);
                            mImageAdapter.setInSubCatalog(false);
                        } else {
                            ImageFolder folder = (ImageFolder) adapterView.getItemAtPosition(position);
                            if (null != folder) {
                                mImageAdapter.setData(folder.images);
                                mCategoryText.setText(folder.name);
                            }
                            mImageAdapter.setInSubCatalog(true);
                        }
                        // 滑动到最初始位置
                        mGridView.smoothScrollToPosition(0);
                    }
                }, 100);

            }
        });
    }

    AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView absListView, int state) {

//            final Picasso picasso = Picasso.with(mContext);
//            if (state == SCROLL_STATE_IDLE || state == SCROLL_STATE_TOUCH_SCROLL) {
//                picasso.resumeTag(mContext);
//            } else {
//                picasso.pauseTag(mContext);
//            }

            if (state == SCROLL_STATE_IDLE) {
                // 停止滑动，日期指示器消失
                mTimeLineText.setVisibility(View.GONE);

                mTimeLineText.startAnimation(AnimationUtils
                        .loadAnimation(getApplicationContext(),
                                R.anim.alpha_to_zero));

            } else if (state == SCROLL_STATE_FLING) {
                mTimeLineText.setVisibility(View.VISIBLE);
                mTimeLineText.startAnimation(AnimationUtils
                        .loadAnimation(getApplicationContext(),
                                R.anim.alpha_to_one));
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (mTimeLineText.getVisibility() == View.VISIBLE) {
                int index = firstVisibleItem + 1 == view.getAdapter().getCount() ? view.getAdapter().getCount() - 1 : firstVisibleItem + 1;
                ImageBean imageBean = (ImageBean) view.getAdapter().getItem(index);
                if (imageBean != null) {
                    mTimeLineText.setText(TimeUtils.getTimeStatus(imageBean.modifyTime));
                }
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mImageAdapter.isShowCamera() && position == 0) {
            showCameraAction();
        } else {
            ImageBean imageBean = (ImageBean) parent.getItemAtPosition(position);
        }
    }

    private void showCameraAction() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            // 设置系统相机拍照后的输出路径
            // 创建临时文件
            mTmpFile = FileUtils.createTmpFile(mContext);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        } else {
            Toast.makeText(mContext, R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
        }
    }

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.DATE_MODIFIED,
                MediaStore.Images.Media._ID};

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == LOADER_ALL) {
                return new CursorLoader(mContext,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        null, null, IMAGE_PROJECTION[2] + " DESC");
            } else if (id == LOADER_CATEGORY) {
                return new CursorLoader(mContext,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        IMAGE_PROJECTION[0] + " like '%" + args.getString("path") + "%'", null, IMAGE_PROJECTION[2] + " DESC");
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                List<ImageBean> imageBeanList = new ArrayList<>();
                int count = data.getCount();
                if (count > 0) {
                    while (data.moveToNext()) {
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        long modifyDateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        ImageBean imageBean = new ImageBean(path, name, dateTime, modifyDateTime);
                        imageBeanList.add(imageBean);
                        if (!hasFolderGened) {
                            // 获取文件夹名称
                            File imageFile = new File(path);
                            File parentFolder = imageFile.getParentFile();

                            ImageFolder folder = new ImageFolder();
                            folder.name = parentFolder.getName();
                            folder.path = parentFolder.getAbsolutePath();
                            folder.cover = imageBean;

                            if (!mResultFolder.contains(folder)) {
                                List<ImageBean> imageList = new ArrayList<>();
                                imageList.add(imageBean);
                                folder.images = imageList;
                                mResultFolder.add(folder);
                            } else {
                                ImageFolder exitFolder = mResultFolder.get(mResultFolder.indexOf(folder));
                                exitFolder.images.add(imageBean);//新的图片地址加到文件夹图片列表里面去
                            }
                        }
                    }
                    mImageAdapter.setData(imageBeanList);
                    // 设定默认选择
//                    if (resultList != null && resultList.size() > 0) {
////                        mImageAdapter.setDefaultSelected(resultList);
//                    }
                    mFolderAdapter.setData(mResultFolder);
                    hasFolderGened = true;
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
        }
    };

    @Override
    public void onDismiss() {

        maskView.setVisibility(View.INVISIBLE);
        maskView.startAnimation(AnimationUtils
                .loadAnimation(getApplicationContext(),
                        R.anim.alpha_to_zero));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 相机拍照完成后，返回图片路径
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                if (mTmpFile != null) {
                    Log.e(TAG, " dist=" + mTmpFile.getAbsolutePath());
                }
            } else {
                if (mTmpFile != null && mTmpFile.exists()) {
                    mTmpFile.delete();
                }
            }
        } else if (requestCode == REQUEST_PREVIEW) {
            if (resultCode == RESULT_OK) {
                Log.e(TAG, "result ok ");
                setOkResult(data.<ImageBean>getParcelableArrayListExtra(EXTRA_DATA));
            }
        }
    }


    private void setOkResult(ArrayList<ImageBean> datas) {
        ArrayList<String> pathDataList = new ArrayList<>();
        for (ImageBean imageBean : datas) {
            pathDataList.add(imageBean.path);
        }
        setResult(RESULT_OK, new Intent().putExtra(EXTRA_DATA, pathDataList));
        finish();
    }

    @Override
    public void onImageClick(int curSelectedCount, int total, int position) {
        mPreviewBtn.setEnabled(curSelectedCount > 0);
        mPreviewBtn.setClickable(curSelectedCount > 0);
        mPreviewBtn.setText(String.format(getString(R.string.preview_format), curSelectedCount));

        confirmBtn.setEnabled(curSelectedCount > 0);
        if (curSelectedCount != 0) {
            confirmBtn.setText(String.format(getString(R.string.confirm_format), curSelectedCount, total));
        } else {
            confirmBtn.setText(getString(R.string.confirm));
        }


    }

    public void onPreviewClick(View view) {
        startActivityForResult(PreviewActivity.makeIntent(this, mImageAdapter.getSelectedImages(), imgOptions.getSelectedCount()), REQUEST_PREVIEW);
    }

}
