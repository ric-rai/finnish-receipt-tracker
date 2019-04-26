package fi.frt.domain;

import com.tinify.Source;
import com.tinify.Tinify;
import fi.frt.dao.Dao;
import fi.frt.domain.textinput.TextInput;

import java.io.File;
import java.util.List;

public class ReceiptService {

    private Dao<Receipt, Long> receiptDao;
    private List<Receipt> receiptList;

    /**
     * Luo uuden ReceiptService -olion.
     *
     * @param rDao  Dao-rajapinnan toteuttava olio
     * @param rList Lista, jossa pidetään kaikkien kuittien tietoja
     */
    public ReceiptService(Dao<Receipt, Long> rDao, List<Receipt> rList) {
        this.receiptDao = rDao;
        this.receiptList = rList;
        this.receiptList.addAll(receiptDao.list());
        Tinify.setKey("oSYQ7WOXulfGcEfPvHsF0mYvsrOlE3gB");
    }

    /**
     * Luo uuden kuitin.
     *
     * @param receiptInput Syötetekstiolio, joka kuvaa kuittitietoja
     * @param img          kuitin kuvadata tavutaulukkona
     * @return Luotu Receipt-olio
     */
    public Receipt newReceipt(TextInput receiptInput, byte[] img) {
        Receipt receipt = new Receipt(receiptInput.getAttrMap());
        receipt.setImage(img);
        Long receiptId = receiptDao.create(receipt);
        receipt.setId(receiptId);
        receiptList.add(receipt);
        return receipt;
    }

    /**
     * Päivittää kuitin tiedot.
     *
     * @param receipt      Kuitti, eli Receipt-olio, jonka tiedot päivitetään
     * @param receiptInput Syötetekstiolio, joka kuvaa kuittitietoja
     * @param img          kuitin kuvadata tavutaulukkona
     */
    public void updateReceipt(Receipt receipt, TextInput receiptInput, byte[] img) {
        receipt.setFromMap(receiptInput.getAttrMap());
        receipt.setImage(img);
        receiptDao.update(receipt);
    }

    /**
     * Muodostaa uuden kuittikuvan järjestelmään, ja esikäsittelee sen jatkokäsittelyä ajatellen. Esikäsittelyssä kuva
     * pyritään pakkaamaan pienemmäksi.
     *
     * @param file java.io.file -luokan olio, joka kuvaa kuvatiedoston sijaintia tiedostojärjestelmässä
     * @return Tavutaulukko, jossa on kuvatiedoston kuvadata optimaalisesti pakattuna
     * @throws Exception Heittää kaikki poikkeukset kutsujan (käyttöliittymä) käsiteltäväksi
     */
    public byte[] prepareNewImage(File file) throws Exception {
        Source tinifyImg = Tinify.fromFile(file.getPath());
        return tinifyImg.toBuffer();
    }

}
