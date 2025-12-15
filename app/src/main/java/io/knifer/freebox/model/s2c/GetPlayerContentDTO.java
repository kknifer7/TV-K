package io.knifer.freebox.model.s2c;

import androidx.annotation.NonNull;

import java.util.Objects;

/**
 * 获取播放信息
 * @author Knifer
 */
public class GetPlayerContentDTO {

    /**
     * 站点key
     */
    private String sourceKey;

    /**
     * 播放标志
     */
    private String playFlag;

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

    public String getPlayFlag() {
        return playFlag;
    }

    public void setPlayFlag(String playFlag) {
        this.playFlag = playFlag;
    }

    public String getVodId() {
        return vodId;
    }

    public void setVodId(String vodId) {
        this.vodId = vodId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GetPlayerContentDTO that = (GetPlayerContentDTO) o;
        return Objects.equals(sourceKey, that.sourceKey) && Objects.equals(playFlag, that.playFlag) && Objects.equals(vodId, that.vodId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceKey, playFlag, vodId);
    }

    @NonNull
    @Override
    public String toString() {
        return "GetPlayerContentDTO{" +
                "sourceKey='" + sourceKey + '\'' +
                ", playFlag='" + playFlag + '\'' +
                ", vodId='" + vodId + '\'' +
                '}';
    }
}
