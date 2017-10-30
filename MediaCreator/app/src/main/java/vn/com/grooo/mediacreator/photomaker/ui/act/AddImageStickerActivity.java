package vn.com.grooo.mediacreator.photomaker.ui.act;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

import vn.com.grooo.mediacreator.R;
import vn.com.grooo.mediacreator.common.Constants;
import vn.com.grooo.mediacreator.common.base.BaseActivity;
import vn.com.grooo.mediacreator.photomaker.ui.adapt.VPImageStickerAdapter;
import vn.com.grooo.mediacreator.photomaker.ui.fragt.ImageStickerFragment;

/**
 * Created by trungcs on 8/19/17.
 */

public class AddImageStickerActivity extends BaseActivity implements
        ImageStickerFragment.IAddImageSticker {
    private TabLayout tlAddImageSticker;
    private ViewPager vpAddImageSticker;
    private VPImageStickerAdapter mVpImageStickerAdapter;

    @Override
    protected int getLayoutResources() {
        return R.layout.activity_add_image_sticker;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        tlAddImageSticker = (TabLayout) findViewById(R.id.tlAddImageSticker);
        vpAddImageSticker = (ViewPager) findViewById(R.id.vpAddImageSticker);
        mVpImageStickerAdapter =new
                VPImageStickerAdapter(getSupportFragmentManager());
        addFragment();

        vpAddImageSticker.setAdapter(mVpImageStickerAdapter);
        mVpImageStickerAdapter.notifyDataSetChanged();
        tlAddImageSticker.setupWithViewPager(vpAddImageSticker);
        addIcon();

    }
    private void addFragment(){
        mVpImageStickerAdapter.
                addFragment(new ImageStickerFragment("sticker/type1",this,this));
        mVpImageStickerAdapter.
                addFragment(new ImageStickerFragment("sticker/type2",this,this));
        mVpImageStickerAdapter.
                addFragment(new ImageStickerFragment("sticker/type3",this,this));
        mVpImageStickerAdapter.
                addFragment(new ImageStickerFragment("sticker/type4",this,this));
        mVpImageStickerAdapter.
                addFragment(new ImageStickerFragment("sticker/type5",this,this));
        mVpImageStickerAdapter.
                addFragment(new ImageStickerFragment("sticker/type6",this,this));


    }


    private void addIcon(){
        ImageView iv0 = (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_tab_sticker,null);
        try
        {

            InputStream ims = getAssets().open("sticker/type1/beard_1.png");

            Bitmap b = BitmapFactory.decodeStream(ims);

            iv0.setImageBitmap(b);
            ims .close();
        }
        catch(IOException ex)
        {
            return;
        }
        tlAddImageSticker.getTabAt(0).setCustomView(iv0);

        ImageView iv1 = (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_tab_sticker,null);
        try
        {
            // get input stream
            InputStream ims = getAssets().open("sticker/type2/glass_1.png");
            // load image as Drawable
            Bitmap b = BitmapFactory.decodeStream(ims);

            iv1.setImageBitmap(b);
            ims .close();
        }
        catch(IOException ex)
        {
            return;
        }
        tlAddImageSticker.getTabAt(1).setCustomView(iv1);

        ImageView iv2 = (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_tab_sticker,null);
        try
        {
            InputStream ims = getAssets().open("sticker/type3/hair_1.png");
            Bitmap b = BitmapFactory.decodeStream(ims);

            iv2.setImageBitmap(b);
            ims .close();
        }
        catch(IOException ex)
        {
            return;
        }
        tlAddImageSticker.getTabAt(2).setCustomView(iv2);

        ImageView iv3 = (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_tab_sticker,null);
        try
        {
            // get input stream
            InputStream ims = getAssets().open("sticker/type4/icon_face_02.png");
            Bitmap b = BitmapFactory.decodeStream(ims);

            iv3.setImageBitmap(b);
            ims .close();
        }
        catch(IOException ex)
        {
            return;
        }
        tlAddImageSticker.getTabAt(3).setCustomView(iv3);

        ImageView iv4 = (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_tab_sticker,null);
        try
        {
            // get input stream
            InputStream ims = getAssets().open("sticker/type5/sticker_trungcs_noel_02.png");
            Bitmap b = BitmapFactory.decodeStream(ims);

            iv4.setImageBitmap(b);
            ims .close();
        }
        catch(IOException ex)
        {
            return;
        }
        tlAddImageSticker.getTabAt(4).setCustomView(iv4);

        ImageView iv5 = (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_tab_sticker,null);
        try
        {
            // get input stream
            InputStream ims = getAssets().open("sticker/type6/tattoo_1.png");
            Bitmap b = BitmapFactory.decodeStream(ims);

            iv5.setImageBitmap(b);
            ims .close();
        }
        catch(IOException ex)
        {
            return;
        }
        tlAddImageSticker.getTabAt(5).setCustomView(iv5);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }


    @Override
    public void addImageSticker(String path) {
        Intent intent = new Intent();
        intent.putExtra(Constants.KEY_STICKER_IMAGE,path);
        setResult(Constants.CODE_STICKER_IMAGE,intent);
        finish();
    }
}
