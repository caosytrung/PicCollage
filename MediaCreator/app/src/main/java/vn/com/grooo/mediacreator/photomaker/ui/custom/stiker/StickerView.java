package vn.com.grooo.mediacreator.photomaker.ui.custom.stiker;

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

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import vn.com.grooo.mediacreator.R;
import vn.com.grooo.mediacreator.common.utils.Utils;
import vn.com.grooo.mediacreator.saveandroidloadproject.model.ImageFrameInfor;
import vn.com.grooo.mediacreator.saveandroidloadproject.model.StickerInfor;


/**
 * Author: miaoyongjun
 * Date : 17/8/1
 */

public class StickerView extends RelativeLayout {

    private ArrayList<DrawableSticker> mStickers;
    private Paint mStickerPaint;
    private Bitmap btnDeleteBitmap;
    private Bitmap btnRotateBitmap;
    private Sticker currentSticker;
    private PointF lastPoint;
    private TouchState state;

    private int maxStickerCount;
    private float minStickerSizeScale;
    private float imageBeginScale;
    private int closeIcon, rotateIcon;
    private int closeSize, rotateSize;
    private int outLineWidth, outLineColor;
    private Point optionPoint;

    private IStvParenCalback iParentCallback;


    public void setiParentCallback(IStvParenCalback iParentCallback) {
        this.iParentCallback = iParentCallback;
    }

    public StickerView(Context context) {
        super(context);
        init(context);
    }

