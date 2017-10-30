package vn.com.grooo.mediacreator.photomaker.ui.custom.frame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import java.util.List;

import vn.com.grooo.mediacreator.R;
import vn.com.grooo.mediacreator.photomaker.model.FrameObj;
import vn.com.grooo.mediacreator.photomaker.model.ShapeInsideFrame;

/**
 * Created by trungcs on 8/23/17.
 */

public class ImageFrame extends android.support.v7.widget.AppCompatImageView {
    private FrameObj mFrameObj;

    public ImageFrame(Context context) {
        super(context);
    }

    public ImageFrame(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageFrame(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FrameObj getmFrameObj() {
        return mFrameObj;
    }

    public void setmFrameObj(FrameObj mFrameObj) {
        this.mFrameObj = mFrameObj;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setColor(getResources().getColor(R.color.textLine));

        int width = getWidth();
        int height = getHeight();
        Rect rect = new Rect(0,0,width,height);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rect,paint);
        paint.setColor(Color.parseColor("#757575"));
        paint.setStyle(Paint.Style.FILL);

        super.onDraw(canvas);
        if (mFrameObj != null){
            int padding = 8;
            List<ShapeInsideFrame> shapeInsideFrameList = mFrameObj.
                    getShapeList();
            for (int i = 0 ; i < shapeInsideFrameList.size() ; i ++){

                ShapeInsideFrame shapeInsideFrame =
                        shapeInsideFrameList.get(i);
                int left = (int) (shapeInsideFrame.getLeft() * width/ 100);
                int right = (int) (shapeInsideFrame.getRighr() * width/ 100);
                int top = (int) (shapeInsideFrame.getTop() * height/ 100);
                int bottom = (int) (shapeInsideFrame.getBottom() * height/ 100);

                Log.d("asdasdasdaaa", width + "  == " +
                        left + " == " + (right - padding));

                Rect rectFrame = new Rect(left + padding ,
                        top + padding,right - padding,bottom - padding);

//                Rect rectBound = new Rect(left,top,right,bottom);

                canvas.drawRect(rectFrame,paint);
               /// paint.setColor(getResources().getColor(R.color.blueDack));
                ///paint.setStyle(Paint.Style.STROKE);
               // canvas.drawRect(rectBound,paint);
            }
        }
    }




}
