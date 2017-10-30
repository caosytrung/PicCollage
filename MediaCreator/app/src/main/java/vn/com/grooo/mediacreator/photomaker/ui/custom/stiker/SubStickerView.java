package vn.com.grooo.mediacreator.photomaker.ui.custom.stiker;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
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
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

import vn.com.grooo.mediacreator.R;
import vn.com.grooo.mediacreator.common.utils.Utils;
import vn.com.grooo.mediacreator.saveandroidloadproject.model.ImageFrameInfor;

/**
 * Created by trungcs on 8/14/17.
 */

public class SubStickerView extends AppCompatImageView {
    private static final String TAG = "SubStickerVieww";
    private ArrayList<DrawableSticker> mStickers;
    private Paint mStickerPaint;
    private Bitmap btnDeleteBitmap;
    private Bitmap btnRotateBitmap;
    private Sticker currentSticker;
    private PointF lastPoint;
    private SubStickerView.TouchState state;
    private Bitmap mBitmap;
    private int maxStickerCount;
    private float minStickerSizeScale;
    private float imageBeginScale;
    private int closeIcon, rotateIcon;
    private int closeSize, rotateSize;
    private int outLineWidth, outLineColor;
    private StickerView stvParent;
    private IAddImage iAddImage;
    private boolean isFocusParent =false;
    private int widthFrame;
    private int heightFrame;
    private float left;
    private float top;
    private IShowPopup iShowPopup;
    private int pos;
    private PointF oldPoint;
    private boolean hasImage;
    private boolean isShowBorder;
    private boolean isDrawforeGround;
    private int colorForeground;




    public SubStickerView(Context context,
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

    public SubStickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttributes(context, attrs);
        init(context);
    }

    public void setiAddImage(IAddImage iAddImage) {
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

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public SubStickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAttributes(context, attrs);
        init(context);
    }

    public void setiShowPopup(IShowPopup iShowPopup) {
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

    public void changeImage(String uriStr){
        hasImage = true;
        Drawable drawable = null;
        try
        {
            if (mBitmap != null && !mBitmap.isRecycled()) {
                mBitmap.recycle();
                mBitmap = null;
            }
            mBitmap = decodeFile(uriStr);
            drawable  = new BitmapDrawable(getResources(), mBitmap);
            Log.d("BITMAPP","lay dc ben sub " + drawable.getIntrinsicWidth() + " -- " + drawable.getIntrinsicHeight());
        }
        catch (Exception e)
        {
            drawable  = getContext().getResources().getDrawable(R.drawable.icon_download);
            Log.d("BITMAPP","ko dc ben sub");
        }
        if (mStickers.size() > 0){
            mStickers.clear();

            DrawableSticker tmpS = new DrawableSticker(drawable);
            tmpS.setType(Sticker.TYPE_PHOTO);
            tmpS.setInit(false);
            mStickers.add(tmpS);
        } else if (mStickers.size() == 0){
            DrawableSticker tmpS = new DrawableSticker(drawable);
            tmpS.setType(Sticker.TYPE_PHOTO);
            tmpS.setInit(false);
            mStickers.add(tmpS);
        }

        mStickers.get(0).setInit(false);
        currentSticker = mStickers.get(0);
        invalidate();
    }



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
            if (mBitmap != null && !mBitmap.isRecycled()) {
                mBitmap.recycle();
                mBitmap = null;
            }
            mBitmap = decodeFile(uriStr);
            drawable  = new BitmapDrawable(getResources(), mBitmap);
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

    public void addStickerFromAppData(ImageFrameInfor imageFrameInfor){
        String uriStr = imageFrameInfor.getPath();
        hasImage = true;
        Drawable drawable = null;
        Log.d("URISTR",uriStr);
        try {
            File f=new File(uriStr);
            if (mBitmap != null && !mBitmap.isRecycled()) {
                mBitmap.recycle();
                mBitmap = null;
            }
             mBitmap = BitmapFactory.decodeStream(new FileInputStream(f));

            drawable  = new BitmapDrawable(getResources(), mBitmap);

            Log.d("CreteBG",true + "");
            // Glide.with(this).load(is).into(ivResult)
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("CreteBG",false + e.getMessage());
        }

        DrawableSticker drawableSticker = new DrawableSticker(drawable);
        drawableSticker.setType(Sticker.TYPE_PHOTO);
        drawableSticker.getMatrix().reset();
        drawableSticker.setMinStickerSize(80);
        drawableSticker. getMatrix().setValues(imageFrameInfor.getValues());
        drawableSticker.converse();
        drawableSticker.setUriStr(imageFrameInfor.getPath());
        drawableSticker.setInit(true);
        mStickers.add(drawableSticker);
        currentSticker = drawableSticker;
        invalidate();
    }
    public void addSticker(ImageFrameInfor imageFrameInfor) {
        String uriStr = imageFrameInfor.getPath();
        hasImage = true;
        Drawable drawable = null;
        try
        {
            mBitmap = decodeFile(uriStr);
            drawable  = new BitmapDrawable(getResources(), mBitmap);
            Log.d("BITMAPP","lay dc ben sub ");
        }
        catch (Exception e)
        {
            drawable  = getContext().getResources().getDrawable(R.drawable.icon_add_sticker);
            Log.d("BITMAPP","ko dc ben sub");
        }

        DrawableSticker drawableSticker = new DrawableSticker(drawable);
        drawableSticker.setType(Sticker.TYPE_PHOTO);
        drawableSticker.getMatrix().reset();
        drawableSticker.setMinStickerSize(80);
        drawableSticker. getMatrix().setValues(imageFrameInfor.getValues());
        drawableSticker.converse();
        drawableSticker.setInit(true);
        mStickers.add(drawableSticker);
        currentSticker = drawableSticker;
        invalidate();
    }

    public void setRoundRadius(int r){
        RoundedBitmapDrawable rB = RoundedBitmapDrawableFactory.create(getResources(),mBitmap);
        rB.setCornerRadius(r);
        DrawableSticker dS = (DrawableSticker) mStickers.get(0);
        dS.setDrawable(rB);
        invalidate();
    }

    public static Bitmap decodeFile(String filePath) {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024  ;

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


        Path mPath = new Path();
        mPath.moveTo(0, 0);
        mPath.lineTo(canvas.getWidth(),0);
        mPath.lineTo(canvas.getWidth(),canvas.getHeight());
        mPath.lineTo(0,canvas.getHeight());
        mPath.lineTo(0,0);
      //  h and w are height and width of the screen
        Paint mPaint = new Paint();
        mPaint.setARGB(255, 0, 0, 0);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);
        mPaint.setPathEffect(new DashPathEffect(new float[]{10, 5, 10, 5}, 0));
        canvas.drawPath(mPath, mPaint);


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
//        paint.setStyle(Paint.Style.FILL);
//        int width = getMeasuredWidth();
//        int height = getMeasuredHeight();
//
//        Rect rectVer = new Rect(width / 2 - 10,0,width/2 + 10,height );
//        Rect recHoz = new Rect(0,height/2 - 10,width,height/2 + 10);
//
//        canvas.drawRect(rectVer,paint);
//        canvas.drawRect(recHoz,paint);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                R.drawable.icon_add_image_from_gallery);
        int newWidth = 256;
        int newHeight = 128;



        int width = largeIcon.getWidth();
        int height = largeIcon.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleWidth);
        newHeight = (int)scaleWidth * height;


