package vn.com.grooo.mediacreator.saveandroidloadproject.model;

/**
 * Created by trungcs on 9/14/17.
 */

public class StickerInfor {
    public static final int  TYPE_1 = 1 ;
    public static final int  TYPE_2 = 2 ;
    public static final int  TYPE_3 = 3 ;

    public StickerInfor() {
    }
//    public StickerInfor(float degree, float translateX,
//                        float translateY,
//                        float width, float height, int type, String data) {
//        this.degree = degree;
//        this.translateX = translateX;
//        this.translateY = translateY;
//        this.width = width;
//        this.height = height;
//        this.type = type;
//        this.data = data;
//    }

//    private float degree;
//    private float translateX;
//    private float translateY;
    private float width;
    private float height;
    private int type;
    private String data;
    private float[] values;

    public StickerInfor(float width, float height, int type, String data, float[] values) {
        this.width = width;
        this.height = height;
        this.type = type;
        this.data = data;
        this.values = values;
    }

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

//    public float getDegree() {
//        return degree;
//    }
//
//    public void setDegree(float degree) {
//        this.degree = degree;
//    }
//
//    public float getTranslateX() {
//        return translateX;
//    }
//
//    public void setTranslateX(float translateX) {
//        this.translateX = translateX;
//    }
//
//
//    public float getTranslateY() {
//        return translateY;
//    }
//
//    public void setTranslateY(float translateY) {
//        this.translateY = translateY;
//    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
