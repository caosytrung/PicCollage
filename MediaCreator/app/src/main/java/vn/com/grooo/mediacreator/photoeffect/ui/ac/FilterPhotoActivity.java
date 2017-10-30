package vn.com.grooo.mediacreator.photoeffect.ui.ac;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.andexert.library.RippleView;

import org.wysaid.common.Common;
import org.wysaid.myUtils.ImageUtil;
import org.wysaid.view.ImageGLSurfaceView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import vn.com.grooo.mediacreator.R;
import vn.com.grooo.mediacreator.common.Constants;
import vn.com.grooo.mediacreator.common.base.BaseActivity;
import vn.com.grooo.mediacreator.common.utils.Utils;
import vn.com.grooo.mediacreator.imageeffect.filter.base.BaseFilterFactory;
import vn.com.grooo.mediacreator.imageeffect.ui.ac.ChooseEffectActivity;
import vn.com.grooo.mediacreator.imageeffect.ui.ac.ImageResultFilterActivity;
import vn.com.grooo.mediacreator.imageeffect.ui.adap.RVEffectAdapter;
import vn.com.grooo.mediacreator.photoeffect.models.FilterObject;
import vn.com.grooo.mediacreator.photoeffect.ui.utils.FilterUtils;
import vn.com.grooo.mediacreator.photomaker.callback.IRVClickListener;
import vn.com.grooo.mediacreator.photomaker.ui.act.CollageActivity;

/**
 * Created by trungcs on 9/29/17.
 */

public class FilterPhotoActivity extends BaseActivity implements DialogInterface.OnDismissListener,
        View.OnClickListener {
    public static final String DEFAULT_EFFCT = BaseFilterFactory.BRIGHTNESS;
    public static final String FILTER_FOR_COLLAGE = "FILTER_FOR_COLLAGE";
    public static final String FILTER_ONLY = "FILTER_ONLY";
    private ImageGLSurfaceView ivPreview;
    private RecyclerView rvEffect;
    private TextView txtToolbarTitle;
    private Toolbar toolbar;
    private ImageView ivSave;
    private ImageView imgToolbarIcon;
    private RippleView rvToolbar;
    private List<FilterObject> filterObjectList;
    private String pathImage;
    private ProgressDialog mDialog;
    private SeekBar sbEffect;
    private String tmpEffect;
    private int tmpProgress;
    private RVEffectAdapter mAdapter;
    private String TASK;
    private Bitmap mBitmap;
    private FilterPhotoActivity.MyAsynTask myAsynTask;
    private Bitmap bitmapToCollage;
    private FirstTimtToLoadImageAsyncTask firstTimtToLoadImageAsyncTask;
    private int currentConfig = 0;


    @Override
    protected int getLayoutResources() {
        return R.layout.activity_choose_effect;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        filterObjectList = FilterUtils.getFilterObject();
        TASK = FILTER_ONLY;
        pathImage = getIntent().getStringExtra(Constants.
                SEND_FILE_PATH_TO_PHOTO_EFFECT);

        if (getIntent().getStringExtra(Constants.KIND_CHOOSE ) != null){
            TASK = FILTER_FOR_COLLAGE;
            pathImage = getIntent().getStringExtra(Constants.KIND_CHOOSE);
        }



        ivPreview = (ImageGLSurfaceView) findViewById(R.id.ivChooseEffectPreview);



        imgToolbarIcon = (ImageView) findViewById(R.id.imgToolbarIcon);
        txtToolbarTitle = (TextView) findViewById(R.id.tvToolBarTitle);
        toolbar = (Toolbar) findViewById(R.id.toolbarCustom);
        rvToolbar = (RippleView) findViewById(R.id.rvToolbar);
        ivSave = (ImageView) findViewById(R.id.imgToolbarDone);
        imgToolbarIcon.setImageDrawable(getResources().
                getDrawable(R.drawable.icon_back));
        ivSave.setImageDrawable(getResources().
                getDrawable(R.drawable.icon_checkmark));
        ivSave.setVisibility(View.VISIBLE);
        txtToolbarTitle.setText("Effects");
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Loading ...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setOnDismissListener(this);


        imgToolbarIcon.setOnClickListener(this);
        ivSave.setOnClickListener(this);
      //  toolbar.setBackgroundColor(getResources().getColor(R.color.md_white_1000));

        rvEffect = (RecyclerView) findViewById(R.id.rvEffect);
        sbEffect = (SeekBar) findViewById(R.id.sbEffectZ);
        sbEffect.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float intensity = i / 100.0f;
                Log.d("intentent",intensity + " ");
                ivPreview.setFilterIntensity(intensity);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {



        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvEffect.setItemViewCacheSize(20);
        rvEffect.setLayoutManager(manager);

//
//        if (firstTimtToLoadImageAsyncTask != null){
//            firstTimtToLoadImageAsyncTask.cancel(true);
//            firstTimtToLoadImageAsyncTask = null;
//        }
//        firstTimtToLoadImageAsyncTask = new FirstTimtToLoadImageAsyncTask();
//        firstTimtToLoadImageAsyncTask.execute();
        final Bitmap bmPreview = Utils.getBitMapFromFile(pathImage);
        ivPreview.setSurfaceCreatedCallback(new ImageGLSurfaceView.OnSurfaceCreatedCallback() {
            @Override
            public void surfaceCreated() {
                Log.d("surfaceCreated","true");
                ivPreview.setImageBitmap(bmPreview);
                ivPreview.setFilterWithConfig(FilterUtils.EFFECT_CONFIGS[currentConfig]);
            }
        });

        ivPreview.setDisplayMode(ImageGLSurfaceView.DisplayMode.DISPLAY_ASPECT_FIT);
        mAdapter = new RVEffectAdapter(FilterPhotoActivity.this);
        mAdapter.setFilterFactories(filterObjectList);
        rvEffect.setAdapter(mAdapter);

        mAdapter.setIrvClickListener(new IRVClickListener() {
            @Override
            public void onItemClick(final int pos, View v) {
                sbEffect.setProgress(60);
                ivPreview.setFilterIntensity(0.6f);
                ivPreview.post(new Runnable() {
                    @Override
                    public void run() {
                        String mCurrentConfig = FilterUtils.
                                EFFECT_CONFIGS[filterObjectList.get(pos).getOffset()];
                        ivPreview.setFilterWithConfig(mCurrentConfig);
                        currentConfig = filterObjectList.get(pos).getOffset();
                        mAdapter.setSelectPost(pos);
                        mAdapter.notifyDataSetChanged();
                        int indexScroll = pos;
                        rvEffect.setAdapter(mAdapter);
                        if (pos <= filterObjectList.size() - 3 && pos  >= 2){
                            indexScroll = pos - 2;
                        } else {
                            if (pos == 1){
                                indexScroll = pos - 1;
                            }
                        }

                        rvEffect.scrollToPosition(indexScroll);
                        rvEffect.invalidate();
                    }
                });
            }

            @Override
            public void onItemLongClick(int pos, View v) {

            }
        });


    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {


    }
    public  Bitmap getBitMapFromFile(String path,int maxWidth) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);


        ExifInterface ei = null;
        try {
            ei = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap rotatedBitmap = null;
        switch(orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap.copy(bitmap.getConfig(),bitmap.isMutable());
        }
        Log.d("vaoroi1",bitmap.isMutable() + " ");
        int width = rotatedBitmap.getWidth();
        int height = rotatedBitmap.getHeight();

        if (maxWidth > width){
            return  rotatedBitmap;
        }
        float scaleWidth = ((float) maxWidth) / width;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleWidth);
        Log.d("vaoroi2",bitmap.isMutable() + " ");

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                rotatedBitmap, 0, 0, width, height, matrix, false);
        rotatedBitmap.recycle();
        Log.d("vaoroi3",bitmap.isMutable() + " ");
        return resizedBitmap;
        // return rotatedBitmap;
    }
    public  Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap result = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
        source.recycle();
        return result;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgToolbarDone:
                mDialog.show();
