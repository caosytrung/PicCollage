#include <jni.h>
#include <string>
#include <android/bitmap.h>
#include <math.h>
#include <stdlib.h>

static int rgb_clamp(int value) {
    if(value > 255) {
        return 255;
    }
    if(value < 0) {
        return 0;
    }
    return value;
}
extern "C"
JNIEXPORT void JNICALL
Java_vn_com_grooo_mediacreator_imageeffect_filter_base_BaseFilterFactory_sepiaGreen(JNIEnv *env,
                                                                                    jclass type,
                                                                                    jobject bitmap,
                                                                                    jint green) {

    AndroidBitmapInfo  info;
    int ret;
    void* pixels;

    if ((ret = AndroidBitmap_getInfo(env, bitmap, &info)) < 0) {
        return;
    }
    if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        return;
    }

    if ((ret = AndroidBitmap_lockPixels(env, bitmap, &pixels)) < 0) {
    }

    int xx, yy, R, G, B;
    uint32_t* line;


    for(yy = 0; yy < info.height; yy++){
        line = (uint32_t*)pixels;
        for(xx =0; xx < info.width; xx++){

            R = (int) ((line[xx] & 0x00FF0000) >> 16);
            G = (int)((line[xx] & 0x0000FF00) >> 8);
            B = (int) (line[xx] & 0x00000FF );

           // G += green;
            if(G > 160) { G = 160; }

//            G += (0.5 * redBlue);
//            if(G > 255) { G = 255; }

//            B += (depth * blue);
//            if(B > 255) { B = 255; }

            line[xx] = (line[xx] & 0xFF000000) |
                       ((R << 16) & 0x00FF0000) |
                       ((G << 8) & 0x0000FF00) |
                       (B & 0x000000FF);
        }

        pixels = (char*)pixels + info.stride;
    }

    AndroidBitmap_unlockPixels(env, bitmap);

}

extern "C"
JNIEXPORT void JNICALL
Java_vn_com_grooo_mediacreator_imageeffect_filter_base_BaseFilterFactory_sepiaRedMaj(JNIEnv *env,
                                                                                     jclass type,
                                                                                     jobject bitmap,
                                                                                     jint red) {

    AndroidBitmapInfo  info;
    int ret;
    void* pixels;

    if ((ret = AndroidBitmap_getInfo(env, bitmap, &info)) < 0) {
        return;
    }
    if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        return;
    }

    if ((ret = AndroidBitmap_lockPixels(env, bitmap, &pixels)) < 0) {
    }

    int xx, yy, R, G, B;
    uint32_t* line;
    double GS_RED = 0.3;
    double GS_GREEN = 0.59;
    double GS_BLUE = 0.11;

    for(yy = 0; yy < info.height; yy++){
        line = (uint32_t*)pixels;
        for(xx =0; xx < info.width; xx++){

            R = (int) ((line[xx] & 0x00FF0000) >> 16);
            G = (int)((line[xx] & 0x0000FF00) >> 8);
            B = (int) (line[xx] & 0x00000FF );
            R = (int)(GS_RED * R + GS_GREEN * G + GS_BLUE * B);

            R += (0,4 * red);
            if(R > 255) { R = 255; }

//            G += (0.5 * redBlue);
//            if(G > 255) { G = 255; }

//            B += (depth * blue);
//            if(B > 255) { B = 255; }

            line[xx] = (line[xx] & 0xFF000000) |
                       ((R << 16) & 0x00FF0000) |
                       ((G << 8) & 0x0000FF00) |
                       (B & 0x000000FF);
        }

        pixels = (char*)pixels + info.stride;
    }

    AndroidBitmap_unlockPixels(env, bitmap);

}

