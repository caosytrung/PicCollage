package vn.com.grooo.mediacreator.photomaker.ui.act;

import android.app.Notification;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.annotations.Until;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import vn.com.grooo.mediacreator.MainActivity;
import vn.com.grooo.mediacreator.R;
import vn.com.grooo.mediacreator.TMainActivity;
import vn.com.grooo.mediacreator.TestFacebookSdk;
import vn.com.grooo.mediacreator.common.Constants;
import vn.com.grooo.mediacreator.common.base.BaseActivity;
import vn.com.grooo.mediacreator.common.utils.Utils;
import vn.com.grooo.mediacreator.photomaker.service.DownloadImageService;

/**
 * Created by trungcs on 8/27/17.
 */

public class ImageResultActivity extends BaseActivity implements View.OnClickListener {
    private ImageView tvBackToCollage;
    private TextView tvBackToHome;
    private TextView tvDownload;
    private TextView tvShareFacebook;
    private TextView tvShareTwiter;
    private ImageView ivResult;
    private ProgressDialog prgSave;
    private AlertDialog.Builder builder;
    private String filename;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private  Bitmap bmp;
    private AccessToken accessToken;
    private ProgressDialog mDialog;

    @Override
    protected int getLayoutResources() {
        return R.layout.activity_image_result;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        FacebookSdk.isInitialized();
        callbackManager = CallbackManager.Factory.create();
        tvBackToCollage = (ImageView) findViewById(R.id.tvBackToCollage);
        tvBackToHome = (TextView) findViewById(R.id.tvbackToHome);
        tvDownload = (TextView) findViewById(R.id.tvSaveToGallery);
        tvShareFacebook = (TextView) findViewById(R.id.tvShareFacebook);
        tvShareTwiter = (TextView) findViewById(R.id.tvShareSwiter);
        ivResult = (ImageView) findViewById(R.id.ivShowImageResult);

       // Utils.setFontAnswesSome(tvBackToCollage,this);
        Utils.setFontAnswesSome(tvBackToHome,this);
        Utils.setFontAnswesSome(tvDownload,this);
        Utils.setFontAnswesSome(tvShareFacebook,this);
        Utils.setFontAnswesSome(tvShareTwiter,this);


       // tvBackToCollage.setText("\uf053");
        tvBackToHome.setText("\uf015");
        tvDownload.setText("\uf019");
        tvShareFacebook.setText("\uf09a");
        tvShareTwiter.setText("\uf099");

        tvDownload.setOnClickListener(this);
        tvBackToCollage.setOnClickListener(this);
        tvBackToHome.setOnClickListener(this);

        prgSave = new ProgressDialog(this);
        prgSave.setTitle("Download");
        prgSave.setMessage("Loading ....");
        prgSave.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Toast.makeText(ImageResultActivity.this,
                        "Image saved successfully ",Toast.LENGTH_LONG).show();
            }
        });
        builder =  new AlertDialog.Builder(this);
        builder.setMessage("Do you want to save this session?");
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int j) {

//                for (int i = 0; i < subStickerViewList.size() ; i ++){
//                    subStickerViewList.get(i).removeBordes();
//                }
//                progressDialog.show();
//                (new CollageActivity.SaveProjectAsynTask()).execute();
                dialogInterface.dismiss();
                Intent intent = new Intent();
                intent.putExtra("filename",filename);
                setResult(Constants.CODE_SAVE_PROJECT,intent);
                finish();

            }
        });
        builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setNeutralButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent in1 = new Intent(ImageResultActivity.this, TMainActivity.class);
                finish();
                startActivity(in1);
            }
        });
        tvShareFacebook.setOnClickListener(this);
        tvShareTwiter.setOnClickListener(this);
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Sharing ....");
        mDialog.setCanceledOnTouchOutside(false);
        LoginManager.getInstance().logOut();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Successssss", "Login : " + loginResult.getAccessToken().getToken());
                        accessToken = loginResult.getAccessToken();
                        (new MyAssynTask()).execute();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(ImageResultActivity.this, "Login Cancel",
                                Toast.LENGTH_LONG).show();
                        Log.d("Successssss", "Cancle : " );
                        (new MyAssynTask()).execute();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(ImageResultActivity.this, exception.getMessage(),
                                Toast.LENGTH_LONG).show();
                        Log.d("Successssss", "Errror " );
                    }
                });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

         filename = getIntent().getStringExtra("image");
        try {
            FileInputStream is = this.openFileInput(filename);
            bmp = BitmapFactory.decodeStream(is);
            is.close();
            ivResult.setImageBitmap(bmp);
           // Glide.with(this).load(is).into(ivResult)
        } catch (Exception e) {
            e.printStackTrace();
        }


        shareDialog = new ShareDialog(this);
        // this part is optional

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvShareFacebook:
                shareFacebook();
                break;
            
            
            case R.id.tvSaveToGallery:
                prgSave.show();
                Bitmap bitmap = Bitmap.createBitmap(ivResult.getWidth(),
                        ivResult.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                ivResult.draw(canvas);
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
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.close();
                } catch (FileNotFoundException e) {
                    //Log.d(TAG,"Errorrr compress " + e.getMessage());
                    e.printStackTrace();
                } catch (IOException e) {
                e.printStackTrace();
                } finally {

            }
            MediaScannerConnection.scanFile(this,
                new String[] { source }, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.d("Sqcannnn","scan Image ");
                        prgSave.dismiss();
                        Intent intent = new Intent(ImageResultActivity.this, DownloadImageService.class);
                        intent.putExtra("URI_KEY",source);
                        startService(intent);
                    }
                }

        );

                break;
            case R.id.tvBackToCollage:
                finish();

                break;
            case R.id.tvbackToHome:
                builder.show();
                break;
        }
    }

    private void shareFacebook() {

        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(bmp)
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();
        boolean bb = ShareDialog.canShow(SharePhotoContent.class);

        if(shareDialog.canShow(content)){
            Log.d("FacebookShare","Share With appp");
            shareDialog.show(content);
        } else {
            if (AccessToken.getCurrentAccessToken() != null){
                Log.d("FacebookShare","Share existing Token");
                (new MyAssynTask()).execute();
            } else {

                Log.d("FacebookShare","New Login");
                LoginManager.getInstance().logInWithPublishPermissions(
                        ImageResultActivity.this,
                        Arrays.asList("publish_actions"));
            }
        }


//        if (ShareDialog.canShow(ShareLinkContent.class)) {
//            ShareLinkContent linkContent = new ShareLinkContent.Builder()
//                    .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
//                    .build();
//            shareDialog.show(linkContent);
//        }

    }
    private  class MyAssynTask extends AsyncTask<Void ,Void,Void> {
        @Override
        protected void onPreExecute() {
            mDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            if (accessToken == null) {
                accessToken = AccessToken.getCurrentAccessToken();
            }
            Bundle postParams = new Bundle();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 80, stream);
            byte[] byteArray = stream.toByteArray();
            postParams.putByteArray("picture", byteArray);
            postParams.putString("caption", "my picture");

            GraphRequest request = new GraphRequest(accessToken,
                    "me/photos",
                    postParams, HttpMethod.POST, null);

            GraphResponse response = request.executeAndWait();
            FacebookRequestError error = response.getError();
            if (error != null) {
                Log.d("FaceBook errrr", error.toString());
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mDialog.dismiss();
        }
    }


}
