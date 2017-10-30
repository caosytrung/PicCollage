package vn.com.grooo.mediacreator.imageeffect.ui.ac;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
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
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import vn.com.grooo.mediacreator.MainActivity;
import vn.com.grooo.mediacreator.R;
import vn.com.grooo.mediacreator.common.Constants;
import vn.com.grooo.mediacreator.common.base.BaseActivity;
import vn.com.grooo.mediacreator.common.base.BaseFilter;

import vn.com.grooo.mediacreator.common.utils.Utils;
import vn.com.grooo.mediacreator.imageeffect.filter.BrightnessFilter;
import vn.com.grooo.mediacreator.imageeffect.filter.ColorDepth;
import vn.com.grooo.mediacreator.imageeffect.filter.ColorFilter;
import vn.com.grooo.mediacreator.imageeffect.filter.ContractFilter;
import vn.com.grooo.mediacreator.imageeffect.filter.GammaFilter;
import vn.com.grooo.mediacreator.imageeffect.filter.GreyScaleFilter;
import vn.com.grooo.mediacreator.imageeffect.filter.base.BaseFilterFactory;
import vn.com.grooo.mediacreator.imageeffect.ui.adap.RVEffectAdapter;
import vn.com.grooo.mediacreator.photomaker.callback.IRVClickListener;
import vn.com.grooo.mediacreator.photomaker.service.DownloadImageService;
import vn.com.grooo.mediacreator.photomaker.ui.act.CollageActivity;
import vn.com.grooo.mediacreator.photomaker.ui.act.ImageResultActivity;

/**
 * Created by trungcs on 9/6/17.
 */

public class ChooseEffectActivity extends BaseActivity implements View.OnClickListener,
        IRVClickListener, SeekBar.OnSeekBarChangeListener, DialogInterface.OnDismissListener {
    public static final String DEFAULT_EFFCT = BaseFilterFactory.BRIGHTNESS;
    public static final String FILTER_FOR_COLLAGE = "FILTER_FOR_COLLAGE";
    public static final String FILTER_ONLY = "FILTER_ONLY";
    private ImageView ivPreview;
    private RecyclerView rvEffect;
    private TextView txtToolbarTitle;
    private Toolbar toolbar;
    private RippleView rvToolbar;
    private ImageView ivSave;
    private ImageView imgToolbarIcon;
    private RVEffectAdapter mAdapter;
   // private List<BaseFilter> baseFilters;
    private String pathImage;
    private Bitmap mBitmap;
    private List<String> filterList;
    private SeekBar sbEffect;
    private String tmpEffect;
    private int tmpProgress;
    private ProgressDialog mDialog;
    private MyAsynTask myAsynTask;
    private String fileName;
    private String TASK;
    private Bitmap bitmapToCollage;

    @Override
    protected int getLayoutResources() {
        return R.layout.activity_choose_effect;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        TASK = FILTER_ONLY;
        pathImage = getIntent().getStringExtra(Constants.
                SEND_FILE_PATH_TO_PHOTO_EFFECT);
        if (getIntent().getStringExtra(Constants.KIND_CHOOSE ) != null){
            TASK = FILTER_FOR_COLLAGE;
            pathImage = getIntent().getStringExtra(Constants.KIND_CHOOSE);
;       }


      //  ivPreview = (ImageView) findViewById(R.id.ivChooseEffectPreview);
        rvEffect = (RecyclerView) findViewById(R.id.rvEffect);
        imgToolbarIcon = (ImageView) findViewById(R.id.imgToolbarIcon);
        txtToolbarTitle = (TextView) findViewById(R.id.tvToolBarTitle);
        toolbar = (Toolbar) findViewById(R.id.toolbarCustom);
        rvToolbar = (RippleView) findViewById(R.id.rvToolbar);
        ivSave = (ImageView) findViewById(R.id.imgToolbarDone);
        imgToolbarIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
        ivSave.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_black_24dp));
        ivSave.setVisibility(View.VISIBLE);
        txtToolbarTitle.setText("Photo Effect");
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Loading ...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setOnDismissListener(this);


        imgToolbarIcon.setOnClickListener(this);
        ivSave.setOnClickListener(this);
        toolbar.setBackgroundColor(getResources().getColor(R.color.md_white_1000));

        ivPreview.setAdjustViewBounds(true);
        Glide.with(this).load(new File(pathImage)).into(ivPreview);

        sbEffect = (SeekBar) findViewById(R.id.sbEffectZ);
        sbEffect.setOnSeekBarChangeListener(this);
        tmpEffect= DEFAULT_EFFCT;
        ivSave.setOnClickListener(this);


    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        Bitmap bSource = Utils.getBitMapFromFile(pathImage);
        Bitmap mBitmapSource = Utils.getResizedBitmap(bSource,160,160);
        int[] intArray = new int[mBitmapSource.getWidth()*mBitmapSource.getHeight()];
        mBitmapSource.getPixels(intArray, 0, mBitmapSource.getWidth(),
                0, 0, mBitmapSource.getWidth(), mBitmapSource.getHeight());

        bSource.recycle();
        bSource = null;;
        bSource = Utils.getBitMapFromFile(pathImage);
        mBitmap = Utils.getResizedBitmap(bSource,512,512);
        //baseFilters = FilterUtils.getListBlurFilter(mBitmap);
        filterList = BaseFilterFactory.getFilterList();