extern "C"
JNIEXPORT void JNICALL
Java_vn_com_grooo_mediacreator_imageeffect_filter_base_BaseFilterFactory_sepiaRedGreen(JNIEnv *env,
                                                                                       jclass type,
                                                                                       jobject bitmap,
                                                                                       jint redGreen) {

    AndroidBitmapInfo  info;
    int ret;
    void* pixels;

    if ((ret = AndroidBitmap_getInfo(env, bitmap, &info)) < 0) {
        return;
    }
    if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        return;
    }

    if ((ret = AndroidBitmap_lockPixels(env, bitmap, &pixels)) < 0) {
    }

    int xx, yy, R, G, B;
    uint32_t* line;
    double GS_RED = 0.3;
    double GS_GREEN = 0.59;
    double GS_BLUE = 0.11;

    for(yy = 0; yy < info.height; yy++){
        line = (uint32_t*)pixels;
        for(xx =0; xx < info.width; xx++){

            R = (int) ((line[xx] & 0x00FF0000) >> 16);
            G = (int)((line[xx] & 0x0000FF00) >> 8);
            B = (int) (line[xx] & 0x00000FF );
            B = G = R = (int)(GS_RED * R + GS_GREEN * G + GS_BLUE * B);

            R += (0,5 * redGreen);
            if(R > 255) { R = 255; }

            G += (0.5 * redGreen);
            if(G > 255) { G = 255; }

//            B += (depth * blue);
//            if(B > 255) { B = 255; }

            line[xx] = (line[xx] & 0xFF000000) |
                       ((R << 16) & 0x00FF0000) |
                       ((G << 8) & 0x0000FF00) |
                       (B & 0x000000FF);
        }

        pixels = (char*)pixels + info.stride;
    }

    AndroidBitmap_unlockPixels(env, bitmap);

}

extern "C"
JNIEXPORT void JNICALL
Java_vn_com_grooo_mediacreator_imageeffect_filter_base_BaseFilterFactory_invert(JNIEnv *env,
                                                                                jclass type,
                                                                                jobject bitmap) {

    int color_max = 128;
    AndroidBitmapInfo  info;
    int ret;
    void* pixels;

    if ((ret = AndroidBitmap_getInfo(env, bitmap, &info)) < 0) {
        return;
    }
    if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        return;
    }

    if ((ret = AndroidBitmap_lockPixels(env, bitmap, &pixels)) < 0) {
    }




    int xx, yy, R, G, B;
    uint32_t* line;
    for(yy = 0; yy < info.height; yy++){
        line = (uint32_t*)pixels;
        for(xx =0; xx < info.width; xx++){

            R = (int) ((line[xx] & 0x00FF0000) >> 16);
            G = (int)((line[xx] & 0x0000FF00) >> 8);
            B = (int) (line[xx] & 0x00000FF );
            R = 255 - R;
            G = 255 - G;
            B = 255 - B;


            line[xx] = (line[xx] & 0xFF000000) |
                       ((R << 16) & 0x00FF0000) |
                       ((G << 8) & 0x0000FF00) |
                       (B & 0x000000FF);
        }

        pixels = (char*)pixels + info.stride;
    }
    AndroidBitmap_unlockPixels(env, bitmap);

}

extern "C"
JNIEXPORT void JNICALL
Java_vn_com_grooo_mediacreator_imageeffect_filter_base_BaseFilterFactory_snow(JNIEnv *env,
                                                                              jclass type,
                                                                              jobject bitmap,int color_max) {

    AndroidBitmapInfo  info;
    int ret;
    void* pixels;

    if ((ret = AndroidBitmap_getInfo(env, bitmap, &info)) < 0) {
        return;
    }
    if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        return;
    }

    if ((ret = AndroidBitmap_lockPixels(env, bitmap, &pixels)) < 0) {
    }




    int xx, yy, R, G, B;
    uint32_t* line;
    int randomCor;
    for(yy = 0; yy < info.height; yy++){
        line = (uint32_t*)pixels;
        for(xx =0; xx < info.width; xx++){

            R = (int) ((line[xx] & 0x00FF0000) >> 16);
            G = (int)((line[xx] & 0x0000FF00) >> 8);
            B = (int) (line[xx] & 0x00000FF );
            randomCor = rand() % color_max;
            if(R > randomCor && G > randomCor && B > randomCor) {
                R = color_max;
                G = color_max;
                B = color_max;
            }


            line[xx] = (line[xx] & 0xFF000000) |
                       ((R << 16) & 0x00FF0000) |
                       ((G << 8) & 0x0000FF00) |
                       (B & 0x000000FF);
        }

        pixels = (char*)pixels + info.stride;
    }
    AndroidBitmap_unlockPixels(env, bitmap);

}

