package fi.frt.utilities;

import java.util.regex.Pattern;

/**
 * Luokkaan on määritelty vakiota käytettäväksi valuuttojen käsittelyyn.
 */
public final class CurrencyUtils {
    /**
     * Pattern, jota käytetään syötettyjen valuuttamäärien oikean muodon tarkistamiseen.
     */
    public static final Pattern CURRENCY_PATTERN = Pattern.compile("^(?!(0\\d+\\.?\\d))([0-9]+(\\.[0-9]{1,2})?)$");
}