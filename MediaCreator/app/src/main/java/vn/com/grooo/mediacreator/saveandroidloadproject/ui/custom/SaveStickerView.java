package vn.com.grooo.mediacreator.saveandroidloadproject.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import vn.com.grooo.mediacreator.R;
import vn.com.grooo.mediacreator.common.utils.Utils;
import vn.com.grooo.mediacreator.photomaker.ui.custom.stiker.DrawableSticker;
import vn.com.grooo.mediacreator.photomaker.ui.custom.stiker.Sticker;
import vn.com.grooo.mediacreator.photomaker.ui.custom.stiker.StickerUtil;
import vn.com.grooo.mediacreator.photomaker.ui.custom.stiker.SubStickerView;

/**
 * Created by trungcs on 9/15/17.
 */

public class SaveStickerView extends RelativeLayout {

    private ArrayList<DrawableSticker> mStickers;
    private Paint mStickerPaint;
    private Bitmap btnDeleteBitmap;
    private Bitmap btnRotateBitmap;
    private Sticker currentSticker;
    private PointF lastPoint;

    private int maxStickerCount;
    private float minStickerSizeScale;
    private float imageBeginScale;
    private int closeIcon, rotateIcon;
    private int closeSize, rotateSize;
    private int outLineWidth, outLineColor;
    private Point optionPoint;

    private vn.com.grooo.mediacreator.photomaker.ui.custom.stiker.StickerView.IStvParenCalback iParentCallback;


    public void setiParentCallback(vn.com.grooo.mediacreator.photomaker.ui.custom.stiker.StickerView.IStvParenCalback iParentCallback) {
        this.iParentCallback = iParentCallback;
    }

    public SaveStickerView(Context context) {
        super(context);
        init(context);
    }

