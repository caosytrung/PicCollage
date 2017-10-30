package vn.com.grooo.mediacreator.photomaker.broadcast;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import vn.com.grooo.mediacreator.photomaker.service.DownloadImageService;

/**
 * Created by trungcs on 8/28/17.
 */

public class OpenImageBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String uri = intent.getStringExtra("IMAGE_KEY");
        Intent intentIm = new Intent();
        intentIm.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentIm.setAction(Intent.ACTION_VIEW);
        intentIm.setDataAndType(Uri.parse("file://" + uri), "image/*");
        context.startActivity(intentIm);

        context.stopService(new Intent(context,DownloadImageService.class));
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(111);
    }
}
