package vn.com.grooo.mediacreator.common.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import vn.com.grooo.mediacreator.R;

/**
 * Created by Thaihn on 8/11/2017.
 */

public class AndroidShareUtils {

    private static final String DEFAULT_SHARE_TITLE = "Check XXX!";
    private static final String DEFAULT_SHARE_URL_PREFIX = "http://MARKET_URL";

    public static void performShare(ShareData shareData, Activity activity, Uri uri) {
        try {
            if (activity != null && shareData != null && (!(activity.isFinishing()))) {
                String title = shareData.getTitle();
                String shareUrl = shareData.getShareUrl();

//                if (StringUtils.isNullOrEmpty(shareUrl)) return;
//                if (StringUtils.isNullOrEmpty(title)) title = DEFAULT_SHARE_TITLE;

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                List<LabeledIntent> targetedShareIntents = new ArrayList<>();

                PackageManager pm = activity.getApplicationContext().getPackageManager();
                List<ResolveInfo> activityList =
                        pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                for (final ResolveInfo app : activityList) {
                    String packageName = app.activityInfo.packageName;
                    String className = app.activityInfo.name;

                    if (packageName.equalsIgnoreCase(activity.getPackageName())) {
                        continue;
                    }
                    Intent dummyIntent = new Intent();
                    dummyIntent.setAction(Intent.ACTION_SEND);
                    dummyIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    dummyIntent.setComponent(new ComponentName(packageName, className));
                    LabeledIntent targetedShareIntent = new LabeledIntent(dummyIntent,packageName, app.loadLabel(pm),app.icon);

                    if (packageName.equalsIgnoreCase("com.whatsapp")) {
                        if (uri != null) {
                            targetedShareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                            targetedShareIntent.setType("image/jpg");
                        } else {
                            targetedShareIntent.setType("text/plain");
                        }
                        targetedShareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
                        targetedShareIntent.putExtra(Intent.EXTRA_TEXT, activity.getString(R.string.share_text) + shareUrl);
                        targetedShareIntents.add(0, targetedShareIntent);
                    } else if (packageName.equalsIgnoreCase("com.facebook.orca")
                            || packageName.equalsIgnoreCase("com.facebook.katana")) {
                        targetedShareIntent.putExtra(Intent.EXTRA_TEXT, title + "\n" + shareUrl);
                        targetedShareIntent.setType("text/plain");
                        targetedShareIntents.add(0, targetedShareIntent);
                    } else if (packageName.equalsIgnoreCase("com.android.mms")
                            || (packageName.equalsIgnoreCase("com.google.android.talk"))) {
                        if (uri != null) {
                            targetedShareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                            targetedShareIntent.setType("image/jpg");
                        } else {
                            targetedShareIntent.setType("text/plain");
                        }
                        targetedShareIntent.putExtra(Intent.EXTRA_TEXT, title + "\n" + shareUrl);
                        targetedShareIntents.add(0, targetedShareIntent);
                    } else if (packageName.equalsIgnoreCase("com.google.android.gm")) {
                        if (uri != null) {
                            targetedShareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                            targetedShareIntent.setType("image/jpg");
                        } else {
                            targetedShareIntent.setType("text/plain");
                        }
                        targetedShareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
                        targetedShareIntent.putExtra(Intent.EXTRA_TEXT, activity.getString(R.string.share_text) + shareUrl);
                        targetedShareIntents.add(0, targetedShareIntent);
                    } else if (packageName.equalsIgnoreCase("com.twitter.android")
                            || packageName.equalsIgnoreCase("com.twidroid")
                            || packageName.equalsIgnoreCase("com.handmark.tweetcaster")
                            || packageName.equalsIgnoreCase("com.thedeck.android")) {
                        targetedShareIntent.setType("text/plain");
                        targetedShareIntent.putExtra(Intent.EXTRA_TEXT, activity.getString(R.string.share_text) + shareUrl);
                        targetedShareIntents.add(0, targetedShareIntent);
                    } else {
                        if (uri != null) {
                            targetedShareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                            targetedShareIntent.setType("image/jpg");
                        } else {
                            targetedShareIntent.setType("text/plain");
                        }
                        targetedShareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
                        targetedShareIntent.putExtra(Intent.EXTRA_TEXT, activity.getString(R.string.share_text) + shareUrl);
                        targetedShareIntents.add(targetedShareIntent);
                    }
                }

                Intent chooserIntent =
                        Intent.createChooser((targetedShareIntents.remove(targetedShareIntents.size() - 1)),
                                "Share on ..");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                        targetedShareIntents.toArray(new Parcelable[] {}));
                activity.startActivity(chooserIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class ShareData {
        String shareUrl;
        String title;

        public ShareData(String title,String shareUrl) {
            this.title = title;
            this.shareUrl = shareUrl;
        }

        public ShareData() {

        }

        public String getShareUrl() {
            return shareUrl;
        }

        public void setShareUrl(String shareUrl) {
            this.shareUrl = shareUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    private static Bitmap takeScreenShot(Activity activity, View myView) {
        Bitmap bitmap = null;
        try {
            View view;
            if (myView != null) {
                view = myView;
            } else {
                view = activity.getWindow().getDecorView();
            }
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache();
            Bitmap bitmap1 = view.getDrawingCache();
            if (myView != null) {
                bitmap =
                        Bitmap.createBitmap(bitmap1, 0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
                Bitmap bitmap2 =
                        BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_launcher);
                bitmap = combineImages(bitmap, bitmap2);
            } else {
                Rect frame = new Rect();
                activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                int statusBarHeight = frame.top;
                int width = activity.getWindowManager().getDefaultDisplay().getWidth();
                int height = activity.getWindowManager().getDefaultDisplay().getHeight();
                bitmap = Bitmap.createBitmap(bitmap1, 0, statusBarHeight, width, height - statusBarHeight);
            }
            view.destroyDrawingCache();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private static boolean savePic(Bitmap bitmap, File strFileName) {
        FileOutputStream fos = null;
        boolean isFileSaved = false;
        try {
            fos = new FileOutputStream(strFileName);
            if (fos != null && bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
                isFileSaved = true;
            }
        } catch (Exception e) {
            isFileSaved = false;
            e.printStackTrace();
        }
        return isFileSaved;
    }

    public static void performShareWithImage(ShareData shareData, Activity activity, View myView) {
        String file_path =
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + activity.getString(
                        R.string.app_name);
        File dir = new File(file_path);
        if (!dir.exists()) dir.mkdirs();
        File file = new File(dir, "product.jpg");
        Uri uri = Uri.fromFile(file);
        boolean isPicSaved = savePic(takeScreenShot(activity, myView), file);
        if (!isPicSaved) {
            performShare(shareData, activity, null);
        } else {
            performShare(shareData, activity, uri);
        }
    }

    public static Bitmap combineImages(Bitmap c, Bitmap s) {
        Bitmap cs = null;

        int width = c.getWidth(), height = c.getHeight();

        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(cs);

        comboImage.drawBitmap(c, 0f, 0f, null);
        comboImage.drawBitmap(s, c.getWidth() - s.getWidth(), 0f, null);

        return cs;
    }
}
