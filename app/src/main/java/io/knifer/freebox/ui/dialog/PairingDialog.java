package io.knifer.freebox.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.fongmi.android.tv.R;
import com.fongmi.android.tv.Setting;
import com.fongmi.android.tv.databinding.DialogFreeBoxPairingBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import io.knifer.freebox.websocket.WSHelper;

public class PairingDialog {

    private DialogFreeBoxPairingBinding binding;
    private AlertDialog dialog;

    /**
     * 静态工厂方法，从 Activity 创建
     */
    public static PairingDialog create(Activity activity) {
        return new PairingDialog(activity);
    }

    /**
     * 私有构造函数
     */
    private PairingDialog(Activity activity) {
        init(activity);
    }

    /**
     * 初始化对话框和视图
     */
    private void init(Activity activity) {
        binding = DialogFreeBoxPairingBinding.inflate(LayoutInflater.from(activity));
        dialog = new MaterialAlertDialogBuilder(activity)
                .setView(binding.getRoot())
                .setCancelable(true)
                .create();
        initView(activity);
        initEvent();
    }

    /**
     * 初始化视图状态
     */
    private void initView(Activity activity) {
        // 获取并显示本机IP
        String localIp = getLocalIPAddress(activity);
        binding.tvAddressForFreeBox.setText(String.format(
                activity.getString(R.string.dialog_local_ip), localIp
        ));

        // 从设置中读取已保存的地址和端口
        String savedAddress = Setting.getFreeBoxServiceAddress();
        int savedPort = Setting.getFreeBoxServicePort();

        binding.freeBoxServiceAddressInput.setText(savedAddress != null ? savedAddress : "");
        binding.freeBoxServicePortInput.setText(savedPort > 0 ? String.valueOf(savedPort) : "");

        // 初始隐藏光标
        binding.freeBoxServiceAddressInput.setCursorVisible(false);
        binding.freeBoxServicePortInput.setCursorVisible(false);

        // 根据当前连接状态更新按钮
        updateButtonState();
    }

    /**
     * 初始化事件监听器
     */
    private void initEvent() {
        // 点击输入框时显示光标
        binding.freeBoxServiceAddressInput.setOnClickListener(v ->
                binding.freeBoxServiceAddressInput.setCursorVisible(true));
        binding.freeBoxServicePortInput.setOnClickListener(v ->
                binding.freeBoxServicePortInput.setCursorVisible(true));

        // 输入框焦点变化时切换光标可见性
        binding.freeBoxServiceAddressInput.setOnFocusChangeListener((v, hasFocus) ->
                binding.freeBoxServiceAddressInput.setCursorVisible(hasFocus));
        binding.freeBoxServicePortInput.setOnFocusChangeListener((v, hasFocus) ->
                binding.freeBoxServicePortInput.setCursorVisible(hasFocus));

        // 连接/重连按钮点击
        binding.freeBoxServiceConnect.setOnClickListener(this::onConnect);

        // 断开按钮点击
        binding.freeBoxServiceDisConnect.setOnClickListener(this::onDisconnect);

        // 点击对话框背景区域时隐藏软键盘
        binding.getRoot().setOnClickListener(v -> hideSoftInput());

        // 软键盘操作绑定
        binding.freeBoxServiceAddressInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                binding.freeBoxServicePortInput.requestFocus();
                binding.freeBoxServicePortInput.setCursorVisible(true);

                return true;
            }

            return false;
        });
        binding.freeBoxServicePortInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideSoftInput();
                onConnect(v);

                return true;
            }
            return false;
        });
    }

    /**
     * 获取本机IP地址
     */
    private String getLocalIPAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null && wifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            if (ipAddress != 0) {
                return ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "."
                        + (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));
            }
        }
        return String.format("<%s>", context.getString(R.string.dialog_local_ip_failed));
    }

    /**
     * 连接/重连按钮点击处理
     */
    private void onConnect(View view) {
        Editable addressEditable = binding.freeBoxServiceAddressInput.getText();
        Editable portEditable = binding.freeBoxServicePortInput.getText();
        Context context;

        if (addressEditable == null || portEditable == null) {
            return;
        }

        String address = addressEditable.toString().trim();
        String portStr = portEditable.toString().trim();

        context = view.getContext();
        if (address.isEmpty() || portStr.isEmpty()) {
            Toast.makeText(context, R.string.dialog_pairing_form_invalid, Toast.LENGTH_SHORT).show();
            return;
        }

        int port;
        try {
            port = Integer.parseInt(portStr);
        } catch (NumberFormatException e) {
            Toast.makeText(context, R.string.dialog_pairing_port_invalid, Toast.LENGTH_SHORT).show();
            return;
        }

        // 禁用按钮防止重复操作
        setButtonsEnabled(false);
        Toast.makeText(context, R.string.dialog_pairing, Toast.LENGTH_SHORT).show();

        // 在新线程中执行网络连接
        new Thread(() -> {
            boolean success;
            WSHelper.close(); // 关闭现有连接
            success = WSHelper.connectBlocking(address, port, false); // 尝试连接
            Window window = dialog.getWindow();

            // 回到主线程更新UI
            if (window != null) {
                window.getDecorView();
                dialog.getWindow().getDecorView().post(() -> {
                    if (success) {
                        // 保存成功连接的信息
                        Setting.putFreeBoxServiceAddress(address);
                        Setting.putFreeBoxServicePort(port);
                        Toast.makeText(view.getContext(), R.string.dialog_pairing_success, Toast.LENGTH_SHORT).show();
                        dialog.dismiss(); // 连接成功后关闭对话框
                    } else {
                        Toast.makeText(view.getContext(), R.string.dialog_pairing_failed, Toast.LENGTH_SHORT).show();
                    }
                    setButtonsEnabled(true);
                    updateButtonState();
                });
            }
        }).start();
    }

    /**
     * 断开连接按钮点击处理
     */
    private void onDisconnect(View view) {
        if (!WSHelper.isOpen()) {
            return;
        }

        setButtonsEnabled(false);
        new Thread(() -> {
            WSHelper.closeBlocking(); // 阻塞式关闭连接

            // 回到主线程更新UI
            if (dialog.getWindow() != null) {
                dialog.getWindow().getDecorView();
                dialog.getWindow().getDecorView().post(() -> {
                    Toast.makeText(view.getContext(), R.string.dialog_disconnected, Toast.LENGTH_SHORT).show();
                    setButtonsEnabled(true);
                    updateButtonState();
                });
            }
        }).start();
    }

    /**
     * 根据连接状态更新按钮文本和可用性
     */
    private void updateButtonState() {
        binding.freeBoxServiceDisConnect.setEnabled(WSHelper.isOpen());
    }

    /**
     * 设置按钮的启用/禁用状态
     */
    private void setButtonsEnabled(boolean enabled) {
        binding.freeBoxServiceConnect.setEnabled(enabled);
        binding.freeBoxServiceDisConnect.setEnabled(enabled);
    }

    /**
     * 隐藏软键盘
     */
    private void hideSoftInput() {
        binding.freeBoxServiceAddressInput.clearFocus();
        binding.freeBoxServicePortInput.clearFocus();
        View focusedView = dialog.getCurrentFocus();
        if (focusedView != null) {
            android.view.inputmethod.InputMethodManager imm =
                    (android.view.inputmethod.InputMethodManager) dialog.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
    }

    /**
     * 显示对话框
     */
    public void show() {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }
}