package vn.com.grooo.mediacreator.saveandroidloadproject.model;

import java.io.Serializable;

/**
 * Created by trungcs on 9/14/17.
 */

public class CollageOb implements Serializable {
    private int id;
    private int frameIndex;
    private String imageFrame;
    private String sticker;
    private int border;
    private String fileName;
    private int color;

    public CollageOb(int frameIndex, String imageFrame,
                     String sticker, int border, String fileName, int color) {
        this.frameIndex = frameIndex;
        this.imageFrame = imageFrame;
        this.sticker = sticker;
        this.border = border;
        this.fileName = fileName;
        this.color = color;
    }

    public CollageOb(int id, int frameIndex, String imageFrame,
                     String sticker, int border, String fileName, int color) {

        this.id = id;
        this.frameIndex = frameIndex;
        this.imageFrame = imageFrame;
        this.sticker = sticker;
        this.border = border;
        this.fileName = fileName;
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getBorder() {
        return border;
    }

    public void setBorder(int border) {
        this.border = border;
    }



    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFrameIndex() {
        return frameIndex;
    }

    public void setFrameIndex(int frameIndex) {
        this.frameIndex = frameIndex;
    }

    public String getImageFrame() {
        return imageFrame;
    }

    public void setImageFrame(String imageFrame) {
        this.imageFrame = imageFrame;
    }

    public String getSticker() {
        return sticker;
    }

    public void setSticker(String sticker) {
        this.sticker = sticker;
    }
}
