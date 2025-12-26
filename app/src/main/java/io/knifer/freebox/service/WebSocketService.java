package io.knifer.freebox.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.fongmi.android.tv.R;
import com.orhanobut.logger.Logger;

public class WebSocketService extends Service {

    private PowerManager.WakeLock wakeLock;
    private WifiManager.WifiLock wifiLock;
    private String titleText;

    private static final String CHANNEL_ID = "freebox_websocket_channel";
    private static final int NOTIFICATION_ID = 1001;
    private static final String LOG_TAG = WebSocketService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        titleText = getBaseContext().getString(R.string.freebox_kebsocket_service);
        createNotificationChannel();
        acquireLocks();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(NOTIFICATION_ID, createNotification());
        return START_STICKY;
    }

    private void acquireLocks() {
        try {
            PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (powerManager != null && wakeLock == null) {
                wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "WebSocketService:WakeLock");
                wakeLock.acquire();
            }

            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm != null ? cm.getActiveNetworkInfo() : null;
            boolean isWifiConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting()
                    && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;

            if (isWifiConnected) {
                WifiManager wifiManager = (WifiManager) getApplicationContext()
                        .getSystemService(Context.WIFI_SERVICE);
                if (wifiManager != null && wifiLock == null) {
                    wifiLock = wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF, "WebSocketService:WifiLock");
                    wifiLock.acquire();
                }
            }
        } catch (Exception e) {
            Logger.t(LOG_TAG).e("acquireLocks error", e);
        }
    }

    private void releaseLocks() {
        try {
            if (wakeLock != null && wakeLock.isHeld()) {
                wakeLock.release();
                wakeLock = null;
            }

            if (wifiLock != null && wifiLock.isHeld()) {
                wifiLock.release();
                wifiLock = null;
            }
        } catch (Exception e) {
            Logger.t(LOG_TAG).e("releaseLocks error", e);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    titleText,
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription(titleText);
            channel.setShowBadge(false);

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private Notification createNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(titleText)
                .setContentText(titleText)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseLocks();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void startService(Context context) {
        if (context == null) {
            Logger.t(LOG_TAG).e("Context is null, cannot start service");
            return;
        }

        Intent intent = new Intent(context, WebSocketService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    public static void stopService(Context context) {
        if (context == null) {
            Logger.t(LOG_TAG).e("Context is null, cannot stop service");
            return;
        }

        Intent intent = new Intent(context, WebSocketService.class);
        context.stopService(intent);
    }
}