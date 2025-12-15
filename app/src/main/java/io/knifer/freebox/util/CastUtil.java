package io.knifer.freebox.util;


/**
 * 类型转换工具类
 *
 * @author Knifer
 */
public class CastUtil {

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj) {
        if (obj == null) {
            return null;
        }

        return (T) obj;
    }
}
