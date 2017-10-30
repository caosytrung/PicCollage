package vn.com.grooo.mediacreator.photomaker.ui.act;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;


import com.azeesoft.lib.colorpicker.ColorPickerDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import vn.com.grooo.mediacreator.MainActivity;
import vn.com.grooo.mediacreator.R;
import vn.com.grooo.mediacreator.TMainActivity;
import vn.com.grooo.mediacreator.common.Constants;
import vn.com.grooo.mediacreator.common.base.BaseActivity;
import vn.com.grooo.mediacreator.common.utils.FrameUntils;
import vn.com.grooo.mediacreator.common.utils.Utils;
import vn.com.grooo.mediacreator.imageeffect.ui.ac.ChooseEffectActivity;
import vn.com.grooo.mediacreator.photoeffect.ui.ac.FilterPhotoActivity;
import vn.com.grooo.mediacreator.photomaker.callback.IRVClickListener;
import vn.com.grooo.mediacreator.photomaker.model.FrameObj;
import vn.com.grooo.mediacreator.photomaker.model.IntentBitmap;
import vn.com.grooo.mediacreator.photomaker.model.ShapeInsideFrame;
import vn.com.grooo.mediacreator.photomaker.ui.adapt.RVFrameDialogAdapter;
import vn.com.grooo.mediacreator.photomaker.ui.custom.dialog.FrameDialog;
import vn.com.grooo.mediacreator.photomaker.ui.custom.frame.ImageFrame;
import vn.com.grooo.mediacreator.photomaker.ui.custom.popup_window.BubblePopupWindow;
import vn.com.grooo.mediacreator.photomaker.ui.custom.stiker.DrawableSticker;
import vn.com.grooo.mediacreator.photomaker.ui.custom.stiker.Sticker;
import vn.com.grooo.mediacreator.photomaker.ui.custom.stiker.StickerView;
import vn.com.grooo.mediacreator.photomaker.ui.custom.stiker.SubStickerView;
import vn.com.grooo.mediacreator.saveandroidloadproject.db.CTDatabaseHelper;
import vn.com.grooo.mediacreator.saveandroidloadproject.model.CollageOb;
import vn.com.grooo.mediacreator.saveandroidloadproject.model.ImageFrameInfor;
import vn.com.grooo.mediacreator.saveandroidloadproject.model.StickerInfor;
import vn.com.grooo.mediacreator.saveandroidloadproject.ui.frag.ProjectCollageFragment;

/**
 * Created by trungcs on 8/15/17.
 */

