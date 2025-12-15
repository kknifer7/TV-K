package io.knifer.freebox.model.s2c;

import androidx.annotation.NonNull;

import java.util.Objects;

/**
 * 获取详情信息参数
 * @author Knifer
 */
public class GetDetailContentDTO {

    /**
     * 站点key
     */
    private String sourceKey;

    /**
     * 视频ID
     */
    private String vodId;

    public String getSourceKey() {
        return sourceKey;
    }

    public void setSourceKey(String sourceKey) {
        this.sourceKey = sourceKey;
    }

    public String getVodId() {
        return vodId;
    }

    public void setVodId(String vodId) {
        this.vodId = vodId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetDetailContentDTO that = (GetDetailContentDTO) o;
        return Objects.equals(sourceKey, that.sourceKey) && Objects.equals(vodId, that.vodId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceKey, vodId);
    }

    @NonNull
    @Override
    public String toString() {
        return "GetDetailContentDTO{" +
                "sourceKey='" + sourceKey + '\'' +
                ", vodId='" + vodId + '\'' +
                '}';
    }
}
