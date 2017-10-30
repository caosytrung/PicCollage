package vn.com.grooo.mediacreator.photomaker.model;

import android.graphics.Bitmap;

/**
 * Created by trungcs on 8/22/17.
 */

public class IntentBitmap {
    private int result;
    private Bitmap bitmap;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public IntentBitmap(int result, Bitmap bitmap) {

        this.result = result;
        this.bitmap = bitmap;
    }
}
