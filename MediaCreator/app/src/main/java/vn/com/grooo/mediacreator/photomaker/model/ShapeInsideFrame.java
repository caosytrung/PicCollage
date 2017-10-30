package vn.com.grooo.mediacreator.photomaker.model;

/**
 * Created by trungcs on 8/15/17.
 */

public class ShapeInsideFrame  {
    private double left;
    private double righr;
    private double  top;
    private double bottom;
    private boolean status;
    private String uriImage;

    public ShapeInsideFrame() {
        left = 0;
        righr = 0;
        top = 0;
        bottom = 0;
    }

    public ShapeInsideFrame(double left, double righr, double top,
                            double bottom, String uriImage) {
        this.left = left;
        this.righr = righr;
        this.top = top;
        this.bottom = bottom;
        this.status = false;
        this.uriImage = uriImage;

    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getUriImage() {
        return uriImage;
    }

    public void setUriImage(String uriImage) {
        this.uriImage = uriImage;
    }

    public ShapeInsideFrame(double left, double righr,
                            double top, double bottom) {
        this.left = left;
        this.righr = righr;
        this.top = top;
        this.bottom = bottom;
        this.status = false;
    }

    public double getLeft() {

        return left;
    }

    public void setLeft(double left) {
        this.left = left;
    }

    public double getRighr() {
        return righr;
    }

    public void setRighr(double righr) {
        this.righr = righr;
    }

    public double getTop() {
        return top;
    }

    public void setTop(double top) {
        this.top = top;
    }

    public double getBottom() {
        return bottom;
    }

    public void setBottom(double bottom) {
        this.bottom = bottom;
    }
}
