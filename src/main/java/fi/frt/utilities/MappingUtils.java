package fi.frt.utilities;

import org.springframework.util.StringUtils;

import java.beans.Expression;
import java.beans.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public final class MappingUtils {
    public static <V> Map<String, V> toStrMap(Object... p) {
        return IntStream.range(0, p.length / 2)
                .mapToObj(i -> new Object[]{p[i * 2], p[i * 2 + 1]})
                .filter((e) -> e[1] != null)
                .collect(Collectors.toMap(e -> String.valueOf(e[0]), e -> (V) e[1]));
    }

    public static <V> Map<String, V> toStrMap(Map<String, V> map1, Object... p) {
        Map<String, V> map2 = toStrMap(p);
        map2.forEach((k, v) -> map1.merge(k, v, (v1, v2) -> v2));
        return map1;
    }


    public static void setProperty(Object target, String setterName, Object obj) {
        String methodName = "set" + StringUtils.capitalize(setterName);
        Statement stmt = new Statement(target, methodName, new Object[]{obj});
        try {
            stmt.execute();
        } catch (Exception ignored) {
        }
    }

    public static String getStrProperty(Object target, String getterName) {
        return (String)getProperty(target, getterName);
    }

    public static Object getProperty(Object target, String getterName) {
        String methodName = "get" + StringUtils.capitalize(getterName);
        Expression expr = new Expression(target, methodName, null);
        try {
            expr.execute();
            return expr.getValue().toString();
        } catch (Exception ignored) {
        }
        return null;
    }

    public static String toLowerUnderscore(String string) {
        return string.replaceAll("([a-z\\d])([A-Z\\d])", "$1_$2").toLowerCase();
    }

    public static Map<String, Object> keysToLowerUnderscore(Map<String, Object> map) {
        Map<String, Object> newMap = new HashMap<>();
        map.forEach((k, v) -> newMap.put(toLowerUnderscore(k), v));
        return newMap;
    }

}
