package io.knifer.freebox.model.c2s;

import androidx.annotation.NonNull;

import java.util.Objects;

/**
 * 注册信息
 *
 * @author Knifer
 */
public class RegisterInfo {

    private String clientId;

    private String clientName;

    private int kType;

    private int protocolVersionCode;

    public RegisterInfo() {
    }

    public RegisterInfo(String clientId, String clientName, int kType, int protocolVersionCode) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.kType = kType;
        this.protocolVersionCode = protocolVersionCode;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public int getKType() {
        return kType;
    }

    public void setKType(int kType) {
        this.kType = kType;
    }

    public int getProtocolVersionCode() {
        return protocolVersionCode;
    }

    public void setProtocolVersionCode(int protocolVersionCode) {
        this.protocolVersionCode = protocolVersionCode;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RegisterInfo that = (RegisterInfo) o;
        return kType == that.kType && protocolVersionCode == that.protocolVersionCode && Objects.equals(clientId, that.clientId) && Objects.equals(clientName, that.clientName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, clientName, kType, protocolVersionCode);
    }

    @NonNull
    @Override
    public String toString() {
        return "RegisterInfo{" +
                "clientId='" + clientId + '\'' +
                ", clientName='" + clientName + '\'' +
                ", kType=" + kType +
                ", protocolVersionCode=" + protocolVersionCode +
                '}';
    }

    public static RegisterInfo of(
            String clientId, String clientName, int kType, int protocolVersionCode
    ) {
        return new RegisterInfo(clientId, clientName, kType, protocolVersionCode);
    }
}
