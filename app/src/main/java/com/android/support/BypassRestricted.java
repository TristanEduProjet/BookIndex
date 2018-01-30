package com.android.support;

import android.support.annotation.NonNull;
import android.support.v7.view.menu.MenuBuilder;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Simple classe pour outre-passer la restriction posser sur certaines méthodes
 */
public final class BypassRestricted {
    private BypassRestricted() {;}

    public static void menuSetOptionalIconsVisible(@NonNull final MenuBuilder menu, final boolean isVisible) {
        //menu.setOptionalIconsVisible(isVisible);
        try {
            final Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
            method.setAccessible(true); //for old apis (before 2016, commit 002d5bbb223ba6742d1c20a9a87064fbe62cd78f)
            method.invoke(menu, isVisible);
        } catch (IllegalAccessException e) {
            Log.e("BypassRestricted", "menuSetOptionalIconsVisible", e);
        } catch (InvocationTargetException e) {
            Log.e("BypassRestricted", "menuSetOptionalIconsVisible", e);
        } catch (NoSuchMethodException e) {
            Log.e("BypassRestricted", "menuSetOptionalIconsVisible", e);
        }
    }
}
