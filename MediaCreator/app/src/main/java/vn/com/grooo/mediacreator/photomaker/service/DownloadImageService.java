package vn.com.grooo.mediacreator.photomaker.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import vn.com.grooo.mediacreator.R;

/**
 * Created by trungcs on 8/28/17.
 */

public class DownloadImageService extends Service {
    private String uri;
    private PendingIntent pendingIntentOpenNote;
  //  private StopServiceBroadcast broadcast;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        uri = intent.getStringExtra("URI_KEY");
        createNotification();
//        broadcast = new StopServiceBroadcast();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction("OPEN_IMAGE");
//        registerReceiver(broadcast, filter);
        return START_NOT_STICKY;
    }

    private void createNotification() {
        Intent intentOpen = new Intent();
        intentOpen.setAction("OPEN_IMAGE");
        intentOpen.putExtra("IMAGE_KEY",uri);
        pendingIntentOpenNote = PendingIntent.
                getBroadcast(this, 1, intentOpen,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.icon_save);

        builder.setContentTitle("Media Creator");
        builder.setContentText(uri);
        builder.setContentIntent(pendingIntentOpenNote);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(111, builder.build());

    }




    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
