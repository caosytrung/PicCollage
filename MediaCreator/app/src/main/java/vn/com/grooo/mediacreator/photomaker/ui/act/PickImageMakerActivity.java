package vn.com.grooo.mediacreator.photomaker.ui.act;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import vn.com.grooo.mediacreator.R;
import vn.com.grooo.mediacreator.camerafillter.CameraFilterActivity;
import vn.com.grooo.mediacreator.common.Constants;
import vn.com.grooo.mediacreator.common.base.BaseActivity;
import vn.com.grooo.mediacreator.common.utils.Utils;
import vn.com.grooo.mediacreator.imageeffect.ui.ac.ChooseEffectActivity;
import vn.com.grooo.mediacreator.photoeffect.ui.ac.FilterPhotoActivity;
import vn.com.grooo.mediacreator.photomaker.callback.IAddUri;
import vn.com.grooo.mediacreator.photomaker.callback.IRVClickListener;
import vn.com.grooo.mediacreator.photomaker.ui.adapt.ImagePickMakerAdapter;
import vn.com.grooo.mediacreator.photomaker.ui.adapt.RVImageToCollageAdapter;
import vn.com.grooo.mediacreator.photomaker.ui.fragt.ImageMakerFragment;

/**
 * Created by trungcs on 8/14/17.
 */

