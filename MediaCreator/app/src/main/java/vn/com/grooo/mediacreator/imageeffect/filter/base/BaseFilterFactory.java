package vn.com.grooo.mediacreator.imageeffect.filter.base;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trungcs on 9/8/17.
 */

public class BaseFilterFactory {
    public static final String ORIGINAL = "Origin";
    public static  final String BRIGHTNESS = "Brightness";
    public static  final String SEPIA_RED_BLUE = "Sepia";
    public static  final String SEPIA_RED_MAJ = "Sepia Red";

    public static  final String CONTRACT = "Contract";
    public static  final String SNOW = "Snow";
    public static  final String INVERT = "Invert";
    public static final String GREEN = "green";


    public static List<String> getFilterList(){

        List<String> filterList  = new ArrayList<>();
        filterList.add(ORIGINAL);
        filterList.add(BRIGHTNESS);
        filterList.add(SEPIA_RED_BLUE);
        filterList.add(SEPIA_RED_MAJ);

        filterList.add(SNOW);
        filterList.add(CONTRACT);
        filterList.add(INVERT);
        filterList.add(GREEN);

        return filterList;
    }
    static {
        System.loadLibrary("native-lib");
    }


    public static native void brightness(Bitmap bitmap,float value); // -100 -> 100
//    public static native void colorDepth(Bitmap bitmap,int offset); // 0 - 128
//    public static native void colorRGB(Bitmap bitmap,float R,float G,float B); // 0 - 200
    public static native void contrast(Bitmap bitmap,double value); // 0 - 100
    public static native void snow(Bitmap bitmap,int colorMax); //1 -256
    public static native void invert(Bitmap bitmap);
    public static native void sepiaRedGreen(Bitmap bitmap,int redBLue);
    public static native void sepiaRedMaj(Bitmap bitmap,int red);
    public static native void sepiaGreen(Bitmap bitmap,int green);


}
