package io.knifer.freebox.model.s2c;

import androidx.annotation.NonNull;

import java.util.Objects;

/**
 * 收藏影片
 * @author knifer
 */
public class SaveMovieCollectionDTO {

    /**
     * 影片ID
     */
    private String vodId;
    /**
     * 站点key
     */
    private String sourceKey;
    /**
     * 影片名称
     */
    private String vodName;
    /**
     * 影片海报
     */
    private String vodPic;

    public SaveMovieCollectionDTO() {
    }

    public SaveMovieCollectionDTO(String vodId, String sourceKey, String vodName, String vodPic) {
        this.vodId = vodId;
        this.sourceKey = sourceKey;
        this.vodName = vodName;
        this.vodPic = vodPic;
    }

    public String getVodId() {
        return vodId;
    }

    public void setVodId(String vodId) {
        this.vodId = vodId;
    }

    public String getSourceKey() {
        return sourceKey;
    }

    public void setSourceKey(String sourceKey) {
        this.sourceKey = sourceKey;
    }

    public String getVodName() {
        return vodName;
    }

    public void setVodName(String vodName) {
        this.vodName = vodName;
    }

    public String getVodPic() {
        return vodPic;
    }

    public void setVodPic(String vodPic) {
        this.vodPic = vodPic;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SaveMovieCollectionDTO that = (SaveMovieCollectionDTO) o;
        return Objects.equals(vodId, that.vodId) && Objects.equals(sourceKey, that.sourceKey) && Objects.equals(vodName, that.vodName) && Objects.equals(vodPic, that.vodPic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vodId, sourceKey, vodName, vodPic);
    }

    @NonNull
    @Override
    public String toString() {
        return "SaveMovieCollectionDTO{" +
                "vodId='" + vodId + '\'' +
                ", sourceKey='" + sourceKey + '\'' +
                ", vodName='" + vodName + '\'' +
                ", vodPic='" + vodPic + '\'' +
                '}';
    }
}