        Bitmap resizedBitmap = Bitmap.createBitmap(
                largeIcon, 0, 0, width, height, matrix, false);
        largeIcon.recycle();

        int x = canvas.getWidth() / 2 - resizedBitmap.getWidth() / 2;
        int y = canvas.getHeight() / 2 - resizedBitmap.getHeight() / 2;

        canvas.drawBitmap(resizedBitmap,x,y,paint);


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



    final Handler handler = new Handler();
    Runnable mLongPressed = new Runnable() {
        public void run() {
            if (!hasImage){
                return;
            }
            Log.i("Longprrssss", "Long press!");
            deleteFocus();
            iShowPopup.showPopup(pos,oldPoint.x,oldPoint.y);
            return;

        }
    };

    public void cancleRunable(){
        handler.removeCallbacks(mLongPressed);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("isForcussChild" , isFocusParent + " ");
        float evX = event.getX(0);
        float evY = event.getY(0);

        int action = MotionEventCompat.getActionMasked(event);


        switch (action) {
            case MotionEvent.ACTION_POINTER_DOWN:
                handler.removeCallbacks(mLongPressed);
                oldPoint = new PointF(evX,evY);
                midPoint = calculateMidPoint(event);
                oldDistance = StickerUtil.
                        calculateDistance(event);
                oldRotation = StickerUtil.
                        calculateRotation(event);

                if (touchInsideSticker(event.getX(0), event.getY(0))
                        && touchInsideSticker(event.getX(1), event.getY(1))) {
                    state = SubStickerView.TouchState.DOUBLE_TOUCH;
                }
                break;
            case MotionEvent.ACTION_DOWN:

                oldPoint = new PointF(evX,evY);
                handler.postDelayed(mLongPressed,300);
                iShowPopup.increaseCount();
                Log.d("DeleteFocus","2");
                //deleteFocus();
                //                if (stvParent.touchInsideSticker(evX,evY)){
                //                    isFocusParent = true;
                //                    iTounchListerner.OnTouchAdapter(event);
                //                    Log.d("isForcuss1" , isFocusParent + " ");
                //                    return false;
                //                }
                Log.d("isForcuss2" , isFocusParent + " ");



                if (touchInsideRotateButton(evX, evY)) {
                    state = SubStickerView.TouchState.PRESS_SCALE_AND_ROTATE;
                    break;
                }
                if (touchInsideSticker(evX, evY)) {
                    state = SubStickerView.TouchState.TOUCHING_INSIDE;
                } else {
                    state = SubStickerView.TouchState.TOUCHING_OUTSIDE;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (caculateDistance(evX,evY,oldPoint.x,oldPoint.y) > 20){
                    handler.removeCallbacks(mLongPressed);
                }

                //                if (isFocusParent){
                //                    iTounchListerner.OnTouchAdapter(event);
                //                    return  false;
                //                }
                float dx = evX - lastPoint.x;
                float dy = evY - lastPoint.y;
                if (state == SubStickerView.TouchState.PRESS_SCALE_AND_ROTATE) {
                    rotateAndScale(evX, evY);
                }
                if (state == SubStickerView.TouchState.DOUBLE_TOUCH) {
                    rotateAndScaleDoubleTouch(event);
                }
                if (state == SubStickerView.TouchState.TOUCHING_INSIDE) {
                    translate(dx, dy);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:

                handler.removeCallbacks(mLongPressed);
                if (!hasImage){
                    Toast.makeText(getContext(), "AddImageeee", Toast.LENGTH_SHORT).show();
                    iAddImage.addIamge(pos);
                }
                //                if (isFocusParent){
                //                    iTounchListerner.OnTouchAdapter(event);
                //                    isFocusParent =false;
                //                    return  false;
                //                }

                if (state == SubStickerView.TouchState.TOUCHING_INSIDE ||
                        state == SubStickerView.TouchState.PRESS_SCALE_AND_ROTATE) {
                    break;
                }
                if (state == SubStickerView.TouchState.TOUCHING_OUTSIDE) {
                    currentSticker = null;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                handler.removeCallbacks(mLongPressed);
                oldDistance = 0;
                oldRotation = 0;
                break;
        }
        lastPoint.x = evX;
        lastPoint.y = evY;
        return true;
    }

    public  static float  caculateDistance(float x1,float y1,float x2,float y2){
        float distance = (float) Math.sqrt((x2 -x1)*(x2 - x1) + (y2 - y1) * (y2 - y1));
        return distance;
    }

    public void deleteFocus(){

        currentSticker = null;
        invalidate();
        Log.d("DeleteFocus","1");
    }

    private void rotateAndScaleDoubleTouch(MotionEvent event) {
        if (event.getPointerCount() < 2) {
            return;
        }

///        Log.d("INITTT", currentSticker.isInit() + "");
        currentSticker = mStickers.get(0);
        float centerX = (currentSticker.getDst()[0] + currentSticker.getDst()[4]) / 2;
        float centerY = (currentSticker.getDst()[1] + currentSticker.getDst()[5]) / 2;
        float rightBottomX = currentSticker.getDst()[4];
        float rightBottomY = currentSticker.getDst()[5];

        float pathMeasureLength = getPathMeasureLength(centerX, centerY, rightBottomX, rightBottomY);
        float newDistance = StickerUtil.calculateDistance(event);
        float newRotation = StickerUtil.calculateRotation(event);
        if (oldDistance != 0) {
            Matrix matrix = currentSticker.getMatrix();

            if ((newDistance - oldDistance) > 0) {
                matrix.postScale(newDistance / oldDistance, newDistance / oldDistance, midPoint.x,
                        midPoint.y);
            } else if (pathMeasureLength > currentSticker.getMinStickerSize()) {
                matrix.postScale(newDistance / oldDistance, newDistance / oldDistance, midPoint.x,
                        midPoint.y);
            }
            matrix.postRotate(newRotation - oldRotation, midPoint.x, midPoint.y);
            currentSticker.converse();
        }
        oldDistance = newDistance;
        oldRotation = newRotation;
    }

    private void rotateAndScale(float evX, float evY) {
        float[] src = currentSticker.getRotateSrcPts();
        float[] dst = new float[4];
        float centerX = (currentSticker.getDst()[0] + currentSticker.getDst()[4]) / 2;
        float centerY = (currentSticker.getDst()[1] + currentSticker.getDst()[5]) / 2;

        float pathMeasureLength = getPathMeasureLength(centerX, centerY, evX, evY);
        if (pathMeasureLength < currentSticker.getMinStickerSize()) {
            evX = currentSticker.getMinStickerSize() * (evX - centerX) / pathMeasureLength + centerX;
            evY = currentSticker.getMinStickerSize() * (evY - centerY) / pathMeasureLength + centerY;
        }
        dst[0] = centerX;
        dst[1] = centerY;
        dst[2] = evX;
        dst[3] = evY;
        Matrix matrix = currentSticker.getMatrix();
        matrix.reset();

        matrix.setPolyToPoly(src, 0, dst, 0, 2);
        currentSticker.converse();
    }

    private float getPathMeasureLength(float moveX, float moveY, float lineX, float lineY) {
        Path path = new Path();
        path.moveTo(moveX, moveY);
        path.lineTo(lineX, lineY);
        PathMeasure pathMeasure = new PathMeasure(path, false);
        return pathMeasure.getLength();
    }

    private float oldDistance = 0f;
    private float oldRotation = 0f;
    private PointF midPoint = new PointF();


    protected PointF calculateMidPoint(@Nullable MotionEvent event) {
        if (event == null || event.getPointerCount() < 2) {
            midPoint.set(0, 0);
            return midPoint;
        }
        try {
            float x = (event.getX(0) + event.getX(1)) / 2;
            float y = (event.getY(0) + event.getY(1)) / 2;
            midPoint.set(x, y);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return midPoint;
    }


    private boolean touchInsideRotateButton(float evX, float evY) {
        return currentSticker != null && new RectF(currentSticker.getDst()[4] - btnRotateBitmap.getWidth() / 2,
                currentSticker.getDst()[5] - btnRotateBitmap.getHeight() / 2,
                currentSticker.getDst()[4] + btnRotateBitmap.getWidth() / 2,
                currentSticker.getDst()[5] + btnRotateBitmap.getHeight() / 2).contains(evX, evY);
    }



    private void translate(float dx, float dy) {

        if (currentSticker == null) {
            return;
        }
        if (!isInDomainTranslate(dx,dy)){
            return;
        }
        boolean l = currentSticker == null;

        Log.d(TAG,l + "  State current s");
        Matrix matrix = currentSticker.getMatrix();
        matrix.postTranslate(dx, dy);
        currentSticker.converse();
    }

    private boolean isInDomainTranslate(float dx,float dy){

        float dts[] = currentSticker.getDst();
        RectF rectF = getRecBoundFromDts(dts);

//        Log.d("GetInForFrame " , getTop() + " - " + getBottom() + " -" + getLeft() + " - " + getRight());
//        Log.d("GetInForFrame " , rectF.top + " - " + rectF.bottom + " -" + rectF.left + " - " + rectF.right);
        if (dx <= 0){
            if ((rectF.right - 0) < 64){
                Log.d("asdsa","mamamamright " + dx);
                return false;
            }
        } else {
            Log.d("witaddledt", "aa " + (getWidth() - rectF.left));
            if ((getWidth() - rectF.left) < 64){
                Log.d("asdsa","mamamamleft");
                return false;
            }
        }
        if (dy < 0){
            if ((rectF.bottom - 0  ) < 64){
                Log.d("asdsa","mamamamtop");
                return false;
            }
        } else {
            if ((getHeight() - rectF.top) < 64){
                Log.d("asdsa","mamamambot");
                return  false;
            }
        }
        return  true;
    }
    private RectF getRecBoundFromDts(float[] dts){
        float left = dts[0];
        float top = dts[1];
        float right = dts[0];
        float bottom = dts[1];
        for (int i = 0 ; i < dts.length ; i ++){
            if (i % 2 == 0 ){
                if (left > dts[i]){
                    left = dts[i];
                }
                if (right < dts[i]){
                    right = dts[i];
                }
            } else {
                if (top > dts[i]){
                    top = dts[i];
                }

                if (bottom < dts[i]){
                    bottom = dts[i];
                }
            }
        }

        RectF rectF = new RectF(left,top,right,bottom);
        return rectF;
    }

    private boolean touchInsideSticker(float evX, float evY) {
        for (Sticker sticker : mStickers) {
            Region region = new Region();
            region.setPath(sticker.getBoundPath(), new Region(0, 0, getMeasuredWidth(), getMeasuredHeight()));
            if (region.contains((int) evX, (int) evY)) {
                currentSticker = sticker;
                int index = mStickers.indexOf(currentSticker);
                Collections.swap(mStickers, index, mStickers.size() - 1);
                return true;
            }
        }
        return false;
    }

    public ArrayList<DrawableSticker> getmStickers() {
        return mStickers;
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
    public final OnTouchListener mOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return false;
        }

    };

}
