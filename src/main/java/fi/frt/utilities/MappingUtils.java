package fi.frt.utilities;

import org.springframework.util.StringUtils;

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

    public static void setProperty(Object target, String setterName, Object... objects) {
        String methodName = "set" + StringUtils.capitalize(setterName);
        Statement stmt = new Statement(target, methodName, objects);
        try {
            stmt.execute();
        } catch (Exception ignored) {
        }
    }

}
