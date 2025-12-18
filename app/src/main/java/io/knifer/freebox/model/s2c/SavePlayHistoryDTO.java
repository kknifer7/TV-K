package io.knifer.freebox.model.s2c;

import androidx.annotation.NonNull;

import java.util.Objects;

/**
 * 保存观看历史
 * @author knifer
 */
public class SavePlayHistoryDTO {

    /**
     * 站点key
     */
    private String sourceKey;

    /**
     * 影片ID
     */
    private String vodId;

    /**
     * 影片名称
     */
    private String vodName;

    /**
     * 影片海报
     */
    private String vodPic;

    /**
     * 播放的ep名称
     */
    private String episodeFlag;

    /**
     * 播放的源名称
     */
    private String playFlag;

    /**
     * 播放的ep索引
     */
    private int episodeIndex;

    /**
     * 播放的ep地址
     */
    private String episodeUrl;

    /**
     * 是否倒序
     */
    private boolean revSort;

    /**
     * 播放位置
     */
    private long position;

    /**
     * 影片时长
     */
    private long duration;

    public SavePlayHistoryDTO() {
    }

    public SavePlayHistoryDTO(String sourceKey, String vodId, String vodName, String vodPic, String episodeFlag, String playFlag, int episodeIndex, String episodeUrl, boolean revSort, long position, long duration) {
        this.sourceKey = sourceKey;
        this.vodId = vodId;
        this.vodName = vodName;
        this.vodPic = vodPic;
        this.episodeFlag = episodeFlag;
        this.playFlag = playFlag;
        this.episodeIndex = episodeIndex;
        this.episodeUrl = episodeUrl;
        this.revSort = revSort;
        this.position = position;
        this.duration = duration;
    }

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

    public String getEpisodeFlag() {
        return episodeFlag;
    }

    public void setEpisodeFlag(String episodeFlag) {
        this.episodeFlag = episodeFlag;
    }

    public String getPlayFlag() {
        return playFlag;
    }

    public void setPlayFlag(String playFlag) {
        this.playFlag = playFlag;
    }

    public int getEpisodeIndex() {
        return episodeIndex;
    }

    public void setEpisodeIndex(int episodeIndex) {
        this.episodeIndex = episodeIndex;
    }

    public String getEpisodeUrl() {
        return episodeUrl;
    }

    public void setEpisodeUrl(String episodeUrl) {
        this.episodeUrl = episodeUrl;
    }

    public boolean isRevSort() {
        return revSort;
    }

    public void setRevSort(boolean revSort) {
        this.revSort = revSort;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SavePlayHistoryDTO that = (SavePlayHistoryDTO) o;
        return episodeIndex == that.episodeIndex && revSort == that.revSort && position == that.position && duration == that.duration && Objects.equals(sourceKey, that.sourceKey) && Objects.equals(vodId, that.vodId) && Objects.equals(vodName, that.vodName) && Objects.equals(vodPic, that.vodPic) && Objects.equals(episodeFlag, that.episodeFlag) && Objects.equals(playFlag, that.playFlag) && Objects.equals(episodeUrl, that.episodeUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceKey, vodId, vodName, vodPic, episodeFlag, playFlag, episodeIndex, episodeUrl, revSort, position, duration);
    }

    @NonNull
    @Override
    public String toString() {
        return "SavePlayHistoryDTO{" +
                "sourceKey='" + sourceKey + '\'' +
                ", vodId='" + vodId + '\'' +
                ", vodName='" + vodName + '\'' +
                ", vodPic='" + vodPic + '\'' +
                ", episodeFlag='" + episodeFlag + '\'' +
                ", playFlag='" + playFlag + '\'' +
                ", episodeIndex=" + episodeIndex +
                ", episodeUrl='" + episodeUrl + '\'' +
                ", revSort=" + revSort +
                ", position=" + position +
                ", duration=" + duration +
                '}';
    }
}
