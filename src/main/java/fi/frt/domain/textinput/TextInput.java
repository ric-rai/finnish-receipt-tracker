package fi.frt.domain.textinput;

import java.util.Map;
import java.util.Set;

/**
 * Rajapinnan toteuttavat luokat kuvaavat johonkin datamalliin liittyviä tekstisyötteitä.
 */
public interface TextInput {
    /**
     * Metodin tulee palauttaa true, jos kaikki rajapinnan toteuttavalle oliolle annetut syötetekstit ovat valideja.
     *
     * @return true, jos kaikki syötetekstit valideja
     */
    boolean isValid();

    /**
     * Metodin tulee palauttaa merkkijonosetti, joka sisältää kaikkien epäkelpojen syötetekstikenttien nimet.
     *
     * @return
     */
    Set<String> getInvalidFields();

    /**
     * Metodi palauttaa kelvollisista syöteteksteistä muodostetut syöteoliot Map-oliona. Esimerkiksi syötekentälle,
     * jonka nimi on "date", ja jonka syötetekstiksi on asetettu validi päivämäärä, voidaan muodostaa syötettä vastaava
     * Date-olio. Tällöin palautettavassa Map-oliossa tulee olla avaimena "date" ja arvona Date-olio. HUOM:
     * Palautettavan Map-olion oikeellisuuteen voidaan luottaa vain, jos syötetekstit tiedetään kelvollisiksi.
     *
     * @return Map, jossa avaimena syötekentän nimi ja arvona syötettä vastaava olio
     */
    Map<String, Object> getAttrMap();

    /**
     * Metodi asettaa rajapinnan toteuttavan olion attribuutit annetun Map-olion mukaisiin arvoihin. Map-oliossa tulee
     * olla avain-arvo -pareina asetettavan olion attribuuttien nimet ja arvot. Metodia kutsuvan luokan ei siis
     * välttämättä tarvitse tuntea rajapinnan toteuttaavaa oliota, kunhan kutsuvalla luokalla on käytössään sopiva
     * attribuutit määrittelevä Map-olio.
     *
     * @param map Map-olio, johon on määritelty attribuuttien uusia arvoja
     */
    void setFromMap(Map<String, Object> map);
}