    public SaveStickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttributes(context, attrs);
        init(context);
    }

    public SaveStickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAttributes(context, attrs);
        init(context);
    }

    private void setAttributes(Context context, AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.StickerView);
        try {
            imageBeginScale = typedArray.getFloat(R.styleable.StickerView_m_image_init_scale, 0.3f);
            maxStickerCount = typedArray.getInt(R.styleable.StickerView_m_max_count, 20);
            minStickerSizeScale = typedArray.getFloat(R.styleable.StickerView_m_image_min_size_scale, 0.5f);
            closeIcon = typedArray.getResourceId(R.styleable.StickerView_m_close_icon, R.drawable.sticker_closed);
            rotateIcon = typedArray.getResourceId(R.styleable.StickerView_m_rotate_icon, R.drawable.sticker_rotate);
            closeSize = typedArray.getDimensionPixelSize(R.styleable.StickerView_m_close_icon_size, dip2px(context, 20));
            rotateSize = typedArray.getDimensionPixelSize(R.styleable.StickerView_m_rotate_icon_size, dip2px(context, 20));
            outLineWidth = typedArray.getDimensionPixelSize(R.styleable.StickerView_m_outline_width, dip2px(context, 1));
            outLineColor = typedArray.getColor(R.styleable.StickerView_m_outline_color, Color.WHITE);

        } finally {
            typedArray.recycle();
        }
    }

    private void init(Context context) {
        optionPoint = new Point();
        mStickerPaint = new Paint();
        mStickerPaint.setAntiAlias(true);
        mStickerPaint.setStyle(Paint.Style.STROKE);
        mStickerPaint.setStrokeWidth(outLineWidth);
        mStickerPaint.setColor(outLineColor);

        Paint mBtnPaint = new Paint();
        mBtnPaint.setAntiAlias(true);
        mBtnPaint.setColor(Color.BLACK);
        mBtnPaint.setStyle(Paint.Style.FILL);

        mStickers = new ArrayList<>();

        btnDeleteBitmap = BitmapFactory.decodeResource(getResources(), closeIcon);
        btnDeleteBitmap = Bitmap.createScaledBitmap(btnDeleteBitmap,
                closeSize, closeSize, true);
        btnRotateBitmap = BitmapFactory.decodeResource(getResources(), rotateIcon);
        btnRotateBitmap = Bitmap.createScaledBitmap(btnRotateBitmap,
                rotateSize, rotateSize, true);

        lastPoint = new PointF();

    }

    public ArrayList<DrawableSticker> getmStickers() {
        return mStickers;
    }

    public boolean addSticker(@DrawableRes int res) {
        if (mStickers.size() >= maxStickerCount) {
            return false;
        }
        Drawable drawable =
                ContextCompat.getDrawable(getContext(), res);
        return addSticker(drawable);
    }



    public boolean addSticker(Drawable drawable) {
        DrawableSticker drawableSticker = new DrawableSticker(drawable);
        drawableSticker.setType(Sticker.TYPE_STICKER);
        mStickers.add(drawableSticker);
        currentSticker = drawableSticker;
        invalidate();
        return true;
    }

    public void addSticker(Bitmap bitmap){
        Log.d("BitmapDrawablecc" , bitmap.getWidth() + " ");
        BitmapDrawable drawable = new BitmapDrawable(getResources(),bitmap);

        Log.d("BitmapDrawablecc" , bitmap.getWidth() + " ");
        DrawableSticker drawableSticker = new DrawableSticker(drawable);
        drawableSticker.setText(true);
        drawableSticker.setType(Sticker.TYPE_STICKER);
        mStickers.add(drawableSticker);
        currentSticker = drawableSticker;
        invalidate();
    }

    public void removeSticker(int pos){
        for(int i = 0 ; i < mStickers.size() ; i ++){
            mStickers.remove(pos);
            invalidate();
            break;
        }
    }


    public void removeSticker(String uriStr){
        for (int i = 0; i < mStickers.size();i ++ ){
            if (mStickers.get(i).getType() == Sticker.TYPE_PHOTO){
                if (mStickers.get(i).getUriStr().equals(uriStr)){
                    mStickers.remove(i);
                    invalidate();
                    break;
                }
            }
        }
    }

    public void removeSticker(Sticker sticker) {
        mStickers.remove(sticker);
        invalidate();
    }

    public void clearSticker() {
        mStickers.clear();
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawStickers(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // drawStickers(canvas);
    }
    public  Bitmap decodeFile(String filePath) {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 512;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, o2);
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap rotatedBitmap = null;
        switch(orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = Utils.rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = Utils.rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = Utils.rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
                break;
        }
        return rotatedBitmap;

    }
    public boolean addSticker(String uriStr) {
        Drawable drawable = null;
        Bitmap mBitmap;
        try
        {
            mBitmap = decodeFile(uriStr);

            drawable  = new BitmapDrawable(getResources(), mBitmap);
            Log.d("BITMAPP","lay dc ben sub");
        }
        catch (Exception e)
        {
            drawable  = getContext().getResources().
                    getDrawable(R.drawable.icon_add_sticker);
            Log.d("BITMAPP","ko dc ben sub");
        }

        DrawableSticker drawableSticker =
                new DrawableSticker(drawable);
        drawableSticker.setType(Sticker.TYPE_PHOTO);
        drawableSticker.setUriStr(uriStr);
        mStickers.add(drawableSticker);
        currentSticker = drawableSticker;
        invalidate();
        return true;
    }


    public List<String> getListImage(){
        List<String> uriList = new ArrayList<>();

        for (int i = 0; i < mStickers.size();i ++ ){
            if (mStickers.get(i).getType() == Sticker.TYPE_PHOTO){
                uriList.add(mStickers.get(i).getUriStr());
            }
        }
        Log.d("xxxx", uriList.size() + " -- ");
        return  uriList;
    }

    private void drawStickers(Canvas canvas) {
        for (Sticker sticker : mStickers) {
            if (!sticker.isInit()) {
                float imageWidth = imageBeginScale * getMeasuredWidth();
                if (sticker.isText()){
                    imageWidth = sticker.getWidth();
                }
                if (sticker.getType() == Sticker.TYPE_PHOTO){

                    float minEdge = getMeasuredWidth() *2/5;
                    imageWidth = sticker.getWidth() > sticker.getHeight() ?
                            (sticker.getWidth() * minEdge / sticker.getHeight()) :
                            minEdge;

                }
                Log.d("asdsadasdasImage",imageWidth + " ");
                float imageHeight = imageWidth / sticker.getBitmapScale();
                float minSize = (float) Math.sqrt(imageWidth * imageWidth + imageHeight * imageHeight);
                Log.d("asdsadasdasImages",minSize + " ");


                sticker.setMinStickerSize(minSize * minStickerSizeScale / 2);
                sticker.getMatrix().postScale(imageWidth / sticker.getWidth(), imageWidth / sticker.getWidth());
                sticker.getMatrix().postTranslate(
                        (getMeasuredWidth() - imageWidth) / 2,
                        (getMeasuredHeight() - imageHeight) / 2);
                if (sticker.getType() == Sticker.TYPE_PHOTO){
                    Random rand = new Random();

                    int  n = rand.nextInt(360) + 1;
                    sticker.getMatrix().postRotate(n,(getMeasuredWidth()) / 2,
                            (getMeasuredHeight() ) / 2);
                }
                sticker.converse();
                sticker.setInit(true);
            }
            if (sticker.getType() == Sticker.TYPE_PHOTO){
                sticker.draw(canvas,getResources().getColor(R.color.colorAccent),true);
            } else {
                sticker.draw(canvas,getResources().getColor(R.color.colorAccent),false);
                if (sticker == currentSticker) {
                    float[] dst = currentSticker.getDst();
                    canvas.drawLine(dst[0], dst[1], dst[2], dst[3], mStickerPaint);
                    canvas.drawLine(dst[2], dst[3], dst[4], dst[5], mStickerPaint);
                    canvas.drawLine(dst[4], dst[5], dst[6], dst[7], mStickerPaint);
                    canvas.drawLine(dst[6], dst[7], dst[0], dst[1], mStickerPaint);
                    drawBtn(sticker, canvas);
                }
            }


        }
    }

    public void refresSticker(){
        currentSticker = null;
        invalidate();
    }

    private void drawBtn(Sticker sticker, Canvas canvas) {
        canvas.drawBitmap(btnDeleteBitmap,
                sticker.getDst()[0] - btnDeleteBitmap.getWidth() / 2,
                sticker.getDst()[1] - btnDeleteBitmap.getHeight() / 2,
                null);
        canvas.drawBitmap(btnRotateBitmap,
                sticker.getDst()[4] - btnRotateBitmap.getWidth() / 2,
                sticker.getDst()[5] - btnRotateBitmap.getHeight() / 2,
                null);
    }

    final Handler handler = new Handler();
    Runnable mLongPressed = new Runnable() {
        public void run() {
            iParentCallback.showOptionalImage(mStickers.
                    indexOf(currentSticker),optionPoint.x,optionPoint.y);
            return;

        }
    };
    final Handler handlerSwap = new Handler();
    Runnable runableSwap = new Runnable() {
        public void run() {
//            iParentCallback.showOptionalImage(mStickers.
//                    indexOf(currentSticker),optionPoint.x,optionPoint.y);
            isSwap = true;
            iParentCallback.focusChild(startSwap.x,startSwap.y,isSwap);
            return;

        }
    };

    private Point startSwap = new Point();
    boolean isSwap = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return false;
    }















    private enum TouchState {
        TOUCHING_INSIDE, TOUCHING_OUTSIDE, PRESS_DELETE, PRESS_SCALE_AND_ROTATE, DOUBLE_TOUCH;
    }

    public int getMaxStickerCount() {
        return maxStickerCount;
    }

    public void setMaxStickerCount(int maxStickerCount) {
        this.maxStickerCount = maxStickerCount;
    }

    public float getMinStickerSizeScale() {
        return minStickerSizeScale;
    }

    public void setMinStickerSizeScale(float minStickerSizeScale) {
        this.minStickerSizeScale = minStickerSizeScale;
    }

    public float getImageBeginScale() {
        return imageBeginScale;
    }

    public void setImageBeginScale(float imageBeginScale) {
        this.imageBeginScale = imageBeginScale;
    }

    public int getCloseIcon() {
        return closeIcon;
    }

    public void setCloseIcon(int closeIcon) {
        this.closeIcon = closeIcon;
    }

    public int getRotateIcon() {
        return rotateIcon;
    }

    public void setRotateIcon(int rotateIcon) {
        this.rotateIcon = rotateIcon;
    }

    public int getCloseSize() {
        return closeSize;
    }

    public void setCloseSize(int closeSize) {
        this.closeSize = closeSize;
    }

    public int getRotateSize() {
        return rotateSize;
    }

    public void setRotateSize(int rotateSize) {
        this.rotateSize = rotateSize;
    }

    public int getOutLineWidth() {
        return outLineWidth;
    }

    public void setOutLineWidth(int outLineWidth) {
        this.outLineWidth = outLineWidth;
    }

    public int getOutLineColor() {
        return outLineColor;
    }

    public void setOutLineColor(int outLineColor) {
        this.outLineColor = outLineColor;
    }

    public int dip2px(Context c, float dpValue) {
        final float scale = c.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public Bitmap saveSticker() {
        currentSticker = null;
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        draw(canvas);
        return bitmap;
    }

    public interface IStvParenCalback{
        void deleteFocus();
        void showOptionalImage(int pos,int x,int y);
        void swapImage(int pos,float x,float y);
        void focusChild(float x,float y,boolean isSwap);
        void removeFocus();
    }
}
