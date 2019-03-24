package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Test;

public class KassapaateTest {
    Kassapaate kassapaate = new Kassapaate();

    @Test
    public void konstruktoriToimiiOikein(){
       assertTrue(kassapaate.kassassaRahaa() == 100000);
       assertTrue(kassapaate.maukkaitaLounaitaMyyty() == 0);
    }

    @Test
    public void kateisostotToimii(){
        int kassassaRahaa = kassapaate.kassassaRahaa();
        int vaihto = kassapaate.syoEdullisesti(230);
        assertTrue(kassapaate.kassassaRahaa() == kassassaRahaa);
        assertTrue(vaihto == 230);
        assertTrue(kassapaate.edullisiaLounaitaMyyty() == 0);
        vaihto = kassapaate.syoEdullisesti(250);
        assertTrue(kassapaate.kassassaRahaa() == (kassassaRahaa + 240));
        assertTrue(vaihto == 10);
        assertTrue(kassapaate.edullisiaLounaitaMyyty() == 1);

        kassassaRahaa = kassapaate.kassassaRahaa();
        vaihto = kassapaate.syoMaukkaasti(390);
        assertTrue(kassapaate.kassassaRahaa() == kassassaRahaa);
        assertTrue(vaihto == 390);
        assertTrue(kassapaate.maukkaitaLounaitaMyyty() == 0);
        vaihto = kassapaate.syoMaukkaasti(410);
        assertTrue(kassapaate.kassassaRahaa() == (kassassaRahaa + 400));
        assertTrue(vaihto == 10);
        assertTrue(kassapaate.maukkaitaLounaitaMyyty() == 1);
    }

    @Test
    public void korttiostotToimii(){
        Maksukortti mk1 = new Maksukortti(230),
                    mk2 = new Maksukortti(250),
                    mk3 = new Maksukortti(390),
                    mk4 = new Maksukortti( 410);

        int kassassaRahaa = kassapaate.kassassaRahaa();
        boolean veloitettu = kassapaate.syoEdullisesti(mk1);
        assertTrue(kassapaate.kassassaRahaa() == kassassaRahaa);
        assertFalse(veloitettu);
        assertTrue(kassapaate.edullisiaLounaitaMyyty() == 0);
        veloitettu = kassapaate.syoEdullisesti(mk2);
        assertTrue(veloitettu);
        assertTrue(kassapaate.edullisiaLounaitaMyyty() == 1);

        kassassaRahaa = kassapaate.kassassaRahaa();
        veloitettu = kassapaate.syoMaukkaasti(mk3);
        assertTrue(kassapaate.kassassaRahaa() == kassassaRahaa);
        assertFalse(veloitettu);
        assertTrue(kassapaate.maukkaitaLounaitaMyyty() == 0);
        veloitettu = kassapaate.syoMaukkaasti(mk4);
        assertTrue(kassapaate.kassassaRahaa() == kassassaRahaa);
        assertTrue(veloitettu);
        assertTrue(kassapaate.maukkaitaLounaitaMyyty() == 1);
    }

    @Test
    public void kortinLatausToimii(){
       Maksukortti maksukortti = new Maksukortti(0);
       int kassassaRahaa = kassapaate.kassassaRahaa();
       kassapaate.lataaRahaaKortille(maksukortti, -100);
       assertTrue(kassapaate.kassassaRahaa() == kassassaRahaa);
       assertTrue(maksukortti.saldo() == 0);
       kassapaate.lataaRahaaKortille(maksukortti, 100);
       assertTrue(kassapaate.kassassaRahaa() == kassassaRahaa + 100);
       assertTrue(maksukortti.saldo() == 100);
    }
}
