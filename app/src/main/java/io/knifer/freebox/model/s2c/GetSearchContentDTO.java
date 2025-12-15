package io.knifer.freebox.model.s2c;

import androidx.annotation.NonNull;

import java.util.Objects;

/**
 * 影视搜索
 *
 * @author Knifer
 */
public class GetSearchContentDTO {

    /**
     * 站点key
     */
    private String sourceKey;

    /**
     * 关键字
     */
    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSourceKey() {
        return sourceKey;
    }

    public void setSourceKey(String sourceKey) {
        this.sourceKey = sourceKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetSearchContentDTO that = (GetSearchContentDTO) o;
        return Objects.equals(sourceKey, that.sourceKey) && Objects.equals(keyword, that.keyword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceKey, keyword);
    }

    @NonNull
    @Override
    public String toString() {
        return "GetSearchContentDTO{" +
                "sourceKey='" + sourceKey + '\'' +
                ", keyword='" + keyword + '\'' +
                '}';
    }
}
