package fi.frt.utilities;

import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public final class DateUtils {
    public static final Pattern DATE_PATTERN = Pattern.compile("(^(((0[1-9]|1[0-9]|2[0-8])[.](0[1-9]|1[012]))|((29|30|31)[.](0[13578]|1[02]))|((29|30)[.](0[469]|11)))[.](19|[2-9][0-9])\\d\\d$)|(^29[.]02[.](19|[2-9][0-9])(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)$)");
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
}