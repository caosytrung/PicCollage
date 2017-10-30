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
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import vn.com.grooo.mediacreator.R;
import vn.com.grooo.mediacreator.common.utils.Utils;
import vn.com.grooo.mediacreator.photomaker.ui.custom.stiker.DrawableSticker;
import vn.com.grooo.mediacreator.photomaker.ui.custom.stiker.Sticker;
import vn.com.grooo.mediacreator.photomaker.ui.custom.stiker.StickerUtil;
import vn.com.grooo.mediacreator.photomaker.ui.custom.stiker.StickerView;
import vn.com.grooo.mediacreator.photomaker.ui.custom.stiker.SubStickerView;

/**
 * Created by trungcs on 9/15/17.
 */

public class SaveSubStickerView extends AppCompatImageView{
    private static final String TAG = "SubStickerVieww";
    private ArrayList<DrawableSticker> mStickers;
    private Paint mStickerPaint;
    private Bitmap btnDeleteBitmap;
    private Bitmap btnRotateBitmap;
    private Sticker currentSticker;
    private PointF lastPoint;
    private Bitmap mBitmap;
    private int maxStickerCount;
    private float minStickerSizeScale;
    private float imageBeginScale;
    private int closeIcon, rotateIcon;
    private int closeSize, rotateSize;
    private int outLineWidth, outLineColor;
    private StickerView stvParent;
    private SubStickerView.IAddImage iAddImage;
    private boolean isFocusParent =false;
    private int widthFrame;
    private int heightFrame;
    private float left;
    private float top;
    private SubStickerView.IShowPopup iShowPopup;
    private int pos;
    private PointF oldPoint;
    private boolean hasImage;
    private boolean isShowBorder;
    private boolean isDrawforeGround;
    private int colorForeground;




    public SaveSubStickerView(Context context,
                          int widthFrame,int heightFrame,int left,int top) {
        super(context);
        this.widthFrame = widthFrame;
        this.heightFrame = heightFrame;
        this.left = left;
        this.top = top;
        init(context);
        isShowBorder = true;

    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public SaveSubStickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttributes(context, attrs);
        init(context);
    }

    public void setiAddImage(SubStickerView.IAddImage iAddImage) {
        this.iAddImage = iAddImage;
    }

    public boolean isHasImage() {
        return hasImage;
    }

    public void setHasImage(boolean hasImage) {
        mStickers.clear();
        this.hasImage = hasImage;
        invalidate();
    }