public class PickImageMakerActivity extends
        BaseActivity implements IRVClickListener, IAddUri {

    public static final int  CODE_CAMERA = 1;
    private ImagePickMakerAdapter mImagePickMakerAdapter;
    private TabLayout tlPickMaker;
    private ViewPager vpPickMaker;
    private List<String> listTitle;
    private RecyclerView rvImageToCollage;
    private RVImageToCollageAdapter mAdapter;
    private ArrayList<String> uriListToCollge;
    private String kindChoose;
    private LinearLayout lnTopView;
    private ProgressDialog mDialog;
    private ImageView btnToCollage;

    @Override
    protected int getLayoutResources() {
        return R.layout.activity_choose_image_maker;
    }


    @Override
    protected void initVariables(Bundle savedInstanceState) {

        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Loadng ... ");
        lnTopView = (LinearLayout) findViewById(R.id.lnTopView);
        tlPickMaker = (TabLayout) findViewById(R.id.tlPickMaker);
        vpPickMaker = (ViewPager) findViewById(R.id.vpPickMaker);
        rvImageToCollage = (RecyclerView) findViewById(R.id.rvImageToCollage);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvImageToCollage.setLayoutManager(manager);
        uriListToCollge = new ArrayList<>();

        getData();

        mAdapter = new RVImageToCollageAdapter(this);
        mAdapter.setUriList(uriListToCollge);
        mAdapter.setIrvClickListener(this);

        rvImageToCollage.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void createTabTitle() {
        mImagePickMakerAdapter = new ImagePickMakerAdapter(getSupportFragmentManager());
        listTitle = Utils.getALlTitleImage(this);
        for (int i = 0; i < listTitle.size(); i++) {
            Log.d("CheckTile", listTitle.get(i));


            mImagePickMakerAdapter.addFragment(new ImageMakerFragment(listTitle.get(i), this));
        }
        vpPickMaker.setAdapter(mImagePickMakerAdapter);
        // vpPickMaker.setPageTransformer(true,new CubeOutTransformer());
        tlPickMaker.setupWithViewPager(vpPickMaker);
        mImagePickMakerAdapter.notifyDataSetChanged();
        for (int i = 0; i < listTitle.size(); i++) {
//            TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_icon,null);
//
//            tabOne.setText(listTitle.get(i));
//            tlPickMaker.getTabAt(i).setCustomView(tabOne);
            tlPickMaker.getTabAt(i).setText(listTitle.get(i));
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        btnToCollage.setEnabled(true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        createTabTitle();
//        findViewById(R.id.btnNextToCollage).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(PickImageMakerActivity.this, CollageActivity.class);
//                intent.putStringArrayListExtra("key", uriListToCollge);
//                startActivity(intent);
//            }
//        });
        btnToCollage = (ImageView) findViewById(R.id.btnNextToCollage);
        btnToCollage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnToCollage.setEnabled(false);
                Intent intent = new Intent(PickImageMakerActivity.this, CollageActivity.class);
                intent.putStringArrayListExtra("key", uriListToCollge);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onItemClick(int pos, View v) {
        uriListToCollge.remove(pos);
        refreshList();
    }

    @Override
    public void onItemLongClick(int pos, View v) {

    }

    @Override
    public void addUri(String uriStr) {
        switch (kindChoose){
            case Constants.FILTER_IMAGE_CHOOSE:
                Intent intent = new Intent(PickImageMakerActivity.this, FilterPhotoActivity.class);
                intent.putExtra(Constants.SEND_FILE_PATH_TO_PHOTO_EFFECT, uriStr);
                startActivity(intent);
                break;
            case Constants.FRAME_IMAGE_CHOOSE:
                uriListToCollge.add(uriStr);
                refreshList();
                rvImageToCollage.scrollToPosition(uriListToCollge.size() - 1);
                break;
            case Constants.CHANGE_IMAGE_MAKER:
                Intent intentChangImage = new Intent();
                intentChangImage.putExtra(Constants.KEY_IMAGE_FOR_CHANGE,uriStr);
                setResult(Constants.CODE_CHANGE_IMAGE,intentChangImage);
                finish();
                break;
            case Constants.ADD_IMAGE_OUTSIDE_FRAME:
                Intent intentAddImage = new Intent();
                intentAddImage.putExtra(Constants.KEY_IMAGE_OUTSIDE,uriStr);
                setResult(Constants.CODE_ADD_IMAGE_OUTSIDE,intentAddImage);
                finish();
                break;


        }


    }
    String source;
    @Override
    public void moveToCamera() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
//        Date today = Calendar.getInstance().getTime();
//        String reportDate = df.format(today);
//
//        source = Environment.getExternalStorageDirectory().
//                getPath() +"/MediaCreator/" + reportDate + ".jpg";
//        File file = new File(source);
//        if (!file.exists()){
//            file.mkdir();
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        Uri mPhotoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                new ContentValues());
//        source = mPhotoUri.toString();
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
//        startActivityForResult(intent,CODE_CAMERA   );
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(intent, CODE_CAMERA);
//        }

        Intent intent = new
                Intent(this, CameraFilterActivity.class);
        startActivityForResult(intent, CODE_CAMERA);
    }

    private void refreshList() {
        mAdapter.setUriList(uriListToCollge);
        mAdapter.notifyDataSetChanged();
    }

    private void hideTopView(){
        lnTopView.setVisibility(View.GONE);
    }
    private void showTopView(){
        lnTopView.setVisibility(View.VISIBLE);
    }

    private void getData() {
        kindChoose =
                getIntent().
                        getStringExtra(Constants.KIND_CHOOSE);

        switch (kindChoose){
            case Constants.FRAME_IMAGE_CHOOSE:
                showTopView();
                break;
            case Constants.FILTER_IMAGE_CHOOSE:
                hideTopView();
                break;
            case Constants.CHANGE_IMAGE_MAKER:
                hideTopView();
                break;
            case Constants.ADD_IMAGE_OUTSIDE_FRAME:
                hideTopView();
                break;

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
       // Log.d("URIRRR",requestCode  + " " + resultCode + "   " + RESULT_OK);

//            Log.d("SourceURIi",source);
          //  String tempUri = getRealPathFromURI(this,Uri.parse(source));
//            Log.d("URIRRR",selectedImageUri.toString()  + " ");
        if (resultCode != RESULT_OK){
            return;
        }
            String tempUri = data.getStringExtra("URI_CAMERA");

            switch (kindChoose){
                case Constants.FILTER_IMAGE_CHOOSE:
                    Intent intent = new Intent(PickImageMakerActivity.this, FilterPhotoActivity.class);
                    intent.putExtra(Constants.SEND_FILE_PATH_TO_PHOTO_EFFECT, tempUri);
                    startActivity(intent);
                    break;
                case Constants.FRAME_IMAGE_CHOOSE:
                    uriListToCollge.add(tempUri);
                    refreshList();
                    rvImageToCollage.scrollToPosition(uriListToCollge.size() - 1);
                    break;
                case Constants.CHANGE_IMAGE_MAKER:
                    Intent intentChangImage = new Intent();
                    intentChangImage.putExtra(Constants.KEY_IMAGE_FOR_CHANGE,tempUri);
                    setResult(Constants.CODE_CHANGE_IMAGE,intentChangImage);
                    finish();
                    break;
                case Constants.ADD_IMAGE_OUTSIDE_FRAME:
                    Intent intentAddImage = new Intent();
                    intentAddImage.putExtra(Constants.KEY_IMAGE_OUTSIDE,tempUri);
                    setResult(Constants.CODE_ADD_IMAGE_OUTSIDE,intentAddImage);
                    finish();
                    break;
            }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
