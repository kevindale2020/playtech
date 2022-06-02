package com.example.playtech;

import android.app.NotificationManager;
import android.app.Application;
import android.nfc.Tag;
import android.text.TextUtils;
import android.app.NotificationChannel;
import android.os.Build;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class AppController extends Application {
    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";
    private RequestQueue mRequestQueue;
    private static AppController mInstance;
    public static final String TAG = AppController.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("This is Channel 1");

            NotificationChannel channe2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Channel 2",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("This is Channel 2");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
            manager.createNotificationChannel(channe2);
        }
    }


    public static synchronized AppController getmInstance() {
        return mInstance;
    }

    public RequestQueue getmRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request, String tag) {

        request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag((TextUtils.isEmpty(tag) ? TAG :tag));
        getmRequestQueue().add(request);

        //request.setTag((TextUtils.isEmpty(tag) ? TAG :tag));
        //getmRequestQueue().add(request);
    }

    public <T> void addToRequestQueue(Request<T> request) {

        request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag(TAG);
        getmRequestQueue().add(request);

        //request.setTag(TAG);
        //getmRequestQueue().add(request);
    }

    public void cancelPendingRequest(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}