    public StickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttributes(context, attrs);
        init(context);
    }

    public StickerView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        Log.d("DrawableWidth ", drawable.getIntrinsicWidth() +  " ");
        DrawableSticker drawableSticker = new DrawableSticker(drawable);
        drawableSticker.setType(Sticker.TYPE_STICKER);
        mStickers.add(drawableSticker);
        currentSticker = drawableSticker;
        invalidate();
        return true;
    }
   // public  boolean addSticker(Bitmap bitmap)

    public void addSticker(Bitmap bitmap,boolean isResize){
        Log.d("BitmapDrawablecc" , bitmap.getWidth() + " ");
        BitmapDrawable drawable = new BitmapDrawable(getResources(),bitmap);

        Log.d("BitmapDrawablecc" , drawable.getIntrinsicWidth() + " ");
        DrawableSticker drawableSticker = new DrawableSticker(drawable);
        if (!isResize){
            drawableSticker.setText(true);
        }
        drawableSticker.setType(Sticker.TYPE_STICKER);
        mStickers.add(drawableSticker);
        currentSticker = drawableSticker;
        invalidate();
    }

    public void addSticker(String uri, StickerInfor stickerInfor) {
        Drawable drawable = null;
        Bitmap mBitmap = null;
        try
        {
            File f=new File(uri);
            if (mBitmap != null && !mBitmap.isRecycled()) {
                mBitmap.recycle();
                mBitmap = null;
            }
            mBitmap = BitmapFactory.decodeStream(new FileInputStream(f));

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
        drawableSticker.setUriStr(uri);


        drawableSticker.getMatrix().reset();
        drawableSticker.setMinStickerSize(60);
//        float scale  =   stickerInfor.getWidth() / drawableSticker.getWidth();
//        Log.d("STICKETINFORR",stickerInfor.getWidth() + " ");
//        float[] values = stickerInfor.getValues();
//        float rAngle = Math.round(Math.atan2(values[Matrix.MSKEW_X],
//                values[Matrix.MSCALE_X]) * (180 / Math.PI));
//        Log.d("InforStickerDraw1", values[Matrix.MTRANS_X] + " == " + values[Matrix.MTRANS_Y]);
//        Log.d("InforStickerDraw1", drawableSticker.getWidth() +
//                " ==" + stickerInfor.getWidth()  + " == " + scale);
//
//        drawableSticker.getMatrix().postScale(scale,scale
//        );
//        drawableSticker.getMatrix().postTranslate(values[Matrix.MTRANS_X],values[Matrix.MTRANS_Y]);
//        drawableSticker.getMatrix().postRotate(-rAngle,values[Matrix.MTRANS_X] ,
//                values[Matrix.MTRANS_Y] );
        drawableSticker.getMatrix().setValues(stickerInfor.getValues());
        drawableSticker.converse();
        drawableSticker.setInit(true);


        mStickers.add(drawableSticker);
        currentSticker = drawableSticker;
        invalidate();
    }

    public synchronized void addSticker(StickerInfor stickerInfor) {
        Bitmap bitmap = Utils.stringToBitMap(stickerInfor.getData());
        Log.d("BitmapINFOR", bitmap.getWidth() + " == " + stickerInfor.getWidth());
        BitmapDrawable drawable = new BitmapDrawable(getResources(),bitmap);

        //Log.d("BitmapDrawablecc" , bitmap.get + " ");
        DrawableSticker drawableSticker = new DrawableSticker(drawable);
        drawableSticker.setText(true);
        drawableSticker.setType(Sticker.TYPE_STICKER);
        drawableSticker.getMatrix().reset();
        drawableSticker.setMinStickerSize(40);
//        float scale  =  drawableSticker.getWidth() / stickerInfor.getWidth();
//        float[] values = stickerInfor.getValues();
//        float rAngle = Math.round(Math.atan2(values[Matrix.MSKEW_X], values[Matrix.MSCALE_X]) * (180 / Math.PI));
//
//        drawableSticker.getMatrix().postScale(scale,scale
//               );
//        drawableSticker.getMatrix().postTranslate(values[Matrix.MTRANS_X],values[Matrix.MTRANS_Y]);
//        drawableSticker.getMatrix().postRotate(-rAngle,values[Matrix.MTRANS_X] ,
//                values[Matrix.MTRANS_Y] );
        drawableSticker.  getMatrix().setValues(stickerInfor.getValues()    );
        drawableSticker.converse();
        drawableSticker.setInit(true);

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

    public boolean addSticker(String uriStr) {
        Drawable drawable = null;
         Bitmap mBitmap;
        try
        {
            mBitmap = SubStickerView.decodeFile(uriStr);

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



    public boolean changeSticker(String uriStr,int pos) {
        Drawable drawable = null;
        Bitmap mBitmap;
        try
        {
            mBitmap = SubStickerView.decodeFile(uriStr);
            RoundedBitmapDrawable r= RoundedBitmapDrawableFactory.
                    create(getResources(),mBitmap);
            drawable = r;
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
        mStickers.set(pos,drawableSticker);
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
        Log.d("StickerCount",mStickers.size() +  " ");
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
                Log.d("StickerCountDraw",mStickers.size() +  " ");
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
        float evX = event.getX(0);
        float evY = event.getY(0);
        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.d("poiterdown","alin");

                handler.removeCallbacks(mLongPressed);
                handlerSwap.removeCallbacks(runableSwap);
                midPoint = calculateMidPoint(event);
                iParentCallback.removeFocus();
                oldDistance = StickerUtil.calculateDistance(event);
                oldRotation = StickerUtil.calculateRotation(event);
                if (touchInsideSticker(event.getX(0), event.getY(0))
                        && touchInsideSticker(event.getX(1), event.getY(1))) {
                    state = TouchState.DOUBLE_TOUCH;
                }
                break;
            case MotionEvent.ACTION_DOWN:
                if (touchInsideDeleteButton(evX, evY)) {
                    state = TouchState.PRESS_DELETE;
                    break;
                }
                if (touchInsideRotateButton(evX, evY)) {
                    state = TouchState.PRESS_SCALE_AND_ROTATE;
                    break;
                }
                if (touchInsideSticker(evX, evY)) {
                    if (currentSticker.getType() == Sticker.TYPE_PHOTO){
                        optionPoint.x = (int) evX;
                        optionPoint.y = (int) evY;
                        startSwap.x = (int) evX;
                        startSwap.y = (int) evY;
                        handler.postDelayed(mLongPressed,350);
                    }

                    state = TouchState.TOUCHING_INSIDE;
                } else {
                    state = TouchState.TOUCHING_OUTSIDE;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                float dx = evX - lastPoint.x;
                float dy = evY - lastPoint.y;
                if (state == TouchState.PRESS_SCALE_AND_ROTATE) {
                    rotateAndScale(evX, evY);
                }
                if (state == TouchState.DOUBLE_TOUCH) {
                    rotateAndScaleDoubleTouch(event);
                }
                if (state == TouchState.TOUCHING_INSIDE) {

                    if (SubStickerView.caculateDistance(evX,evY,optionPoint.x,optionPoint.y) > 20){
                        handler.removeCallbacks(mLongPressed);
                    }
                    if (currentSticker.getType() == Sticker.TYPE_PHOTO){
                        iParentCallback.focusChild(evX,evY,isSwap);
                        if (SubStickerView.caculateDistance(evX,evY,startSwap.x,startSwap.y) > 20 ){
                            Log.d("hihihihica",isSwap + "  asdsa");
                            if (isSwap == true){
                                isSwap = false;
                                startSwap.x = (int) evX;
                                startSwap.y = (int) evY;
                                handlerSwap.
                                        postDelayed(runableSwap,300);
                            } else {
                                startSwap.x = (int) evX;
                                startSwap.y = (int) evY;
                                handlerSwap.
                                        removeCallbacks(runableSwap);
                                handlerSwap.
                                        postDelayed(runableSwap,300);
                            }

                        }
                    }
                    translate(dx, dy);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (state == TouchState.PRESS_DELETE &&
                        touchInsideDeleteButton(evX, evY)) {
                    mStickers.remove(currentSticker);
                    currentSticker = null;
                    invalidate();
                    break;
                }
                if (state == TouchState.TOUCHING_INSIDE ||
                        state == TouchState.PRESS_SCALE_AND_ROTATE) {
                    handlerSwap.removeCallbacks(runableSwap);
                    handler.removeCallbacks(mLongPressed);
                    iParentCallback.removeFocus();
                    Log.d("hihihihiins",isSwap + "  asdsa");
                    if (isSwap && currentSticker.getType() == Sticker.TYPE_PHOTO){
                        Log.d("hihihihi",isSwap + "  asdsa");
                        iParentCallback.swapImage(mStickers.indexOf(currentSticker),evX,evY);
                        isSwap = false;
                    }

                    break;
                }
                if (state == TouchState.TOUCHING_OUTSIDE) {
                    currentSticker = null;
                    invalidate();
                }

                break;
            case MotionEvent.ACTION_POINTER_UP:
                oldDistance = 0;
                oldRotation = 0;
                break;
        }
        lastPoint.x = evX;
        lastPoint.y = evY;
        return true;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        float evX = event.getX(0);
        float evY = event.getY(0);
        if (event.getAction() == MotionEvent.ACTION_POINTER_DOWN){
            Log.d("poiterdown","alin");
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN){

            if (touchInsideSticker(evX,evY) ||
                    touchInsideRotateButton(evX,evY) ||
                    touchInsideDeleteButton(evX,evY)){
                iParentCallback.deleteFocus();
                return true;
            }

            currentSticker = null;
            invalidate();

            return false;
        }
        return false;
    }




    private void rotateAndScaleDoubleTouch(MotionEvent event) {
        if (event.getPointerCount() < 2) {
            return;
        }
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
        return currentSticker != null && new RectF(currentSticker.getDst()[4] -
                btnRotateBitmap.getWidth() / 2, currentSticker.getDst()[5] -
                btnRotateBitmap.getHeight() / 2, currentSticker.getDst()[4] +
                btnRotateBitmap.getWidth() / 2, currentSticker.getDst()[5] +
                btnRotateBitmap.getHeight() / 2).contains(evX, evY);
    }

    private boolean touchInsideDeleteButton(float evX, float evY) {
        return currentSticker != null && new RectF(currentSticker.getDst()[0] -
                btnDeleteBitmap.getWidth() / 2, currentSticker.getDst()[1] -
                btnDeleteBitmap.getHeight() / 2, currentSticker.getDst()[0] +
                btnDeleteBitmap.getWidth() / 2, currentSticker.getDst()[1] +
                btnDeleteBitmap.getHeight() / 2).contains(evX, evY);
    }

    private void translate(float dx, float dy) {
        if (currentSticker == null) {
            return;
        }
        Matrix matrix = currentSticker.getMatrix();
        matrix.postTranslate(dx, dy);
        currentSticker.converse();
    }

    public boolean touchInsideSticker(float evX, float evY) {
        for (int i = mStickers.size() - 1; i >= 0 ; i --) {
            Sticker sticker = mStickers.get(i);
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