//                if (myAsynTask != null ){
//                    myAsynTask = null;
//                }

                ivPreview.getResultBitmap(new ImageGLSurfaceView.QueryResultBitmapCallback() {
                    @Override
                    public void get(Bitmap bmp) {
//                        mBitmap = bmp.copy(mBitmap.getConfig(),true);
//                        myAsynTask = new FilterPhotoActivity.MyAsynTask();
//                        myAsynTask.execute();
                        String s = ImageUtil.saveBitmap(bmp);
                        Log.d("lalalaa",s);
                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                Uri.parse("file://" + s)));
                        Intent intent = new Intent(FilterPhotoActivity.this,
                                CollageActivity.class);
                        intent.putExtra("KEY_IMAGE_FOR_CHANGE", s);
                        setResult(Constants.CODE_CHANGE_IMAGE, intent);

                        FilterPhotoActivity.this.finish();
                    }
                });


                break;
            case R.id.imgToolbarIcon:
                finish();
                break;
        }
    }
    private class MyAsynTask extends
            AsyncTask<Void,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {

            String fileName = "bitmap.png";
            FileOutputStream stream = null;
            if (TASK.equals(FILTER_FOR_COLLAGE)){
                bitmapToCollage = mBitmap.copy(mBitmap.getConfig(), true);
                mBitmap .recycle();
                mBitmap = null;
                return null;

            }
            try {
                stream = FilterPhotoActivity.this.openFileOutput(fileName, Context.MODE_PRIVATE);
                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                //Cleanup
                stream.close();
                mBitmap.recycle();

                //Pop intent
//                Intent in1 = new Intent(ChooseEffectActivity.this, ImageResultActivity.class);
//                in1.putExtra("image", filename);
//                startActivity(in1);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return fileName;

        }


        @Override
        protected void onPostExecute(String fileName) {
            super.onPostExecute(fileName);

            if (TASK.equals(FILTER_ONLY)) {
                if (fileName != null) {
                    Intent in1 = new Intent(FilterPhotoActivity.this, ImageResultFilterActivity.class);
                    in1.putExtra("image", fileName);
                    startActivity(in1);
                    mDialog.dismiss();
                }
            } else {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                Date today = Calendar.getInstance().getTime();
                String reportDate = df.format(today);

                final String source = Environment.getExternalStorageDirectory().
                        getPath() +"/MediaCreator/" + reportDate + ".jpg";
                //  Log.d(TAG,source);
                File file = new File(Environment.getExternalStorageDirectory().
                        getPath() +"/MediaCreator");
                if (!file.exists()){
                    file.mkdir();
                }
                try {
                    FileOutputStream out = new FileOutputStream(source);
                    bitmapToCollage.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {

                }

                MediaScannerConnection.scanFile(FilterPhotoActivity.this,
                        new String[]{source}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                                Log.d("Sqcannnn", "scan Image ");

                                Intent intent = new Intent(FilterPhotoActivity.this,
                                        CollageActivity.class);
                                intent.putExtra("KEY_IMAGE_FOR_CHANGE", source);
                                setResult(Constants.CODE_CHANGE_IMAGE, intent);

                                FilterPhotoActivity.this.finish();
                                ivPreview.release();
                                mDialog.dismiss();
                            }
                        }

                );
            }
        }


    }
    private class FirstTimtToLoadImageAsyncTask extends AsyncTask<Void,Void,Bitmap>{

        @Override
        protected Bitmap  doInBackground(Void... voids) {
          //  final Bitmap bSource = Utils.getBitMapFromFile(pathImage);
            Bitmap bSource = Utils.getBitMapFromFile(pathImage);

            return bSource;
        }

        @Override
        protected void onPostExecute(final Bitmap bSource) {
            super.onPostExecute(bSource);

            Log.d("surfaceCreated",bSource.getWidth() + "" + bSource.getHeight());
            float xScale = 1;
            if (bSource.getWidth() > 1024){
                xScale = 1024/ bSource.getWidth();
            }

//            //create scaled bitmap using Matrix
//            Matrix matrix = new Matrix();
//            matrix.postScale(xScale, xScale);
//
//            Bitmap bitmapScaled = Bitmap.createBitmap(
//                    bSource,
//                    0, 0,
//                    1080, 1440,
//                    matrix, true);




            Log.d("surfaceCreated","true2");
//            ivPreview.setSurfaceCreatedCallback(new ImageGLSurfaceView.OnSurfaceCreatedCallback() {
//                @Override
//                public void surfaceCreated() {
//                    Log.d("surfaceCreated","true");
//                    ivPreview.setImageBitmap(bSource);
//                    ivPreview.setFilterWithConfig(FilterUtils.EFFECT_CONFIGS[0]);
//                }
//            });
//
//            ivPreview.setDisplayMode(ImageGLSurfaceView.DisplayMode.DISPLAY_ASPECT_FIT);

//            ivPreview.setSurfaceCreatedCallback(new ImageGLSurfaceView.OnSurfaceCreatedCallback() {
//                @Override
//                public void surfaceCreated() {
//                    ivPreview.setImageBitmap(mBitmapSource);
//                    Log.d("surfaceCreated","true");
//                    ivPreview.setFilterWithConfig(FilterUtils.EFFECT_CONFIGS[18]);
//                }
//            });
            mAdapter = new RVEffectAdapter(FilterPhotoActivity.this);
            mAdapter.setFilterFactories(filterObjectList);
            rvEffect.setAdapter(mAdapter);

            mAdapter.setIrvClickListener(new IRVClickListener() {
                @Override
                public void onItemClick(final int pos, View v) {
                    sbEffect.setProgress(60);
                    ivPreview.setFilterIntensity(0.6f);
                    ivPreview.post(new Runnable() {
                        @Override
                        public void run() {
                            String mCurrentConfig = FilterUtils.
                                    EFFECT_CONFIGS[filterObjectList.get(pos).getOffset()];
                            ivPreview.setFilterWithConfig(mCurrentConfig);
                            mAdapter.setSelectPost(pos);
                            mAdapter.notifyDataSetChanged();
                            int indexScroll = pos;
                            rvEffect.setAdapter(mAdapter);
                            if (pos <= filterObjectList.size() - 3 && pos  >= 2){
                               indexScroll = pos - 2;
                            } else {
                                if (pos == 1){
                                    indexScroll = pos - 1;
                                }
                            }

                            rvEffect.scrollToPosition(indexScroll);
                            rvEffect.invalidate();
                        }
                    });
                }

                @Override
                public void onItemLongClick(int pos, View v) {

                }
            });
        }
    }
    @Override
    public void onPause() {
        Log.i(Common.LOG_TAG, "Filter Demo onPause...");
        super.onPause();
        ivPreview.release();
        ivPreview.onPause();
    }

    @Override
    public void onResume() {
        Log.i(Common.LOG_TAG, "Filter Demo onResume...");
        super.onResume();
        ivPreview.onResume();
    }
}
