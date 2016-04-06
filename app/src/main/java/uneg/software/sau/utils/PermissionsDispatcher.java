package uneg.software.sau.utils;

import android.app.AlarmManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.PermissionUtils;
import uneg.software.sau.activities.AlertarActivity;
import uneg.software.sau.activities.NoticiasActivity;
import uneg.software.sau.fragments.NoticiasFragment;

/**
 * Created by Jhonny on 21/3/2016.
 */
public class PermissionsDispatcher {
    private static final int REQUEST = 1;

    private static final String[] PERMISSIONS = new String[] {"android.permission.WRITE_EXTERNAL_STORAGE","android.permission.ACCESS_COARSE_LOCATION","android.permission.ACCESS_FINE_LOCATION","android.permission.CAMERA"};

    private PermissionsDispatcher() {
    }

    public static void showDialogPermissions(NoticiasActivity target) {
        if (PermissionUtils.hasSelfPermissions(target, PERMISSIONS)) {
            target.openAlertar();
        } else {
            ActivityCompat.requestPermissions(target, PERMISSIONS, REQUEST);
        }
    }

    public static void onRequestPermissionsResult(NoticiasActivity target, int requestCode, int[] grantResults) {
        if (Build.VERSION.SDK_INT  <= Build.VERSION_CODES.KITKAT) {
            target.openAlertar();
            return;
        }
        switch (requestCode) {
            case REQUEST:
                if (!PermissionUtils.hasSelfPermissions(target, PERMISSIONS)) {
                    target.finish();
                }else
                {
                    target.openAlertar();
                }
                break;
            default:
                break;
        }
    }
}