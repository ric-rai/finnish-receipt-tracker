package fi.frt.utilities;

import org.springframework.util.StringUtils;

import java.beans.Expression;
import java.beans.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Luokka tarjoaa apumetodeja, joita käytetään pääasiassa datamallien konversiossa.
 */
public final class MappingUtils {
    /**
     * Luo annetuista argumenttipareista Map-olion. Argumenttien määrän tulee olla parillinen; ensimmäisestä
     * argumentista tulee avain, ja toisesta argumentista arvo avaimelle. Näin toimitaan jokaisen argumenttiparin
     * kohdalla.
     *
     * @param p "avain1", arvo1, "avain2", arvo2 ...
     * @return Map
     */
    public static <V> Map<String, V> toStrMap(Object... p) {
        return IntStream.range(0, p.length / 2)
                .mapToObj(i -> new Object[]{p[i * 2], p[i * 2 + 1]})
                .filter((e) -> e[1] != null)
                .collect(Collectors.toMap(e -> String.valueOf(e[0]), e -> (V) e[1]));
    }

    /**
     * Luo annetusta Map-oliosta ja olioargumenttipareista uuden Map-olion sulauttamalla argumenttiparit annettuun
     * Map-olioon. Käyttää sisäisesti Map-rajapinnan merge()-metodia.
     *
     * @param map1 Map-olio, johon argumenttiparit sulautetaan
     * @param p "avain1", arvo1, "avain2", arvo2 ...
     * @return Map
     */
    public static <V> Map<String, V> toStrMap(Map<String, V> map1, Object... p) {
        Map<String, V> map2 = toStrMap(p);
        map2.forEach((k, v) -> map1.merge(k, v, (v1, v2) -> v2));
        return map1;
    }

    /**
     * Apumetodi, joka käyttää java.beans.Statement -luokkaa asettaakseen annettun olion annetun nimisen attribuutin
     * annettuun arvoon. Oliolla on oltava kyseiselle attribuutille asetettu Java Beans -käytännön mukainen set-metodi.
     *
     * @param target       Olio, jonka attribuutin arvo asetetaan
     * @param propertyName Asetettavan attribuutin nimi
     * @param obj          Attribuuttiin asetettava arvo
     */
    public static void setProperty(Object target, String propertyName, Object obj) {
        String methodName = "set" + StringUtils.capitalize(propertyName);
        Statement stmt = new Statement(target, methodName, new Object[]{obj});
        try {
            stmt.execute();
        } catch (Exception e) {
            System.out.println("Setting the property " + propertyName + " of target " + target.toString()
                    + " failed with the following message:");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Apumetodi, joka käyttää java.beans.Expression -luokkaa palauttaakseen annetun olion annetun nimisen attribuutin
     * arvon. Oliolla on oltava kyseiselle attribuutille asetettu Java Beans -käytännön mukainen get-metodi.
     *
     * @param target       Olio, jonka attribuutin arvo luetaan
     * @param propertyName Luettavan attribuutin nimi
     * @return Attribuutin arvo
     */
    public static String getStrProperty(Object target, String propertyName) {
        return (String) getProperty(target, propertyName);
    }

    /**
     * Apumetodi, joka käyttää java.beans.Expression -luokkaa palauttaakseen annetun olion annetun nimisen attribuutin
     * arvon. Oliolla on oltava kyseiselle attribuutille asetettu Java Beans -käytännön mukainen get-metodi.
     *
     * @param target       Olio, jonka attribuutin arvo luetaan
     * @param propertyName Luettavan attribuutin nimi
     * @return Attribuutin arvo
     */
    public static Object getProperty(Object target, String propertyName) {
        String methodName = "get" + StringUtils.capitalize(propertyName);
        Expression expr = new Expression(target, methodName, null);
        try {
            expr.execute();
            return expr.getValue();
        } catch (Exception e) {
            System.out.println("Getting the property " + propertyName + " of target " + target.toString()
                    + " failed with the following message:");
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Muuntaa annetun merkkijonon muodosta camelCase muotoon camel_case.
     *
     * @param string Muunnettava merkkijono
     * @return Muunnettu merkkijono
     */
    public static String toLowerUnderscore(String string) {
        return string.replaceAll("([a-z\\d])([A-Z\\d])", "$1_$2").toLowerCase();
    }

    /**
     * Luo annetusta Map-oliosta uuden Map-olion, jonka merkkijonomuotoiset avaimet on muunnettu muodosta camelCase
     * muotoon camel_case.
     *
     * @param map Map-olio, josta uusi olio muodostetaan
     * @return Uusi Map-olio
     */
    public static Map<String, Object> keysToLowerUnderscore(Map<String, Object> map) {
        Map<String, Object> newMap = new HashMap<>();
        map.forEach((k, v) -> newMap.put(toLowerUnderscore(k), v));
        return newMap;
    }

}