public class CollageActivity extends BaseActivity
        implements View.OnClickListener,
        FrameDialog.IFrameDialogCallback,
        SubStickerView.IShowPopup,
        SubStickerView.IAddImage,
        ColorPickerDialog.OnColorPickedListener,
        StickerView.IStvParenCalback {
    private static final int STATE_CHANGE_PARENT_IMAGE = 1;
    private static final int STATE_CHANGE_CHILD_IAMGE = 2;
    private int stateChangeImage;
    private ProgressDialog progressDialog;
    public static final String EMPTY_IMAGE = "EMPTY_IMAGE";
    public List<SubStickerView> subStickerViewList;
    private StickerView stvParent;
    private int parentWidth;
    private int parentHeight;
    private List<String> mUriList;
    private String TAG = "CollageActivity/de";
    private int type;
    private ArrayList<String> frameList;
    private ImageView ivShowFrame;
    private ImageView ivAddImageSticker;
    private ImageView ivAddTextSticker;
    private ImageView ivChooseBackground;
    private int mBorder;
    private int tmpChangeImagePos;
    private RecyclerView rvPopupFrame;
    private RVFrameDialogAdapter mPopFrameAdapter;
    private int connerRadius;
    private List<FrameObj>  frameObjList;
    private ProgressDialog prgDialog;
    private ImageView ivAddPhotoForParent;
    private ImageView ivSave;
    private int countTounch = 0 ;
    private int indexFrame;
    private boolean isEdit;
    private List<StickerInfor> stickerInforList;
    private List<ImageFrameInfor> imageFrameInforList;
    private int preIndex;
    private int backGroundColor;
    private AlertDialog.Builder builder;
    private int idProject;


    @Override
    protected int getLayoutResources() {
        return R.layout.activity_collage;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        subStickerViewList = new ArrayList<>();

        frameObjList = new ArrayList<>();
        frameList = new ArrayList<>();
        mBorder = 0;
        connerRadius = 0;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading ...");
        progressDialog.setCanceledOnTouchOutside(false);
        EventBus.getDefault().register(this);
        tmpChangeImagePos = 0;
        backGroundColor = Color.parseColor("#ffffffff");
        Intent intent = getIntent();
        mUriList = intent.getStringArrayListExtra("key");
        if (mUriList == null){
            isEdit = true;
             idProject = intent.getIntExtra(ProjectCollageFragment.KEY_ID,0);
            CollageOb collageOb = CTDatabaseHelper.getINSTANCE(this).getCollageObj(idProject);
            preIndex = collageOb.getFrameIndex();
            Log.d("asdas",preIndex + " ");
            Type type1 = new TypeToken<ArrayList<ImageFrameInfor>>() {
                 }.getType();
            Type type2 = new TypeToken<ArrayList<StickerInfor>>() {
                 }.getType();
           imageFrameInforList =  new Gson().fromJson(collageOb.getImageFrame(),type1);
            stickerInforList = new Gson().fromJson(collageOb.getSticker(),type2);
            mBorder = collageOb.getBorder();
            backGroundColor = collageOb.getColor();
            mUriList = new ArrayList<>();
            for (int i = 0 ; i < imageFrameInforList.size() ; i++){
                  mUriList.add(imageFrameInforList.get(i).getPath());
            }

            //createProjectFromPreviousData();
        }
        builder =  new AlertDialog.Builder(this);
        builder.setMessage("Do you want to save this session?");
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int j) {
                stvParent.refresSticker();
                for (int i = 0; i < subStickerViewList.size() ; i ++){
                    subStickerViewList.get(i).removeBordes();
                }
                progressDialog.show();
                (new SaveProjectAsynTask()).execute();
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

                Intent in1 = new Intent(CollageActivity.this, TMainActivity.class);
                startActivity(in1);
            }
        });



    }

    private void createProjectFromExistProject( ){
        int pos = preIndex;


        frameObjList = new ArrayList<>();
        frameObjList = FrameUntils.
                getFrameObjectList(mUriList.size(),this);

        indexFrame = pos;

        FrameObj defaultFrame = frameObjList.get(pos);
        stvParent.removeAllViews();
        subStickerViewList.clear();

        List<ShapeInsideFrame> shapeInsideFrameList = defaultFrame.getShapeList();
        Log.d(TAG,"Sizeee :" + frameObjList.size() + "   " + mUriList.size() + "  " +
                shapeInsideFrameList.size());
        if (pos == 0 ){
            for (int i = 0 ; i < stickerInforList.size() ; i ++){
                StickerInfor stickerInfor = stickerInforList.get(i);

                if (stickerInfor.getType() == StickerInfor.TYPE_1){
                    String uri = stickerInfor.getData();
                    stvParent.addSticker(uri, stickerInfor);
                }  else {
                    Log.d("StickerInforListINside", stickerInforList.size() + " ");
                    stvParent.addSticker(stickerInfor);
                }

            }
            mUriList.clear();
            return;
        }
        for (int i = 0 ; i < shapeInsideFrameList.size() ; i ++){

            ShapeInsideFrame shapeTmp = shapeInsideFrameList.get(i);
            int widthFrame = (int)(shapeTmp.getRighr() - shapeTmp.getLeft()) * parentWidth/100  - 10;
            int heightFrame = (int)(shapeTmp.getBottom() - shapeTmp.getTop()) * parentHeight / 100 - 10;
            int marginTop = (int)(shapeTmp.getTop() * parentHeight/100) + mBorder;
            int marginLeft = (int)(shapeTmp.getLeft() * parentWidth /100) + mBorder;

            SubStickerView sbv = new SubStickerView(this,widthFrame,
                    heightFrame,marginLeft,marginTop);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    (int)(shapeTmp.getRighr() - shapeTmp.getLeft()) * parentWidth/100  - mBorder * 2,
                    (int)(shapeTmp.getBottom() - shapeTmp.getTop()) * parentHeight / 100 - mBorder * 2
            );

            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.setMargins((int)(shapeTmp.getLeft() * parentWidth /100) ,
                    (int)(shapeTmp.getTop() * parentHeight/100) ,0,0);

            sbv.setLayoutParams(params);
            sbv.setiAddImage(this);
            sbv.setPos(i);
            stvParent.addView(sbv);
            // sbv.setBackgroundDrawable(getDrawable(R.drawable.bg_sub_stickerview));

            subStickerViewList.add(sbv);
        }
//        for (int i = 0 ;i < mUriList.size() ;  i ++){
//            if (!mUriList.get(i).equals(EMPTY_IMAGE)){
//                subStickerViewList.get(i).addSticker(mUriList.get(i));
//            }
//        }

        for (int i = 0 ; i < imageFrameInforList.size() ; i ++){
            Log.d("StickerInforList",imageFrameInforList.get(i).getPath());
            if (!imageFrameInforList.get(i).getPath().equals(EMPTY_IMAGE)){
                Log.d("Path12" , imageFrameInforList.get(i).getPath());
                subStickerViewList.get(i).addStickerFromAppData(imageFrameInforList.get(i));
            }
        }
        Log.d("StickerInforList", stickerInforList.size() + " ");
        for (int i = 0 ; i < stickerInforList.size() ; i ++){
            StickerInfor stickerInfor = stickerInforList.get(i);

            if (stickerInfor.getType() == StickerInfor.TYPE_1){
                String uri = stickerInfor.getData();
                stvParent.addSticker(uri, stickerInfor);
            }  else {
                Log.d("StickerInforListINside", stickerInforList.size() + " ");
                stvParent.addSticker(stickerInfor);
            }

        }
        for (int i = 0 ; i < subStickerViewList.size(); i ++){
            subStickerViewList.get(i).setiShowPopup(this);
        }

        channgBorder(mBorder);
        stvParent.setBackgroundColor(backGroundColor);

