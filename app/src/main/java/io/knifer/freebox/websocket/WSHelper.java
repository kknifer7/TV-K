package io.knifer.freebox.websocket;

import com.fongmi.android.tv.Setting;
import com.orhanobut.logger.Logger;

import java.net.URI;
import java.net.URISyntaxException;

import io.knifer.freebox.ui.listener.FreeBoxConnectionStatusChangeListener;

/**
 * FreeBox WebSocket Helper
 * @author Knifer
 */
public final class WSHelper {

    private static WSClient client;

    private static volatile boolean initFlag = false;

    private static volatile String clientId;

    private static FreeBoxConnectionStatusChangeListener connectionStatusChangeListener;

    private static final String LOG_TAG = WSHelper.class.getSimpleName();

    public static boolean init(String clientId) {
        if (initFlag) {
            return false;
        }

        String address = Setting.getFreeBoxServiceAddress();
        int port = Setting.getFreeBoxServicePort();

        WSHelper.clientId = clientId;
        initFlag = true;
        if (address != null) {
            // 目前只支持ws协议
            connect(address, port, false);
        }
        Logger.t(LOG_TAG).i("WSHelper init successfully");

        return true;
    }

    private static void createClient(String address, int port, boolean safeFlag) {
        try {
            client = new WSClient(
                    new URI(safeFlag ? "wss" : "ws" + "://" + address + ":" + port),
                    clientId
            );
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        if (connectionStatusChangeListener != null) {
            client.setConnectionStatusListener(connectionStatusChangeListener);
        }
    }

    public static void connect(String address, int port, boolean safeFlag) {
        if (!initFlag) {
            throw new IllegalStateException("WSHelper not init");
        }
        if (isOpen()) {
            throw new IllegalStateException("WebSocket Service already connected");
        }
        createClient(address, port, safeFlag);
        client.connect();
    }

    public static boolean connectBlocking(String address, int port, boolean safeFlag) {
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
            client.connectBlocking();
        } catch (Exception e) {
            Logger.t(LOG_TAG).e("unknown exception: " + e.getMessage());

            return false;
        }

        return true;
    }

    public static boolean isOpen() {
        return client != null && client.isOpen();
    }

    public static boolean isClosed() {
        return client != null && client.isClosed();
    }

    public static boolean isClosing() {
        return client != null && client.isClosing();
    }

    public static void close() {
        if (canClose()) {
            client.close();
        }
    }

    public static void closeBlocking() {
        if (canClose()) {
            try {
                client.closeBlocking();
            } catch (InterruptedException ignored) {}
        }
    }

    private static boolean canClose() {
        return client != null && !client.isClosed() && !client.isClosing();
    }

    public static void setConnectionStatusListener(FreeBoxConnectionStatusChangeListener listener) {
        connectionStatusChangeListener = listener;
        if (client != null) {
            client.setConnectionStatusListener(listener);
        }
    }
}
