package vn.com.grooo.mediacreator.saveandroidloadproject.model;

/**
 * Created by trungcs on 9/14/17.
 */

public class ImageFrameInfor {

    private float scale;
    private String path;
    private float[] values;
    private float width;
    private float height;

    public float[] getValues() {
        return values;
    }

    public void setValues(float[] values) {
        this.values = values;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public ImageFrameInfor() {
    }



    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


}
