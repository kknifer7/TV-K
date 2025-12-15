package io.knifer.freebox.model.s2c;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Objects;

/**
 * 获取指定分类信息
 * @author Knifer
 */
public class GetCategoryContentDTO {

    /**
     * 站点key
     */
    private String sourceKey;

    /**
     * 分类id
     */
    private String tid;

    /**
     * 是否过滤
     */
    private boolean filter;

    /**
     * 页数
     */
    private String page;

    /**
     * 筛选参数
     */
    private HashMap<String, String> extend;

    public GetCategoryContentDTO() {}

    public GetCategoryContentDTO(String sourceKey, String tid, boolean filter, String page, HashMap<String, String> extend) {
        this.sourceKey = sourceKey;
        this.tid = tid;
        this.filter = filter;
        this.page = page;
        this.extend = extend;
    }

    public String getSourceKey() {
        return sourceKey;
    }

    public void setSourceKey(String sourceKey) {
        this.sourceKey = sourceKey;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public boolean isFilter() {
        return filter;
    }

    public void setFilter(boolean filter) {
        this.filter = filter;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public HashMap<String, String> getExtend() {
        return extend;
    }

    public void setExtend(HashMap<String, String> extend) {
        this.extend = extend;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GetCategoryContentDTO that = (GetCategoryContentDTO) o;
        return filter == that.filter && Objects.equals(sourceKey, that.sourceKey) && Objects.equals(tid, that.tid) && Objects.equals(page, that.page) && Objects.equals(extend, that.extend);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceKey, tid, filter, page, extend);
    }

    @NonNull
    @Override
    public String toString() {
        return "GetCategoryContentDTO{" +
                "sourceKey='" + sourceKey + '\'' +
                ", tid='" + tid + '\'' +
                ", filter=" + filter +
                ", page=" + page +
                ", extend=" + extend +
                '}';
    }
}
