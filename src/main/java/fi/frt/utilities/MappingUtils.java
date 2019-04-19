package fi.frt.utilities;

import org.springframework.util.StringUtils;

import java.beans.Expression;
import java.beans.Statement;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public final class MappingUtils {
    public static <V> Map<String, V> toMap(Object... p) {
        return IntStream.range(0, p.length / 2)
                .mapToObj(i -> new Object[]{p[i * 2], p[i * 2 + 1]})
                .collect(Collectors.toMap(e -> String.valueOf(e[0]), e -> (V) e[1]));
    }

    public static void setProperty(Object target, String setterName, Object obj) {
        String methodName = "set" + StringUtils.capitalize(setterName);
        Statement stmt = new Statement(target, methodName, new Object[]{obj});
        try {
            stmt.execute();
        } catch (Exception ignored) {
        }
    }

    public static String getStringProperty(Object target, String getterName) {
        String methodName = "get" + StringUtils.capitalize(getterName);
        Expression expr = new Expression(target, methodName, null);
        try {
            expr.execute();
            return expr.getValue().toString();
        } catch (Exception ignored) {
        }
        return null;
    }

}
