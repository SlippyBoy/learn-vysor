package com.xinxin.vysor.core.virtualdisplay;

import android.hardware.display.IDisplayManager;
import android.os.Build;
import android.os.RemoteException;
import android.view.DisplayInfo;
import android.view.IWindowManager;

public class WindowManagerHelper {

    public static int getRotation(IWindowManager wm, IDisplayManager dm) throws RemoteException, NoSuchFieldException, IllegalAccessException {
        if (Build.VERSION.SDK_INT >= 18) {
            try {
                return wm.getRotation();
            } catch (Error e) {
                return ((Integer) DisplayInfo.class.getDeclaredField("rotation").get(dm.getDisplayInfo(0))).intValue();
            }
        } else if (Build.VERSION.SDK_INT != 17) {
            return wm.getRotation();
        } else {
            return ((Integer) DisplayInfo.class.getDeclaredField("rotation").get(dm.getDisplayInfo(0))).intValue();
        }
    }

}