extern "C"
JNIEXPORT void JNICALL
Java_vn_com_grooo_mediacreator_imageeffect_filter_base_BaseFilterFactory_contrast(JNIEnv *env,
                                                                                  jclass type,
                                                                                  jobject bitmap,
                                                                                  jdouble value) {

    AndroidBitmapInfo  info;
    int ret;
    void* pixels;

    if ((ret = AndroidBitmap_getInfo(env, bitmap, &info)) < 0) {
        return;
    }
    if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        return;
    }

    if ((ret = AndroidBitmap_lockPixels(env, bitmap, &pixels)) < 0) {
    }




    int xx, yy, R, G, B;
    uint32_t* line;
    double contrast = pow((100 + value) / 100, 2);
    for(yy = 0; yy < info.height; yy++){
        line = (uint32_t*)pixels;
        for(xx =0; xx < info.width; xx++){

            //extract the RGB values from the pixel
            R = (int) ((line[xx] & 0x00FF0000) >> 16);
            R = (int)(((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
            if(R < 0) { R = 0; }
            else if(R > 255) { R = 255; }

            G = (int)((line[xx] & 0x0000FF00) >> 8);
            G = (int)(((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
            if(G < 0) { G = 0; }
            else if(G > 255) { G = 255; }

            B = (int) (line[xx] & 0x00000FF );
            B = (int)(((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
            if(B < 0) { B = 0; }
            else if(B > 255) { B = 255; }


            line[xx] = (line[xx] & 0xFF000000) |
                       ((R << 16) & 0x00FF0000) |
                       ((G << 8) & 0x0000FF00) |
                       (B & 0x000000FF);
        }

        pixels = (char*)pixels + info.stride;
    }
    AndroidBitmap_unlockPixels(env, bitmap);

}

extern "C"
JNIEXPORT void JNICALL
Java_vn_com_grooo_mediacreator_imageeffect_filter_base_BaseFilterFactory_colorRGB(JNIEnv *env,
                                                                                  jclass type,
                                                                                  jobject bitmap,
                                                                                  jfloat R, jfloat G,
                                                                                  jfloat B) {
    R = R /100;
    G = G/100;
    B = B/100;
    AndroidBitmapInfo  info;
    int ret;
    void* pixels;

    if ((ret = AndroidBitmap_getInfo(env, bitmap, &info)) < 0) {
        return;
    }
    if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        return;
    }

    if ((ret = AndroidBitmap_lockPixels(env, bitmap, &pixels)) < 0) {
    }




    int xx, yy, red, green, blue;
    uint32_t* line;

    for(yy = 0; yy < info.height; yy++){
        line = (uint32_t*)pixels;
        for(xx =0; xx < info.width; xx++){

            //extract the RGB values from the pixel
            red = (int) ((line[xx] & 0x00FF0000) >> 16);
            green = (int)((line[xx] & 0x0000FF00) >> 8);
            blue = (int) (line[xx] & 0x00000FF );

            red = (int)(red * R);
            green = (int)(green * G);
            blue = (int)(blue * B);

            //manipulate each value
//            red = rgb_clamp((int)(red + value));
//            green = rgb_clamp((int)(green + value));
//            blue = rgb_clamp((int)(blue + value));
//
//            // set the new pixel back in
            line[xx] = (line[xx] & 0xFF000000) |
                       ((red << 16) & 0x00FF0000) |
                       ((green << 8) & 0x0000FF00) |
                       (blue & 0x000000FF);
        }

        pixels = (char*)pixels + info.stride;
    }
    AndroidBitmap_unlockPixels(env, bitmap);

}

extern "C"
JNIEXPORT void JNICALL
Java_vn_com_grooo_mediacreator_imageeffect_filter_base_BaseFilterFactory_colorDepth(JNIEnv *env,
                                                                                    jclass type,
                                                                                    jobject bitmap,
                                                                                    jint offset) {
    AndroidBitmapInfo  info;
    int ret;
    void* pixels;

    if ((ret = AndroidBitmap_getInfo(env, bitmap, &info)) < 0) {
        return;
    }
    if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        return;
    }

    if ((ret = AndroidBitmap_lockPixels(env, bitmap, &pixels)) < 0) {
    }




    int xx, yy, red, green, blue;
    uint32_t* line;

    for(yy = 0; yy < info.height; yy++){
        line = (uint32_t*)pixels;
        for(xx =0; xx < info.width; xx++){

            //extract the RGB values from the pixel
            red = (int) ((line[xx] & 0x00FF0000) >> 16);
            green = (int)((line[xx] & 0x0000FF00) >> 8);
            blue = (int) (line[xx] & 0x00000FF );
            red = ((red + (offset / 2)) - ((red + (offset / 2)) % offset) - 1);
            if(red < 0) { red = 0; }
            green = ((green + (offset / 2)) - ((green + (offset / 2)) % offset) - 1);
            if(green < 0) { green = 0; }
            blue = ((blue + (offset / 2)) - ((blue + (offset / 2)) % offset) - 1);
            if(blue < 0) { blue = 0; }

            //manipulate each value
//            red = rgb_clamp((int)(red + value));
//            green = rgb_clamp((int)(green + value));
//            blue = rgb_clamp((int)(blue + value));
//
//            // set the new pixel back in
            line[xx] = (line[xx] & 0xFF000000) |
                       ((red << 16) & 0x00FF0000) |
                       ((green << 8) & 0x0000FF00) |
                       (blue & 0x000000FF);
        }

        pixels = (char*)pixels + info.stride;
    }
    AndroidBitmap_unlockPixels(env, bitmap);

}

extern "C"
JNIEXPORT jstring JNICALL
Java_grooo_com_vn_mediacreator_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT void  JNICALL
Java_vn_com_grooo_mediacreator_imageeffect_filter_base_BaseFilterFactory_brightness(JNIEnv *env,
        jobject ,jobject  bitmap,jfloat  value
        ){

    AndroidBitmapInfo  info;
    int ret;
    void* pixels;

    if ((ret = AndroidBitmap_getInfo(env, bitmap, &info)) < 0) {
        return;
    }
    if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        return;
    }

    if ((ret = AndroidBitmap_lockPixels(env, bitmap, &pixels)) < 0) {
    }

    int xx, yy, red, green, blue;
    uint32_t* line;

    for(yy = 0; yy < info.height; yy++){
        line = (uint32_t*)pixels;
        for(xx =0; xx < info.width; xx++){

            //extract the RGB values from the pixel
            red = (int) ((line[xx] & 0x00FF0000) >> 16);
            green = (int)((line[xx] & 0x0000FF00) >> 8);
            blue = (int) (line[xx] & 0x00000FF );

            //manipulate each value
            red = rgb_clamp((int)(red + value));
            green = rgb_clamp((int)(green + value));
            blue = rgb_clamp((int)(blue + value));

            // set the new pixel back in
            line[xx] = (line[xx] & 0xFF000000) |
                       ((red << 16) & 0x00FF0000) |
                       ((green << 8) & 0x0000FF00) |
                       (blue & 0x000000FF);
        }

        pixels = (char*)pixels + info.stride;
    }

    AndroidBitmap_unlockPixels(env, bitmap);
}



