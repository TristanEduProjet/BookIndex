package fr.esgi.bookindex.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Simple class utility for permissions
 */

public final class PermissionUtils {
    private PermissionUtils() { }

    /**
     * check if application has permission requested since changes in API 23
     * @param activity
     * @param permission
     * @param permissionCode
     * @return {@code true} if API < 23, {@code false} else
     */
    public static boolean checkPermission(final @NonNull Activity activity, final @NonNull String permission, final @IntRange(from = 0) int permissionCode) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(activity, new String[]{permission}, permissionCode);
            else
                permissionAlreadyAccepted(activity, permission, permissionCode);
            return false;
        } else
            return true;
    }
    @RequiresApi(23)
    private static void permissionAlreadyAccepted(final @NonNull Activity activity, final @NonNull String permission, final @IntRange(from = 0) int permissionCode) {
        activity.onRequestPermissionsResult(permissionCode, new String[]{permission}, new int[]{PackageManager.PERMISSION_GRANTED});
    }

    public static boolean isGranted (final @NonNull Context context, final @NonNull String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
}
