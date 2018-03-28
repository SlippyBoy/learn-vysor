package com.xinxin.vysor.core.virtualdisplay;

import android.graphics.Point;
import android.hardware.display.IDisplayManager;
import android.os.Build;
import android.os.IBinder;
import android.view.DisplayInfo;
import android.view.IWindowManager;

import java.lang.reflect.Method;

public class SurfaceControlVirtualDisplayFactory {


    public static Point getCurrentDisplaySize(boolean rotate) {
        int rotation;
        try {
            Method getServiceMethod = Class.forName("android.os.ServiceManager").getDeclaredMethod("getService", new Class[]{String.class});
            Point displaySize = new Point();
            IWindowManager wm;
            if (Build.VERSION.SDK_INT >= 18) {
                wm = IWindowManager.Stub.asInterface((IBinder) getServiceMethod.invoke(null, new Object[]{"window"}));
                wm.getBaseDisplaySize(0, displaySize);
                try {
                    rotation = wm.getRotation();
                } catch (Error e) {
                    rotation = ((Integer) DisplayInfo.class.getDeclaredField("rotation").get(IDisplayManager.Stub.asInterface((IBinder) getServiceMethod.invoke(null, new Object[]{"display"})).getDisplayInfo(0))).intValue();
                }
            } else if (Build.VERSION.SDK_INT == 17) {
                DisplayInfo di = IDisplayManager.Stub.asInterface((IBinder) getServiceMethod.invoke(null, new Object[]{"display"})).getDisplayInfo(0);
                displaySize.x = ((Integer) DisplayInfo.class.getDeclaredField("logicalWidth").get(di)).intValue();
                displaySize.y = ((Integer) DisplayInfo.class.getDeclaredField("logicalHeight").get(di)).intValue();
                rotation = ((Integer) DisplayInfo.class.getDeclaredField("rotation").get(di)).intValue();
            } else {
                wm = IWindowManager.Stub.asInterface((IBinder) getServiceMethod.invoke(null, new Object[]{"window"}));
                wm.getRealDisplaySize(displaySize);
                rotation = wm.getRotation();
            }
            if (rotate && (rotation == 1 || rotation == 3)) {
                int swap = displaySize.x;
                displaySize.x = displaySize.y;
                displaySize.y = swap;
            }
            return displaySize;
        } catch (Exception e2) {
            throw new AssertionError(e2);
        }
    }

}