    public SaveSubStickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAttributes(context, attrs);
        init(context);
    }

    public void setiShowPopup(SubStickerView.IShowPopup iShowPopup) {
        this.iShowPopup = iShowPopup;
    }

    private void setAttributes(Context context, AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.StickerView);
        imageBeginScale = 1;
        maxStickerCount = 8;
        minStickerSizeScale = 0.5f;
        closeIcon = R.drawable.sticker_closed;
        rotateIcon = R.drawable.sticker_rotate;
        closeSize  = dip2px(context,20);
        rotateSize = dip2px(context,20);
        outLineWidth = dip2px(context,1);
        outLineColor = Color.WHITE;

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
        imageBeginScale = 1;
        maxStickerCount = 8;
        minStickerSizeScale = 0.5f;
        closeIcon = R.drawable.sticker_closed;
        rotateIcon = R.drawable.sticker_rotate;
        closeSize  = dip2px(context,20);
        rotateSize = dip2px(context,20);
        outLineWidth = dip2px(context,1);
        outLineColor = Color.WHITE;

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
        btnDeleteBitmap = Bitmap.createScaledBitmap(btnDeleteBitmap, closeSize, closeSize, true);
        btnRotateBitmap = BitmapFactory.decodeResource(getResources(), rotateIcon);
        btnRotateBitmap = Bitmap.createScaledBitmap(btnRotateBitmap, rotateSize, rotateSize, true);

        lastPoint = new PointF();

    }


    //    public boolean addSticker(@DrawableRes int res) {
    //        if (mStickers.size() >= maxStickerCount) {
    //            return false;
    //        }
    //        Drawable drawable =
    //                ContextCompat.getDrawable(getContext(), res);
    //        return addSticker(drawable);
    //    }





    public boolean isDrawforeGround() {
        return isDrawforeGround;
    }

    public void setDrawforeGround(boolean drawforeGround,int color) {
        isDrawforeGround = drawforeGround;
        colorForeground = color;
        invalidate();
    }

    public boolean addSticker(String uriStr) {
        hasImage = true;
        Drawable drawable = null;
        try
        {
            Bitmap bSource = Utils.getBitMapFromFile(uriStr);
            Bitmap mBitmapSource = Utils.getResizedBitmap(bSource,120,300);
            drawable  = new BitmapDrawable(getResources(), mBitmapSource);
           //  drawable = Drawable.createFromStream(getContext().getAssets().open(R.d), null);
            Log.d("BITMAPP","lay dc ben sub ");
        }
        catch (Exception e)
        {
            drawable  = getContext().getResources().getDrawable(R.drawable.icon_add_sticker);
            Log.d("BITMAPP","ko dc ben sub");
        }

        DrawableSticker drawableSticker = new DrawableSticker(drawable);
        drawableSticker.setType(Sticker.TYPE_PHOTO);
        drawableSticker.setInit(false);
        mStickers.add(drawableSticker);
        currentSticker = drawableSticker;
        invalidate();
        return true;
    }

    public void setRoundRadius(int r){
        RoundedBitmapDrawable rB = RoundedBitmapDrawableFactory.create(getResources(),mBitmap);
        rB.setCornerRadius(r);
        DrawableSticker dS = (DrawableSticker) mStickers.get(0);
        dS.setDrawable(rB);
        invalidate();
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
            width_tmp /=2;
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


    public void removeBordes(){
        isShowBorder = false;
        invalidate();
    }


    public void clearSticker() {
        mStickers.clear();
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);


        if (!hasImage){
            Log.d(TAG,"Draw Add Image");
            drawAdditionImage(canvas);
        } else {

            drawStickers(canvas);
            Log.d(TAG,"Draw Iamge " );
        }


        if (isDrawforeGround){
            canvas.drawColor(colorForeground);
            Log.d(TAG,"Draw Iamgeeee "  + isDrawforeGround);
        }

        //        Path path = RoundedRect(0,0,canvas.getWidth(),canvas.getHeight(),30,30,false);
        //        Paint paint = new Paint();
        //        paint.setColor(Color.YELLOW);
        //        canvas.drawPath(path,paint);
    }

    private void drawAdditionImage(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        Rect rectVer = new Rect(width / 2 - 10,0,width/2 + 10,height );
        Rect recHoz = new Rect(0,height/2 - 10,width,height/2 + 10);

        canvas.drawRect(rectVer,paint);
        canvas.drawRect(recHoz,paint);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // drawStickers(canvas);
    }

    private void drawStickers(Canvas canvas) {

        for (Sticker sticker : mStickers) {
            if (!sticker.isInit()) {
                Log.d("isNTTT" , pos + " == " + sticker.isInit());
                sticker.getMatrix().reset();
                //                float imageWidth = imageBeginScale * getMeasuredWidth();
                //                float imageHeight = imageWidth / sticker.getBitmapScale();
                //                float minSize = (float) Math.sqrt(imageWidth * imageWidth + imageHeight * imageHeight);
                //                sticker.setMinStickerSize(minSize * minStickerSizeScale / 2);
                //                sticker.getMatrix().postScale(imageWidth / sticker.getWidth(), imageWidth / sticker.getWidth());
                //                sticker.getMatrix().postTranslate(
                //                        (getMeasuredWidth() - imageWidth) / 2,
                //                        (getMeasuredHeight() - imageHeight) / 2);
                //                sticker.converse();
                float imageWidth = sticker.getWidth();
                float imageHeight = sticker.getHeight();
                float scale = 0;

                if (widthFrame /imageWidth> heightFrame/imageHeight){
                    scale = (widthFrame  + 64)/imageWidth;
                } else {
                    scale =( heightFrame + 64) /imageHeight;
                }
                Log.d("SubFrame", widthFrame + " == " + imageWidth  + " == " + heightFrame + " == " + imageHeight   );

                imageWidth = imageWidth * scale;
                imageHeight = imageHeight  * scale;
//                if (imageWidth > widthFrame){
//                    left = (int)(widthFrame - imageWidth) /2 + 10;
//                } else {
//                    left =10;
//                }
//                if (imageHeight > heightFrame){
//                    top = (int) (heightFrame - imageHeight) / 2 + 10;
//                } else {
//                    top = 10;

//                }
                Log.d("SubFrame", widthFrame + " == " + imageWidth  + " == " + heightFrame + " == " + imageHeight   );

                left = (widthFrame - imageWidth) / 2;
                top = (heightFrame - imageHeight) / 2;



                imageWidth = widthFrame;
                imageHeight = imageWidth / sticker.getBitmapScale();
                float minSize = (float) Math.sqrt(imageWidth * imageWidth + imageHeight * imageHeight);
                sticker.setMinStickerSize(minSize * minStickerSizeScale / 2);
                sticker.getMatrix().postScale(scale,scale);
                sticker.getMatrix().postTranslate(
                        left,
                        top);
                sticker.converse();
                sticker.setInit(true);
            }
            sticker.draw(canvas,getResources().getColor(R.color.colorAccent),true);

            if (sticker == currentSticker) {
                float[] dst = currentSticker.getDst();
//                canvas.drawLine(dst[0], dst[1], dst[2], dst[3], mStickerPaint);
//                canvas.drawLine(dst[2], dst[3], dst[4], dst[5], mStickerPaint);
//                canvas.drawLine(dst[4], dst[5], dst[6], dst[7], mStickerPaint);
//                canvas.drawLine(dst[6], dst[7], dst[0], dst[1], mStickdrawSerPaint);
//                drawBtn(sticker, canvas);
            }
        }
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


    public interface IShowPopup{
        void increaseCount();
        void showPopup(int position,float x,float y);
    }

    public interface IAddImage{
        void addIamge(int pos);
    }
    public final View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return false;
        }

    };

}
