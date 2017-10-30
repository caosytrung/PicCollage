package vn.com.grooo.mediacreator.common.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trungcs on 10/4/17.
 */

public class UtilPermission {
    public static boolean checkPermissions(Activity act, int requestCode) {
        // tra ve true thi permission can check duoc kich hoat
        // tra ve false thi permission can check chua duoc kich hoat
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        // danh cho 23 tro len
        List<String> permissionRequestS = new ArrayList<>();

        if (ActivityCompat.checkSelfPermission(act,
                Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED) {
            permissionRequestS.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (ActivityCompat.checkSelfPermission(act,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED) {
            permissionRequestS.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ActivityCompat.checkSelfPermission(act,
                Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_DENIED) {
            permissionRequestS.add(Manifest.permission.CAMERA);
        }



        if (permissionRequestS.size() == 0) {
            return true;
        } else {
            String per[] = new String[permissionRequestS.size()];
            for (int i = 0; i < permissionRequestS.size(); i++) {
                per[i] = permissionRequestS.get(i);
            }
            //show dialog
            ActivityCompat.requestPermissions(act, per, requestCode);
            return false;
        }


    }

}
