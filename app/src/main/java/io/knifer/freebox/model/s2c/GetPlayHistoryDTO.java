package io.knifer.freebox.model.s2c;

import androidx.annotation.NonNull;

import java.util.Objects;

/**
 * 获取观看历史
 * @author knifer
 */
public class GetPlayHistoryDTO {

    /**
     * 数据条数
     */
    private Integer limit;

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetPlayHistoryDTO that = (GetPlayHistoryDTO) o;
        return Objects.equals(limit, that.limit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(limit);
    }

    @NonNull
    @Override
    public String toString() {
        return "GetPlayHistoryDTO{" +
                "limit=" + limit +
                '}';
    }
}
