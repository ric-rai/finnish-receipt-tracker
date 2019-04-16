package fi.frt.utilities;

import java.util.regex.Pattern;

public final class CurrencyUtils {
    public static final Pattern CURRENCY_PATTERN = Pattern.compile("^(?!(0\\d+\\.?\\d))([0-9]+(\\.[0-9]{1,2})?)$");
}