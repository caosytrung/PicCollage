package vn.com.grooo.mediacreator.photomaker.model;

import android.graphics.drawable.shapes.Shape;
import android.widget.RelativeLayout;

import java.util.List;

/**
 * Created by trungcs on 8/15/17.
 */

public class FrameObj {

    private List<ShapeInsideFrame> shapeList;

    public FrameObj() {
    }

    public FrameObj(List<ShapeInsideFrame> shapeList, String sourceFrame) {
        this.shapeList = shapeList;
    }

    public List<ShapeInsideFrame> getShapeList() {
        return shapeList;
    }

    public void setShapeList(List<ShapeInsideFrame> shapeList) {
        this.shapeList = shapeList;
    }

}
