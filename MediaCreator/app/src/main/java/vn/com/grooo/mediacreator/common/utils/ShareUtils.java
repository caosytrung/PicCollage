package vn.com.grooo.mediacreator.common.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

//import com.facebook.share.model.ShareLinkContent;
//import com.facebook.share.model.SharePhoto;
//import com.facebook.share.model.SharePhotoContent;
//import com.facebook.share.widget.ShareDialog;

import java.io.ByteArrayOutputStream;

/**
 * Created by Thaihn on 8/11/2017.
 */

public class ShareUtils {

//    public static void shareImageToTwitter(Bitmap bm, Context context, String msg) {
//        Uri uri = getImageUri(context, bm);
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_SEND);
//        intent.putExtra(Intent.EXTRA_TEXT, msg);
//        intent.setType("text/plain");
//        intent.putExtra(Intent.EXTRA_STREAM, uri);
//        intent.setType("image/jpeg");
//        intent.setPackage("com.twitter.android");
//        context.startActivity(intent);
//    }
//
//    public static void shareImageToFacebook(AppCompatActivity context, Bitmap bitmap) {
//        ShareDialog shareDialog = new ShareDialog(context);
//        SharePhoto photo = new SharePhoto.Builder()
//                .setBitmap(bitmap)
//                .build();
//        SharePhotoContent content = new SharePhotoContent.Builder()
//                .addPhoto(photo)
//                .build();
//        shareDialog.show(content);
//    }
//
//    public static void shareLinkToFacebook(AppCompatActivity context, String title, String desc, Uri url) {
//        ShareDialog shareDialog = new ShareDialog(context);
//        ShareLinkContent shareLinkContent;
//        if (shareDialog.canShow(ShareLinkContent.class)) {
//            shareLinkContent = new ShareLinkContent.Builder()
//                    .setContentTitle(title)
//                    .setContentDescription(desc)
//                    .setContentUrl(url)
//                    .build();
//            shareDialog.show(shareLinkContent);
//        }
//    }
//
//    public static boolean checkAppInstall(Context context, String uri) {
//        PackageManager pm = context.getPackageManager();
//        try {
//            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
//            return true;
//        } catch (PackageManager.NameNotFoundException e) {
//        }
//
//        return false;
//    }
//
//    public static Uri getImageUri(Context inContext, Bitmap inImage) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
//        return Uri.parse(path);
//    }
}
