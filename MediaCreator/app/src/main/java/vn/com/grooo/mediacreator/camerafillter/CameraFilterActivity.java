package vn.com.grooo.mediacreator.camerafillter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import org.wysaid.camera.CameraInstance;
import org.wysaid.myUtils.ImageUtil;
import org.wysaid.view.CameraGLSurfaceView;
import org.wysaid.view.CameraRecordGLSurfaceView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import vn.com.grooo.mediacreator.R;
import vn.com.grooo.mediacreator.common.base.BaseActivity;
import vn.com.grooo.mediacreator.imageeffect.ui.ac.ImageResultFilterActivity;
import vn.com.grooo.mediacreator.imageeffect.ui.adap.RVEffectAdapter;
import vn.com.grooo.mediacreator.photoeffect.models.FilterObject;
import vn.com.grooo.mediacreator.photoeffect.ui.ac.FilterPhotoActivity;
import vn.com.grooo.mediacreator.photoeffect.ui.utils.FilterUtils;
import vn.com.grooo.mediacreator.photomaker.callback.IRVClickListener;

/**
 * Created by trungcs on 10/2/17.
 */

public class CameraFilterActivity extends BaseActivity implements
            CameraGLSurfaceView.OnCreateCallback,
        IRVClickListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private RecyclerView rvCameraFillter;
    private CameraRecordGLSurfaceView mCameraView;
    private RVEffectAdapter mAdapter;
    private List<FilterObject> filterObjectList;
    private ImageView ivSwitchCamera;
    private ImageView ivBackToMain;
    private SeekBar sbEffect;
    private ImageView ivTakeImages;
    private String mCurrentConfig;

    @Override
    protected int getLayoutResources() {
        return R.layout.activity_camera_filter;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        mCurrentConfig = FilterUtils.EFFECT_CONFIGS[0];
        ivSwitchCamera = (ImageView) findViewById(R.id.ivSwitchCamera);
        mCameraView = (CameraRecordGLSurfaceView) findViewById(R.id.cmrPreview);
        ivBackToMain = (ImageView) findViewById(R.id.ivBackToMain);
        ivTakeImages = (ImageView) findViewById(R.id.ivTakeCamera);

        sbEffect = (SeekBar) findViewById(R.id.sbEffectZ);

        mCameraView.presetCameraForward(false);
        mCameraView.presetRecordingSize(480, 640);
        mCameraView.setPictureSize(1024, 1024, true);
        mCameraView.setZOrderOnTop(false);
        mCameraView.setZOrderMediaOverlay(true);

        mCameraView.setOnCreateCallback(this);

        mAdapter = new RVEffectAdapter(this);
        mAdapter.setIrvClickListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvCameraFillter = (RecyclerView) findViewById(R.id.rvFilterCamera);
        rvCameraFillter.setLayoutManager(manager);

        filterObjectList = FilterUtils.getFilterObject();
        mAdapter.setFilterFactories(filterObjectList);
        rvCameraFillter.setItemViewCacheSize(20);
        rvCameraFillter.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        ivSwitchCamera.setOnClickListener(this);
        ivBackToMain.setOnClickListener(this);
        sbEffect.setOnSeekBarChangeListener(this);
        ivTakeImages.setOnClickListener(this);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    public void createOver(boolean success) {
        if (!success){
            Toast.makeText(this,"Open camera failed !!",Toast.LENGTH_LONG).show();
            finish();
        }

    }

    @Override
    public void onItemClick(int pos, View v) {
        sbEffect.setProgress(60);
        mAdapter.setSelectPost(pos);
        mAdapter.notifyDataSetChanged();
        int indexScroll = pos;
        rvCameraFillter.setAdapter(mAdapter);
        if (pos <= filterObjectList.size() - 3 && pos  >= 2){
            indexScroll = pos - 2;
        } else {
            if (pos == 1){
                indexScroll = pos - 1;
            }
        }

        rvCameraFillter.scrollToPosition(indexScroll);
        rvCameraFillter.invalidate();


        mCurrentConfig = FilterUtils.
                EFFECT_CONFIGS[filterObjectList.get(pos).getOffset()];
        mCameraView.setFilterWithConfig(FilterUtils.
                EFFECT_CONFIGS[filterObjectList.get(pos).getOffset()]);
    }

    @Override
    public void onItemLongClick(int pos, View v) {

    }
    @Override
    public void onResume() {
        super.onResume();

        mCameraView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        CameraInstance.getInstance().stopCamera();
        mCameraView.release(null);
        mCameraView.onPause();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivSwitchCamera:
                mCameraView.switchCamera();
                break;
            case R.id.ivBackToMain:
                CameraInstance.getInstance().stopCamera();
                mCameraView.release(null);
                finish();

                break;
            case R.id.ivTakeCamera:
                Log.d("captureeee","ru");
                mCameraView.takePicture(new CameraRecordGLSurfaceView.TakePictureCallback() {
                    @Override
                    public void takePictureOK(Bitmap bmp) {
                        if (bmp != null) {
//                            String fileName = "bitmap.png";
//                            FileOutputStream stream = null;
//                            try {
//                                stream = CameraFilterActivity.this.
//                                        openFileOutput(fileName, Context.MODE_PRIVATE);
//                                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                                //Cleanup
//                                stream.close();
//                                bmp.recycle();
//                            } catch (FileNotFoundException e) {
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
                            String s = ImageUtil.saveBitmap(bmp);
                            Log.d("lalalaa",s);
                            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                    Uri.parse("file://" + s)));

                            Log.d("captureeee","ruuuuu");
                          //  CameraFilterActivity.this.finish();

                            Intent in1 = new Intent();
                            in1.putExtra("URI_CAMERA", s);
                            setResult(RESULT_OK,in1);
                            CameraFilterActivity.this.finish();
//                            String s = ImageUtil.saveBitmap(bmp);
//                            bmp.recycle();
//
//                            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + s)));
                        }
//                            showText("Take picture failed!");
                    }
                }, null, mCurrentConfig, 1.0f, true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        float intensity = i / 100.0f;
        mCameraView.setFilterIntensity(intensity);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
