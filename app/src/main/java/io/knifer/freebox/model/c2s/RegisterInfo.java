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

    public RegisterInfo() {
    }

    public RegisterInfo(String clientId, String clientName, int kType) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.kType = kType;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RegisterInfo that = (RegisterInfo) o;
        return kType == that.kType && Objects.equals(clientId, that.clientId) && Objects.equals(clientName, that.clientName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, clientName, kType);
    }

    @NonNull
    @Override
    public String toString() {
        return "RegisterInfo{" +
                "clientId='" + clientId + '\'' +
                ", clientName='" + clientName + '\'' +
                ", kType=" + kType +
                '}';
    }

    public static RegisterInfo of(String clientId, String clientName, int kType) {
        return new RegisterInfo(clientId, clientName, kType);
    }
}
