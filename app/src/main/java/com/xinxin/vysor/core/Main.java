package com.xinxin.vysor.core;

import android.graphics.Bitmap;
import android.hardware.display.IDisplayManager;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.IWindowManager;

import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.http.server.AsyncHttpServer;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;

public class Main {

    private static final String TAG = "Main";

    static AsyncServer server = new AsyncServer();

    static Looper looper;

    public static void main(String[] args) throws Exception {
        Log.d(TAG, "enter main...");
        Looper.prepare();
        looper = Looper.myLooper();

        Method getServiceMethod = Class.forName("android.os.ServiceManager").getDeclaredMethod("getService", new Class[]{String.class});

        final IWindowManager wm = IWindowManager.Stub.asInterface((IBinder) getServiceMethod.invoke(null, new Object[]{"window"}));
        final IDisplayManager dm = IDisplayManager.Stub.asInterface((IBinder) getServiceMethod.invoke(null, new Object[]{"display"}));


        AsyncHttpServer httpServer = new AsyncHttpServer();
        httpServer.get("/screenshot.jpg", (req, resp)-> {
            resp.getHeaders().set("Cache-Control", "no-cache");
            try {
                Bitmap bitmap = EncoderFeeder.screenshot(wm, dm);
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bout);
                bout.flush();
                resp.send("image/jpeg", bout.toByteArray());
            } catch (Exception e) {
                resp.code(500);
                resp.send(e.toString());
            }
        });

        httpServer.listen(server, 53518);

        Looper.loop();
        Log.d(TAG, "Loop done");
        httpServer.stop();
    }
}
