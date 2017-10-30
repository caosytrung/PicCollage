package vn.com.grooo.mediacreator.imageeffect.ui.ac;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import vn.com.grooo.mediacreator.R;
import vn.com.grooo.mediacreator.TMainActivity;
import vn.com.grooo.mediacreator.common.base.BaseActivity;
import vn.com.grooo.mediacreator.common.utils.Utils;
import vn.com.grooo.mediacreator.photomaker.service.DownloadImageService;

/**
 * Created by trungcs on 8/27/17.
 */

public class ImageResultFilterActivity extends BaseActivity implements View.OnClickListener {
    private ImageView tvBackToCollage;
    private TextView tvBackToHome;
    private TextView tvDownload;
    private TextView tvShareFacebook;
    private TextView tvShareTwiter;
    private ImageView ivResult;
    private ProgressDialog prgSave;


    @Override
    protected int getLayoutResources() {
        return R.layout.activity_image_result;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
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
                Toast.makeText(ImageResultFilterActivity.this,
                        "Image saved successfully ",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Bitmap bmp = null;
        String filename = getIntent().getStringExtra("image");
        Log.d("Filenamee", filename);
        try {
            FileInputStream is = this.openFileInput(filename);
            bmp = BitmapFactory.decodeStream(is);
            is.close();
            ivResult.setImageBitmap(bmp);
           // Glide.with(this).load(is).into(ivResult)
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvSaveToGallery:
                prgSave.show();
                Bitmap bitmap = Bitmap.createBitmap(ivResult.getWidth(), ivResult.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                ivResult.draw(canvas);
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                Date today = Calendar.getInstance().getTime();
                String reportDate = df.format(today);

                final String source = Environment.getExternalStorageDirectory().
                        getPath() +"/MediaCreator/" + reportDate + ".jpg";
                File file = new File(Environment.getExternalStorageDirectory().
                        getPath() +"/MediaCreator");
                if (!file.exists()){
                    file.mkdir();
                }
            //  Log.d(TAG,source);
                try {
                    Bitmap tmp = Utils.getResizedBitmap(bitmap,400,400);
                    FileOutputStream out = new FileOutputStream(source);
                    tmp.compress(Bitmap.CompressFormat.JPEG, 60, out);
                    out.close();
                } catch (FileNotFoundException e) {
                    Log.d("errrorororro","Errorrr compress " + e.getMessage());
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
                        Intent intent = new Intent(ImageResultFilterActivity.this, DownloadImageService.class);
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
                startActivity(new Intent(this, TMainActivity.class));
                break;
        }
    }
}
