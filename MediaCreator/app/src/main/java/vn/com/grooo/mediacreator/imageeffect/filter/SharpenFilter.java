package vn.com.grooo.mediacreator.imageeffect.filter;

import android.graphics.Bitmap;

import vn.com.grooo.mediacreator.imageeffect.filter.base.ConvolutionMatrix;

/**
 * Created by trungcs on 9/8/17.
 */

public class SharpenFilter {
    public static Bitmap sharpen(Bitmap src, double weight) {
        double[][] SharpConfig = new double[][] {
                { 0 , -2    , 0  },
                { -2, weight, -2 },
                { 0 , -2    , 0  }
        };
        ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
        convMatrix.applyConfig(SharpConfig);
        convMatrix.Factor = weight - 8;
        return ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
    }
}
