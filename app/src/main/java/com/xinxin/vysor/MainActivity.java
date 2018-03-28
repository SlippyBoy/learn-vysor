package com.xinxin.vysor;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.koushikdutta.async.http.server.AsyncHttpServer;

public class MainActivity extends AppCompatActivity {

    private AsyncHttpServer httpServer = new AsyncHttpServer();

    private boolean started;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
    }

    public void start() {
        if (started)
            return;

        started = true;


        httpServer.get("/", (req, resp) -> {
            resp.send("hello world");
        });

        httpServer.get("/app", (req, resp) -> {
            resp.send("sourceDir: " + getApplicationInfo().sourceDir);
        });

        httpServer.listen(5000);
    }

    public void stop() {
        if (!started)
            return;
        httpServer.stop();
    }

    public void startHttpServer(View view) {
        start();
    }

    public void stopHttpServer(View view) {
        stop();
    }
}
