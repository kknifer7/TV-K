package io.knifer.freebox.websocket;

import android.content.Context;

import com.fongmi.android.tv.Setting;
import com.orhanobut.logger.Logger;

import java.net.URI;
import java.net.URISyntaxException;

import io.knifer.freebox.service.WebSocketService;
import io.knifer.freebox.ui.listener.FreeBoxConnectionStatusChangeListener;

public final class WSHelper {

    private WSClient client;
    private boolean initFlag = false;
    private String clientId;
    private FreeBoxConnectionStatusChangeListener connectionStatusChangeListener;
    private final Context appContext;
    private boolean serviceStarted = false;

    private static final String LOG_TAG = WSHelper.class.getSimpleName();
    private static volatile WSHelper instance;

    private WSHelper(Context context) {
        this.appContext = context.getApplicationContext();
    }

    public static WSHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (WSHelper.class) {
                if (instance == null) {
                    instance = new WSHelper(context);
                }
            }
        }
        return instance;
    }

    public boolean init(String clientId) {
        if (initFlag) {
            return false;
        }

        String address = Setting.getFreeBoxServiceAddress();
        int port = Setting.getFreeBoxServicePort();

        this.clientId = clientId;
        initFlag = true;
        if (address != null) {
            connect(address, port, false);
        }
        Logger.t(LOG_TAG).i("WSHelper init successfully");

        return true;
    }

    private void createClient(String address, int port, boolean safeFlag) {
        try {
            client = new WSClient(
                    appContext,
                    new URI(safeFlag ? "wss" : "ws" + "://" + address + ":" + port),
                    clientId,
                    new WSClient.ConnectionListener() {
                        @Override
                        public void onConnected() {
                            if (!serviceStarted) {
                                WebSocketService.startService(appContext);
                                serviceStarted = true;
                                Logger.t(LOG_TAG).d("Connected, service started");
                            }
                        }

                        @Override
                        public void onDisconnected() {
                            if (serviceStarted) {
                                WebSocketService.stopService(appContext);
                                serviceStarted = false;
                                Logger.t(LOG_TAG).d("Disconnected, service stopped");
                            }
                        }
                    }
            );
        } catch (URISyntaxException e) {
            Logger.t(LOG_TAG).i("createClient failed, address illegal", e);
        }
        if (connectionStatusChangeListener != null) {
            client.setConnectionStatusListener(connectionStatusChangeListener);
        }
    }

    public void connect(String address, int port, boolean safeFlag) {
        if (!initFlag) {
            throw new IllegalStateException("WSHelper not init");
        }
        if (isOpen()) {
            throw new IllegalStateException("WebSocket Service already connected");
        }

        createClient(address, port, safeFlag);
        client.connect();
    }

    public boolean connectBlocking(String address, int port, boolean safeFlag) {
        if (!initFlag) {
            Logger.t(LOG_TAG).e("WSHelper not init");
            return false;
        }
        if (isOpen()) {
            Logger.t(LOG_TAG).e("WebSocket Service already connected");
            return false;
        }

        createClient(address, port, safeFlag);
        try {
            return client.connectBlocking();
        } catch (Exception e) {
            Logger.t(LOG_TAG).e("unknown exception: " + e.getMessage());
            return false;
        }
    }

    public boolean isOpen() {
        return client != null && client.isOpen();
    }

    public void close() {
        if (canClose()) {
            client.close();
        }
        if (serviceStarted) {
            WebSocketService.stopService(appContext);
            serviceStarted = false;
        }
    }

    public void closeBlocking() {
        if (canClose()) {
            try {
                client.closeBlocking();
            } catch (InterruptedException ignored) {}
        }
        if (serviceStarted) {
            WebSocketService.stopService(appContext);
            serviceStarted = false;
        }
    }

    private boolean canClose() {
        return client != null && !client.isClosed() && !client.isClosing();
    }

    public void setConnectionStatusListener(FreeBoxConnectionStatusChangeListener listener) {
        connectionStatusChangeListener = listener;
        if (client != null) {
            client.setConnectionStatusListener(listener);
        }
    }
}