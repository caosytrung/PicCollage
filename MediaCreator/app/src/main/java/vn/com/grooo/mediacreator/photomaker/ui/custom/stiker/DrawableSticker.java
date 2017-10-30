package vn.com.grooo.mediacreator.photomaker.ui.custom.stiker;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Author: miaoyongjun
 * Date : 17/8/6
 */

public class DrawableSticker extends Sticker {
    private Drawable drawable;
    private Rect realBounds;

    public DrawableSticker(Drawable drawable) {
        this.drawable = drawable;
        realBounds = new Rect(0, 0, getWidth(), getHeight());
        init(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
    }


    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    @Override
    public void draw(@NonNull Canvas canvas,int color,boolean hasBorder) {
        canvas.save();
        canvas.concat(getMatrix());
        Log.d("Drawwa",hasBorder  + " ");

        drawable.setBounds(realBounds);
        drawable.draw(canvas);

        if (hasBorder){
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(color);
            int stroke = 8;
            paint.setStrokeWidth(stroke);
            Log.d("DrawableWidth", getWidth()   + "  ");

            canvas.drawLine(-stroke,-stroke/2,getWidth() + stroke,-stroke/2,paint);
            canvas.drawLine(-stroke/2,-stroke,-stroke/ 2,getHeight() + stroke,paint);

            canvas.drawLine(getWidth() + stroke / 2,-stroke,
                    getWidth() + stroke / 2,getHeight() + stroke ,paint);
            canvas.drawLine(-stroke ,getHeight() + stroke / 2,
                    getWidth() + stroke,getHeight() +  stroke/ 2,paint);
        }
        canvas.restore();

    }

    @Override
    public int getWidth() {
        return drawable.getIntrinsicWidth();

    }

    @Override
    public int getHeight() {
        return drawable.getIntrinsicHeight();
    }
}
