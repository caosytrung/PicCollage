package vn.com.grooo.mediacreator.saveandroidloadproject.ui.frag;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import vn.com.grooo.mediacreator.R;
import vn.com.grooo.mediacreator.common.base.BaseFragment;
import vn.com.grooo.mediacreator.common.utils.FrameUntils;
import vn.com.grooo.mediacreator.common.utils.Utils;
import vn.com.grooo.mediacreator.photomaker.model.FrameObj;
import vn.com.grooo.mediacreator.photomaker.model.ShapeInsideFrame;
import vn.com.grooo.mediacreator.photomaker.ui.act.CollageActivity;
import vn.com.grooo.mediacreator.photomaker.ui.custom.stiker.Sticker;
import vn.com.grooo.mediacreator.photomaker.ui.custom.stiker.StickerView;
import vn.com.grooo.mediacreator.photomaker.ui.custom.stiker.SubStickerView;
import vn.com.grooo.mediacreator.saveandroidloadproject.model.ImageFrameInfor;
import vn.com.grooo.mediacreator.saveandroidloadproject.model.StickerInfor;
import vn.com.grooo.mediacreator.saveandroidloadproject.ui.custom.SaveStickerView;
import vn.com.grooo.mediacreator.saveandroidloadproject.ui.custom.SaveSubStickerView;

/**
 * Created by trungcs on 9/15/17.
 */

public class ProjectCollageFragment extends BaseFragment {
    public static final String KEY_ID = "id";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_STICKER = "sticker";
    public static final String KEY_INDEX = "index";
    public static final String KEY_FILE = "file";
    private int id;
    private int index;
    private List<StickerInfor> stickerInforList;
    private List<ImageFrameInfor> imageFrameInforList;
    private ImageView ivFrame;
    private int parentWidth;
    private int parentHeight;
    private List<String> mUriList;
    private List<SaveSubStickerView> subStickerViewList;
    private String fileName;

    @Override
    protected int getLayoutResource() {
        return R.layout.item_object_list;
    }

    @Override
    protected void initVariables(Bundle saveInstanceState, final View rootView) {

        ivFrame = (ImageView) rootView.findViewById(R.id.ivShowObject);


        Bundle args = getArguments();
        id = args.getInt(KEY_ID);
        fileName = args.getString(KEY_FILE);
        ivFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),CollageActivity.class);
                intent.putExtra(KEY_ID,id);
                startActivity(intent);

            }
        });


//        Type type1 = new TypeToken<ArrayList<ImageFrameInfor>>() {
//        }.getType();
//        Type type2 = new TypeToken<ArrayList<StickerInfor>>() {
//        }.getType();
//        imageFrameInforList =  new Gson().fromJson(imageStr,type1);
////        stickerInforList = new Gson().fromJson(stickerStr,type2);
//        mUriList = new ArrayList<>();
//        for (int i = 0 ; i < imageFrameInforList.size() ; i++){
//            mUriList.add(imageFrameInforList.get(i).getPath());
//        }
//        Log.d("Inforr" , index + " == " + mUriList.size());


        try {
            FileInputStream is = getContext().openFileInput(fileName);
            Bitmap bmp = BitmapFactory.decodeStream(is);
            ivFrame.setImageBitmap(bmp);
            Log.d("CreteBG",true + "");

            is.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("CreteBG",false + e.getMessage());
        }


//        ViewTreeObserver viewTreeObserver = stvParent.getViewTreeObserver();
//        if (viewTreeObserver.isAlive()) {
//            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//
//                @Override
//                public void onGlobalLayout() {
//                    stvParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                    parentWidth = stvParent.getWidth();
//                    parentHeight = stvParent.getHeight();
//                    createFrame();
////                    (new MyAsynTask()).execute();
//
//                }
//            });
//        }
    }

    @Override
    protected void initData(Bundle saveInstanceState) {

    }


//
//    private void createFrame(){
//
//       subStickerViewList = new ArrayList<>();
//
//
//        List<FrameObj> frameObjList = FrameUntils.
//                getFrameObjectList(mUriList.size(),getContext());
//
//
//        int mBorder = 0;
//
//        FrameObj defaultFrame = frameObjList.get(index);
//        List<ShapeInsideFrame> shapeInsideFrameList = defaultFrame.getShapeList();
//        for (int i = 0 ; i < shapeInsideFrameList.size() ; i ++){
//
//            ShapeInsideFrame shapeTmp = shapeInsideFrameList.get(i);
//            int widthFrame = (int)(shapeTmp.getRighr() - shapeTmp.getLeft()) * parentWidth/100  - 10;
//            int heightFrame = (int)(shapeTmp.getBottom() - shapeTmp.getTop()) * parentHeight / 100 - 10;
//            int marginTop = (int)(shapeTmp.getTop() * parentHeight/100) + mBorder;
//            int marginLeft = (int)(shapeTmp.getLeft() * parentWidth /100) + mBorder;
//
//            SaveSubStickerView sbv = new SaveSubStickerView (getContext(),widthFrame,
//                    heightFrame,marginLeft,marginTop);
//
//            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
//                    (int)(shapeTmp.getRighr() - shapeTmp.getLeft()) * parentWidth/100  - mBorder * 2,
//                    (int)(shapeTmp.getBottom() - shapeTmp.getTop()) * parentHeight / 100 - mBorder * 2
//            );
//
//            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//            params.setMargins((int)(shapeTmp.getLeft() * parentWidth /100) ,
//                    (int)(shapeTmp.getTop() * parentHeight/100) ,0,0);
//
//            sbv.setLayoutParams(params);
//            sbv.setPos(i);
//
//
//            stvParent.addView(sbv);
//            sbv.setClickable(false);
//
//            // sbv.setBackgroundDrawable(getDrawable(R.drawable.bg_sub_stickerview));
//
//            subStickerViewList.add(sbv);
//        }
//
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
//                    mUriList.add(CollageActivity.EMPTY_IMAGE);
//                }
//            }
//        } else if (maxImageInsideFrame < mUriList.size()){
//            Log.d("zzzzxx", mUriList.size() + " --  " + subStickerViewList.size() + " -- " );
//            //List<String> utiStickImage = stvParent.getListImage();
//            for (int i = maxImageInsideFrame ; i < mUriList.size() ; i ++){
//                if (!mUriList.get(i).equals(CollageActivity.EMPTY_IMAGE)){
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
//
//            if (mUriList.get(i).equals(CollageActivity.EMPTY_IMAGE)){
//                subStickerViewList.get(i).setHasImage(false);
//            } else {
//                subStickerViewList.get(i).addSticker(mUriList.get(i));
//            }
//        }
//
//    }
//
//    @Override
//    protected void initData(Bundle saveInstanceState) {
//
//    }
//
//    private class MyAsynTask extends AsyncTask<Void,Void,Void>{
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            createFrame();
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            for (int i = 0 ; i < subStickerViewList.size() ; i ++){
//                stvParent.addView(subStickerViewList.get(i));
//                if (!mUriList.get(i).equals(CollageActivity.EMPTY_IMAGE)) {
//                    subStickerViewList.get(i).addSticker(mUriList.get(i));
//                } else {
//                    subStickerViewList.get(i).setHasImage(false);
//                }
//            }
//        }
//    }
}
