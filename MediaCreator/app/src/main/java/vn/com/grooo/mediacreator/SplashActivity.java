package vn.com.grooo.mediacreator;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import vn.com.grooo.mediacreator.common.base.BaseActivity;

/**
 * Created by trungcs on 9/28/17.
 */

public class SplashActivity extends BaseActivity implements Animation.AnimationListener {
    private TextView mChar1;
    private TextView mChar2;
    private TextView mChar3;
    private TextView mChar4;
    private TextView mChar5;
    private TextView mChar6;
    private TextView mChar7;
    private ImageView mIvCow;
    private RelativeLayout mRvStart;
    private Animation mAniChar1;
    private Animation mAniChar2;
    private Animation mAniChar3;
    private Animation mAniChar4;
    private Animation mAniChar5;
    private Animation mAniChar6;
    private Animation mAniChar7;
    private Animation mAniBg;
    private Animation mAniCow;
    @Override
    protected int getLayoutResources() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "your.package",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        mChar1 = (TextView)findViewById(R.id.char_1);
        mChar2 = (TextView)findViewById(R.id.char_2);
        mChar3 = (TextView)findViewById(R.id.char_3);
        mChar4 = (TextView)findViewById(R.id.char_4);
        mChar5 = (TextView)findViewById(R.id.char_5);
        mChar6 = (TextView)findViewById(R.id.char_6);
        mChar7 = (TextView)findViewById(R.id.char_7);
        mIvCow = (ImageView)findViewById(R.id.img_cow);
        mRvStart = (RelativeLayout) findViewById(R.id.rv_start);

        mIvCow.setVisibility(View.INVISIBLE);

        Typeface typeface = Typeface.
                createFromAsset(getAssets(),"textstyle/Blkck.ttf");
//
//        mChar1.setTypeface(typeface);
//        mChar2.setTypeface(typeface);
//        mChar3.setTypeface(typeface);
//        mChar4.setTypeface(typeface);
//        mChar5.setTypeface(typeface);
//        mChar6.setTypeface(typeface);
//        mChar7.setTypeface(typeface);

        mAniBg = AnimationUtils.loadAnimation(this,
                R.anim.ani_bg_start);
        mRvStart.setAnimation(mAniBg);
        mAniBg.setAnimationListener(this);
        mAniBg.start();

        mAniChar1 = AnimationUtils.loadAnimation(
                this,
                R.anim.ani_char1
        );
        mChar1.setAnimation(mAniChar1);
        mAniChar1.start();

        mAniChar2 = AnimationUtils.loadAnimation(
                this,
                R.anim.ani_char2
        );
        mChar2.setAnimation(mAniChar2);
        mAniChar2.start();

        mAniChar3 = AnimationUtils.loadAnimation(
                this,
                R.anim.ani_char3
        );
        mChar3.setAnimation(mAniChar3);
        mAniChar3.start();

        mAniChar4 = AnimationUtils.loadAnimation(
                this,
                R.anim.ani_char4
        );
        mChar4.setAnimation(mAniChar4);
        mAniChar4.start();

        mAniChar5 = AnimationUtils.loadAnimation(
                this,
                R.anim.ani_char5
        );
        mChar5.setAnimation(mAniChar5);
        mAniChar5.start();

        mAniChar6 = AnimationUtils.loadAnimation(
                this,
                R.anim.ani_char6
        );
        mChar6.setAnimation(mAniChar6);
        mAniChar6.start();

        mAniChar7 = AnimationUtils.loadAnimation(
                this,
                R.anim.ani_char7
        );
        mChar7.setAnimation(mAniChar7);
        mAniChar7.start();

        mAniCow =AnimationUtils.loadAnimation(
                this,
                R.anim.ani_bg_cow);
        mIvCow.setAnimation(mAniCow);
        mAniCow.start();


    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        Intent intent  = new Intent(this,TMainActivity.class);


        startActivity(intent);
        finish();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
