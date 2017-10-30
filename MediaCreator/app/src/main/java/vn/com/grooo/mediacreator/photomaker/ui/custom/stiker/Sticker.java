package vn.com.grooo.mediacreator.photomaker.ui.custom.stiker;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Author: miaoyongjun
 * Date : 17/8/1
 */

public abstract class Sticker {
    public static final int TYPE_STICKER = 0;
    public static final int TYPE_PHOTO = 1;


    private boolean isText;

    private boolean init;

    private Matrix matrix;

    private float[] srcPts;

    private float[] dst;

    private float[] rotateSrcPts;

    private Path boundPath;

    private float minStickerSize;

    private int type;

    private String uriStr;

    public String getUriStr() {
        return uriStr;
    }

    public void setUriStr(String uriStr) {
        this.uriStr = uriStr;
    }

    public void init(int width, int height) {
        type = 0;
        isText =false;
        matrix = new Matrix();

        srcPts = new float[]{0, 0,                                    // 左上
                width, 0,                              // 右上
                width, height,                // 右下
                0, height};
        /*
         * 原始旋转效果的点  图片中心点和图片的右下角的点
         * 触摸时获取到触摸点以及和中心点形成另一组的点
         * 之后通过matrix.setPolyToPoly(src, 0, dst, 0, 2) 方法来获取变换后的matrix
         */
        rotateSrcPts = new float[]{
                width / 2, height / 2,
                width, height,
        };
        dst = new float[8];
        boundPath = new Path();
    }

    public boolean isText() {
        return isText;
    }

    public void setText(boolean text) {
        isText = text;
    }

    public abstract void draw(@NonNull Canvas canvas,int color,boolean isParent);

    public abstract int getWidth();

    public abstract int getHeight();

    public float getMinStickerSize() {
        return minStickerSize;
    }

    public void setMinStickerSize(float minStickerSize) {
        this.minStickerSize = minStickerSize;
    }

    public boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public float getBitmapScale() {
        return getWidth() / (float) getHeight();
    }

    public float[] getDst() {
        return dst;
    }

    public float[] getRotateSrcPts() {
        return rotateSrcPts;
    }

    public void converse()
    {

        matrix.mapPoints(dst, srcPts);
        Log.d("setDataFordts","asd"  + dst.length );
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    Path getBoundPath() {
        boundPath.reset();
        boundPath.moveTo(dst[0], dst[1]);
        boundPath.lineTo(dst[2], dst[3]);
        boundPath.lineTo(dst[4], dst[5]);
        boundPath.lineTo(dst[6], dst[7]);
        boundPath.lineTo(dst[0], dst[1]);
        return boundPath;
    }
}