//        mAdapter = new RVEffectAdapter(this,intArray,mBitmapSource);
//        mAdapter.setFilterFactories(filterList);
        mAdapter.setIrvClickListener(this);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvEffect.setLayoutManager(manager);
        rvEffect.setHasFixedSize(true);
        rvEffect.setItemViewCacheSize(20);
        rvEffect.setDrawingCacheEnabled(true);
        rvEffect.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        rvEffect.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgToolbarDone:
                mDialog.show();
                if (myAsynTask != null ){
                    myAsynTask = null;
                }

                myAsynTask = new MyAsynTask();
                myAsynTask.execute();
                break;
            case R.id.imgToolbarIcon:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(int pos, View v) {
       Bitmap result = null;
//        switch (baseFilters.get(pos).getType()){
//            case 1:
//                FilterFactory filterFactory = (FilterFactory) baseFilters.get(pos).getFilterObject();
//                int width = mBitmap.getWidth();
//                int height = mBitmap.getHeight();
//                int[] pixels = new int[width * height];
//                mBitmap.getPixels(pixels, 0,width, 0, 0, width, height);
//                tmp = filterFactory.filterImage(pixels,width,height);
//                ivPreview.setImageBitmap(tmp);
//                break;
//            case 2:
//                ImageFactory imageFactory = (ImageFactory) baseFilters.get(pos).getFilterObject();
//                tmp = imageFactory.filterImage(mBitmap);
//                ivPreview.setImageBitmap(tmp);
//
//                break;
//            default:
//                break;
//        }
//        Bitmap b = factoryList.get(pos).filterImage(mBitmap);
//        ivPreview.setImageBitmap(b);
        Bitmap tmp = mBitmap.copy(mBitmap.getConfig(),true);
        tmpEffect = filterList.get(pos);
        switch (filterList.get(pos)){
            case BaseFilterFactory.ORIGINAL:
                sbEffect.setVisibility(View.GONE);
                ivPreview.setImageBitmap(tmp);
                break;

            case BaseFilterFactory.BRIGHTNESS:
                BaseFilterFactory.brightness(tmp,80);
                ivPreview.setImageBitmap(tmp);
                sbEffect.setVisibility(View.VISIBLE);
                sbEffect.setMin(-100);
                sbEffect.setMax(100);
                sbEffect.setProgress(80);
                tmpProgress = 80;
                break;

            case BaseFilterFactory.CONTRACT:
                sbEffect.setVisibility(View.VISIBLE);
                BaseFilterFactory.contrast(tmp,64);
                ivPreview.setImageBitmap(tmp);
                sbEffect.setMin(0);
                sbEffect.setMax(100);
                sbEffect.setProgress(64);
                tmpProgress = 64;
                break;
            case BaseFilterFactory.INVERT:
                sbEffect.setVisibility(View.GONE);
                BaseFilterFactory.invert(tmp);
                ivPreview.setImageBitmap(tmp);
                break;
            case BaseFilterFactory.SNOW:
                BaseFilterFactory.snow(tmp , 128);
                ivPreview.setImageBitmap(tmp);
                sbEffect.setVisibility(View.VISIBLE);
                sbEffect.setMin(8);
                sbEffect.setMax(200);
                sbEffect.setProgress(128);
                tmpProgress = 128;

                break;
            case BaseFilterFactory.SEPIA_RED_BLUE:
                sbEffect.setVisibility(View.VISIBLE);
                BaseFilterFactory.sepiaRedGreen(tmp , 200);
                ivPreview.setImageBitmap(tmp);
                sbEffect.setMin(8);
                sbEffect.setMax(256);
                sbEffect.setProgress(200);
                tmpProgress = 200;
                break;
            case BaseFilterFactory.SEPIA_RED_MAJ:
                sbEffect.setVisibility(View.VISIBLE);
                BaseFilterFactory.sepiaRedMaj(tmp , 200);
                ivPreview.setImageBitmap(tmp);
                sbEffect.setMin(8);
                sbEffect.setMax(256);
                sbEffect.setProgress(200);
                tmpProgress = 200;
                break;
            case BaseFilterFactory.GREEN:
                sbEffect.setVisibility(View.VISIBLE);
                BaseFilterFactory.sepiaGreen(tmp , 24);
                ivPreview.setImageBitmap(tmp);
                sbEffect.setMin(1);
                sbEffect.setMax(256);
                sbEffect.setProgress(24);
                tmpProgress = 24;
                break;
            default:

                break;

        }

    }

    @Override
    public void onItemLongClick(int pos, View v) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        tmpProgress = i;
        Bitmap tmp = mBitmap.copy(mBitmap.getConfig(),true);
        switch (tmpEffect){
            case BaseFilterFactory.BRIGHTNESS:
                BaseFilterFactory.brightness(tmp,i);
                ivPreview.setImageBitmap(tmp);
                sbEffect.setVisibility(View.VISIBLE);
                break;

            case BaseFilterFactory.CONTRACT:
                BaseFilterFactory.contrast(tmp,i);
                ivPreview.setImageBitmap(tmp);
            case BaseFilterFactory.SNOW:
                BaseFilterFactory.snow(tmp,i);
                ivPreview.setImageBitmap(tmp);
                break;
            case BaseFilterFactory.SEPIA_RED_BLUE:
                BaseFilterFactory.sepiaRedGreen(tmp,i);
                ivPreview.setImageBitmap(tmp);
                break;
            case BaseFilterFactory.SEPIA_RED_MAJ:
                BaseFilterFactory.sepiaRedMaj(tmp,i);
                ivPreview.setImageBitmap(tmp);
                break;
            case BaseFilterFactory.GREEN:
                BaseFilterFactory.sepiaGreen(tmp,i);
                ivPreview.setImageBitmap(tmp);
                break;
            default:
                break;

        }
    }


    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {

    }

    private class MyAsynTask extends
            AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... voids) {
            Bitmap bitmap = Utils.getBitMapFromFile(pathImage);

            switch (tmpEffect) {
                case BaseFilterFactory.BRIGHTNESS:
                    BaseFilterFactory.brightness(bitmap, tmpProgress);
                    break;
                case BaseFilterFactory.CONTRACT:
                    BaseFilterFactory.contrast(bitmap, tmpProgress);
                case BaseFilterFactory.SNOW:
                    BaseFilterFactory.snow(bitmap, tmpProgress);
                    break;
                case BaseFilterFactory.SEPIA_RED_BLUE:
                    BaseFilterFactory.sepiaRedGreen(bitmap, tmpProgress);
                    break;
                case BaseFilterFactory.SEPIA_RED_MAJ:
                    BaseFilterFactory.sepiaRedMaj(bitmap, tmpProgress);
                    break;
                case BaseFilterFactory.INVERT:
                    BaseFilterFactory.invert(bitmap);
                    break;
                case BaseFilterFactory.GREEN:
                    BaseFilterFactory.sepiaGreen(bitmap, tmpProgress);
                    break;
                default:
                    break;
            }
            fileName = "bitmap.png";
            FileOutputStream stream = null;
            if (TASK.equals(FILTER_FOR_COLLAGE)){
                bitmapToCollage = bitmap.copy(bitmap.getConfig(), true);
                bitmap .recycle();
                bitmap = null;
                return null;

            }
            try {
                stream = ChooseEffectActivity.this.openFileOutput(fileName, Context.MODE_PRIVATE);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                //Cleanup
                stream.close();
                bitmap.recycle();

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
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String bitmap) {
            super.onPostExecute(bitmap);

            if (TASK.equals(FILTER_ONLY)){
                if(fileName!= null){
                    Intent in1 = new Intent(ChooseEffectActivity.this, ImageResultFilterActivity.class);
                    in1.putExtra("image", fileName);
                    startActivity(in1);
                    mDialog.dismiss();
                }
            } else {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                Date today = Calendar.getInstance().getTime();
                String reportDate = df.format(today);

                final String source = Environment.getExternalStorageDirectory().
                        getPath() +"/Pictures/" + reportDate + ".jpg";
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
                MediaScannerConnection.scanFile(ChooseEffectActivity.this,
                        new String[] { source }, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                                Log.d("Sqcannnn","scan Image ");

                                Intent intent = new Intent(ChooseEffectActivity.this,
                                        CollageActivity.class);
                                intent.putExtra("KEY_IMAGE_FOR_CHANGE",source);
                                setResult(Constants.CODE_CHANGE_IMAGE,intent);
                                ChooseEffectActivity.this.finish();
                                mDialog.dismiss();
                            }
                        }

                );
            }
        }
    }
}