//        int maxImageInsideFrame = subStickerViewList.size() > mUriList.size() ?
//                mUriList.size() : subStickerViewList.size();
//        if (maxImageInsideFrame < subStickerViewList.size()){
//
//            List<String> uriStickImage = stvParent.getListImage();
//            Log.d("zzzzaa", mUriList.size() + " --  " + subStickerViewList.size() + " -- " + uriStickImage.size());
//            for (int i = maxImageInsideFrame; i < subStickerViewList.size() ; i ++){
//                if (i - maxImageInsideFrame < uriStickImage.size() ){
//                    mUriList.add(uriStickImage.get(i - maxImageInsideFrame));
//                    stvParent.removeSticker(uriStickImage.get(i - maxImageInsideFrame));
//                    // Log.d("zzzz", mUriList.size() + "  ");
//                } else {
//                    mUriList.add(EMPTY_IMAGE);
//                }
//            }
//        } else if (maxImageInsideFrame < mUriList.size()){
//            Log.d("zzzzxx", mUriList.size() + " --  " + subStickerViewList.size() + " -- " );
//            //List<String> utiStickImage = stvParent.getListImage();
//            for (int i = maxImageInsideFrame ; i < mUriList.size() ; i ++){
//                if (!mUriList.get(i).equals(EMPTY_IMAGE)){
//                    stvParent.addSticker(mUriList.get(i));
//                    mUriList.remove(i);
//                    i--;
//                } else {
//                    mUriList.remove(i);
//                    i--;
//                }
//            }
//        } else {
//
//        }
//        for (int i = 0 ; i  < subStickerViewList.size()  ; i ++) {
//            Log.d(TAG,"count : " + i  );
//            defaultFrame.getShapeList().get(i).setUriImage(mUriList.get(i));
//            defaultFrame.getShapeList().get(i).setStatus(true);
//
//
//            if (mUriList.get(i).equals(EMPTY_IMAGE)){
//                Log.d(TAG,"vaoccm");
//                subStickerViewList.get(i).setHasImage(false);
//            } else {
//                subStickerViewList.get(i).addSticker(mUriList.get(i));
//            }
//            subStickerViewList.get(i).setiShowPopup(this);
//        }

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        stvParent = (StickerView) findViewById(R.id.stvParent);
        stvParent.setiParentCallback(this);
        ivShowFrame = (ImageView) findViewById(R.id.ivShowFrame);
        ivShowFrame.setOnClickListener(this);
        ivAddImageSticker = (ImageView) findViewById(R.id.ivAddImageSticker);
        ivAddTextSticker = (ImageView) findViewById(R.id.ivSaveTextSticker) ;
        ivChooseBackground = (ImageView) findViewById(R.id.ivChooseBG);
        ivAddPhotoForParent = ( ImageView) findViewById(R.id.ivAddPhotoForParent);

        ivAddPhotoForParent.setOnClickListener(this);
        ivAddImageSticker.setOnClickListener(this);
        ivAddTextSticker.setOnClickListener(this);
        ivChooseBackground.setOnClickListener(this);
        ivSave = (ImageView) findViewById(R.id.ivSaveToImage);
        ivSave.setOnClickListener(this);
        findViewById(R.id.ivBackFromCollage).setOnClickListener(this);

        ViewTreeObserver viewTreeObserver = stvParent.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                @Override
                public void onGlobalLayout() {
                    ivSave.setVisibility(View.VISIBLE);
                    stvParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    parentWidth = stvParent.getWidth();
                    parentHeight = stvParent.getHeight();
                    Log.d("SizeParent" , parentWidth  + " " + parentHeight);
                    if (isEdit){
                        createProjectFromExistProject();

                    } else {
                        createDefaultView();
                    }

                }
            });
        }
    }

    private void createDefaultView(){
//        if (mUriList.size()  == 1){
//            Log.d(TAG,"1 Frame");
//            stvParent.addSticker(mUriList.get(0));
//            return;
//        }
        createFrame(0,true,true);

    }



    private void createFrame(int pos,boolean
            isDeletefocus,boolean isFirst){
        mBorder = 0;
        for (int i = 0 ; i < mUriList.size() ; i ++){
            if (mUriList.get(i).equals(EMPTY_IMAGE)){
                mUriList.remove(i);
                i --;
            }
        }



        frameObjList = new ArrayList<>();
        frameObjList = FrameUntils.
                getFrameObjectList(mUriList.size(),this);

        stvParent.removeAllViews();
        subStickerViewList.clear();



        Log.d(TAG,"Size :" + frameObjList.size());
        if (frameObjList.size() == 0){
            return;
        }


        if (isFirst){
            for (int i = 0 ; i < frameObjList.size() ; i ++){
                if (mUriList.size() ==
                        frameObjList.get(i).getShapeList().size()){
                    pos = i;
                    break;
                }
            }
        }

        if (pos == 0 ){
            for (int i = 0 ; i < mUriList.size(); i ++){
                stvParent.addSticker(mUriList.get(i));
            }
            mUriList.clear();
            return;
        }
        indexFrame = pos;


        FrameObj defaultFrame = frameObjList.get(pos);
        List<ShapeInsideFrame> shapeInsideFrameList = defaultFrame.getShapeList();
        Log.d(TAG,"Sizeeeee :" + frameObjList.size() + "   " + mUriList.size() + "  " +
                shapeInsideFrameList.size());
        for (int i = 0 ; i < shapeInsideFrameList.size() ; i ++){

            ShapeInsideFrame shapeTmp = shapeInsideFrameList.get(i);
            int widthFrame = (int)(shapeTmp.getRighr() - shapeTmp.getLeft()) * parentWidth/100  - 10;
            int heightFrame = (int)(shapeTmp.getBottom() - shapeTmp.getTop()) * parentHeight / 100 - 10;
            int marginTop = (int)(shapeTmp.getTop() * parentHeight/100) + mBorder;
            int marginLeft = (int)(shapeTmp.getLeft() * parentWidth /100) + mBorder;

            SubStickerView sbv = new SubStickerView(this,widthFrame,
                    heightFrame,marginLeft,marginTop);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    (int)(shapeTmp.getRighr() - shapeTmp.getLeft()) * parentWidth/100  - mBorder * 2,
                    (int)(shapeTmp.getBottom() - shapeTmp.getTop()) * parentHeight / 100 - mBorder * 2
            );

            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.setMargins((int)(shapeTmp.getLeft() * parentWidth /100) ,
                    (int)(shapeTmp.getTop() * parentHeight/100) ,0,0);

            sbv.setLayoutParams(params);
            sbv.setiAddImage(this);
            sbv.setPos(i);
            stvParent.addView(sbv);
            // sbv.setBackgroundDrawable(getDrawable(R.drawable.bg_sub_stickerview));

            subStickerViewList.add(sbv);
        }

        int maxImageInsideFrame = subStickerViewList.size() > mUriList.size() ?
                mUriList.size() : subStickerViewList.size();
        if (maxImageInsideFrame < subStickerViewList.size()){

            List<String> uriStickImage = stvParent.getListImage();
            Log.d("zzzzaa", mUriList.size() + " --  " + subStickerViewList.size() + " -- " + uriStickImage.size());
            for (int i = maxImageInsideFrame; i < subStickerViewList.size() ; i ++){
                if (i - maxImageInsideFrame < uriStickImage.size() ){
                    mUriList.add(uriStickImage.get(i - maxImageInsideFrame));
                    stvParent.removeSticker(uriStickImage.get(i - maxImageInsideFrame));
                    // Log.d("zzzz", mUriList.size() + "  ");
                } else {
                    mUriList.add(EMPTY_IMAGE);
                }
            }
        } else if (maxImageInsideFrame < mUriList.size()){
            Log.d("zzzzxx", mUriList.size() + " --  " + subStickerViewList.size() + " -- " );
            //List<String> utiStickImage = stvParent.getListImage();
            for (int i = maxImageInsideFrame ; i < mUriList.size() ; i ++){
                if (!mUriList.get(i).equals(EMPTY_IMAGE)){
                    stvParent.addSticker(mUriList.get(i));
                    mUriList.remove(i);
                    i--;
                } else {
                    mUriList.remove(i);
                    i--;
                }
            }
        } else {

        }
        for (int i = 0 ; i  < subStickerViewList.size()  ; i ++) {
            Log.d(TAG,"count : " + i  );
            defaultFrame.getShapeList().get(i).setUriImage(mUriList.get(i));
            defaultFrame.getShapeList().get(i).setStatus(true);


            if (mUriList.get(i).equals(EMPTY_IMAGE)){
                Log.d(TAG,"vaoccm");
                subStickerViewList.get(i).setHasImage(false);
            } else {
                subStickerViewList.get(i).addSticker(mUriList.get(i));
            }
            subStickerViewList.get(i).setiShowPopup(this);
        }
        if (isDeletefocus){
            deleteFocus();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivSaveToImage:
                stvParent.refresSticker();
                for (int i = 0; i < subStickerViewList.size() ; i ++){
                    subStickerViewList.get(i).removeBordes();
                }

               // saveImage();
               // saveProject();
//                String fileName = saveImage();
//                Intent in1 = new Intent(CollageActivity.this, ImageResultActivity.class);
//                in1.putExtra("image", fileName);
//                startActivity(in1);
                progressDialog.show();
                (new SaveImageAs()).execute();

                break;
            case R.id.ivSaveTextSticker:
                moveToAddTextStikerAct();
                break;
            case R.id.ivShowFrame:
                showFrame(view);
                break;
            case R.id.ivAddImageSticker:
                Log.d(TAG,"MoveTOImageSticker");
                moveToAddImageStickerAct();
                break;
            case R.id.ivBackFromCollage:
                //finish();
                builder.show();
                break;
            case R.id.ivChooseBG:
                ColorPickerDialog dialog = ColorPickerDialog.
                        createColorPickerDialog(this,ColorPickerDialog.DARK_THEME);
                dialog.setOnColorPickedListener(this);
                dialog.show();
                break;
            case R.id.ivAddPhotoForParent:
                Intent intent = new Intent(CollageActivity.this,
                        PickImageMakerActivity.class);
                intent.putExtra(Constants.KIND_CHOOSE,Constants.ADD_IMAGE_OUTSIDE_FRAME);
                startActivityForResult(intent,Constants.CODE_ADD_IMAGE_OUTSIDE);
                break;
            default:
                break;
        }
    }

    private void saveProject(String fileName) {

        List<ImageFrameInfor> imageFrameInforList = new ArrayList<>();
        List<StickerInfor> stickerInforList  = new ArrayList<>();


//        for (int i = 0 ;i < mUriList.size() ; i ++){
//            ImageFrameInfor imageFrameInfor = new ImageFrameInfor();
//            if (mUriList.get(i).equals(EMPTY_IMAGE)){
//                imageFrameInfor.setPath(EMPTY_IMAGE);
//            } else {
//                imageFrameInfor.setPath(mUriList.get(i));
//                Sticker sticker = subStickerViewList.get(i).getmStickers().get(0);
//                float[] values = new float[9];
//                sticker.getMatrix().getValues(values);
//                imageFrameInfor.setValues(values);
//                imageFrameInfor.setWidth(sticker.getWidth() );
//                imageFrameInfor.setHeight(sticker.getHeight());
//            }
//            imageFrameInforList.add(imageFrameInfor);
//        }
//
        imageFrameInforList = saveImageFrameToAppData();
//        for (int i = 0 ; i < stvParent.getmStickers().size(); i ++){
//            DrawableSticker sticker = stvParent.getmStickers().get(i);
//            StickerInfor stickerInfor = new StickerInfor();
//            if (sticker.getType() == Sticker.TYPE_PHOTO){
//                stickerInfor.setType(StickerInfor.TYPE_1);
//                stickerInfor.setData(sticker.getUriStr());
//            } else {
//                ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                Bitmap b = Utils.drawableToBitmap(sticker.getDrawable());
//                Log.d("BitmapToDrawable" , sticker.getDrawable().getIntrinsicWidth() + " == " + b.getWidth());
//                String source = Utils.bitMapToString(b);
//                Log.d("BitmapToString",source);
//                stickerInfor.setType(StickerInfor.TYPE_2);
//                stickerInfor.setData(source);
//
//            }
//
//            float[] values = new float[9];
//
//            sticker.getMatrix().getValues(values);
//            stickerInfor.setValues(values);
//            stickerInfor.setWidth(sticker.getWidth() );
//            stickerInfor.setHeight(sticker.getHeight());
//            Log.d("STICKETINFORRCol",stickerInfor.getWidth() * values[Matrix.MSCALE_X] + " ");
//            Log.d("InforStickerDraw2", values[Matrix.MTRANS_X] + " == " + values[Matrix.MTRANS_Y]);
//            Log.d("InforStickerDraw2", sticker.getWidth() +
//                    " ==" + stickerInfor.getWidth()  + " == " + values[Matrix.MSCALE_X]  + " " + values[Matrix.MSCALE_Y] + " == "+
//                   sticker.getHeight());
//
//
//            stickerInforList.add(stickerInfor);
//        }
        stickerInforList = saveSticekr();
        Gson gson = new Gson();
        // convert your list to json
        String imageInsideFrame = gson.toJson(imageFrameInforList);
        String sticker = gson.toJson(stickerInforList);
        Log.d("CheckSave" , imageInsideFrame + " == "  + imageFrameInforList.size());

        CollageOb collageOb = new CollageOb(indexFrame,
                imageInsideFrame,sticker,mBorder,fileName,backGroundColor);
        Log.d("GetLisyINdorrrr2",  "---- "+ collageOb.getFileName());
        if (isEdit){
            collageOb.setId(idProject);
            CTDatabaseHelper.getINSTANCE(this).updateRecord(collageOb);
        } else {
            CTDatabaseHelper.getINSTANCE(this).addRecord(collageOb);
        }


        Log.d("REcordSizes" , CTDatabaseHelper.getINSTANCE(this).getListRecord().size() + " ");
    }

    @Override
    protected void onResume() {

        super.onResume();
        stvParent.invalidate();
    }

    private String  saveImage() {
        Bitmap bitmap = Bitmap.createBitmap(stvParent.getWidth(),
                stvParent.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        stvParent.draw(canvas);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date today = Calendar.getInstance().getTime();
        String reportDate = df.format(today);

        String filename = "bm" + reportDate + ".png";
        FileOutputStream stream = null;
        try {
            stream = this.openFileOutput(filename, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 60, stream);
            //Cleanup
            stream.close();
            bitmap.recycle();

            //Pop intent
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filename;
    }

    private void moveToAddTextStikerAct(){
        Intent intent = new Intent(this,AddTextStickerActivity.class);
        startActivity(intent);

    }

    private void moveToAddImageStickerAct(){
        Intent intent = new Intent(this,AddImageStickerActivity.class);
        startActivityForResult(intent,Constants.CODE_STICKER_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("BitmapDrawablecc" ,  "asd ");
        if (requestCode == Constants.CODE_STICKER_IMAGE &&
                resultCode == Constants.CODE_STICKER_IMAGE){
            String path = data.getStringExtra(Constants.KEY_STICKER_IMAGE);
            addSticker(path);
        }
        if (requestCode == Constants.CODE_CHANGE_IMAGE && resultCode == Constants.CODE_CHANGE_IMAGE){
            String tmpUri = data.getStringExtra(Constants.KEY_IMAGE_FOR_CHANGE);
            if (stateChangeImage == STATE_CHANGE_CHILD_IAMGE){
                Log.d("asdasdsadas",tmpUri);
                mUriList.set(tmpChangeImagePos,tmpUri);
                subStickerViewList.get(tmpChangeImagePos).changeImage(tmpUri);
            } else {
                stvParent.changeSticker(tmpUri,tmpChangeImagePos);

            }
        }
        if (requestCode == Constants.CODE_ADD_IMAGE_OUTSIDE &&
                resultCode == Constants.CODE_ADD_IMAGE_OUTSIDE){
            String tmpUri = data.getStringExtra(Constants.KEY_IMAGE_OUTSIDE);
            stvParent.addSticker(tmpUri);
        }
        if (requestCode == Constants.CODE_SAVE_PROJECT && resultCode == Constants.CODE_SAVE_PROJECT){
            progressDialog.show();
            (new SaveProjectAsynTask()).execute();
        }

//        if (requestCode == Constants.CODE_STICKER_TEXT && requestCode == Constants.CODE_STICKER_TEXT){
//            Log.d("BitmapDrawablecc" ,  "asd ");
//            Bitmap bitmap = (Bitmap) data.getParcelableExtra(Constants.KEY_STICKER_TEXT);
//            addSticker(bitmap);
//        }
    }
    private void addSticker(Bitmap  bitmap){
        stvParent.addSticker(bitmap,false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(IntentBitmap iBitmap){
        if (iBitmap.getResult() == 1){
            addSticker(iBitmap.getBitmap());
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    private void addSticker(String path){
        try {
            Drawable d = Drawable.createFromStream(getAssets().open(path), null);
            Bitmap b = Utils.drawableToBitmap(d);
            stvParent.addSticker(b,true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void channgBorder(int border){
        for (int i = 0 ; i < subStickerViewList.size() ; i ++){
            Log.d(TAG,"Borderr : "  + border);
            RelativeLayout.LayoutParams tmpParam = (RelativeLayout.LayoutParams)
                    subStickerViewList.get(i).getLayoutParams();
            int leftMargin = tmpParam.leftMargin;
            int topMargin = tmpParam.topMargin;
            int width = tmpParam.width;
            int height = tmpParam.height;

            int newLeftMargin = leftMargin - mBorder + border;
            int newToptMargin = topMargin - mBorder + border;
            int newWidth = width + mBorder * 2 - border * 2;
            int newHeight = height + mBorder * 2- border * 2;
            tmpParam.width = newWidth;
            tmpParam.height = newHeight;
            tmpParam.setMargins(newLeftMargin,newToptMargin,0,0);
            subStickerViewList.get(i).setLayoutParams(tmpParam);

        }

        mBorder = border;
    }

    private void showFrame(View v) {
        BubblePopupWindow framePopup  = new BubblePopupWindow(this,parentWidth);
        View layoutBubble = LayoutInflater.from(this).inflate(R.layout.layout_popup_frame,null);
        rvPopupFrame = (RecyclerView) layoutBubble.findViewById(R.id.rvPopupFrame);
        mPopFrameAdapter = new RVFrameDialogAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvPopupFrame.setLayoutManager(manager);
        SeekBar sbBorder = (SeekBar) layoutBubble.findViewById(R.id.sbBorder);
        mPopFrameAdapter.setIrvClickListener(new IRVClickListener() {
            @Override
            public void onItemClick(int pos, View v) {
                changeFrame(pos);
            }

            @Override
            public void onItemLongClick(int pos, View v) {

            }
        });

        sbBorder.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                channgBorder(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mPopFrameAdapter.setFrameObjList(frameObjList);
        rvPopupFrame.setAdapter(mPopFrameAdapter);
        mPopFrameAdapter.notifyDataSetChanged();
        framePopup.setBubbleView(layoutBubble);
        framePopup.show(v, Gravity.TOP);


//
//        FrameDialog dialog = new FrameDialog(CollageActivity.this,
//                android.R.style.Theme_Translucent, frameList  z
//        );
//        dialog.setiFrameDialogCallback(this);
//        dialog.setCanceledOnTouchOutside(true);
//        dialog.show();
    }

    @Override
    public void changeFrame(int pos) {
        createFrame(pos,true,false);

    }

    @Override
    public void deleteFocus() {
        for (int i = 0; i < subStickerViewList.size() ; i ++){
            subStickerViewList.get(i).deleteFocus();
        }
    }

    @Override
    public void showOptionalImage(final int pos, int x, int y) {
        stateChangeImage = STATE_CHANGE_PARENT_IMAGE;

        final ViewGroup root = (ViewGroup) getWindow().
                getDecorView().findViewById(android.R.id.content);

        final View view = new View(this);
        view.setLayoutParams(new ViewGroup.LayoutParams(1, 1));
        view.setBackgroundColor(Color.TRANSPARENT);

        root.addView(view);

        view.setX(x);
        view.setY(y);

        PopupMenu popupMenu = new PopupMenu(this,view,Gravity.CENTER);
        popupMenu.getMenuInflater().inflate(R.menu.
                menu_popup_change_image_frame,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_remove:
                        stvParent.removeSticker(pos);
                        break;
                    case R.id.item_change_image:
                        Intent intent = new Intent(CollageActivity.this,
                                PickImageMakerActivity.class);
                        intent.putExtra(Constants.KIND_CHOOSE,
                                Constants.CHANGE_IMAGE_MAKER);
                        startActivityForResult(intent,Constants.CODE_CHANGE_IMAGE);
                        tmpChangeImagePos = pos;
                        break;
                    case R.id.item_effect: ///storage/emulated/0/MediaCreator/2017-10-05-15-14-06.jpg
                        Intent intent1 = new Intent(CollageActivity.this,
                                FilterPhotoActivity.class);
                        intent1.putExtra(Constants.KIND_CHOOSE,
                               stvParent.getmStickers().get(pos).getUriStr());
                        Log.d("GotoEfect", stvParent.getmStickers().get(pos).getUriStr());
                        startActivityForResult(intent1,Constants.CODE_CHANGE_IMAGE);
                        tmpChangeImagePos = pos;
                        break;
                }
                return true;
            }
        });

        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                root.removeView(view);
            }
        });
        popupMenu.show();
    }

    @Override
    public void swapImage(int pos,float x,float y) {
        for (int i = subStickerViewList.size() - 1 ; i >= 0 ; i --){
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) subStickerViewList.get(i).getLayoutParams();
            RectF rectF = new RectF(params.leftMargin,params.topMargin,
                    params.width + params.leftMargin,params.height + params.topMargin);
            if (rectF.contains(x,y)){
                String uriParent = stvParent.getmStickers().get(pos).getUriStr();
                String uriChild = mUriList.get(i);
                SubStickerView s = subStickerViewList.get(i);
                if (mUriList.get(i).equals(EMPTY_IMAGE)){
                    Log.d("asdsadasd","ahahahahEmpry");
                    s.addSticker(uriParent);
                    stvParent.removeSticker(pos);
                }  else {
                    Log.d("asdsadasd","ahahahah");
                    stvParent.changeSticker(uriChild,pos);
                    s.changeImage(uriParent);
                }
                mUriList.set(i,uriParent);
                break;
            }
        }
    }

    @Override
    public void focusChild(float x, float y,boolean isSwap) {
        Log.d("thelastcall", "focus");
        for (int i = subStickerViewList.size() - 1 ; i >= 0 ; i --){
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) subStickerViewList.get(i).getLayoutParams();
            RectF rectF = new RectF(params.leftMargin,params.topMargin,
                    params.width + params.leftMargin,params.height + params.topMargin);
            if (rectF.contains(x,y)){
                if (isSwap){
                    subStickerViewList.get(i).setDrawforeGround(true,Color.parseColor("#77F57C00"));
                } else {
                    subStickerViewList.get(i).setDrawforeGround(true,Color.parseColor("#7726C6DA"));
                }
            } else {
                subStickerViewList.get(i).setDrawforeGround(false,Color.parseColor("#7726C6DA"));
            }
        }
    }

    @Override
    public void removeFocus() {
        Log.d("thelastcall", "remoeve");
        for (int i = subStickerViewList.size() - 1 ; i >= 0 ; i --){
            subStickerViewList.get(i).setDrawforeGround(false,Color.parseColor("#7726C6DA"));
        }
    }

    @Override
    public void increaseCount() {
        countTounch ++;
        if (countTounch >= 2){
            countTounch = 0;
            for (int i = 0 ; i < subStickerViewList.size(); i++){
                subStickerViewList.get(i).cancleRunable();
            }
        }
    }

    @Override
    public void showPopup(final int pos, float x, float y) {
        countTounch = 0;
        stateChangeImage = STATE_CHANGE_CHILD_IAMGE;
        RelativeLayout.LayoutParams params  =
                (RelativeLayout.LayoutParams) subStickerViewList.get(pos).getLayoutParams();
        x = x + params.leftMargin;
        y = y + params.topMargin;

        final ViewGroup root = (ViewGroup) getWindow().
                getDecorView().findViewById(android.R.id.content);

        final View view = new View(this);
        view.setLayoutParams(new ViewGroup.LayoutParams(1, 1));
        view.setBackgroundColor(Color.TRANSPARENT);

        root.addView(view);

        view.setX(x);
        view.setY(y);

        PopupMenu popupMenu = new PopupMenu(this,view,Gravity.CENTER);
        popupMenu.getMenuInflater().inflate(R.menu.
                menu_popup_change_image_frame,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_remove:
                        subStickerViewList.get(pos).setHasImage(false);
                        mUriList.set(pos,EMPTY_IMAGE);
                        Log.d(TAG,"vaoccm");
                        break;
                    case R.id.item_change_image:
                        Intent intent = new Intent(CollageActivity.this,
                                PickImageMakerActivity.class);
                        intent.putExtra(Constants.KIND_CHOOSE,
                                Constants.CHANGE_IMAGE_MAKER);
                        startActivityForResult(intent,Constants.CODE_CHANGE_IMAGE);
                        tmpChangeImagePos = pos;
                        break;
                    case R.id.item_effect:
                        Intent intent1 = new Intent(CollageActivity.this,
                                FilterPhotoActivity.class);
                        intent1.putExtra(Constants.KIND_CHOOSE,
                                mUriList.get(pos));
                        startActivityForResult(intent1,Constants.CODE_CHANGE_IMAGE);
                        tmpChangeImagePos = pos;

                        break;
                }
                return true;
            }
        });

        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                root.removeView(view);
            }
        });
        popupMenu.show();

    }

    @Override
    public void addIamge(int pos) {
        stateChangeImage = STATE_CHANGE_CHILD_IAMGE;
        tmpChangeImagePos = pos;
        Intent intent = new Intent(CollageActivity.this,
                PickImageMakerActivity.class);

        intent.putExtra(Constants.KIND_CHOOSE,Constants.CHANGE_IMAGE_MAKER);
        startActivityForResult(intent,Constants.CODE_CHANGE_IMAGE);

    }

    @Override
    public void onColorPicked(int color, String hexVal) {
        backGroundColor = color;
        stvParent.setBackgroundColor(color);
    }


    private List<ImageFrameInfor> saveImageFrameToAppData(){
        List<ImageFrameInfor> imageFrameInforList = new ArrayList<>();
        for (int i = 0 ; i < mUriList.size() ; i++){
            ImageFrameInfor imageFrameInfor = new ImageFrameInfor();
            if (mUriList.get(i).equals(EMPTY_IMAGE)){
                imageFrameInfor.setPath(EMPTY_IMAGE);
            } else {
                Sticker sticker = subStickerViewList.get(i).getmStickers().get(0);
                float[] values = new float[9];
                sticker.getMatrix().getValues(values);
                imageFrameInfor.setValues(values);
                imageFrameInfor.setWidth(sticker.getWidth() );
                imageFrameInfor.setHeight(sticker.getHeight());
                if (mUriList.get(i).startsWith("/data/")){
                    imageFrameInfor.setPath(mUriList.get(i));
                } else {
                    Bitmap bitmap = subStickerViewList.get(i).getmBitmap();
                    String[] pathArr = mUriList.get(i).split("/");
                    String fileN    = pathArr[pathArr.length - 1];
                    ContextWrapper cw = new ContextWrapper(getApplicationContext());
                    File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

                    File mypath=new File(directory.getAbsolutePath(),fileN);

                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(mypath);
                        // Use the compress method on the BitMap object to write image to the OutputStream
                        bitmap.compress(Bitmap.CompressFormat.PNG, 60, fos);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    imageFrameInfor.setPath(directory.getAbsolutePath() + "/" + fileN);
                    Log.d("Path11" , imageFrameInfor.getPath());
                }
            }
            imageFrameInforList.add(imageFrameInfor);
        }
       return imageFrameInforList;
    }
    private List<StickerInfor> saveSticekr(){
        List<StickerInfor> stickerInforList  = new ArrayList<>();

        for (int i = 0 ; i < stvParent.getmStickers().size(); i ++){
            DrawableSticker sticker = stvParent.getmStickers().get(i);
            StickerInfor stickerInfor = new StickerInfor();
            if (sticker.getType() == Sticker.TYPE_PHOTO){
                stickerInfor.setType(StickerInfor.TYPE_1);
               // stickerInfor.setData(sticker.getUriStr());
                if (sticker.getUriStr().startsWith("/data/")){
                    stickerInfor.setData(sticker.getUriStr());
                } else {
                    Bitmap bitmap = SubStickerView.decodeFile(sticker.getUriStr());
                    String[] pathArr = sticker.getUriStr().split("/");
                    String fileN    = pathArr[pathArr.length - 1];
                    ContextWrapper cw = new ContextWrapper(getApplicationContext());
                    File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

                    File mypath=new File(directory.getAbsolutePath(),fileN);

                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(mypath);
                        // Use the compress method on the BitMap object to write image to the OutputStream
                        bitmap.compress(Bitmap.CompressFormat.PNG, 60, fos);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    stickerInfor.setData(directory.getAbsolutePath() + "/" + fileN);
                    Log.d("Path11" , stickerInfor.getData());
                }

            } else {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                Bitmap b = Utils.drawableToBitmap(sticker.getDrawable());
                Log.d("BitmapToDrawable" , sticker.getDrawable().getIntrinsicWidth() +
                        " == " + b.getWidth());
                String source = Utils.bitMapToString(b);
                Log.d("BitmapToString",source);
                stickerInfor.setType(StickerInfor.TYPE_2);
                stickerInfor.setData(source);

            }

            float[] values = new float[9];

            sticker.getMatrix().getValues(values);
            stickerInfor.setValues(values);
            stickerInfor.setWidth(sticker.getWidth() );
            stickerInfor.setHeight(sticker.getHeight());


            stickerInforList.add(stickerInfor);
        }
        return stickerInforList;
    }

    private class SaveProjectAsynTask extends AsyncTask<Void,Void,String>{

        @Override
        protected String  doInBackground(Void... voids) {

            String fileName = saveImage();
            Log.d("GetLisyINdorrrr1",  "---- "+ fileName);
            saveProject(fileName);
            return fileName;
        }

        @Override
        protected void onPostExecute(String fileName) {
            super.onPostExecute(fileName);
            Intent in1 = new Intent(CollageActivity.this, TMainActivity.class);
           // in1.putExtra("image", fileName);
            startActivity(in1);
            progressDialog.dismiss();
        }
    }
    private class SaveImageAs extends AsyncTask<Void,Void,String>{

        @Override
        protected String  doInBackground(Void... voids) {

            String fileName = saveImage();

            return fileName;
        }

        @Override
        protected void onPostExecute(String fileName) {
            super.onPostExecute(fileName);
            Intent in1 = new Intent(CollageActivity.this, ImageResultActivity.class);
             in1.putExtra("image", fileName);
            startActivityForResult(in1,Constants.CODE_SAVE_PROJECT);
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
       builder.show();
    }
}



