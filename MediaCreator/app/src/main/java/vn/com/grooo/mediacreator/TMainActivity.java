package vn.com.grooo.mediacreator;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.example.stevenyang.snowfalling.SnowFlakesLayout;
import com.licrafter.snowlayout.library.SnowLayout;

import vn.com.grooo.mediacreator.camerafillter.CameraFilterActivity;
import vn.com.grooo.mediacreator.common.Constants;
import vn.com.grooo.mediacreator.common.base.BaseActivity;
import vn.com.grooo.mediacreator.common.utils.UtilPermission;
import vn.com.grooo.mediacreator.photomaker.ui.act.PickImageMakerActivity;
import vn.com.grooo.mediacreator.saveandroidloadproject.ui.act.ProjectListActivity;

/**
 * Created by trungcs on 9/27/17.
 */

public class TMainActivity extends BaseActivity {
    private CardView cvCollage;
    private CardView cvProject;
    private TextView tvAbout;
//    private CardView cvFilter;
//    private CardView cvCamera;

    private TextView tvAppName;
    private SnowFlakesLayout snowFlakesLayout;
    private int state;


    @Override
    protected int getLayoutResources() {
        return R.layout.activity_t_main;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {

        tvAbout = (TextView) findViewById(R.id.tvNextToAblout);

        tvAppName = (TextView) findViewById(R.id.tvAppName);
        snowFlakesLayout = (SnowFlakesLayout) findViewById(R.id.snLMain);
        snowFlakesLayout.init();
        snowFlakesLayout.setWholeAnimateTiming(3000000);
        snowFlakesLayout.setAnimateDuration(10000);
        snowFlakesLayout.setGenerateSnowTiming(300);
        snowFlakesLayout.setRandomSnowSizeRange(40, 1);
        snowFlakesLayout.setImageResourceID(R.drawable.ic_snow5);
        snowFlakesLayout.setEnableRandomCurving(true);
        snowFlakesLayout.setEnableAlphaFade(true);
        snowFlakesLayout.startSnowing();

        cvCollage = (CardView) findViewById(R.id.cvCollage);
        cvProject = (CardView) findViewById(R.id.cvProject);
//        cvFilter = (CardView) findViewById(R.id.cvFilter);
//        cvCamera = (CardView) findViewById(R.id.cvCamera);

        cvCollage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                state = 1;
                if (UtilPermission.checkPermissions(TMainActivity.this,1)){
                    Intent intent = new Intent(TMainActivity.this, PickImageMakerActivity.class);
                    intent.putExtra(Constants.KIND_CHOOSE, Constants.FRAME_IMAGE_CHOOSE);
                    startActivity(intent);
                }


//                Intent intent = new Intent(TMainActivity.this, PickImageMakerActivity.class);
//                intent.putExtra(Constants.KIND_CHOOSE, Constants.FRAME_IMAGE_CHOOSE);
//                startActivity(intent);
            }
        });

        cvProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state = 2;
                if (UtilPermission.checkPermissions(TMainActivity.this,1)){
                    startActivity(new Intent(TMainActivity.this, ProjectListActivity.class));

                }

            }
        });
//        cvFilter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                state = 3;
//                if (UtilPermission.checkPermissions(TMainActivity.this,1)){
//                    Intent intent = new Intent(TMainActivity.this, PickImageMakerActivity.class);
//                    intent.putExtra(Constants.KIND_CHOOSE, Constants.FILTER_IMAGE_CHOOSE);
//                    startActivity(intent);
//                }
//            }
//        });
//        cvCamera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                state = 4;
//                if (UtilPermission.checkPermissions(TMainActivity.this,1)){
//                    startActivity(new Intent(TMainActivity.this, CameraFilterActivity.class));
//
//                }
//
//            }
//        });
//
        tvAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TMainActivity.this,AboutActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for ( int i = 0; i < grantResults.length; i++ ) {
            if ( grantResults[i] == PackageManager.PERMISSION_DENIED ) {

                return;
            }
        }
        if (state == 1){
            Intent intent = new Intent(TMainActivity.this, PickImageMakerActivity.class);
            intent.putExtra(Constants.KIND_CHOOSE, Constants.FRAME_IMAGE_CHOOSE);
            startActivity(intent);
            finish();

        } else if (state == 2){
            startActivity(new Intent(TMainActivity.this, ProjectListActivity.class));
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }
}
