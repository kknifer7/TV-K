package io.knifer.freebox.ui.listener;

@FunctionalInterface
public interface FreeBoxConnectionStatusChangeListener {

    void onChange(boolean newValue, String ipAddress);
}
