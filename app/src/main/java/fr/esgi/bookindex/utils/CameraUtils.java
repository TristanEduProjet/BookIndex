package fr.esgi.bookindex.utils;

import android.Manifest;
import android.content.Context;
import android.support.annotation.NonNull;

//import com.google.android.cameraview.CameraViewConfig;
//import com.jaredrummler.android.device.DeviceName;

/**
 * Camera helper
 */
public final class CameraUtils {
    private CameraUtils() {
    }

    private static String deviceName = null;

    /**
     * set camera's global configuration
     */
    /*public static void configureCamera(final @NonNull Context context) {
        if (deviceName == null)
            DeviceName.with(context).request((info, error) -> {
                deviceName = info.getName();
                //https://github.com/google/cameraview/issues/184
                if ((deviceName != null) && (deviceName.contains("Xperia") && deviceName.contains("XZ") || deviceName.contains("Compact")))
                    CameraViewConfig.isForceCamera1 = true;
            });
    }*/

    /**
     * if app has camera permission
     * @param context
     * @return
     */
    public static boolean isGranted(final @NonNull Context context) {
        return PermissionUtils.isGranted(context, Manifest.permission.CAMERA);
    }
}
