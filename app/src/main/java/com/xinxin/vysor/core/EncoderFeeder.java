package com.xinxin.vysor.core;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.hardware.display.IDisplayManager;
import android.os.Build;
import android.view.IWindowManager;
import android.view.Surface;

import com.xinxin.vysor.core.virtualdisplay.SurfaceControlVirtualDisplayFactory;
import com.xinxin.vysor.core.virtualdisplay.WindowManagerHelper;

public class EncoderFeeder {

    public static Bitmap screenshot(IWindowManager wm, IDisplayManager dm) throws Exception {
        String surfaceClassName;
        Point size = SurfaceControlVirtualDisplayFactory.getCurrentDisplaySize(false);
        if (Build.VERSION.SDK_INT <= 17) {
            surfaceClassName = "android.view.Surface";
        } else {
            surfaceClassName = "android.view.SurfaceControl";
        }
        Bitmap b = (Bitmap) Class.forName(surfaceClassName).getDeclaredMethod("screenshot", new Class[]{Integer.TYPE, Integer.TYPE}).invoke(null, new Object[]{Integer.valueOf(size.x), Integer.valueOf(size.y)});
        int rotation = WindowManagerHelper.getRotation(wm, dm);
        if (rotation == Surface.ROTATION_0) {
            return b;
        }
        Matrix m = new Matrix();
        if (rotation == Surface.ROTATION_90) {
            m.postRotate(-90.0f);
        } else if (rotation == Surface.ROTATION_180) {
            m.postRotate(-180.0f);
        } else if (rotation == Surface.ROTATION_270) {
            m.postRotate(-270.0f);
        }
        return Bitmap.createBitmap(b, 0, 0, size.x, size.y, m, false);
    }
}
